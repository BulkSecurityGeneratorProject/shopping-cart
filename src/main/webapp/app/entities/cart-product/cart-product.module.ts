import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShoppingCartSharedModule } from 'app/shared';
import {
  CartProductComponent,
  CartProductDetailComponent,
  CartProductUpdateComponent,
  CartProductDeletePopupComponent,
  CartProductDeleteDialogComponent,
  cartProductRoute,
  cartProductPopupRoute
} from './';

const ENTITY_STATES = [...cartProductRoute, ...cartProductPopupRoute];

@NgModule({
  imports: [ShoppingCartSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CartProductComponent,
    CartProductDetailComponent,
    CartProductUpdateComponent,
    CartProductDeleteDialogComponent,
    CartProductDeletePopupComponent
  ],
  entryComponents: [CartProductComponent, CartProductUpdateComponent, CartProductDeleteDialogComponent, CartProductDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppingCartCartProductModule {}
