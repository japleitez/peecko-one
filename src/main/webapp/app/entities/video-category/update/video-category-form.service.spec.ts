import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../video-category.test-samples';

import { VideoCategoryFormService } from './video-category-form.service';

describe('VideoCategory Form Service', () => {
  let service: VideoCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VideoCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createVideoCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVideoCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            title: expect.any(Object),
            label: expect.any(Object),
            created: expect.any(Object),
            released: expect.any(Object),
            archived: expect.any(Object),
          }),
        );
      });

      it('passing IVideoCategory should create a new form with FormGroup', () => {
        const formGroup = service.createVideoCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            title: expect.any(Object),
            label: expect.any(Object),
            created: expect.any(Object),
            released: expect.any(Object),
            archived: expect.any(Object),
          }),
        );
      });
    });

    describe('getVideoCategory', () => {
      it('should return NewVideoCategory for default VideoCategory initial value', () => {
        const formGroup = service.createVideoCategoryFormGroup(sampleWithNewData);

        const videoCategory = service.getVideoCategory(formGroup) as any;

        expect(videoCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewVideoCategory for empty VideoCategory initial value', () => {
        const formGroup = service.createVideoCategoryFormGroup();

        const videoCategory = service.getVideoCategory(formGroup) as any;

        expect(videoCategory).toMatchObject({});
      });

      it('should return IVideoCategory', () => {
        const formGroup = service.createVideoCategoryFormGroup(sampleWithRequiredData);

        const videoCategory = service.getVideoCategory(formGroup) as any;

        expect(videoCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVideoCategory should not enable id FormControl', () => {
        const formGroup = service.createVideoCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVideoCategory should disable id FormControl', () => {
        const formGroup = service.createVideoCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
