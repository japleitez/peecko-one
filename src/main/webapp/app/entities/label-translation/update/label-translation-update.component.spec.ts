import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LabelTranslationService } from '../service/label-translation.service';
import { ILabelTranslation } from '../label-translation.model';
import { LabelTranslationFormService } from './label-translation-form.service';

import { LabelTranslationUpdateComponent } from './label-translation-update.component';

describe('LabelTranslation Management Update Component', () => {
  let comp: LabelTranslationUpdateComponent;
  let fixture: ComponentFixture<LabelTranslationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let labelTranslationFormService: LabelTranslationFormService;
  let labelTranslationService: LabelTranslationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LabelTranslationUpdateComponent],
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
      .overrideTemplate(LabelTranslationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LabelTranslationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    labelTranslationFormService = TestBed.inject(LabelTranslationFormService);
    labelTranslationService = TestBed.inject(LabelTranslationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const labelTranslation: ILabelTranslation = { id: 456 };

      activatedRoute.data = of({ labelTranslation });
      comp.ngOnInit();

      expect(comp.labelTranslation).toEqual(labelTranslation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabelTranslation>>();
      const labelTranslation = { id: 123 };
      jest.spyOn(labelTranslationFormService, 'getLabelTranslation').mockReturnValue(labelTranslation);
      jest.spyOn(labelTranslationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labelTranslation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: labelTranslation }));
      saveSubject.complete();

      // THEN
      expect(labelTranslationFormService.getLabelTranslation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(labelTranslationService.update).toHaveBeenCalledWith(expect.objectContaining(labelTranslation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabelTranslation>>();
      const labelTranslation = { id: 123 };
      jest.spyOn(labelTranslationFormService, 'getLabelTranslation').mockReturnValue({ id: null });
      jest.spyOn(labelTranslationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labelTranslation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: labelTranslation }));
      saveSubject.complete();

      // THEN
      expect(labelTranslationFormService.getLabelTranslation).toHaveBeenCalled();
      expect(labelTranslationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabelTranslation>>();
      const labelTranslation = { id: 123 };
      jest.spyOn(labelTranslationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labelTranslation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(labelTranslationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
