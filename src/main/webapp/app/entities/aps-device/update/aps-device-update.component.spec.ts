import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IApsUser } from 'app/entities/aps-user/aps-user.model';
import { ApsUserService } from 'app/entities/aps-user/service/aps-user.service';
import { ApsDeviceService } from '../service/aps-device.service';
import { IApsDevice } from '../aps-device.model';
import { ApsDeviceFormService } from './aps-device-form.service';

import { ApsDeviceUpdateComponent } from './aps-device-update.component';

describe('ApsDevice Management Update Component', () => {
  let comp: ApsDeviceUpdateComponent;
  let fixture: ComponentFixture<ApsDeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apsDeviceFormService: ApsDeviceFormService;
  let apsDeviceService: ApsDeviceService;
  let apsUserService: ApsUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApsDeviceUpdateComponent],
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
      .overrideTemplate(ApsDeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsDeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apsDeviceFormService = TestBed.inject(ApsDeviceFormService);
    apsDeviceService = TestBed.inject(ApsDeviceService);
    apsUserService = TestBed.inject(ApsUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApsUser query and add missing value', () => {
      const apsDevice: IApsDevice = { id: 456 };
      const apsUser: IApsUser = { id: 10290 };
      apsDevice.apsUser = apsUser;

      const apsUserCollection: IApsUser[] = [{ id: 16681 }];
      jest.spyOn(apsUserService, 'query').mockReturnValue(of(new HttpResponse({ body: apsUserCollection })));
      const additionalApsUsers = [apsUser];
      const expectedCollection: IApsUser[] = [...additionalApsUsers, ...apsUserCollection];
      jest.spyOn(apsUserService, 'addApsUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apsDevice });
      comp.ngOnInit();

      expect(apsUserService.query).toHaveBeenCalled();
      expect(apsUserService.addApsUserToCollectionIfMissing).toHaveBeenCalledWith(
        apsUserCollection,
        ...additionalApsUsers.map(expect.objectContaining),
      );
      expect(comp.apsUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apsDevice: IApsDevice = { id: 456 };
      const apsUser: IApsUser = { id: 8455 };
      apsDevice.apsUser = apsUser;

      activatedRoute.data = of({ apsDevice });
      comp.ngOnInit();

      expect(comp.apsUsersSharedCollection).toContain(apsUser);
      expect(comp.apsDevice).toEqual(apsDevice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsDevice>>();
      const apsDevice = { id: 123 };
      jest.spyOn(apsDeviceFormService, 'getApsDevice').mockReturnValue(apsDevice);
      jest.spyOn(apsDeviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsDevice }));
      saveSubject.complete();

      // THEN
      expect(apsDeviceFormService.getApsDevice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apsDeviceService.update).toHaveBeenCalledWith(expect.objectContaining(apsDevice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsDevice>>();
      const apsDevice = { id: 123 };
      jest.spyOn(apsDeviceFormService, 'getApsDevice').mockReturnValue({ id: null });
      jest.spyOn(apsDeviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsDevice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsDevice }));
      saveSubject.complete();

      // THEN
      expect(apsDeviceFormService.getApsDevice).toHaveBeenCalled();
      expect(apsDeviceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsDevice>>();
      const apsDevice = { id: 123 };
      jest.spyOn(apsDeviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apsDeviceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApsUser', () => {
      it('Should forward to apsUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(apsUserService, 'compareApsUser');
        comp.compareApsUser(entity, entity2);
        expect(apsUserService.compareApsUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
