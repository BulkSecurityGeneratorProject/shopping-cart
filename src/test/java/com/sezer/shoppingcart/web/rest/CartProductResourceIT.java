package com.sezer.shoppingcart.web.rest;

import com.sezer.shoppingcart.ShoppingCartApp;
import com.sezer.shoppingcart.domain.CartProduct;
import com.sezer.shoppingcart.repository.CartProductRepository;
import com.sezer.shoppingcart.service.CartProductService;
import com.sezer.shoppingcart.service.dto.CartProductDTO;
import com.sezer.shoppingcart.service.mapper.CartProductMapper;
import com.sezer.shoppingcart.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sezer.shoppingcart.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CartProductResource} REST controller.
 */
@SpringBootTest(classes = ShoppingCartApp.class)
public class CartProductResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private CartProductMapper cartProductMapper;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCartProductMockMvc;

    private CartProduct cartProduct;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CartProductResource cartProductResource = new CartProductResource(cartProductService);
        this.restCartProductMockMvc = MockMvcBuilders.standaloneSetup(cartProductResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartProduct createEntity(EntityManager em) {
        CartProduct cartProduct = new CartProduct()
            .quantity(DEFAULT_QUANTITY);
        return cartProduct;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartProduct createUpdatedEntity(EntityManager em) {
        CartProduct cartProduct = new CartProduct()
            .quantity(UPDATED_QUANTITY);
        return cartProduct;
    }

    @BeforeEach
    public void initTest() {
        cartProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartProduct() throws Exception {
        int databaseSizeBeforeCreate = cartProductRepository.findAll().size();

        // Create the CartProduct
        CartProductDTO cartProductDTO = cartProductMapper.toDto(cartProduct);
        restCartProductMockMvc.perform(post("/api/cart-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartProductDTO)))
            .andExpect(status().isCreated());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeCreate + 1);
        CartProduct testCartProduct = cartProductList.get(cartProductList.size() - 1);
        assertThat(testCartProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createCartProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartProductRepository.findAll().size();

        // Create the CartProduct with an existing ID
        cartProduct.setId(1L);
        CartProductDTO cartProductDTO = cartProductMapper.toDto(cartProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartProductMockMvc.perform(post("/api/cart-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartProductRepository.findAll().size();
        // set the field null
        cartProduct.setQuantity(null);

        // Create the CartProduct, which fails.
        CartProductDTO cartProductDTO = cartProductMapper.toDto(cartProduct);

        restCartProductMockMvc.perform(post("/api/cart-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartProductDTO)))
            .andExpect(status().isBadRequest());

        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCartProducts() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        // Get all the cartProductList
        restCartProductMockMvc.perform(get("/api/cart-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getCartProduct() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        // Get the cartProduct
        restCartProductMockMvc.perform(get("/api/cart-products/{id}", cartProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartProduct.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingCartProduct() throws Exception {
        // Get the cartProduct
        restCartProductMockMvc.perform(get("/api/cart-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartProduct() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();

        // Update the cartProduct
        CartProduct updatedCartProduct = cartProductRepository.findById(cartProduct.getId()).get();
        // Disconnect from session so that the updates on updatedCartProduct are not directly saved in db
        em.detach(updatedCartProduct);
        updatedCartProduct
            .quantity(UPDATED_QUANTITY);
        CartProductDTO cartProductDTO = cartProductMapper.toDto(updatedCartProduct);

        restCartProductMockMvc.perform(put("/api/cart-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartProductDTO)))
            .andExpect(status().isOk());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
        CartProduct testCartProduct = cartProductList.get(cartProductList.size() - 1);
        assertThat(testCartProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();

        // Create the CartProduct
        CartProductDTO cartProductDTO = cartProductMapper.toDto(cartProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartProductMockMvc.perform(put("/api/cart-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCartProduct() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        int databaseSizeBeforeDelete = cartProductRepository.findAll().size();

        // Delete the cartProduct
        restCartProductMockMvc.perform(delete("/api/cart-products/{id}", cartProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartProduct.class);
        CartProduct cartProduct1 = new CartProduct();
        cartProduct1.setId(1L);
        CartProduct cartProduct2 = new CartProduct();
        cartProduct2.setId(cartProduct1.getId());
        assertThat(cartProduct1).isEqualTo(cartProduct2);
        cartProduct2.setId(2L);
        assertThat(cartProduct1).isNotEqualTo(cartProduct2);
        cartProduct1.setId(null);
        assertThat(cartProduct1).isNotEqualTo(cartProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartProductDTO.class);
        CartProductDTO cartProductDTO1 = new CartProductDTO();
        cartProductDTO1.setId(1L);
        CartProductDTO cartProductDTO2 = new CartProductDTO();
        assertThat(cartProductDTO1).isNotEqualTo(cartProductDTO2);
        cartProductDTO2.setId(cartProductDTO1.getId());
        assertThat(cartProductDTO1).isEqualTo(cartProductDTO2);
        cartProductDTO2.setId(2L);
        assertThat(cartProductDTO1).isNotEqualTo(cartProductDTO2);
        cartProductDTO1.setId(null);
        assertThat(cartProductDTO1).isNotEqualTo(cartProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cartProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cartProductMapper.fromId(null)).isNull();
    }
}
