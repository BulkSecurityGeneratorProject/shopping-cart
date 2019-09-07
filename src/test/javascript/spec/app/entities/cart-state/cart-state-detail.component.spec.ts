/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartStateDetailComponent } from 'app/entities/cart-state/cart-state-detail.component';
import { CartState } from 'app/shared/model/cart-state.model';

describe('Component Tests', () => {
  describe('CartState Management Detail Component', () => {
    let comp: CartStateDetailComponent;
    let fixture: ComponentFixture<CartStateDetailComponent>;
    const route = ({ data: of({ cartState: new CartState(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartStateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CartStateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CartStateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cartState).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
