import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApsDevice } from '../aps-device.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aps-device.test-samples';

import { ApsDeviceService, RestApsDevice } from './aps-device.service';

const requireRestSample: RestApsDevice = {
  ...sampleWithRequiredData,
  installedOn: sampleWithRequiredData.installedOn?.toJSON(),
};

describe('ApsDevice Service', () => {
  let service: ApsDeviceService;
  let httpMock: HttpTestingController;
  let expectedResult: IApsDevice | IApsDevice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApsDeviceService);
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

    it('should create a ApsDevice', () => {
      const apsDevice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(apsDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApsDevice', () => {
      const apsDevice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(apsDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApsDevice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApsDevice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApsDevice', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApsDeviceToCollectionIfMissing', () => {
      it('should add a ApsDevice to an empty array', () => {
        const apsDevice: IApsDevice = sampleWithRequiredData;
        expectedResult = service.addApsDeviceToCollectionIfMissing([], apsDevice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsDevice);
      });

      it('should not add a ApsDevice to an array that contains it', () => {
        const apsDevice: IApsDevice = sampleWithRequiredData;
        const apsDeviceCollection: IApsDevice[] = [
          {
            ...apsDevice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApsDeviceToCollectionIfMissing(apsDeviceCollection, apsDevice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApsDevice to an array that doesn't contain it", () => {
        const apsDevice: IApsDevice = sampleWithRequiredData;
        const apsDeviceCollection: IApsDevice[] = [sampleWithPartialData];
        expectedResult = service.addApsDeviceToCollectionIfMissing(apsDeviceCollection, apsDevice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsDevice);
      });

      it('should add only unique ApsDevice to an array', () => {
        const apsDeviceArray: IApsDevice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const apsDeviceCollection: IApsDevice[] = [sampleWithRequiredData];
        expectedResult = service.addApsDeviceToCollectionIfMissing(apsDeviceCollection, ...apsDeviceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apsDevice: IApsDevice = sampleWithRequiredData;
        const apsDevice2: IApsDevice = sampleWithPartialData;
        expectedResult = service.addApsDeviceToCollectionIfMissing([], apsDevice, apsDevice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apsDevice);
        expect(expectedResult).toContain(apsDevice2);
      });

      it('should accept null and undefined values', () => {
        const apsDevice: IApsDevice = sampleWithRequiredData;
        expectedResult = service.addApsDeviceToCollectionIfMissing([], null, apsDevice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apsDevice);
      });

      it('should return initial array if no ApsDevice is added', () => {
        const apsDeviceCollection: IApsDevice[] = [sampleWithRequiredData];
        expectedResult = service.addApsDeviceToCollectionIfMissing(apsDeviceCollection, undefined, null);
        expect(expectedResult).toEqual(apsDeviceCollection);
      });
    });

    describe('compareApsDevice', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApsDevice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApsDevice(entity1, entity2);
        const compareResult2 = service.compareApsDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApsDevice(entity1, entity2);
        const compareResult2 = service.compareApsDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApsDevice(entity1, entity2);
        const compareResult2 = service.compareApsDevice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
