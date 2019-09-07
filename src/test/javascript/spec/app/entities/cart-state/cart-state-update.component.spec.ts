/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartStateUpdateComponent } from 'app/entities/cart-state/cart-state-update.component';
import { CartStateService } from 'app/entities/cart-state/cart-state.service';
import { CartState } from 'app/shared/model/cart-state.model';

describe('Component Tests', () => {
  describe('CartState Management Update Component', () => {
    let comp: CartStateUpdateComponent;
    let fixture: ComponentFixture<CartStateUpdateComponent>;
    let service: CartStateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartStateUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CartStateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CartStateUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CartStateService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CartState(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CartState();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
