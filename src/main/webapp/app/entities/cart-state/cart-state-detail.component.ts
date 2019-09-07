import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICartState } from 'app/shared/model/cart-state.model';

@Component({
  selector: 'jhi-cart-state-detail',
  templateUrl: './cart-state-detail.component.html'
})
export class CartStateDetailComponent implements OnInit {
  cartState: ICartState;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cartState }) => {
      this.cartState = cartState;
    });
  }

  previousState() {
    window.history.back();
  }
}
