package com.sezer.shoppingcart.service.impl;

import com.sezer.shoppingcart.domain.Cart;
import com.sezer.shoppingcart.domain.User;
import com.sezer.shoppingcart.enums.CartStateEnum;
import com.sezer.shoppingcart.enums.DiscountTypeEnum;
import com.sezer.shoppingcart.repository.CartRepository;
import com.sezer.shoppingcart.repository.UserRepository;
import com.sezer.shoppingcart.security.SecurityUtils;
import com.sezer.shoppingcart.service.*;
import com.sezer.shoppingcart.service.dto.*;
import com.sezer.shoppingcart.service.helper.DeliveryCostCalculator;
import com.sezer.shoppingcart.service.mapper.CartMapper;
import com.sezer.shoppingcart.service.vm.CategoryPriceQuantityVM;
import com.sezer.shoppingcart.web.rest.vm.CartDetailVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Cart}.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final UserRepository userRepository;

    private final CartProductService cartProductService;

    private final CampaignService campaignService;

    private final CouponService couponService;

    private final ProductService productService;

    private final DeliveryCostCalculator deliveryCostCalculator;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, UserRepository userRepository, CartProductService cartProductService, CampaignService campaignService, CouponService couponService, ProductService productService, DeliveryCostCalculator deliveryCostCalculator) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
        this.cartProductService = cartProductService;
        this.campaignService = campaignService;
        this.couponService = couponService;
        this.productService = productService;
        this.deliveryCostCalculator = deliveryCostCalculator;
    }

    /**
     * Save a cart.
     *
     * @param cartDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CartDTO save(CartDTO cartDTO) {
        log.debug("Request to save Cart : {}", cartDTO);
        Cart cart = cartMapper.toEntity(cartDTO);
        cart = cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    /**
     * Get all the carts.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartDTO> findAll() {
        log.debug("Request to get all Carts");
        return cartRepository.findAll().stream()
            .map(cartMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cart by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CartDTO> findOne(Long id) {
        log.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id)
            .map(cartMapper::toDto);
    }

    /**
     * Delete the cart by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        cartRepository.deleteById(id);
    }

    @Override
    public CartDetailVM addItem(ProductDTO product) {
        CartDetailVM cartDetailVM = new CartDetailVM();
        // If user has cart in pending order state get this cart first else generate new Cart
        CartDTO cartDTO = cartMapper.toDto(cartRepository.findByUserIsCurrentUser(CartStateEnum.PENDING_ORDER.getId()).orElse(new Cart()));

        // If user does not have a cart firstly generate cart for him/her
        if (cartDTO.getId() == null) {
            String username = SecurityUtils.getCurrentUserLogin().orElse(null);
            User user = userRepository.findOneByLogin(username).orElse(null);
            cartDTO.setUserId(user.getId());
            cartDTO.setCartStateId(CartStateEnum.PENDING_ORDER.getId());
            cartDTO = this.save(cartDTO);
        } else {
            List<CartProductDTO> allProducts = cartProductService.findAllByCartId(cartDTO.getId());
            CartProductDTO cartProductDTO = allProducts.stream().filter(cp -> cp.getProductId().equals(product.getId())).findAny().orElse(null);
            if (cartProductDTO != null) {
                cartProductDTO.setQuantity(cartProductDTO.getQuantity() + product.getQuantity());
                cartProductService.save(cartProductDTO);
                // Just used one of campaign codes specified before
                getTotalAmountAfterDiscounts(cartDetailVM, "006f2e76");
                return cartDetailVM;
            }
        }

        // Product added to CartProduct entity
        CartProductDTO cartProductDTO = new CartProductDTO();
        cartProductDTO.setCartId(cartDTO.getId());
        cartProductDTO.setProductId(product.getId());
        cartProductDTO.setQuantity(product.getQuantity());
        cartProductService.save(cartProductDTO);
        // Just used one of campaign codes specified before
        getTotalAmountAfterDiscounts(cartDetailVM, "006f2e76");
        return cartDetailVM;
    }

    @Override
    public CartDetailVM getTotalAmountAfterDiscounts(CartDetailVM cartDetailVM, String campaignCode) {
        CartDTO cartDTO = cartMapper.toDto(cartRepository.findByUserIsCurrentUser(CartStateEnum.PENDING_ORDER.getId()).orElse(null));
        cartDetailVM = this.getCampaignDiscount(cartDTO, cartDetailVM);
        cartDetailVM = this.getCouponDiscount(campaignCode, cartDetailVM);
        Double deliveryCost = getDeliveryCost(cartDTO);
        cartDetailVM.setDelivery(deliveryCost);
        cartDetailVM.setFinalTotal(cartDetailVM.getAfterDiscount() + deliveryCost);
        return cartDetailVM;
    }

    @Override
    public CartDetailVM getCampaignDiscount(CartDTO cartDTO, CartDetailVM cartDetailVM) {
        if (cartDTO == null)
            return cartDetailVM;

        List<CartProductDTO> cartProductList = cartProductService.findAllByCartId(cartDTO.getId());

        // Grouped by categoryId with quantity and price informations
        Map<Long, CategoryPriceQuantityVM> categoryQuantityMap = getCategoryQuantityMap(cartProductList);

        // Check for all campaigns
        List<CampaignDTO> campaignList = campaignService.findAll();

        List<CampaignDTO> rateCampaigns = new ArrayList<>();
        List<CampaignDTO> amountCampaigns = new ArrayList<>();

        // Group different type of campaigns and order them descending order by quantity param
        groupCampaigns(campaignList, rateCampaigns, amountCampaigns);

        Double total = 0d;
        Double subTotal = 0d;
        // Iterate on map for checking campaigns.
        for (Long categoryId : categoryQuantityMap.keySet()) {
            CategoryPriceQuantityVM categoryPriceAndQuantity = categoryQuantityMap.get(categoryId);
            Integer quantity = categoryPriceAndQuantity.getQuantity();
            Double price = categoryPriceAndQuantity.getPrice();
            total += price;
            // We want to apply first rate campaign that can supply the rule
            for (CampaignDTO campaign : rateCampaigns) {
                if (campaign.getBaseProductQuantity() < quantity) {
                    categoryPriceAndQuantity.setPrice(price * (1 - campaign.getDiscount()));
                    break;
                }
            }
            // We want to apply first amount campaign that can supply the rule
            for (CampaignDTO campaign : amountCampaigns) {
                if (campaign.getBaseProductQuantity() < quantity) {
                    categoryPriceAndQuantity.setPrice(categoryPriceAndQuantity.getPrice() - campaign.getDiscount());
                    break;
                }
            }
            subTotal += categoryPriceAndQuantity.getPrice();
        }
        cartDetailVM.setTotal(total);
        cartDetailVM.setSubTotal(subTotal);
        cartDetailVM.setAfterDiscount(subTotal);
        cartDetailVM.setCampaignDiscount(total - subTotal);
        return cartDetailVM;
    }

    private void groupCampaigns(List<CampaignDTO> campaignList, List<CampaignDTO> rateCampaigns, List<CampaignDTO> amountCampaigns) {
        if (campaignList != null) {
            campaignList.stream().forEach(c -> {
                if (c.getDiscountTypeId().equals(DiscountTypeEnum.RATE.getId())) {
                    rateCampaigns.add(c);
                }
                if (c.getDiscountTypeId().equals(DiscountTypeEnum.AMOUNT.getId())) {
                    amountCampaigns.add(c);
                }
            });

            // Sort Different Type Campaigns For Checking Campaign Order By Product Quantity
            Collections.sort(rateCampaigns, new Comparator<CampaignDTO>() {
                @Override
                public int compare(CampaignDTO o1, CampaignDTO o2) {
                    return o2.getBaseProductQuantity().compareTo(o1.getBaseProductQuantity());
                }
            });

            Collections.sort(amountCampaigns, new Comparator<CampaignDTO>() {
                @Override
                public int compare(CampaignDTO o1, CampaignDTO o2) {
                    return o2.getBaseProductQuantity().compareTo(o1.getBaseProductQuantity());
                }
            });
        }
    }

    @Override
    public CartDetailVM getCouponDiscount(String code, CartDetailVM cartDetailVM) {
        CouponDTO couponDTO = couponService.findCouponByCode(code).orElse(null);
        // If code is valid
        if (couponDTO != null) {
            if (couponDTO.getMinimumAmount() <= cartDetailVM.getSubTotal()) {
                cartDetailVM.setCouponDiscount(cartDetailVM.getSubTotal() * 0.1d);
                cartDetailVM.setAfterDiscount(cartDetailVM.getSubTotal() * 0.9d);
            }
        }
        return cartDetailVM;
    }

    @Override
    public double getDeliveryCost(CartDTO cartDTO) {
        List<CartProductDTO> cartProducts = cartProductService.findAllByCartId(cartDTO.getId());
        cartProducts.stream().forEach(cp -> {
            ProductDTO product = productService.findOne(cp.getProductId()).orElse(new ProductDTO());
            cp.setProduct(product);
        });
        return deliveryCostCalculator.calculateFor(cartProducts);
    }

    // This function returns quantity of the items bought from same category with categoryId
    private Map<Long, CategoryPriceQuantityVM> getCategoryQuantityMap(List<CartProductDTO> cartProductList) {

        Map<Long, CategoryPriceQuantityVM> categoryQuantityMap = new HashMap<>();

        // Build map With price and quantity infos
        if (cartProductList != null) {
            cartProductList.stream().forEach(cp -> {
                ProductDTO product = productService.findOne(cp.getProductId()).orElse(new ProductDTO());
                Long categoryId = product.getCategory().getId();
                if (categoryQuantityMap.containsKey(categoryId)) {
                    CategoryPriceQuantityVM categoryPriceQuantity = categoryQuantityMap.get(categoryId);
                    categoryPriceQuantity.setQuantity(categoryPriceQuantity.getQuantity() + cp.getQuantity());
                    categoryPriceQuantity.setPrice(categoryPriceQuantity.getPrice() + product.getPrice() * cp.getQuantity());
                    categoryQuantityMap.put(categoryId, categoryPriceQuantity);
                } else {
                    CategoryPriceQuantityVM categoryPriceQuantity = new CategoryPriceQuantityVM();
                    categoryPriceQuantity.setQuantity(cp.getQuantity());
                    categoryPriceQuantity.setPrice(product.getPrice() * cp.getQuantity());
                    categoryQuantityMap.put(categoryId, categoryPriceQuantity);
                }
            });
        }
        return categoryQuantityMap;
    }
}
