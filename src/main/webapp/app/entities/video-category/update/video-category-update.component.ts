import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVideoCategory, VIDEO_CATEGORY_ACCESS, VideoCategoryAccess } from '../video-category.model';
import { VideoCategoryService } from '../service/video-category.service';
import { VideoCategoryFormService, VideoCategoryFormGroup } from './video-category-form.service';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-video-category-update',
  templateUrl: './video-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf]
})
export class VideoCategoryUpdateComponent implements OnInit {
  ua: VideoCategoryAccess = this.getVideoCategoryAccess();
  isSaving = false;
  videoCategory: IVideoCategory | null = null;

  editForm: VideoCategoryFormGroup = this.videoCategoryFormService.createVideoCategoryFormGroup(undefined, this.getVideoCategoryAccess());

  constructor(
    protected videoCategoryService: VideoCategoryService,
    protected videoCategoryFormService: VideoCategoryFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videoCategory }) => {
      this.videoCategory = videoCategory;
      if (videoCategory) {
        this.updateForm(videoCategory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const videoCategory = this.videoCategoryFormService.getVideoCategory(this.editForm);
    if (videoCategory.id !== null) {
      this.subscribeToSaveResponse(this.videoCategoryService.update(videoCategory));
    } else {
      this.subscribeToSaveResponse(this.videoCategoryService.create(videoCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVideoCategory>>): void {
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

  protected updateForm(videoCategory: IVideoCategory): void {
    this.videoCategory = videoCategory;
    this.videoCategoryFormService.resetForm(this.editForm, videoCategory);
  }

  protected getVideoCategoryAccess(): VideoCategoryAccess {
    return VIDEO_CATEGORY_ACCESS;
  }

}
