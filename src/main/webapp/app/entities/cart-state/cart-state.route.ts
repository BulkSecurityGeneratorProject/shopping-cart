import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CartState } from 'app/shared/model/cart-state.model';
import { CartStateService } from './cart-state.service';
import { CartStateComponent } from './cart-state.component';
import { CartStateDetailComponent } from './cart-state-detail.component';
import { CartStateUpdateComponent } from './cart-state-update.component';
import { CartStateDeletePopupComponent } from './cart-state-delete-dialog.component';
import { ICartState } from 'app/shared/model/cart-state.model';

@Injectable({ providedIn: 'root' })
export class CartStateResolve implements Resolve<ICartState> {
  constructor(private service: CartStateService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICartState> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<CartState>) => response.ok),
        map((cartState: HttpResponse<CartState>) => cartState.body)
      );
    }
    return of(new CartState());
  }
}

export const cartStateRoute: Routes = [
  {
    path: '',
    component: CartStateComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartStates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CartStateDetailComponent,
    resolve: {
      cartState: CartStateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartStates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CartStateUpdateComponent,
    resolve: {
      cartState: CartStateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartStates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CartStateUpdateComponent,
    resolve: {
      cartState: CartStateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartStates'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cartStatePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CartStateDeletePopupComponent,
    resolve: {
      cartState: CartStateResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CartStates'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
