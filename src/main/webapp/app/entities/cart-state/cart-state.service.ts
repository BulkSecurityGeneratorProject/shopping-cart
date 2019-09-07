import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICartState } from 'app/shared/model/cart-state.model';

type EntityResponseType = HttpResponse<ICartState>;
type EntityArrayResponseType = HttpResponse<ICartState[]>;

@Injectable({ providedIn: 'root' })
export class CartStateService {
  public resourceUrl = SERVER_API_URL + 'api/cart-states';

  constructor(protected http: HttpClient) {}

  create(cartState: ICartState): Observable<EntityResponseType> {
    return this.http.post<ICartState>(this.resourceUrl, cartState, { observe: 'response' });
  }

  update(cartState: ICartState): Observable<EntityResponseType> {
    return this.http.put<ICartState>(this.resourceUrl, cartState, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICartState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICartState[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
