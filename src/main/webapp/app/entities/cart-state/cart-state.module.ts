import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShoppingCartSharedModule } from 'app/shared';
import {
  CartStateComponent,
  CartStateDetailComponent,
  CartStateUpdateComponent,
  CartStateDeletePopupComponent,
  CartStateDeleteDialogComponent,
  cartStateRoute,
  cartStatePopupRoute
} from './';

const ENTITY_STATES = [...cartStateRoute, ...cartStatePopupRoute];

@NgModule({
  imports: [ShoppingCartSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CartStateComponent,
    CartStateDetailComponent,
    CartStateUpdateComponent,
    CartStateDeleteDialogComponent,
    CartStateDeletePopupComponent
  ],
  entryComponents: [CartStateComponent, CartStateUpdateComponent, CartStateDeleteDialogComponent, CartStateDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppingCartCartStateModule {}
