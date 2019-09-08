import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICart, Cart } from 'app/shared/model/cart.model';
import { CartService } from './cart.service';
import { ICartState } from 'app/shared/model/cart-state.model';
import { CartStateService } from 'app/entities/cart-state';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-cart-update',
  templateUrl: './cart-update.component.html'
})
export class CartUpdateComponent implements OnInit {
  isSaving: boolean;

  cartstates: ICartState[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    cartStateId: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cartService: CartService,
    protected cartStateService: CartStateService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ cart }) => {
      this.updateForm(cart);
    });
    this.cartStateService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICartState[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICartState[]>) => response.body)
      )
      .subscribe((res: ICartState[]) => (this.cartstates = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(cart: ICart) {
    this.editForm.patchValue({
      id: cart.id,
      cartStateId: cart.cartStateId,
      userId: cart.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const cart = this.createFromForm();
    if (cart.id !== undefined) {
      this.subscribeToSaveResponse(this.cartService.update(cart));
    } else {
      this.subscribeToSaveResponse(this.cartService.create(cart));
    }
  }

  private createFromForm(): ICart {
    return {
      ...new Cart(),
      id: this.editForm.get(['id']).value,
      cartStateId: this.editForm.get(['cartStateId']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICart>>) {
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

  trackCartStateById(index: number, item: ICartState) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
