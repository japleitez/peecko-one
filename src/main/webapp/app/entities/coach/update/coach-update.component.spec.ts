import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CoachService } from '../service/coach.service';
import { ICoach } from '../coach.model';
import { CoachFormService } from './coach-form.service';

import { CoachUpdateComponent } from './coach-update.component';

describe('Coach Management Update Component', () => {
  let comp: CoachUpdateComponent;
  let fixture: ComponentFixture<CoachUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coachFormService: CoachFormService;
  let coachService: CoachService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CoachUpdateComponent],
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
      .overrideTemplate(CoachUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoachUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coachFormService = TestBed.inject(CoachFormService);
    coachService = TestBed.inject(CoachService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const coach: ICoach = { id: 456 };

      activatedRoute.data = of({ coach });
      comp.ngOnInit();

      expect(comp.coach).toEqual(coach);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoach>>();
      const coach = { id: 123 };
      jest.spyOn(coachFormService, 'getCoach').mockReturnValue(coach);
      jest.spyOn(coachService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coach });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coach }));
      saveSubject.complete();

      // THEN
      expect(coachFormService.getCoach).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(coachService.update).toHaveBeenCalledWith(expect.objectContaining(coach));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoach>>();
      const coach = { id: 123 };
      jest.spyOn(coachFormService, 'getCoach').mockReturnValue({ id: null });
      jest.spyOn(coachService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coach: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coach }));
      saveSubject.complete();

      // THEN
      expect(coachFormService.getCoach).toHaveBeenCalled();
      expect(coachService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoach>>();
      const coach = { id: 123 };
      jest.spyOn(coachService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coach });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coachService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
