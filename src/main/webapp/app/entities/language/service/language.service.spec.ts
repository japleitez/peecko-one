import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { LanguageService } from './language.service';

describe('Language Service', () => {
  let service: LanguageService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(LanguageService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('queryActiveCodes', () => {
    it('should return the language codes from the backend', () => {
      let result: string[] | undefined;
      service.queryActiveCodes().subscribe(codes => (result = codes));

      const req = httpMock.expectOne({ method: 'GET' });
      expect(req.request.url).toContain('api/languages/codes');
      req.flush(['EN', 'FR']);

      expect(result).toEqual(['EN', 'FR']);
    });

    it('should log the failure and return an empty list when the languages cannot be loaded', () => {
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => undefined);
      let result: string[] | undefined;
      service.queryActiveCodes().subscribe(codes => (result = codes));

      httpMock.expectOne({ method: 'GET' }).flush('error', { status: 503, statusText: 'Service Unavailable' });

      expect(result).toEqual([]);
      expect(consoleSpy).toHaveBeenCalled();
      consoleSpy.mockRestore();
    });
  });

  describe('addLanguageCodeIfMissing', () => {
    it('should add the current code when it is missing', () => {
      expect(service.addLanguageCodeIfMissing(['EN'], 'IT')).toEqual(['EN', 'IT']);
    });

    it('should not duplicate an existing code or add an empty one', () => {
      expect(service.addLanguageCodeIfMissing(['EN'], 'EN')).toEqual(['EN']);
      expect(service.addLanguageCodeIfMissing([], null)).toEqual([]);
    });
  });
});
