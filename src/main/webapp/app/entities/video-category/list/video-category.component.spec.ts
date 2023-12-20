import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VideoCategoryService } from '../service/video-category.service';

import { VideoCategoryComponent } from './video-category.component';

describe('VideoCategory Management Component', () => {
  let comp: VideoCategoryComponent;
  let fixture: ComponentFixture<VideoCategoryComponent>;
  let service: VideoCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'video-category', component: VideoCategoryComponent }]),
        HttpClientTestingModule,
        VideoCategoryComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(VideoCategoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VideoCategoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VideoCategoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.videoCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to videoCategoryService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getVideoCategoryIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getVideoCategoryIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
