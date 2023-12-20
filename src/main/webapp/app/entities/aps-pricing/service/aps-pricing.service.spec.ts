import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApsPricing } from '../aps-pricing.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aps-pricing.test-samples';

import { ApsPricingService } from './aps-pricing.service';

const requireRestSample: IApsPricing = {
  ...sampleWithRequiredData,
};

describe('ApsPricing Service', () => {
  let service: ApsPricingService;
  let httpMock: HttpTestingController;
  let expectedResult: IApsPricing | IApsPricing[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApsPricingService);
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

    it('should create a ApsPricing', () => {
      const apsPricing = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apsPricing).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApsPricing', () => {
      const apsPricing = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apsPricing).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApsPricing', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApsPricing', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApsPricing', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApsPricingToCollectionIfMissing', () => {
      it('should add a ApsPricing to an empty array', () => {
        const apsPricing: IApsPricing = sampleWithRequiredData;
        expectedResult = service.addApsPricingToCollectionIfMissing([], apsPricing);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsPricing);
      });

      it('should not add a ApsPricing to an array that contains it', () => {
        const apsPricing: IApsPricing = sampleWithRequiredData;
        const apsPricingCollection: IApsPricing[] = [
          {
            ...apsPricing,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApsPricingToCollectionIfMissing(apsPricingCollection, apsPricing);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApsPricing to an array that doesn't contain it", () => {
        const apsPricing: IApsPricing = sampleWithRequiredData;
        const apsPricingCollection: IApsPricing[] = [sampleWithPartialData];
        expectedResult = service.addApsPricingToCollectionIfMissing(apsPricingCollection, apsPricing);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsPricing);
      });

      it('should add only unique ApsPricing to an array', () => {
        const apsPricingArray: IApsPricing[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apsPricingCollection: IApsPricing[] = [sampleWithRequiredData];
        expectedResult = service.addApsPricingToCollectionIfMissing(apsPricingCollection, ...apsPricingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apsPricing: IApsPricing = sampleWithRequiredData;
        const apsPricing2: IApsPricing = sampleWithPartialData;
        expectedResult = service.addApsPricingToCollectionIfMissing([], apsPricing, apsPricing2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsPricing);
        expect(expectedResult).toContain(apsPricing2);
      });

      it('should accept null and undefined values', () => {
        const apsPricing: IApsPricing = sampleWithRequiredData;
        expectedResult = service.addApsPricingToCollectionIfMissing([], null, apsPricing, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsPricing);
      });

      it('should return initial array if no ApsPricing is added', () => {
        const apsPricingCollection: IApsPricing[] = [sampleWithRequiredData];
        expectedResult = service.addApsPricingToCollectionIfMissing(apsPricingCollection, undefined, null);
        expect(expectedResult).toEqual(apsPricingCollection);
      });
    });

    describe('compareApsPricing', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApsPricing(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApsPricing(entity1, entity2);
        const compareResult2 = service.compareApsPricing(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApsPricing(entity1, entity2);
        const compareResult2 = service.compareApsPricing(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApsPricing(entity1, entity2);
        const compareResult2 = service.compareApsPricing(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
