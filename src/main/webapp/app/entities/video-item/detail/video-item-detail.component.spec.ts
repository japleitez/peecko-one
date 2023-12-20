import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VideoItemDetailComponent } from './video-item-detail.component';

describe('VideoItem Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoItemDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: VideoItemDetailComponent,
              resolve: { videoItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VideoItemDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load videoItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VideoItemDetailComponent);

      // THEN
      expect(instance.videoItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
