/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartStateDeleteDialogComponent } from 'app/entities/cart-state/cart-state-delete-dialog.component';
import { CartStateService } from 'app/entities/cart-state/cart-state.service';

describe('Component Tests', () => {
  describe('CartState Management Delete Component', () => {
    let comp: CartStateDeleteDialogComponent;
    let fixture: ComponentFixture<CartStateDeleteDialogComponent>;
    let service: CartStateService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartStateDeleteDialogComponent]
      })
        .overrideTemplate(CartStateDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CartStateDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CartStateService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
