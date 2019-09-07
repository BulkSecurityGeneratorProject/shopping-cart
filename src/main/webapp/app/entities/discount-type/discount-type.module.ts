import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShoppingCartSharedModule } from 'app/shared';
import {
  DiscountTypeComponent,
  DiscountTypeDetailComponent,
  DiscountTypeUpdateComponent,
  DiscountTypeDeletePopupComponent,
  DiscountTypeDeleteDialogComponent,
  discountTypeRoute,
  discountTypePopupRoute
} from './';

const ENTITY_STATES = [...discountTypeRoute, ...discountTypePopupRoute];

@NgModule({
  imports: [ShoppingCartSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DiscountTypeComponent,
    DiscountTypeDetailComponent,
    DiscountTypeUpdateComponent,
    DiscountTypeDeleteDialogComponent,
    DiscountTypeDeletePopupComponent
  ],
  entryComponents: [
    DiscountTypeComponent,
    DiscountTypeUpdateComponent,
    DiscountTypeDeleteDialogComponent,
    DiscountTypeDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppingCartDiscountTypeModule {}
