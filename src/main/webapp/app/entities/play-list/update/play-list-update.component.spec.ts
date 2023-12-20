import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IApsUser } from 'app/entities/aps-user/aps-user.model';
import { ApsUserService } from 'app/entities/aps-user/service/aps-user.service';
import { PlayListService } from '../service/play-list.service';
import { IPlayList } from '../play-list.model';
import { PlayListFormService } from './play-list-form.service';

import { PlayListUpdateComponent } from './play-list-update.component';

describe('PlayList Management Update Component', () => {
  let comp: PlayListUpdateComponent;
  let fixture: ComponentFixture<PlayListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playListFormService: PlayListFormService;
  let playListService: PlayListService;
  let apsUserService: ApsUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PlayListUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlayListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playListFormService = TestBed.inject(PlayListFormService);
    playListService = TestBed.inject(PlayListService);
    apsUserService = TestBed.inject(ApsUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApsUser query and add missing value', () => {
      const playList: IPlayList = { id: 456 };
      const apsUser: IApsUser = { id: 30662 };
      playList.apsUser = apsUser;

      const apsUserCollection: IApsUser[] = [{ id: 8814 }];
      jest.spyOn(apsUserService, 'query').mockReturnValue(of(new HttpResponse({ body: apsUserCollection })));
      const additionalApsUsers = [apsUser];
      const expectedCollection: IApsUser[] = [...additionalApsUsers, ...apsUserCollection];
      jest.spyOn(apsUserService, 'addApsUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playList });
      comp.ngOnInit();

      expect(apsUserService.query).toHaveBeenCalled();
      expect(apsUserService.addApsUserToCollectionIfMissing).toHaveBeenCalledWith(
        apsUserCollection,
        ...additionalApsUsers.map(expect.objectContaining),
      );
      expect(comp.apsUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const playList: IPlayList = { id: 456 };
      const apsUser: IApsUser = { id: 20758 };
      playList.apsUser = apsUser;

      activatedRoute.data = of({ playList });
      comp.ngOnInit();

      expect(comp.apsUsersSharedCollection).toContain(apsUser);
      expect(comp.playList).toEqual(playList);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayList>>();
      const playList = { id: 123 };
      jest.spyOn(playListFormService, 'getPlayList').mockReturnValue(playList);
      jest.spyOn(playListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playList }));
      saveSubject.complete();

      // THEN
      expect(playListFormService.getPlayList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playListService.update).toHaveBeenCalledWith(expect.objectContaining(playList));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayList>>();
      const playList = { id: 123 };
      jest.spyOn(playListFormService, 'getPlayList').mockReturnValue({ id: null });
      jest.spyOn(playListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playList }));
      saveSubject.complete();

      // THEN
      expect(playListFormService.getPlayList).toHaveBeenCalled();
      expect(playListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayList>>();
      const playList = { id: 123 };
      jest.spyOn(playListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApsUser', () => {
      it('Should forward to apsUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(apsUserService, 'compareApsUser');
        comp.compareApsUser(entity, entity2);
        expect(apsUserService.compareApsUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
