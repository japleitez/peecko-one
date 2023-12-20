import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VideoCategoryDetailComponent } from './video-category-detail.component';

describe('VideoCategory Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoCategoryDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: VideoCategoryDetailComponent,
              resolve: { videoCategory: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VideoCategoryDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load videoCategory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VideoCategoryDetailComponent);

      // THEN
      expect(instance.videoCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
