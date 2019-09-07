/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ShoppingCartTestModule } from '../../../test.module';
import { DiscountTypeComponent } from 'app/entities/discount-type/discount-type.component';
import { DiscountTypeService } from 'app/entities/discount-type/discount-type.service';
import { DiscountType } from 'app/shared/model/discount-type.model';

describe('Component Tests', () => {
  describe('DiscountType Management Component', () => {
    let comp: DiscountTypeComponent;
    let fixture: ComponentFixture<DiscountTypeComponent>;
    let service: DiscountTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [DiscountTypeComponent],
        providers: []
      })
        .overrideTemplate(DiscountTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DiscountTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DiscountTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DiscountType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.discountTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
