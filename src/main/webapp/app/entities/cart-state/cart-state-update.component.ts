import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICartState, CartState } from 'app/shared/model/cart-state.model';
import { CartStateService } from './cart-state.service';

@Component({
  selector: 'jhi-cart-state-update',
  templateUrl: './cart-state-update.component.html'
})
export class CartStateUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    title: []
  });

  constructor(protected cartStateService: CartStateService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ cartState }) => {
      this.updateForm(cartState);
    });
  }

  updateForm(cartState: ICartState) {
    this.editForm.patchValue({
      id: cartState.id,
      title: cartState.title
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const cartState = this.createFromForm();
    if (cartState.id !== undefined) {
      this.subscribeToSaveResponse(this.cartStateService.update(cartState));
    } else {
      this.subscribeToSaveResponse(this.cartStateService.create(cartState));
    }
  }

  private createFromForm(): ICartState {
    return {
      ...new CartState(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICartState>>) {
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
