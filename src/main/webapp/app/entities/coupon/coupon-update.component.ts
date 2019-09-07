import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICoupon, Coupon } from 'app/shared/model/coupon.model';
import { CouponService } from './coupon.service';
import { IDiscountType } from 'app/shared/model/discount-type.model';
import { DiscountTypeService } from 'app/entities/discount-type';

@Component({
  selector: 'jhi-coupon-update',
  templateUrl: './coupon-update.component.html'
})
export class CouponUpdateComponent implements OnInit {
  isSaving: boolean;

  discounttypes: IDiscountType[];

  editForm = this.fb.group({
    id: [],
    title: [],
    code: [],
    minimumAmount: [],
    discountTypeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected couponService: CouponService,
    protected discountTypeService: DiscountTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ coupon }) => {
      this.updateForm(coupon);
    });
    this.discountTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDiscountType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDiscountType[]>) => response.body)
      )
      .subscribe((res: IDiscountType[]) => (this.discounttypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(coupon: ICoupon) {
    this.editForm.patchValue({
      id: coupon.id,
      title: coupon.title,
      code: coupon.code,
      minimumAmount: coupon.minimumAmount,
      discountTypeId: coupon.discountTypeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const coupon = this.createFromForm();
    if (coupon.id !== undefined) {
      this.subscribeToSaveResponse(this.couponService.update(coupon));
    } else {
      this.subscribeToSaveResponse(this.couponService.create(coupon));
    }
  }

  private createFromForm(): ICoupon {
    return {
      ...new Coupon(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      code: this.editForm.get(['code']).value,
      minimumAmount: this.editForm.get(['minimumAmount']).value,
      discountTypeId: this.editForm.get(['discountTypeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoupon>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackDiscountTypeById(index: number, item: IDiscountType) {
    return item.id;
  }
}
