export interface ICart {
  id?: number;
  cartStateId?: number;
  userId?: number;
}

export class Cart implements ICart {
  constructor(public id?: number, public cartStateId?: number, public userId?: number) {}
}
