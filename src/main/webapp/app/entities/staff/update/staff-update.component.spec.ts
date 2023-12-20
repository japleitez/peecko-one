import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { StaffService } from '../service/staff.service';
import { IStaff } from '../staff.model';
import { StaffFormService } from './staff-form.service';

import { StaffUpdateComponent } from './staff-update.component';

describe('Staff Management Update Component', () => {
  let comp: StaffUpdateComponent;
  let fixture: ComponentFixture<StaffUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let staffFormService: StaffFormService;
  let staffService: StaffService;
  let agencyService: AgencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StaffUpdateComponent],
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
      .overrideTemplate(StaffUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StaffUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    staffFormService = TestBed.inject(StaffFormService);
    staffService = TestBed.inject(StaffService);
    agencyService = TestBed.inject(AgencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Agency query and add missing value', () => {
      const staff: IStaff = { id: 456 };
      const agency: IAgency = { id: 25055 };
      staff.agency = agency;

      const agencyCollection: IAgency[] = [{ id: 8270 }];
      jest.spyOn(agencyService, 'query').mockReturnValue(of(new HttpResponse({ body: agencyCollection })));
      const additionalAgencies = [agency];
      const expectedCollection: IAgency[] = [...additionalAgencies, ...agencyCollection];
      jest.spyOn(agencyService, 'addAgencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      expect(agencyService.query).toHaveBeenCalled();
      expect(agencyService.addAgencyToCollectionIfMissing).toHaveBeenCalledWith(
        agencyCollection,
        ...additionalAgencies.map(expect.objectContaining),
      );
      expect(comp.agenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const staff: IStaff = { id: 456 };
      const agency: IAgency = { id: 14821 };
      staff.agency = agency;

      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      expect(comp.agenciesSharedCollection).toContain(agency);
      expect(comp.staff).toEqual(staff);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStaff>>();
      const staff = { id: 123 };
      jest.spyOn(staffFormService, 'getStaff').mockReturnValue(staff);
      jest.spyOn(staffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staff }));
      saveSubject.complete();

      // THEN
      expect(staffFormService.getStaff).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(staffService.update).toHaveBeenCalledWith(expect.objectContaining(staff));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStaff>>();
      const staff = { id: 123 };
      jest.spyOn(staffFormService, 'getStaff').mockReturnValue({ id: null });
      jest.spyOn(staffService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staff: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staff }));
      saveSubject.complete();

      // THEN
      expect(staffFormService.getStaff).toHaveBeenCalled();
      expect(staffService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStaff>>();
      const staff = { id: 123 };
      jest.spyOn(staffService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staff });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(staffService.update).toHaveBeenCalled();
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
