import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDiscountType } from 'app/shared/model/discount-type.model';

type EntityResponseType = HttpResponse<IDiscountType>;
type EntityArrayResponseType = HttpResponse<IDiscountType[]>;

@Injectable({ providedIn: 'root' })
export class DiscountTypeService {
  public resourceUrl = SERVER_API_URL + 'api/discount-types';

  constructor(protected http: HttpClient) {}

  create(discountType: IDiscountType): Observable<EntityResponseType> {
    return this.http.post<IDiscountType>(this.resourceUrl, discountType, { observe: 'response' });
  }

  update(discountType: IDiscountType): Observable<EntityResponseType> {
    return this.http.put<IDiscountType>(this.resourceUrl, discountType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDiscountType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDiscountType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
