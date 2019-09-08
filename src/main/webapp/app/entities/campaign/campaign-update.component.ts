import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICampaign, Campaign } from 'app/shared/model/campaign.model';
import { CampaignService } from './campaign.service';
import { IDiscountType } from 'app/shared/model/discount-type.model';
import { DiscountTypeService } from 'app/entities/discount-type';

@Component({
  selector: 'jhi-campaign-update',
  templateUrl: './campaign-update.component.html'
})
export class CampaignUpdateComponent implements OnInit {
  isSaving: boolean;

  discounttypes: IDiscountType[];

  editForm = this.fb.group({
    id: [],
    title: [],
    baseProductQuantity: [],
    discount: [],
    discountTypeId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected campaignService: CampaignService,
    protected discountTypeService: DiscountTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ campaign }) => {
      this.updateForm(campaign);
    });
    this.discountTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDiscountType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDiscountType[]>) => response.body)
      )
      .subscribe((res: IDiscountType[]) => (this.discounttypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(campaign: ICampaign) {
    this.editForm.patchValue({
      id: campaign.id,
      title: campaign.title,
      baseProductQuantity: campaign.baseProductQuantity,
      discount: campaign.discount,
      discountTypeId: campaign.discountTypeId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const campaign = this.createFromForm();
    if (campaign.id !== undefined) {
      this.subscribeToSaveResponse(this.campaignService.update(campaign));
    } else {
      this.subscribeToSaveResponse(this.campaignService.create(campaign));
    }
  }

  private createFromForm(): ICampaign {
    return {
      ...new Campaign(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      baseProductQuantity: this.editForm.get(['baseProductQuantity']).value,
      discount: this.editForm.get(['discount']).value,
      discountTypeId: this.editForm.get(['discountTypeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampaign>>) {
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
