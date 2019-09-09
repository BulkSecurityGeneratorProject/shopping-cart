import { DiscountType } from 'app/shared/model/discount-type.model';

export interface ICoupon {
  id?: number;
  title?: string;
  code?: string;
  minimumAmount?: number;
  discountTypeId?: number;
  discountType?: DiscountType;
}

export class Coupon implements ICoupon {
  constructor(
    public id?: number,
    public title?: string,
    public code?: string,
    public minimumAmount?: number,
    public discountTypeId?: number,
    public discountType?: DiscountType
  ) {}
}
