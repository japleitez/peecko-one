import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApsUser } from 'app/entities/aps-user/aps-user.model';
import { ApsUserService } from 'app/entities/aps-user/service/aps-user.service';
import { IPlayList } from '../play-list.model';
import { PlayListService } from '../service/play-list.service';
import { PlayListFormService, PlayListFormGroup } from './play-list-form.service';

@Component({
  standalone: true,
  selector: 'jhi-play-list-update',
  templateUrl: './play-list-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlayListUpdateComponent implements OnInit {
  isSaving = false;
  playList: IPlayList | null = null;

  apsUsersSharedCollection: IApsUser[] = [];

  editForm: PlayListFormGroup = this.playListFormService.createPlayListFormGroup();

  constructor(
    protected playListService: PlayListService,
    protected playListFormService: PlayListFormService,
    protected apsUserService: ApsUserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApsUser = (o1: IApsUser | null, o2: IApsUser | null): boolean => this.apsUserService.compareApsUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playList }) => {
      this.playList = playList;
      if (playList) {
        this.updateForm(playList);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playList = this.playListFormService.getPlayList(this.editForm);
    if (playList.id !== null) {
      this.subscribeToSaveResponse(this.playListService.update(playList));
    } else {
      this.subscribeToSaveResponse(this.playListService.create(playList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayList>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(playList: IPlayList): void {
    this.playList = playList;
    this.playListFormService.resetForm(this.editForm, playList);

    this.apsUsersSharedCollection = this.apsUserService.addApsUserToCollectionIfMissing<IApsUser>(
      this.apsUsersSharedCollection,
      playList.apsUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apsUserService
      .query()
      .pipe(map((res: HttpResponse<IApsUser[]>) => res.body ?? []))
      .pipe(map((apsUsers: IApsUser[]) => this.apsUserService.addApsUserToCollectionIfMissing<IApsUser>(apsUsers, this.playList?.apsUser)))
      .subscribe((apsUsers: IApsUser[]) => (this.apsUsersSharedCollection = apsUsers));
  }
}
