/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { DiscountTypeUpdateComponent } from 'app/entities/discount-type/discount-type-update.component';
import { DiscountTypeService } from 'app/entities/discount-type/discount-type.service';
import { DiscountType } from 'app/shared/model/discount-type.model';

describe('Component Tests', () => {
  describe('DiscountType Management Update Component', () => {
    let comp: DiscountTypeUpdateComponent;
    let fixture: ComponentFixture<DiscountTypeUpdateComponent>;
    let service: DiscountTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [DiscountTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DiscountTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DiscountTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DiscountTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DiscountType(123);
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
        const entity = new DiscountType();
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
