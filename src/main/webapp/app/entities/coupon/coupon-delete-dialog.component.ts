import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICoupon } from 'app/shared/model/coupon.model';
import { CouponService } from './coupon.service';

@Component({
  selector: 'jhi-coupon-delete-dialog',
  templateUrl: './coupon-delete-dialog.component.html'
})
export class CouponDeleteDialogComponent {
  coupon: ICoupon;

  constructor(protected couponService: CouponService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.couponService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'couponListModification',
        content: 'Deleted an coupon'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-coupon-delete-popup',
  template: ''
})
export class CouponDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ coupon }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CouponDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.coupon = coupon;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/coupon', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/coupon', { outlets: { popup: null } }]);
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
