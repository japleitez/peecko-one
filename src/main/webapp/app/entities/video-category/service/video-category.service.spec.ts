import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVideoCategory } from '../video-category.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../video-category.test-samples';

import { VideoCategoryService, RestVideoCategory } from './video-category.service';

const requireRestSample: RestVideoCategory = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.toJSON(),
  released: sampleWithRequiredData.released?.toJSON(),
  archived: sampleWithRequiredData.archived?.toJSON(),
};

describe('VideoCategory Service', () => {
  let service: VideoCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IVideoCategory | IVideoCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VideoCategoryService);
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

    it('should create a VideoCategory', () => {
      const videoCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(videoCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VideoCategory', () => {
      const videoCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(videoCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VideoCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VideoCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VideoCategory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVideoCategoryToCollectionIfMissing', () => {
      it('should add a VideoCategory to an empty array', () => {
        const videoCategory: IVideoCategory = sampleWithRequiredData;
        expectedResult = service.addVideoCategoryToCollectionIfMissing([], videoCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(videoCategory);
      });

      it('should not add a VideoCategory to an array that contains it', () => {
        const videoCategory: IVideoCategory = sampleWithRequiredData;
        const videoCategoryCollection: IVideoCategory[] = [
          {
            ...videoCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVideoCategoryToCollectionIfMissing(videoCategoryCollection, videoCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VideoCategory to an array that doesn't contain it", () => {
        const videoCategory: IVideoCategory = sampleWithRequiredData;
        const videoCategoryCollection: IVideoCategory[] = [sampleWithPartialData];
        expectedResult = service.addVideoCategoryToCollectionIfMissing(videoCategoryCollection, videoCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(videoCategory);
      });

      it('should add only unique VideoCategory to an array', () => {
        const videoCategoryArray: IVideoCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const videoCategoryCollection: IVideoCategory[] = [sampleWithRequiredData];
        expectedResult = service.addVideoCategoryToCollectionIfMissing(videoCategoryCollection, ...videoCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const videoCategory: IVideoCategory = sampleWithRequiredData;
        const videoCategory2: IVideoCategory = sampleWithPartialData;
        expectedResult = service.addVideoCategoryToCollectionIfMissing([], videoCategory, videoCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(videoCategory);
        expect(expectedResult).toContain(videoCategory2);
      });

      it('should accept null and undefined values', () => {
        const videoCategory: IVideoCategory = sampleWithRequiredData;
        expectedResult = service.addVideoCategoryToCollectionIfMissing([], null, videoCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(videoCategory);
      });

      it('should return initial array if no VideoCategory is added', () => {
        const videoCategoryCollection: IVideoCategory[] = [sampleWithRequiredData];
        expectedResult = service.addVideoCategoryToCollectionIfMissing(videoCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(videoCategoryCollection);
      });
    });

    describe('compareVideoCategory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVideoCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVideoCategory(entity1, entity2);
        const compareResult2 = service.compareVideoCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVideoCategory(entity1, entity2);
        const compareResult2 = service.compareVideoCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVideoCategory(entity1, entity2);
        const compareResult2 = service.compareVideoCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
