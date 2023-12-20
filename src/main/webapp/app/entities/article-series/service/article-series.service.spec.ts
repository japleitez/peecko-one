import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IArticleSeries } from '../article-series.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../article-series.test-samples';

import { ArticleSeriesService, RestArticleSeries } from './article-series.service';

const requireRestSample: RestArticleSeries = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.toJSON(),
  updated: sampleWithRequiredData.updated?.toJSON(),
  released: sampleWithRequiredData.released?.toJSON(),
  archived: sampleWithRequiredData.archived?.toJSON(),
};

describe('ArticleSeries Service', () => {
  let service: ArticleSeriesService;
  let httpMock: HttpTestingController;
  let expectedResult: IArticleSeries | IArticleSeries[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ArticleSeriesService);
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

    it('should create a ArticleSeries', () => {
      const articleSeries = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(articleSeries).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ArticleSeries', () => {
      const articleSeries = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(articleSeries).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ArticleSeries', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ArticleSeries', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ArticleSeries', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArticleSeriesToCollectionIfMissing', () => {
      it('should add a ArticleSeries to an empty array', () => {
        const articleSeries: IArticleSeries = sampleWithRequiredData;
        expectedResult = service.addArticleSeriesToCollectionIfMissing([], articleSeries);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(articleSeries);
      });

      it('should not add a ArticleSeries to an array that contains it', () => {
        const articleSeries: IArticleSeries = sampleWithRequiredData;
        const articleSeriesCollection: IArticleSeries[] = [
          {
            ...articleSeries,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArticleSeriesToCollectionIfMissing(articleSeriesCollection, articleSeries);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ArticleSeries to an array that doesn't contain it", () => {
        const articleSeries: IArticleSeries = sampleWithRequiredData;
        const articleSeriesCollection: IArticleSeries[] = [sampleWithPartialData];
        expectedResult = service.addArticleSeriesToCollectionIfMissing(articleSeriesCollection, articleSeries);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(articleSeries);
      });

      it('should add only unique ArticleSeries to an array', () => {
        const articleSeriesArray: IArticleSeries[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const articleSeriesCollection: IArticleSeries[] = [sampleWithRequiredData];
        expectedResult = service.addArticleSeriesToCollectionIfMissing(articleSeriesCollection, ...articleSeriesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const articleSeries: IArticleSeries = sampleWithRequiredData;
        const articleSeries2: IArticleSeries = sampleWithPartialData;
        expectedResult = service.addArticleSeriesToCollectionIfMissing([], articleSeries, articleSeries2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(articleSeries);
        expect(expectedResult).toContain(articleSeries2);
      });

      it('should accept null and undefined values', () => {
        const articleSeries: IArticleSeries = sampleWithRequiredData;
        expectedResult = service.addArticleSeriesToCollectionIfMissing([], null, articleSeries, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(articleSeries);
      });

      it('should return initial array if no ArticleSeries is added', () => {
        const articleSeriesCollection: IArticleSeries[] = [sampleWithRequiredData];
        expectedResult = service.addArticleSeriesToCollectionIfMissing(articleSeriesCollection, undefined, null);
        expect(expectedResult).toEqual(articleSeriesCollection);
      });
    });

    describe('compareArticleSeries', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArticleSeries(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareArticleSeries(entity1, entity2);
        const compareResult2 = service.compareArticleSeries(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareArticleSeries(entity1, entity2);
        const compareResult2 = service.compareArticleSeries(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareArticleSeries(entity1, entity2);
        const compareResult2 = service.compareArticleSeries(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
