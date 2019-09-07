import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Campaign } from 'app/shared/model/campaign.model';
import { CampaignService } from './campaign.service';
import { CampaignComponent } from './campaign.component';
import { CampaignDetailComponent } from './campaign-detail.component';
import { CampaignUpdateComponent } from './campaign-update.component';
import { CampaignDeletePopupComponent } from './campaign-delete-dialog.component';
import { ICampaign } from 'app/shared/model/campaign.model';

@Injectable({ providedIn: 'root' })
export class CampaignResolve implements Resolve<ICampaign> {
  constructor(private service: CampaignService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICampaign> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Campaign>) => response.ok),
        map((campaign: HttpResponse<Campaign>) => campaign.body)
      );
    }
    return of(new Campaign());
  }
}

export const campaignRoute: Routes = [
  {
    path: '',
    component: CampaignComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Campaigns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CampaignDetailComponent,
    resolve: {
      campaign: CampaignResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Campaigns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CampaignUpdateComponent,
    resolve: {
      campaign: CampaignResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Campaigns'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CampaignUpdateComponent,
    resolve: {
      campaign: CampaignResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Campaigns'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const campaignPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CampaignDeletePopupComponent,
    resolve: {
      campaign: CampaignResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Campaigns'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
