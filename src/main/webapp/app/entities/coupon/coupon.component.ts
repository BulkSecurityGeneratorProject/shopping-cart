import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICoupon } from 'app/shared/model/coupon.model';
import { AccountService } from 'app/core';
import { CouponService } from './coupon.service';

@Component({
  selector: 'jhi-coupon',
  templateUrl: './coupon.component.html'
})
export class CouponComponent implements OnInit, OnDestroy {
  coupons: ICoupon[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected couponService: CouponService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.couponService
      .query()
      .pipe(
        filter((res: HttpResponse<ICoupon[]>) => res.ok),
        map((res: HttpResponse<ICoupon[]>) => res.body)
      )
      .subscribe(
        (res: ICoupon[]) => {
          this.coupons = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCoupons();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICoupon) {
    return item.id;
  }

  registerChangeInCoupons() {
    this.eventSubscriber = this.eventManager.subscribe('couponListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
