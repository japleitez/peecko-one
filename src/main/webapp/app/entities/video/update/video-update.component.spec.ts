import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IVideoCategory } from 'app/entities/video-category/video-category.model';
import { VideoCategoryService } from 'app/entities/video-category/service/video-category.service';
import { ICoach } from 'app/entities/coach/coach.model';
import { CoachService } from 'app/entities/coach/service/coach.service';
import { IVideo } from '../video.model';
import { VideoService } from '../service/video.service';
import { VideoFormService } from './video-form.service';

import { VideoUpdateComponent } from './video-update.component';

describe('Video Management Update Component', () => {
  let comp: VideoUpdateComponent;
  let fixture: ComponentFixture<VideoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let videoFormService: VideoFormService;
  let videoService: VideoService;
  let videoCategoryService: VideoCategoryService;
  let coachService: CoachService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), VideoUpdateComponent],
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
      .overrideTemplate(VideoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VideoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    videoFormService = TestBed.inject(VideoFormService);
    videoService = TestBed.inject(VideoService);
    videoCategoryService = TestBed.inject(VideoCategoryService);
    coachService = TestBed.inject(CoachService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call VideoCategory query and add missing value', () => {
      const video: IVideo = { id: 456 };
      const videoCategory: IVideoCategory = { id: 12061 };
      video.videoCategory = videoCategory;

      const videoCategoryCollection: IVideoCategory[] = [{ id: 4577 }];
      jest.spyOn(videoCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: videoCategoryCollection })));
      const additionalVideoCategories = [videoCategory];
      const expectedCollection: IVideoCategory[] = [...additionalVideoCategories, ...videoCategoryCollection];
      jest.spyOn(videoCategoryService, 'addVideoCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ video });
      comp.ngOnInit();

      expect(videoCategoryService.query).toHaveBeenCalled();
      expect(videoCategoryService.addVideoCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        videoCategoryCollection,
        ...additionalVideoCategories.map(expect.objectContaining),
      );
      expect(comp.videoCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Coach query and add missing value', () => {
      const video: IVideo = { id: 456 };
      const coach: ICoach = { id: 31107 };
      video.coach = coach;

      const coachCollection: ICoach[] = [{ id: 7564 }];
      jest.spyOn(coachService, 'query').mockReturnValue(of(new HttpResponse({ body: coachCollection })));
      const additionalCoaches = [coach];
      const expectedCollection: ICoach[] = [...additionalCoaches, ...coachCollection];
      jest.spyOn(coachService, 'addCoachToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ video });
      comp.ngOnInit();

      expect(coachService.query).toHaveBeenCalled();
      expect(coachService.addCoachToCollectionIfMissing).toHaveBeenCalledWith(
        coachCollection,
        ...additionalCoaches.map(expect.objectContaining),
      );
      expect(comp.coachesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const video: IVideo = { id: 456 };
      const videoCategory: IVideoCategory = { id: 20026 };
      video.videoCategory = videoCategory;
      const coach: ICoach = { id: 23117 };
      video.coach = coach;

      activatedRoute.data = of({ video });
      comp.ngOnInit();

      expect(comp.videoCategoriesSharedCollection).toContain(videoCategory);
      expect(comp.coachesSharedCollection).toContain(coach);
      expect(comp.video).toEqual(video);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVideo>>();
      const video = { id: 123 };
      jest.spyOn(videoFormService, 'getVideo').mockReturnValue(video);
      jest.spyOn(videoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ video });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: video }));
      saveSubject.complete();

      // THEN
      expect(videoFormService.getVideo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(videoService.update).toHaveBeenCalledWith(expect.objectContaining(video));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVideo>>();
      const video = { id: 123 };
      jest.spyOn(videoFormService, 'getVideo').mockReturnValue({ id: null });
      jest.spyOn(videoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ video: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: video }));
      saveSubject.complete();

      // THEN
      expect(videoFormService.getVideo).toHaveBeenCalled();
      expect(videoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVideo>>();
      const video = { id: 123 };
      jest.spyOn(videoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ video });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(videoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVideoCategory', () => {
      it('Should forward to videoCategoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(videoCategoryService, 'compareVideoCategory');
        comp.compareVideoCategory(entity, entity2);
        expect(videoCategoryService.compareVideoCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCoach', () => {
      it('Should forward to coachService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(coachService, 'compareCoach');
        comp.compareCoach(entity, entity2);
        expect(coachService.compareCoach).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
