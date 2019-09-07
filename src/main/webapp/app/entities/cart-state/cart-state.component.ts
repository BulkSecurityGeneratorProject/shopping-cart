import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICartState } from 'app/shared/model/cart-state.model';
import { AccountService } from 'app/core';
import { CartStateService } from './cart-state.service';

@Component({
  selector: 'jhi-cart-state',
  templateUrl: './cart-state.component.html'
})
export class CartStateComponent implements OnInit, OnDestroy {
  cartStates: ICartState[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected cartStateService: CartStateService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.cartStateService
      .query()
      .pipe(
        filter((res: HttpResponse<ICartState[]>) => res.ok),
        map((res: HttpResponse<ICartState[]>) => res.body)
      )
      .subscribe(
        (res: ICartState[]) => {
          this.cartStates = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCartStates();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICartState) {
    return item.id;
  }

  registerChangeInCartStates() {
    this.eventSubscriber = this.eventManager.subscribe('cartStateListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
