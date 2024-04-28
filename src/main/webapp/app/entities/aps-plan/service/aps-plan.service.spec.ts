import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IApsPlan } from '../aps-plan.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aps-plan.test-samples';

import { ApsPlanService, RestApsPlan } from './aps-plan.service';

const requireRestSample: RestApsPlan = {
  ...sampleWithRequiredData,
  starts: sampleWithRequiredData.starts?.format(DATE_FORMAT),
  ends: sampleWithRequiredData.ends?.format(DATE_FORMAT),
  created: sampleWithRequiredData.created?.toJSON(),
  updated: sampleWithRequiredData.updated?.toJSON(),
};

describe('ApsPlan Service', () => {
  let service: ApsPlanService;
  let httpMock: HttpTestingController;
  let expectedResult: IApsPlan | IApsPlan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApsPlanService);
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

    it('should create a ApsPlan', () => {
      const apsPlan = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apsPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApsPlan', () => {
      const apsPlan = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apsPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApsPlan', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApsPlan', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApsPlan', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApsPlanToCollectionIfMissing', () => {
      it('should add a ApsPlan to an empty array', () => {
        const apsPlan: IApsPlan = sampleWithRequiredData;
        expectedResult = service.addApsPlanToCollectionIfMissing([], apsPlan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsPlan);
      });

      it('should not add a ApsPlan to an array that contains it', () => {
        const apsPlan: IApsPlan = sampleWithRequiredData;
        const apsPlanCollection: IApsPlan[] = [
          {
            ...apsPlan,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApsPlanToCollectionIfMissing(apsPlanCollection, apsPlan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApsPlan to an array that doesn't contain it", () => {
        const apsPlan: IApsPlan = sampleWithRequiredData;
        const apsPlanCollection: IApsPlan[] = [sampleWithPartialData];
        expectedResult = service.addApsPlanToCollectionIfMissing(apsPlanCollection, apsPlan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsPlan);
      });

      it('should add only unique ApsPlan to an array', () => {
        const apsPlanArray: IApsPlan[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apsPlanCollection: IApsPlan[] = [sampleWithRequiredData];
        expectedResult = service.addApsPlanToCollectionIfMissing(apsPlanCollection, ...apsPlanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apsPlan: IApsPlan = sampleWithRequiredData;
        const apsPlan2: IApsPlan = sampleWithPartialData;
        expectedResult = service.addApsPlanToCollectionIfMissing([], apsPlan, apsPlan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsPlan);
        expect(expectedResult).toContain(apsPlan2);
      });

      it('should accept null and undefined values', () => {
        const apsPlan: IApsPlan = sampleWithRequiredData;
        expectedResult = service.addApsPlanToCollectionIfMissing([], null, apsPlan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsPlan);
      });

      it('should return initial array if no ApsPlan is added', () => {
        const apsPlanCollection: IApsPlan[] = [sampleWithRequiredData];
        expectedResult = service.addApsPlanToCollectionIfMissing(apsPlanCollection, undefined, null);
        expect(expectedResult).toEqual(apsPlanCollection);
      });
    });

    describe('compareApsPlan', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApsPlan(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApsPlan(entity1, entity2);
        const compareResult2 = service.compareApsPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApsPlan(entity1, entity2);
        const compareResult2 = service.compareApsPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApsPlan(entity1, entity2);
        const compareResult2 = service.compareApsPlan(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
