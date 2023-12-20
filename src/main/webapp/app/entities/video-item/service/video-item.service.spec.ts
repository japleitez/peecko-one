import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVideoItem } from '../video-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../video-item.test-samples';

import { VideoItemService } from './video-item.service';

const requireRestSample: IVideoItem = {
  ...sampleWithRequiredData,
};

describe('VideoItem Service', () => {
  let service: VideoItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IVideoItem | IVideoItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VideoItemService);
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

    it('should create a VideoItem', () => {
      const videoItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(videoItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VideoItem', () => {
      const videoItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(videoItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VideoItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VideoItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VideoItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVideoItemToCollectionIfMissing', () => {
      it('should add a VideoItem to an empty array', () => {
        const videoItem: IVideoItem = sampleWithRequiredData;
        expectedResult = service.addVideoItemToCollectionIfMissing([], videoItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(videoItem);
      });

      it('should not add a VideoItem to an array that contains it', () => {
        const videoItem: IVideoItem = sampleWithRequiredData;
        const videoItemCollection: IVideoItem[] = [
          {
            ...videoItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVideoItemToCollectionIfMissing(videoItemCollection, videoItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VideoItem to an array that doesn't contain it", () => {
        const videoItem: IVideoItem = sampleWithRequiredData;
        const videoItemCollection: IVideoItem[] = [sampleWithPartialData];
        expectedResult = service.addVideoItemToCollectionIfMissing(videoItemCollection, videoItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(videoItem);
      });

      it('should add only unique VideoItem to an array', () => {
        const videoItemArray: IVideoItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const videoItemCollection: IVideoItem[] = [sampleWithRequiredData];
        expectedResult = service.addVideoItemToCollectionIfMissing(videoItemCollection, ...videoItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const videoItem: IVideoItem = sampleWithRequiredData;
        const videoItem2: IVideoItem = sampleWithPartialData;
        expectedResult = service.addVideoItemToCollectionIfMissing([], videoItem, videoItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(videoItem);
        expect(expectedResult).toContain(videoItem2);
      });

      it('should accept null and undefined values', () => {
        const videoItem: IVideoItem = sampleWithRequiredData;
        expectedResult = service.addVideoItemToCollectionIfMissing([], null, videoItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(videoItem);
      });

      it('should return initial array if no VideoItem is added', () => {
        const videoItemCollection: IVideoItem[] = [sampleWithRequiredData];
        expectedResult = service.addVideoItemToCollectionIfMissing(videoItemCollection, undefined, null);
        expect(expectedResult).toEqual(videoItemCollection);
      });
    });

    describe('compareVideoItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVideoItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVideoItem(entity1, entity2);
        const compareResult2 = service.compareVideoItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVideoItem(entity1, entity2);
        const compareResult2 = service.compareVideoItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVideoItem(entity1, entity2);
        const compareResult2 = service.compareVideoItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
