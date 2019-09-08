export interface ICampaign {
  id?: number;
  title?: string;
  baseProductQuantity?: number;
  discount?: number;
  discountTypeId?: number;
}

export class Campaign implements ICampaign {
  constructor(
    public id?: number,
    public title?: string,
    public baseProductQuantity?: number,
    public discount?: number,
    public discountTypeId?: number
  ) {}
}
