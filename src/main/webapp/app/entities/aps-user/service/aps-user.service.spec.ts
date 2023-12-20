import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApsUser } from '../aps-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aps-user.test-samples';

import { ApsUserService, RestApsUser } from './aps-user.service';

const requireRestSample: RestApsUser = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.toJSON(),
  updated: sampleWithRequiredData.updated?.toJSON(),
};

describe('ApsUser Service', () => {
  let service: ApsUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IApsUser | IApsUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApsUserService);
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

    it('should create a ApsUser', () => {
      const apsUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apsUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApsUser', () => {
      const apsUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apsUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApsUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApsUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApsUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApsUserToCollectionIfMissing', () => {
      it('should add a ApsUser to an empty array', () => {
        const apsUser: IApsUser = sampleWithRequiredData;
        expectedResult = service.addApsUserToCollectionIfMissing([], apsUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsUser);
      });

      it('should not add a ApsUser to an array that contains it', () => {
        const apsUser: IApsUser = sampleWithRequiredData;
        const apsUserCollection: IApsUser[] = [
          {
            ...apsUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApsUserToCollectionIfMissing(apsUserCollection, apsUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApsUser to an array that doesn't contain it", () => {
        const apsUser: IApsUser = sampleWithRequiredData;
        const apsUserCollection: IApsUser[] = [sampleWithPartialData];
        expectedResult = service.addApsUserToCollectionIfMissing(apsUserCollection, apsUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsUser);
      });

      it('should add only unique ApsUser to an array', () => {
        const apsUserArray: IApsUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apsUserCollection: IApsUser[] = [sampleWithRequiredData];
        expectedResult = service.addApsUserToCollectionIfMissing(apsUserCollection, ...apsUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apsUser: IApsUser = sampleWithRequiredData;
        const apsUser2: IApsUser = sampleWithPartialData;
        expectedResult = service.addApsUserToCollectionIfMissing([], apsUser, apsUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsUser);
        expect(expectedResult).toContain(apsUser2);
      });

      it('should accept null and undefined values', () => {
        const apsUser: IApsUser = sampleWithRequiredData;
        expectedResult = service.addApsUserToCollectionIfMissing([], null, apsUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsUser);
      });

      it('should return initial array if no ApsUser is added', () => {
        const apsUserCollection: IApsUser[] = [sampleWithRequiredData];
        expectedResult = service.addApsUserToCollectionIfMissing(apsUserCollection, undefined, null);
        expect(expectedResult).toEqual(apsUserCollection);
      });
    });

    describe('compareApsUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApsUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApsUser(entity1, entity2);
        const compareResult2 = service.compareApsUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApsUser(entity1, entity2);
        const compareResult2 = service.compareApsUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApsUser(entity1, entity2);
        const compareResult2 = service.compareApsUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
