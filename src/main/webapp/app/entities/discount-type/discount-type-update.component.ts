import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IDiscountType, DiscountType } from 'app/shared/model/discount-type.model';
import { DiscountTypeService } from './discount-type.service';

@Component({
  selector: 'jhi-discount-type-update',
  templateUrl: './discount-type-update.component.html'
})
export class DiscountTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    title: []
  });

  constructor(protected discountTypeService: DiscountTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ discountType }) => {
      this.updateForm(discountType);
    });
  }

  updateForm(discountType: IDiscountType) {
    this.editForm.patchValue({
      id: discountType.id,
      title: discountType.title
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const discountType = this.createFromForm();
    if (discountType.id !== undefined) {
      this.subscribeToSaveResponse(this.discountTypeService.update(discountType));
    } else {
      this.subscribeToSaveResponse(this.discountTypeService.create(discountType));
    }
  }

  private createFromForm(): IDiscountType {
    return {
      ...new DiscountType(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiscountType>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
