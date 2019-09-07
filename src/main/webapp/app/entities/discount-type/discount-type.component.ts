import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDiscountType } from 'app/shared/model/discount-type.model';
import { AccountService } from 'app/core';
import { DiscountTypeService } from './discount-type.service';

@Component({
  selector: 'jhi-discount-type',
  templateUrl: './discount-type.component.html'
})
export class DiscountTypeComponent implements OnInit, OnDestroy {
  discountTypes: IDiscountType[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected discountTypeService: DiscountTypeService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.discountTypeService
      .query()
      .pipe(
        filter((res: HttpResponse<IDiscountType[]>) => res.ok),
        map((res: HttpResponse<IDiscountType[]>) => res.body)
      )
      .subscribe(
        (res: IDiscountType[]) => {
          this.discountTypes = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDiscountTypes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDiscountType) {
    return item.id;
  }

  registerChangeInDiscountTypes() {
    this.eventSubscriber = this.eventManager.subscribe('discountTypeListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
