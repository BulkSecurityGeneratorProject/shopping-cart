import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DiscountType } from 'app/shared/model/discount-type.model';
import { DiscountTypeService } from './discount-type.service';
import { DiscountTypeComponent } from './discount-type.component';
import { DiscountTypeDetailComponent } from './discount-type-detail.component';
import { DiscountTypeUpdateComponent } from './discount-type-update.component';
import { DiscountTypeDeletePopupComponent } from './discount-type-delete-dialog.component';
import { IDiscountType } from 'app/shared/model/discount-type.model';

@Injectable({ providedIn: 'root' })
export class DiscountTypeResolve implements Resolve<IDiscountType> {
  constructor(private service: DiscountTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDiscountType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DiscountType>) => response.ok),
        map((discountType: HttpResponse<DiscountType>) => discountType.body)
      );
    }
    return of(new DiscountType());
  }
}

export const discountTypeRoute: Routes = [
  {
    path: '',
    component: DiscountTypeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DiscountTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DiscountTypeDetailComponent,
    resolve: {
      discountType: DiscountTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DiscountTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DiscountTypeUpdateComponent,
    resolve: {
      discountType: DiscountTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DiscountTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DiscountTypeUpdateComponent,
    resolve: {
      discountType: DiscountTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DiscountTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const discountTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DiscountTypeDeletePopupComponent,
    resolve: {
      discountType: DiscountTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'DiscountTypes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
