import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.ShoppingCartCategoryModule)
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.ShoppingCartProductModule)
      },
      {
        path: 'cart',
        loadChildren: () => import('./cart/cart.module').then(m => m.ShoppingCartCartModule)
      },
      {
        path: 'cart-state',
        loadChildren: () => import('./cart-state/cart-state.module').then(m => m.ShoppingCartCartStateModule)
      },
      {
        path: 'campaign',
        loadChildren: () => import('./campaign/campaign.module').then(m => m.ShoppingCartCampaignModule)
      },
      {
        path: 'discount-type',
        loadChildren: () => import('./discount-type/discount-type.module').then(m => m.ShoppingCartDiscountTypeModule)
      },
      {
        path: 'coupon',
        loadChildren: () => import('./coupon/coupon.module').then(m => m.ShoppingCartCouponModule)
      },
      {
        path: 'cart-product',
        loadChildren: () => import('./cart-product/cart-product.module').then(m => m.ShoppingCartCartProductModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppingCartEntityModule {}
