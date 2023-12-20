import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ApsPlanService } from '../service/aps-plan.service';
import { IApsPlan } from '../aps-plan.model';
import { ApsPlanFormService } from './aps-plan-form.service';

import { ApsPlanUpdateComponent } from './aps-plan-update.component';

describe('ApsPlan Management Update Component', () => {
  let comp: ApsPlanUpdateComponent;
  let fixture: ComponentFixture<ApsPlanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apsPlanFormService: ApsPlanFormService;
  let apsPlanService: ApsPlanService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApsPlanUpdateComponent],
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
      .overrideTemplate(ApsPlanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsPlanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apsPlanFormService = TestBed.inject(ApsPlanFormService);
    apsPlanService = TestBed.inject(ApsPlanService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const apsPlan: IApsPlan = { id: 456 };
      const customer: ICustomer = { id: 21482 };
      apsPlan.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 18717 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apsPlan });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apsPlan: IApsPlan = { id: 456 };
      const customer: ICustomer = { id: 28672 };
      apsPlan.customer = customer;

      activatedRoute.data = of({ apsPlan });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.apsPlan).toEqual(apsPlan);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsPlan>>();
      const apsPlan = { id: 123 };
      jest.spyOn(apsPlanFormService, 'getApsPlan').mockReturnValue(apsPlan);
      jest.spyOn(apsPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsPlan }));
      saveSubject.complete();

      // THEN
      expect(apsPlanFormService.getApsPlan).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apsPlanService.update).toHaveBeenCalledWith(expect.objectContaining(apsPlan));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsPlan>>();
      const apsPlan = { id: 123 };
      jest.spyOn(apsPlanFormService, 'getApsPlan').mockReturnValue({ id: null });
      jest.spyOn(apsPlanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsPlan: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsPlan }));
      saveSubject.complete();

      // THEN
      expect(apsPlanFormService.getApsPlan).toHaveBeenCalled();
      expect(apsPlanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsPlan>>();
      const apsPlan = { id: 123 };
      jest.spyOn(apsPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apsPlanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
