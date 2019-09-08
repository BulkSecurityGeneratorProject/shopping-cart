import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICartProduct } from 'app/shared/model/cart-product.model';
import { CartProductService } from './cart-product.service';

@Component({
  selector: 'jhi-cart-product-delete-dialog',
  templateUrl: './cart-product-delete-dialog.component.html'
})
export class CartProductDeleteDialogComponent {
  cartProduct: ICartProduct;

  constructor(
    protected cartProductService: CartProductService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cartProductService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'cartProductListModification',
        content: 'Deleted an cartProduct'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-cart-product-delete-popup',
  template: ''
})
export class CartProductDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cartProduct }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CartProductDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.cartProduct = cartProduct;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/cart-product', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/cart-product', { outlets: { popup: null } }]);
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
