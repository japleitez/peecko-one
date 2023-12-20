import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../video-item.test-samples';

import { VideoItemFormService } from './video-item-form.service';

describe('VideoItem Form Service', () => {
  let service: VideoItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VideoItemFormService);
  });

  describe('Service methods', () => {
    describe('createVideoItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVideoItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            previous: expect.any(Object),
            code: expect.any(Object),
            next: expect.any(Object),
            playList: expect.any(Object),
          }),
        );
      });

      it('passing IVideoItem should create a new form with FormGroup', () => {
        const formGroup = service.createVideoItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            previous: expect.any(Object),
            code: expect.any(Object),
            next: expect.any(Object),
            playList: expect.any(Object),
          }),
        );
      });
    });

    describe('getVideoItem', () => {
      it('should return NewVideoItem for default VideoItem initial value', () => {
        const formGroup = service.createVideoItemFormGroup(sampleWithNewData);

        const videoItem = service.getVideoItem(formGroup) as any;

        expect(videoItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewVideoItem for empty VideoItem initial value', () => {
        const formGroup = service.createVideoItemFormGroup();

        const videoItem = service.getVideoItem(formGroup) as any;

        expect(videoItem).toMatchObject({});
      });

      it('should return IVideoItem', () => {
        const formGroup = service.createVideoItemFormGroup(sampleWithRequiredData);

        const videoItem = service.getVideoItem(formGroup) as any;

        expect(videoItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVideoItem should not enable id FormControl', () => {
        const formGroup = service.createVideoItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVideoItem should disable id FormControl', () => {
        const formGroup = service.createVideoItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
