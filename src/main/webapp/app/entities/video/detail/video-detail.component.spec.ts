import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VideoDetailComponent } from './video-detail.component';

describe('Video Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: VideoDetailComponent,
              resolve: { video: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VideoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load video on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VideoDetailComponent);

      // THEN
      expect(instance.video).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
