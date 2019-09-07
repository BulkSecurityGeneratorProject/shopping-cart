export interface ICartState {
  id?: number;
  title?: string;
}

export class CartState implements ICartState {
  constructor(public id?: number, public title?: string) {}
}
