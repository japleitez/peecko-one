import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApsMembership } from '../aps-membership.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aps-membership.test-samples';

import { ApsMembershipService } from './aps-membership.service';

const requireRestSample: IApsMembership = {
  ...sampleWithRequiredData,
};

describe('ApsMembership Service', () => {
  let service: ApsMembershipService;
  let httpMock: HttpTestingController;
  let expectedResult: IApsMembership | IApsMembership[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApsMembershipService);
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

    it('should create a ApsMembership', () => {
      const apsMembership = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apsMembership).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApsMembership', () => {
      const apsMembership = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apsMembership).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApsMembership', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApsMembership', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApsMembership', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApsMembershipToCollectionIfMissing', () => {
      it('should add a ApsMembership to an empty array', () => {
        const apsMembership: IApsMembership = sampleWithRequiredData;
        expectedResult = service.addApsMembershipToCollectionIfMissing([], apsMembership);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsMembership);
      });

      it('should not add a ApsMembership to an array that contains it', () => {
        const apsMembership: IApsMembership = sampleWithRequiredData;
        const apsMembershipCollection: IApsMembership[] = [
          {
            ...apsMembership,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApsMembershipToCollectionIfMissing(apsMembershipCollection, apsMembership);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApsMembership to an array that doesn't contain it", () => {
        const apsMembership: IApsMembership = sampleWithRequiredData;
        const apsMembershipCollection: IApsMembership[] = [sampleWithPartialData];
        expectedResult = service.addApsMembershipToCollectionIfMissing(apsMembershipCollection, apsMembership);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsMembership);
      });

      it('should add only unique ApsMembership to an array', () => {
        const apsMembershipArray: IApsMembership[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apsMembershipCollection: IApsMembership[] = [sampleWithRequiredData];
        expectedResult = service.addApsMembershipToCollectionIfMissing(apsMembershipCollection, ...apsMembershipArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apsMembership: IApsMembership = sampleWithRequiredData;
        const apsMembership2: IApsMembership = sampleWithPartialData;
        expectedResult = service.addApsMembershipToCollectionIfMissing([], apsMembership, apsMembership2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsMembership);
        expect(expectedResult).toContain(apsMembership2);
      });

      it('should accept null and undefined values', () => {
        const apsMembership: IApsMembership = sampleWithRequiredData;
        expectedResult = service.addApsMembershipToCollectionIfMissing([], null, apsMembership, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsMembership);
      });

      it('should return initial array if no ApsMembership is added', () => {
        const apsMembershipCollection: IApsMembership[] = [sampleWithRequiredData];
        expectedResult = service.addApsMembershipToCollectionIfMissing(apsMembershipCollection, undefined, null);
        expect(expectedResult).toEqual(apsMembershipCollection);
      });
    });

    describe('compareApsMembership', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApsMembership(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApsMembership(entity1, entity2);
        const compareResult2 = service.compareApsMembership(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApsMembership(entity1, entity2);
        const compareResult2 = service.compareApsMembership(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApsMembership(entity1, entity2);
        const compareResult2 = service.compareApsMembership(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
