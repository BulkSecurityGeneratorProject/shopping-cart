/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartProductDetailComponent } from 'app/entities/cart-product/cart-product-detail.component';
import { CartProduct } from 'app/shared/model/cart-product.model';

describe('Component Tests', () => {
  describe('CartProduct Management Detail Component', () => {
    let comp: CartProductDetailComponent;
    let fixture: ComponentFixture<CartProductDetailComponent>;
    const route = ({ data: of({ cartProduct: new CartProduct(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartProductDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CartProductDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CartProductDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cartProduct).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
