import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VideoCategoryService } from '../service/video-category.service';
import { IVideoCategory } from '../video-category.model';
import { VideoCategoryFormService } from './video-category-form.service';

import { VideoCategoryUpdateComponent } from './video-category-update.component';

describe('VideoCategory Management Update Component', () => {
  let comp: VideoCategoryUpdateComponent;
  let fixture: ComponentFixture<VideoCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let videoCategoryFormService: VideoCategoryFormService;
  let videoCategoryService: VideoCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), VideoCategoryUpdateComponent],
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
      .overrideTemplate(VideoCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VideoCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    videoCategoryFormService = TestBed.inject(VideoCategoryFormService);
    videoCategoryService = TestBed.inject(VideoCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const videoCategory: IVideoCategory = { id: 456 };

      activatedRoute.data = of({ videoCategory });
      comp.ngOnInit();

      expect(comp.videoCategory).toEqual(videoCategory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVideoCategory>>();
      const videoCategory = { id: 123 };
      jest.spyOn(videoCategoryFormService, 'getVideoCategory').mockReturnValue(videoCategory);
      jest.spyOn(videoCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ videoCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: videoCategory }));
      saveSubject.complete();

      // THEN
      expect(videoCategoryFormService.getVideoCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(videoCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(videoCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVideoCategory>>();
      const videoCategory = { id: 123 };
      jest.spyOn(videoCategoryFormService, 'getVideoCategory').mockReturnValue({ id: null });
      jest.spyOn(videoCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ videoCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: videoCategory }));
      saveSubject.complete();

      // THEN
      expect(videoCategoryFormService.getVideoCategory).toHaveBeenCalled();
      expect(videoCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVideoCategory>>();
      const videoCategory = { id: 123 };
      jest.spyOn(videoCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ videoCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(videoCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
