import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ShoppingCartSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [ShoppingCartSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ShoppingCartSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ShoppingCartSharedModule {
  static forRoot() {
    return {
      ngModule: ShoppingCartSharedModule
    };
  }
}
