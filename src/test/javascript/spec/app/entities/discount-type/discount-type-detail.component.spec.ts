/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { DiscountTypeDetailComponent } from 'app/entities/discount-type/discount-type-detail.component';
import { DiscountType } from 'app/shared/model/discount-type.model';

describe('Component Tests', () => {
  describe('DiscountType Management Detail Component', () => {
    let comp: DiscountTypeDetailComponent;
    let fixture: ComponentFixture<DiscountTypeDetailComponent>;
    const route = ({ data: of({ discountType: new DiscountType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [DiscountTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DiscountTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DiscountTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.discountType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
