/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartProductUpdateComponent } from 'app/entities/cart-product/cart-product-update.component';
import { CartProductService } from 'app/entities/cart-product/cart-product.service';
import { CartProduct } from 'app/shared/model/cart-product.model';

describe('Component Tests', () => {
  describe('CartProduct Management Update Component', () => {
    let comp: CartProductUpdateComponent;
    let fixture: ComponentFixture<CartProductUpdateComponent>;
    let service: CartProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartProductUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CartProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CartProductUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CartProductService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CartProduct(123);
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
        const entity = new CartProduct();
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
