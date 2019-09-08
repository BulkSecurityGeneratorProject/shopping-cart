/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ShoppingCartTestModule } from '../../../test.module';
import { CartProductComponent } from 'app/entities/cart-product/cart-product.component';
import { CartProductService } from 'app/entities/cart-product/cart-product.service';
import { CartProduct } from 'app/shared/model/cart-product.model';

describe('Component Tests', () => {
  describe('CartProduct Management Component', () => {
    let comp: CartProductComponent;
    let fixture: ComponentFixture<CartProductComponent>;
    let service: CartProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CartProductComponent],
        providers: []
      })
        .overrideTemplate(CartProductComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CartProductComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CartProductService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CartProduct(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cartProducts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
