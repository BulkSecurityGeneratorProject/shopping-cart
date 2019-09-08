import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICartProduct, CartProduct } from 'app/shared/model/cart-product.model';
import { CartProductService } from './cart-product.service';
import { ICart } from 'app/shared/model/cart.model';
import { CartService } from 'app/entities/cart';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
  selector: 'jhi-cart-product-update',
  templateUrl: './cart-product-update.component.html'
})
export class CartProductUpdateComponent implements OnInit {
  isSaving: boolean;

  carts: ICart[];

  products: IProduct[];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required]],
    cartId: [],
    productId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cartProductService: CartProductService,
    protected cartService: CartService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ cartProduct }) => {
      this.updateForm(cartProduct);
    });
    this.cartService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICart[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICart[]>) => response.body)
      )
      .subscribe((res: ICart[]) => (this.carts = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.productService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProduct[]>) => response.body)
      )
      .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(cartProduct: ICartProduct) {
    this.editForm.patchValue({
      id: cartProduct.id,
      quantity: cartProduct.quantity,
      cartId: cartProduct.cartId,
      productId: cartProduct.productId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const cartProduct = this.createFromForm();
    if (cartProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.cartProductService.update(cartProduct));
    } else {
      this.subscribeToSaveResponse(this.cartProductService.create(cartProduct));
    }
  }

  private createFromForm(): ICartProduct {
    return {
      ...new CartProduct(),
      id: this.editForm.get(['id']).value,
      quantity: this.editForm.get(['quantity']).value,
      cartId: this.editForm.get(['cartId']).value,
      productId: this.editForm.get(['productId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICartProduct>>) {
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

  trackCartById(index: number, item: ICart) {
    return item.id;
  }

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }
}
