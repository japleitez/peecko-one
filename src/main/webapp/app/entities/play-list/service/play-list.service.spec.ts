import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlayList } from '../play-list.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../play-list.test-samples';

import { PlayListService, RestPlayList } from './play-list.service';

const requireRestSample: RestPlayList = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.toJSON(),
  updated: sampleWithRequiredData.updated?.toJSON(),
};

describe('PlayList Service', () => {
  let service: PlayListService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlayList | IPlayList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlayListService);
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

    it('should create a PlayList', () => {
      const playList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlayList', () => {
      const playList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlayList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlayList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlayList', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlayListToCollectionIfMissing', () => {
      it('should add a PlayList to an empty array', () => {
        const playList: IPlayList = sampleWithRequiredData;
        expectedResult = service.addPlayListToCollectionIfMissing([], playList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playList);
      });

      it('should not add a PlayList to an array that contains it', () => {
        const playList: IPlayList = sampleWithRequiredData;
        const playListCollection: IPlayList[] = [
          {
            ...playList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlayListToCollectionIfMissing(playListCollection, playList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlayList to an array that doesn't contain it", () => {
        const playList: IPlayList = sampleWithRequiredData;
        const playListCollection: IPlayList[] = [sampleWithPartialData];
        expectedResult = service.addPlayListToCollectionIfMissing(playListCollection, playList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playList);
      });

      it('should add only unique PlayList to an array', () => {
        const playListArray: IPlayList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playListCollection: IPlayList[] = [sampleWithRequiredData];
        expectedResult = service.addPlayListToCollectionIfMissing(playListCollection, ...playListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playList: IPlayList = sampleWithRequiredData;
        const playList2: IPlayList = sampleWithPartialData;
        expectedResult = service.addPlayListToCollectionIfMissing([], playList, playList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playList);
        expect(expectedResult).toContain(playList2);
      });

      it('should accept null and undefined values', () => {
        const playList: IPlayList = sampleWithRequiredData;
        expectedResult = service.addPlayListToCollectionIfMissing([], null, playList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playList);
      });

      it('should return initial array if no PlayList is added', () => {
        const playListCollection: IPlayList[] = [sampleWithRequiredData];
        expectedResult = service.addPlayListToCollectionIfMissing(playListCollection, undefined, null);
        expect(expectedResult).toEqual(playListCollection);
      });
    });

    describe('comparePlayList', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlayList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlayList(entity1, entity2);
        const compareResult2 = service.comparePlayList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlayList(entity1, entity2);
        const compareResult2 = service.comparePlayList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlayList(entity1, entity2);
        const compareResult2 = service.comparePlayList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
