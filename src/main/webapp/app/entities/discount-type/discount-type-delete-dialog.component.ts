import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDiscountType } from 'app/shared/model/discount-type.model';
import { DiscountTypeService } from './discount-type.service';

@Component({
  selector: 'jhi-discount-type-delete-dialog',
  templateUrl: './discount-type-delete-dialog.component.html'
})
export class DiscountTypeDeleteDialogComponent {
  discountType: IDiscountType;

  constructor(
    protected discountTypeService: DiscountTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.discountTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'discountTypeListModification',
        content: 'Deleted an discountType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-discount-type-delete-popup',
  template: ''
})
export class DiscountTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ discountType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DiscountTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.discountType = discountType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/discount-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/discount-type', { outlets: { popup: null } }]);
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
