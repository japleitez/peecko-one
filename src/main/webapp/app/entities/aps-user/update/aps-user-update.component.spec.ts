import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApsUserService } from '../service/aps-user.service';
import { IApsUser } from '../aps-user.model';
import { ApsUserFormService } from './aps-user-form.service';

import { ApsUserUpdateComponent } from './aps-user-update.component';

describe('ApsUser Management Update Component', () => {
  let comp: ApsUserUpdateComponent;
  let fixture: ComponentFixture<ApsUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apsUserFormService: ApsUserFormService;
  let apsUserService: ApsUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApsUserUpdateComponent],
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
      .overrideTemplate(ApsUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apsUserFormService = TestBed.inject(ApsUserFormService);
    apsUserService = TestBed.inject(ApsUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const apsUser: IApsUser = { id: 456 };

      activatedRoute.data = of({ apsUser });
      comp.ngOnInit();

      expect(comp.apsUser).toEqual(apsUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsUser>>();
      const apsUser = { id: 123 };
      jest.spyOn(apsUserFormService, 'getApsUser').mockReturnValue(apsUser);
      jest.spyOn(apsUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsUser }));
      saveSubject.complete();

      // THEN
      expect(apsUserFormService.getApsUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apsUserService.update).toHaveBeenCalledWith(expect.objectContaining(apsUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsUser>>();
      const apsUser = { id: 123 };
      jest.spyOn(apsUserFormService, 'getApsUser').mockReturnValue({ id: null });
      jest.spyOn(apsUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsUser }));
      saveSubject.complete();

      // THEN
      expect(apsUserFormService.getApsUser).toHaveBeenCalled();
      expect(apsUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsUser>>();
      const apsUser = { id: 123 };
      jest.spyOn(apsUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apsUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
