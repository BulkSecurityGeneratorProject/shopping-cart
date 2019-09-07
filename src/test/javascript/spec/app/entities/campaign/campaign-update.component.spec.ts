/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ShoppingCartTestModule } from '../../../test.module';
import { CampaignUpdateComponent } from 'app/entities/campaign/campaign-update.component';
import { CampaignService } from 'app/entities/campaign/campaign.service';
import { Campaign } from 'app/shared/model/campaign.model';

describe('Component Tests', () => {
  describe('Campaign Management Update Component', () => {
    let comp: CampaignUpdateComponent;
    let fixture: ComponentFixture<CampaignUpdateComponent>;
    let service: CampaignService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingCartTestModule],
        declarations: [CampaignUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CampaignUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CampaignUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CampaignService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Campaign(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Campaign();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
