export interface ICoupon {
  id?: number;
  title?: string;
  code?: string;
  minimumAmount?: number;
  discountTypeId?: number;
}

export class Coupon implements ICoupon {
  constructor(
    public id?: number,
    public title?: string,
    public code?: string,
    public minimumAmount?: number,
    public discountTypeId?: number
  ) {}
}
