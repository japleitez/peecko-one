import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlayList } from 'app/entities/play-list/play-list.model';
import { PlayListService } from 'app/entities/play-list/service/play-list.service';
import { IVideoItem } from '../video-item.model';
import { VideoItemService } from '../service/video-item.service';
import { VideoItemFormService, VideoItemFormGroup } from './video-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-video-item-update',
  templateUrl: './video-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VideoItemUpdateComponent implements OnInit {
  isSaving = false;
  videoItem: IVideoItem | null = null;

  playListsSharedCollection: IPlayList[] = [];

  editForm: VideoItemFormGroup = this.videoItemFormService.createVideoItemFormGroup();

  constructor(
    protected videoItemService: VideoItemService,
    protected videoItemFormService: VideoItemFormService,
    protected playListService: PlayListService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePlayList = (o1: IPlayList | null, o2: IPlayList | null): boolean => this.playListService.comparePlayList(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videoItem }) => {
      this.videoItem = videoItem;
      if (videoItem) {
        this.updateForm(videoItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const videoItem = this.videoItemFormService.getVideoItem(this.editForm);
    if (videoItem.id !== null) {
      this.subscribeToSaveResponse(this.videoItemService.update(videoItem));
    } else {
      this.subscribeToSaveResponse(this.videoItemService.create(videoItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideoItem>>): void {
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

  protected updateForm(videoItem: IVideoItem): void {
    this.videoItem = videoItem;
    this.videoItemFormService.resetForm(this.editForm, videoItem);

    this.playListsSharedCollection = this.playListService.addPlayListToCollectionIfMissing<IPlayList>(
      this.playListsSharedCollection,
      videoItem.playList,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playListService
      .query()
      .pipe(map((res: HttpResponse<IPlayList[]>) => res.body ?? []))
      .pipe(
        map((playLists: IPlayList[]) =>
          this.playListService.addPlayListToCollectionIfMissing<IPlayList>(playLists, this.videoItem?.playList),
        ),
      )
      .subscribe((playLists: IPlayList[]) => (this.playListsSharedCollection = playLists));
  }
}
