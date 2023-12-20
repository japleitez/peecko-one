import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILabelTranslation } from '../label-translation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../label-translation.test-samples';

import { LabelTranslationService } from './label-translation.service';

const requireRestSample: ILabelTranslation = {
  ...sampleWithRequiredData,
};

describe('LabelTranslation Service', () => {
  let service: LabelTranslationService;
  let httpMock: HttpTestingController;
  let expectedResult: ILabelTranslation | ILabelTranslation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LabelTranslationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a LabelTranslation', () => {
      const labelTranslation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(labelTranslation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LabelTranslation', () => {
      const labelTranslation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(labelTranslation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LabelTranslation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LabelTranslation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LabelTranslation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLabelTranslationToCollectionIfMissing', () => {
      it('should add a LabelTranslation to an empty array', () => {
        const labelTranslation: ILabelTranslation = sampleWithRequiredData;
        expectedResult = service.addLabelTranslationToCollectionIfMissing([], labelTranslation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(labelTranslation);
      });

      it('should not add a LabelTranslation to an array that contains it', () => {
        const labelTranslation: ILabelTranslation = sampleWithRequiredData;
        const labelTranslationCollection: ILabelTranslation[] = [
          {
            ...labelTranslation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLabelTranslationToCollectionIfMissing(labelTranslationCollection, labelTranslation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LabelTranslation to an array that doesn't contain it", () => {
        const labelTranslation: ILabelTranslation = sampleWithRequiredData;
        const labelTranslationCollection: ILabelTranslation[] = [sampleWithPartialData];
        expectedResult = service.addLabelTranslationToCollectionIfMissing(labelTranslationCollection, labelTranslation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(labelTranslation);
      });

      it('should add only unique LabelTranslation to an array', () => {
        const labelTranslationArray: ILabelTranslation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const labelTranslationCollection: ILabelTranslation[] = [sampleWithRequiredData];
        expectedResult = service.addLabelTranslationToCollectionIfMissing(labelTranslationCollection, ...labelTranslationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const labelTranslation: ILabelTranslation = sampleWithRequiredData;
        const labelTranslation2: ILabelTranslation = sampleWithPartialData;
        expectedResult = service.addLabelTranslationToCollectionIfMissing([], labelTranslation, labelTranslation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(labelTranslation);
        expect(expectedResult).toContain(labelTranslation2);
      });

      it('should accept null and undefined values', () => {
        const labelTranslation: ILabelTranslation = sampleWithRequiredData;
        expectedResult = service.addLabelTranslationToCollectionIfMissing([], null, labelTranslation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(labelTranslation);
      });

      it('should return initial array if no LabelTranslation is added', () => {
        const labelTranslationCollection: ILabelTranslation[] = [sampleWithRequiredData];
        expectedResult = service.addLabelTranslationToCollectionIfMissing(labelTranslationCollection, undefined, null);
        expect(expectedResult).toEqual(labelTranslationCollection);
      });
    });

    describe('compareLabelTranslation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLabelTranslation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLabelTranslation(entity1, entity2);
        const compareResult2 = service.compareLabelTranslation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLabelTranslation(entity1, entity2);
        const compareResult2 = service.compareLabelTranslation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLabelTranslation(entity1, entity2);
        const compareResult2 = service.compareLabelTranslation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
