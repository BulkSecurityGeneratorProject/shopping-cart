import { IProduct } from 'app/shared/model/product.model';

export interface ICart {
  id?: number;
  quantity?: number;
  products?: IProduct[];
  cartStateId?: number;
  userId?: number;
}

export class Cart implements ICart {
  constructor(
    public id?: number,
    public quantity?: number,
    public products?: IProduct[],
    public cartStateId?: number,
    public userId?: number
  ) {}
}
