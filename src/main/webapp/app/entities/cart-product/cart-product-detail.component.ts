import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICartProduct } from 'app/shared/model/cart-product.model';

@Component({
  selector: 'jhi-cart-product-detail',
  templateUrl: './cart-product-detail.component.html'
})
export class CartProductDetailComponent implements OnInit {
  cartProduct: ICartProduct;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cartProduct }) => {
      this.cartProduct = cartProduct;
    });
  }

  previousState() {
    window.history.back();
  }
}
