import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICartState } from 'app/shared/model/cart-state.model';
import { CartStateService } from './cart-state.service';

@Component({
  selector: 'jhi-cart-state-delete-dialog',
  templateUrl: './cart-state-delete-dialog.component.html'
})
export class CartStateDeleteDialogComponent {
  cartState: ICartState;

  constructor(protected cartStateService: CartStateService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cartStateService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'cartStateListModification',
        content: 'Deleted an cartState'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-cart-state-delete-popup',
  template: ''
})
export class CartStateDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cartState }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CartStateDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.cartState = cartState;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/cart-state', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/cart-state', { outlets: { popup: null } }]);
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
