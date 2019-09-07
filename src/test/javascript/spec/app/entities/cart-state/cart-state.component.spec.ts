/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartStateComponent } from 'app/entities/cart-state/cart-state.component';
import { CartStateService } from 'app/entities/cart-state/cart-state.service';
import { CartState } from 'app/shared/model/cart-state.model';

describe('Component Tests', () => {
  describe('CartState Management Component', () => {
    let comp: CartStateComponent;
    let fixture: ComponentFixture<CartStateComponent>;
    let service: CartStateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartStateComponent],
        providers: []
      })
        .overrideTemplate(CartStateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CartStateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CartStateService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CartState(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cartStates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
