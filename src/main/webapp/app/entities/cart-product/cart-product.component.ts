import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICartProduct } from 'app/shared/model/cart-product.model';
import { AccountService } from 'app/core';
import { CartProductService } from './cart-product.service';

@Component({
  selector: 'jhi-cart-product',
  templateUrl: './cart-product.component.html'
})
export class CartProductComponent implements OnInit, OnDestroy {
  cartProducts: ICartProduct[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected cartProductService: CartProductService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.cartProductService
      .query()
      .pipe(
        filter((res: HttpResponse<ICartProduct[]>) => res.ok),
        map((res: HttpResponse<ICartProduct[]>) => res.body)
      )
      .subscribe(
        (res: ICartProduct[]) => {
          this.cartProducts = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCartProducts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICartProduct) {
    return item.id;
  }

  registerChangeInCartProducts() {
    this.eventSubscriber = this.eventManager.subscribe('cartProductListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
