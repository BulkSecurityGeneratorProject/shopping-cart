import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CartProduct } from 'app/shared/model/cart-product.model';
import { CartProductService } from './cart-product.service';
import { CartProductComponent } from './cart-product.component';
import { CartProductDetailComponent } from './cart-product-detail.component';
import { CartProductUpdateComponent } from './cart-product-update.component';
import { CartProductDeletePopupComponent } from './cart-product-delete-dialog.component';
import { ICartProduct } from 'app/shared/model/cart-product.model';

@Injectable({ providedIn: 'root' })
export class CartProductResolve implements Resolve<ICartProduct> {
  constructor(private service: CartProductService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICartProduct> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CartProduct>) => response.ok),
        map((cartProduct: HttpResponse<CartProduct>) => cartProduct.body)
      );
    }
    return of(new CartProduct());
  }
}

export const cartProductRoute: Routes = [
  {
    path: '',
    component: CartProductComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartProducts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CartProductDetailComponent,
    resolve: {
      cartProduct: CartProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartProducts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CartProductUpdateComponent,
    resolve: {
      cartProduct: CartProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartProducts'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CartProductUpdateComponent,
    resolve: {
      cartProduct: CartProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartProducts'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cartProductPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CartProductDeletePopupComponent,
    resolve: {
      cartProduct: CartProductResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartProducts'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
