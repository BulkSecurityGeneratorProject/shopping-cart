import { NgModule } from '@angular/core';

import { ShoppingCartSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [ShoppingCartSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [ShoppingCartSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ShoppingCartSharedCommonModule {}
