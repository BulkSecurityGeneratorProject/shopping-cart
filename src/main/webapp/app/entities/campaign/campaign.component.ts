import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { AccountService } from 'app/core';
import { CampaignService } from './campaign.service';

@Component({
  selector: 'jhi-campaign',
  templateUrl: './campaign.component.html'
})
export class CampaignComponent implements OnInit, OnDestroy {
  campaigns: ICampaign[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected campaignService: CampaignService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.campaignService
      .query()
      .pipe(
        filter((res: HttpResponse<ICampaign[]>) => res.ok),
        map((res: HttpResponse<ICampaign[]>) => res.body)
      )
      .subscribe(
        (res: ICampaign[]) => {
          this.campaigns = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCampaigns();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICampaign) {
    return item.id;
  }

  registerChangeInCampaigns() {
    this.eventSubscriber = this.eventManager.subscribe('campaignListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
