import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../label-translation.test-samples';

import { LabelTranslationFormService } from './label-translation-form.service';

describe('LabelTranslation Form Service', () => {
  let service: LabelTranslationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LabelTranslationFormService);
  });

  describe('Service methods', () => {
    describe('createLabelTranslationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLabelTranslationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            lang: expect.any(Object),
            translation: expect.any(Object),
          }),
        );
      });

      it('passing ILabelTranslation should create a new form with FormGroup', () => {
        const formGroup = service.createLabelTranslationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            lang: expect.any(Object),
            translation: expect.any(Object),
          }),
        );
      });
    });

    describe('getLabelTranslation', () => {
      it('should return NewLabelTranslation for default LabelTranslation initial value', () => {
        const formGroup = service.createLabelTranslationFormGroup(sampleWithNewData);

        const labelTranslation = service.getLabelTranslation(formGroup) as any;

        expect(labelTranslation).toMatchObject(sampleWithNewData);
      });

      it('should return NewLabelTranslation for empty LabelTranslation initial value', () => {
        const formGroup = service.createLabelTranslationFormGroup();

        const labelTranslation = service.getLabelTranslation(formGroup) as any;

        expect(labelTranslation).toMatchObject({});
      });

      it('should return ILabelTranslation', () => {
        const formGroup = service.createLabelTranslationFormGroup(sampleWithRequiredData);

        const labelTranslation = service.getLabelTranslation(formGroup) as any;

        expect(labelTranslation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILabelTranslation should not enable id FormControl', () => {
        const formGroup = service.createLabelTranslationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLabelTranslation should disable id FormControl', () => {
        const formGroup = service.createLabelTranslationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
