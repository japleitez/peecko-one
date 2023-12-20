import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../play-list.test-samples';

import { PlayListFormService } from './play-list-form.service';

describe('PlayList Form Service', () => {
  let service: PlayListFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlayListFormService);
  });

  describe('Service methods', () => {
    describe('createPlayListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlayListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            counter: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            apsUser: expect.any(Object),
          }),
        );
      });

      it('passing IPlayList should create a new form with FormGroup', () => {
        const formGroup = service.createPlayListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            counter: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            apsUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlayList', () => {
      it('should return NewPlayList for default PlayList initial value', () => {
        const formGroup = service.createPlayListFormGroup(sampleWithNewData);

        const playList = service.getPlayList(formGroup) as any;

        expect(playList).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlayList for empty PlayList initial value', () => {
        const formGroup = service.createPlayListFormGroup();

        const playList = service.getPlayList(formGroup) as any;

        expect(playList).toMatchObject({});
      });

      it('should return IPlayList', () => {
        const formGroup = service.createPlayListFormGroup(sampleWithRequiredData);

        const playList = service.getPlayList(formGroup) as any;

        expect(playList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlayList should not enable id FormControl', () => {
        const formGroup = service.createPlayListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlayList should disable id FormControl', () => {
        const formGroup = service.createPlayListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
