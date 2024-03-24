import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVideoCategory } from 'app/entities/video-category/video-category.model';
import { VideoCategoryService } from 'app/entities/video-category/service/video-category.service';
import { ICoach } from 'app/entities/coach/coach.model';
import { CoachService } from 'app/entities/coach/service/coach.service';
import { Language } from 'app/entities/enumerations/language.model';
import { Player } from 'app/entities/enumerations/player.model';
import { Intensity } from 'app/entities/enumerations/intensity.model';
import { VideoService } from '../service/video.service';
import { IVideo, VIDEO_ACCESS, VideoAccess } from '../video.model';
import { VideoFormService, VideoFormGroup } from './video-form.service';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-video-update',
  templateUrl: './video-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf]
})
export class VideoUpdateComponent implements OnInit {
  ua: VideoAccess = this.getVideoAccess();
  isSaving = false;
  video: IVideo | null = null;
  languageValues = Object.keys(Language);
  playerValues = Object.keys(Player);
  intensityValues = Object.keys(Intensity);

  videoCategoriesSharedCollection: IVideoCategory[] = [];
  coachesSharedCollection: ICoach[] = [];

  editForm: VideoFormGroup = this.videoFormService.createVideoFormGroup(undefined, this.getVideoAccess());

  constructor(
    protected videoService: VideoService,
    protected videoFormService: VideoFormService,
    protected videoCategoryService: VideoCategoryService,
    protected coachService: CoachService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareVideoCategory = (o1: IVideoCategory | null, o2: IVideoCategory | null): boolean =>
    this.videoCategoryService.compareVideoCategory(o1, o2);

  compareCoach = (o1: ICoach | null, o2: ICoach | null): boolean => this.coachService.compareCoach(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ video }) => {
      this.video = video;
      if (video) {
        this.updateForm(video);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const video = this.videoFormService.getVideo(this.editForm);
    if (video.id !== null) {
      this.subscribeToSaveResponse(this.videoService.update(video));
    } else {
      this.subscribeToSaveResponse(this.videoService.create(video));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideo>>): void {
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

  protected updateForm(video: IVideo): void {
    this.video = video;
    this.videoFormService.resetForm(this.editForm, video);

    this.videoCategoriesSharedCollection = this.videoCategoryService.addVideoCategoryToCollectionIfMissing<IVideoCategory>(
      this.videoCategoriesSharedCollection,
      video.videoCategory,
    );
    this.coachesSharedCollection = this.coachService.addCoachToCollectionIfMissing<ICoach>(this.coachesSharedCollection, video.coach);
  }

  protected loadRelationshipsOptions(): void {
    this.videoCategoryService
      .query()
      .pipe(map((res: HttpResponse<IVideoCategory[]>) => res.body ?? []))
      .pipe(
        map((videoCategories: IVideoCategory[]) =>
          this.videoCategoryService.addVideoCategoryToCollectionIfMissing<IVideoCategory>(videoCategories, this.video?.videoCategory),
        ),
      )
      .subscribe((videoCategories: IVideoCategory[]) => (this.videoCategoriesSharedCollection = videoCategories));

    this.coachService
      .query()
      .pipe(map((res: HttpResponse<ICoach[]>) => res.body ?? []))
      .pipe(map((coaches: ICoach[]) => this.coachService.addCoachToCollectionIfMissing<ICoach>(coaches, this.video?.coach)))
      .subscribe((coaches: ICoach[]) => (this.coachesSharedCollection = coaches));
  }

  protected getVideoAccess(): VideoAccess {
    return VIDEO_ACCESS;
  }

}
