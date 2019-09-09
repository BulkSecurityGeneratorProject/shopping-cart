import { Category } from 'app/shared/model/category.model';

export interface IProduct {
  id?: number;
  title?: string;
  price?: number;
  categoryId?: number;
  category?: Category;
  cartId?: number;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public title?: string,
    public price?: number,
    public categoryId?: number,
    public category?: Category,
    public cartId?: number
  ) {}
}
