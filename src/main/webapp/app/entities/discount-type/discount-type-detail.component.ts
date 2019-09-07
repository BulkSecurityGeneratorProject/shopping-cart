import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDiscountType } from 'app/shared/model/discount-type.model';

@Component({
  selector: 'jhi-discount-type-detail',
  templateUrl: './discount-type-detail.component.html'
})
export class DiscountTypeDetailComponent implements OnInit {
  discountType: IDiscountType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ discountType }) => {
      this.discountType = discountType;
    });
  }

  previousState() {
    window.history.back();
  }
}
