export interface ICartProduct {
  id?: number;
  quantity?: number;
  cartId?: number;
  productId?: number;
}

export class CartProduct implements ICartProduct {
  constructor(public id?: number, public quantity?: number, public cartId?: number, public productId?: number) {}
}
