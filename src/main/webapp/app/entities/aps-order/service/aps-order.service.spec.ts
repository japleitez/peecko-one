import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApsOrder } from '../aps-order.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aps-order.test-samples';

import { ApsOrderService } from './aps-order.service';

const requireRestSample: IApsOrder = {
  ...sampleWithRequiredData,
};

describe('ApsOrder Service', () => {
  let service: ApsOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IApsOrder | IApsOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApsOrderService);
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

    it('should create a ApsOrder', () => {
      const apsOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apsOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApsOrder', () => {
      const apsOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apsOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApsOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApsOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApsOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApsOrderToCollectionIfMissing', () => {
      it('should add a ApsOrder to an empty array', () => {
        const apsOrder: IApsOrder = sampleWithRequiredData;
        expectedResult = service.addApsOrderToCollectionIfMissing([], apsOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsOrder);
      });

      it('should not add a ApsOrder to an array that contains it', () => {
        const apsOrder: IApsOrder = sampleWithRequiredData;
        const apsOrderCollection: IApsOrder[] = [
          {
            ...apsOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApsOrderToCollectionIfMissing(apsOrderCollection, apsOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApsOrder to an array that doesn't contain it", () => {
        const apsOrder: IApsOrder = sampleWithRequiredData;
        const apsOrderCollection: IApsOrder[] = [sampleWithPartialData];
        expectedResult = service.addApsOrderToCollectionIfMissing(apsOrderCollection, apsOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsOrder);
      });

      it('should add only unique ApsOrder to an array', () => {
        const apsOrderArray: IApsOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apsOrderCollection: IApsOrder[] = [sampleWithRequiredData];
        expectedResult = service.addApsOrderToCollectionIfMissing(apsOrderCollection, ...apsOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apsOrder: IApsOrder = sampleWithRequiredData;
        const apsOrder2: IApsOrder = sampleWithPartialData;
        expectedResult = service.addApsOrderToCollectionIfMissing([], apsOrder, apsOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsOrder);
        expect(expectedResult).toContain(apsOrder2);
      });

      it('should accept null and undefined values', () => {
        const apsOrder: IApsOrder = sampleWithRequiredData;
        expectedResult = service.addApsOrderToCollectionIfMissing([], null, apsOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsOrder);
      });

      it('should return initial array if no ApsOrder is added', () => {
        const apsOrderCollection: IApsOrder[] = [sampleWithRequiredData];
        expectedResult = service.addApsOrderToCollectionIfMissing(apsOrderCollection, undefined, null);
        expect(expectedResult).toEqual(apsOrderCollection);
      });
    });

    describe('compareApsOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApsOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApsOrder(entity1, entity2);
        const compareResult2 = service.compareApsOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApsOrder(entity1, entity2);
        const compareResult2 = service.compareApsOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApsOrder(entity1, entity2);
        const compareResult2 = service.compareApsOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
