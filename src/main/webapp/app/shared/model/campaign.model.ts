import { DiscountType } from 'app/shared/model/discount-type.model';

export interface ICampaign {
  id?: number;
  title?: string;
  baseProductQuantity?: number;
  discount?: number;
  discountTypeId?: number;
  discountType?: DiscountType;
}

export class Campaign implements ICampaign {
  constructor(
    public id?: number,
    public title?: string,
    public baseProductQuantity?: number,
    public discount?: number,
    public discountTypeId?: number,
    public discountType?: DiscountType
  ) {}
}
