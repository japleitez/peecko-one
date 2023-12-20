import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlayListDetailComponent } from './play-list-detail.component';

describe('PlayList Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayListDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PlayListDetailComponent,
              resolve: { playList: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlayListDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load playList on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlayListDetailComponent);

      // THEN
      expect(instance.playList).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
