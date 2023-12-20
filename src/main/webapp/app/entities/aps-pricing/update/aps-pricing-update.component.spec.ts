import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { ApsPricingService } from '../service/aps-pricing.service';
import { IApsPricing } from '../aps-pricing.model';
import { ApsPricingFormService } from './aps-pricing-form.service';

import { ApsPricingUpdateComponent } from './aps-pricing-update.component';

describe('ApsPricing Management Update Component', () => {
  let comp: ApsPricingUpdateComponent;
  let fixture: ComponentFixture<ApsPricingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apsPricingFormService: ApsPricingFormService;
  let apsPricingService: ApsPricingService;
  let agencyService: AgencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApsPricingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApsPricingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsPricingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apsPricingFormService = TestBed.inject(ApsPricingFormService);
    apsPricingService = TestBed.inject(ApsPricingService);
    agencyService = TestBed.inject(AgencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Agency query and add missing value', () => {
      const apsPricing: IApsPricing = { id: 456 };
      const agency: IAgency = { id: 23212 };
      apsPricing.agency = agency;

      const agencyCollection: IAgency[] = [{ id: 18007 }];
      jest.spyOn(agencyService, 'query').mockReturnValue(of(new HttpResponse({ body: agencyCollection })));
      const additionalAgencies = [agency];
      const expectedCollection: IAgency[] = [...additionalAgencies, ...agencyCollection];
      jest.spyOn(agencyService, 'addAgencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apsPricing });
      comp.ngOnInit();

      expect(agencyService.query).toHaveBeenCalled();
      expect(agencyService.addAgencyToCollectionIfMissing).toHaveBeenCalledWith(
        agencyCollection,
        ...additionalAgencies.map(expect.objectContaining),
      );
      expect(comp.agenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apsPricing: IApsPricing = { id: 456 };
      const agency: IAgency = { id: 30377 };
      apsPricing.agency = agency;

      activatedRoute.data = of({ apsPricing });
      comp.ngOnInit();

      expect(comp.agenciesSharedCollection).toContain(agency);
      expect(comp.apsPricing).toEqual(apsPricing);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsPricing>>();
      const apsPricing = { id: 123 };
      jest.spyOn(apsPricingFormService, 'getApsPricing').mockReturnValue(apsPricing);
      jest.spyOn(apsPricingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsPricing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsPricing }));
      saveSubject.complete();

      // THEN
      expect(apsPricingFormService.getApsPricing).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apsPricingService.update).toHaveBeenCalledWith(expect.objectContaining(apsPricing));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsPricing>>();
      const apsPricing = { id: 123 };
      jest.spyOn(apsPricingFormService, 'getApsPricing').mockReturnValue({ id: null });
      jest.spyOn(apsPricingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsPricing: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsPricing }));
      saveSubject.complete();

      // THEN
      expect(apsPricingFormService.getApsPricing).toHaveBeenCalled();
      expect(apsPricingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsPricing>>();
      const apsPricing = { id: 123 };
      jest.spyOn(apsPricingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsPricing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apsPricingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAgency', () => {
      it('Should forward to agencyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(agencyService, 'compareAgency');
        comp.compareAgency(entity, entity2);
        expect(agencyService.compareAgency).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
