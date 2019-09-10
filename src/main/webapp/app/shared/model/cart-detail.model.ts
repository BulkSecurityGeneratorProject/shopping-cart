export interface ICartDetail {
  total?: number;
  campaignDiscount?: number;
  subTotal?: number;
  couponDiscount?: number;
  afterDiscount?: number;
  delivery?: number;
  finalTotal?: number;
}

export class CartDetail implements ICartDetail {
  constructor(
    public total?: number,
    public campaignDiscount?: number,
    public subTotal?: number,
    public couponDiscount?: number,
    public afterDiscount?: number,
    public delivery?: number,
    public finalTotal?: number
  ) {}
}
