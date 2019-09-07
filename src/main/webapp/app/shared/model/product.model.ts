export interface IProduct {
  id?: number;
  title?: string;
  price?: number;
  categoryId?: number;
  cartId?: number;
}

export class Product implements IProduct {
  constructor(public id?: number, public title?: string, public price?: number, public categoryId?: number, public cartId?: number) {}
}
