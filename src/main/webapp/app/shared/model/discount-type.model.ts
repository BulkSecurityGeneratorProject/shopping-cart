export interface IDiscountType {
  id?: number;
  title?: string;
}

export class DiscountType implements IDiscountType {
  constructor(public id?: number, public title?: string) {}
}
