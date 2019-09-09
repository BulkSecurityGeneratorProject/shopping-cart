import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, AccountService, LoginModalService } from 'app/core';
import { ICategory } from 'app/shared/model/category.model';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { CategoryService } from 'app/entities/category';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { CartService } from 'app/entities/cart';
import { ICart } from 'app/shared/model/cart.model';
import { ICartState } from 'app/shared/model/cart-state.model';
import { CartStateService } from 'app/entities/cart-state';
import { Campaign, ICampaign } from 'app/shared/model/campaign.model';
import { CampaignService } from 'app/entities/campaign';
import { DiscountTypeService } from 'app/entities/discount-type';
import { CouponService } from 'app/entities/coupon';
import { CartProductService } from 'app/entities/cart-product';
import { IDiscountType } from 'app/shared/model/discount-type.model';
import { ICoupon } from 'app/shared/model/coupon.model';
import { ICartProduct } from 'app/shared/model/cart-product.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
  account: Account;
  modalRef: NgbModalRef;
  categories: ICategory[];
  products: IProduct[];
  carts: ICart[];
  cartStates: ICartState[];
  campaigns: ICampaign[];
  discountTypes: IDiscountType[];
  coupons: ICoupon[];
  cartProducts: ICartProduct[];

  constructor(
    private accountService: AccountService,
    private categoryService: CategoryService,
    private cartService: CartService,
    private cartStateService: CartStateService,
    private campaignService: CampaignService,
    private discountTypeService: DiscountTypeService,
    private couponService: CouponService,
    private cartProductService: CartProductService,
    private loginModalService: LoginModalService,
    private productService: ProductService,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.accountService.identity().then((account: Account) => {
      this.account = account;
      this.loadAll();
    });
    this.registerAuthenticationSuccess();
  }

  loadAll() {
    this.loadCategories();
    this.loadProducts();
    this.loadCarts();
    this.loadCartStates();
    this.loadCampaigns();
    this.loadDiscountTypes();
    this.loadCoupons();
    this.loadCartProducts();
  }

  loadCategories() {
    this.categoryService
      .query()
      .pipe(
        filter((res: HttpResponse<ICategory[]>) => res.ok),
        map((res: HttpResponse<ICategory[]>) => res.body)
      )
      .subscribe((res: ICategory[]) => {
        this.categories = res;
      });
  }

  loadProducts() {
    this.productService
      .query()
      .pipe(
        filter((res: HttpResponse<IProduct[]>) => res.ok),
        map((res: HttpResponse<IProduct[]>) => res.body)
      )
      .subscribe((res: IProduct[]) => {
        this.products = res;
        res.forEach(p => (p.quantity = 1));
        console.log(res);
      });
  }

  loadCarts() {
    this.cartService
      .query()
      .pipe(
        filter((res: HttpResponse<ICart[]>) => res.ok),
        map((res: HttpResponse<ICart[]>) => res.body)
      )
      .subscribe((res: ICart[]) => {
        this.carts = res;
      });
  }

  loadCartStates() {
    this.cartStateService
      .query()
      .pipe(
        filter((res: HttpResponse<ICartState[]>) => res.ok),
        map((res: HttpResponse<ICartState[]>) => res.body)
      )
      .subscribe((res: ICartState[]) => {
        this.cartStates = res;
      });
  }

  loadCampaigns() {
    this.campaignService
      .query()
      .pipe(
        filter((res: HttpResponse<ICampaign[]>) => res.ok),
        map((res: HttpResponse<ICampaign[]>) => res.body)
      )
      .subscribe((res: ICampaign[]) => {
        this.campaigns = res;
      });
  }

  loadDiscountTypes() {
    this.discountTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<IDiscountType[]>) => res.ok),
        map((res: HttpResponse<IDiscountType[]>) => res.body)
      )
      .subscribe((res: IDiscountType[]) => {
        this.discountTypes = res;
      });
  }

  loadCoupons() {
    this.couponService
      .query()
      .pipe(
        filter((res: HttpResponse<ICoupon[]>) => res.ok),
        map((res: HttpResponse<ICoupon[]>) => res.body)
      )
      .subscribe((res: ICoupon[]) => {
        this.coupons = res;
      });
  }

  loadCartProducts() {
    this.cartProductService
      .query()
      .pipe(
        filter((res: HttpResponse<ICartProduct[]>) => res.ok),
        map((res: HttpResponse<ICartProduct[]>) => res.body)
      )
      .subscribe((res: ICartProduct[]) => {
        this.cartProducts = res;
      });
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().then(account => {
        this.account = account;
        this.loadAll();
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }
}
