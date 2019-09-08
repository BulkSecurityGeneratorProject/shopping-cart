import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICartProduct } from 'app/shared/model/cart-product.model';

type EntityResponseType = HttpResponse<ICartProduct>;
type EntityArrayResponseType = HttpResponse<ICartProduct[]>;

@Injectable({ providedIn: 'root' })
export class CartProductService {
  public resourceUrl = SERVER_API_URL + 'api/cart-products';

  constructor(protected http: HttpClient) {}

  create(cartProduct: ICartProduct): Observable<EntityResponseType> {
    return this.http.post<ICartProduct>(this.resourceUrl, cartProduct, { observe: 'response' });
  }

  update(cartProduct: ICartProduct): Observable<EntityResponseType> {
    return this.http.put<ICartProduct>(this.resourceUrl, cartProduct, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICartProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICartProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
