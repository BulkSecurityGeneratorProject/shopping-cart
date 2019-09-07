import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { CampaignService } from './campaign.service';

@Component({
  selector: 'jhi-campaign-delete-dialog',
  templateUrl: './campaign-delete-dialog.component.html'
})
export class CampaignDeleteDialogComponent {
  campaign: ICampaign;

  constructor(protected campaignService: CampaignService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.campaignService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'campaignListModification',
        content: 'Deleted an campaign'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-campaign-delete-popup',
  template: ''
})
export class CampaignDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ campaign }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CampaignDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.campaign = campaign;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/campaign', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/campaign', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
