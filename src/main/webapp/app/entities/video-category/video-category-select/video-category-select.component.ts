import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { resolveTypedSelection, selectionRequiredValidator } from 'app/shared/autocomplete/autocomplete-selection';
import { IVideoCategory } from '../video-category.model';
import { VideoCategoryArrayResponseType, VideoCategoryService } from '../service/video-category.service';

@Component({
  selector: 'jhi-video-category-select',
  standalone: true,
  imports: [AsyncPipe, ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './video-category-select.component.html',
  styleUrl: './video-category-select.component.scss',
})
export class VideoCategorySelectComponent implements OnInit {
  @Input() control: FormControl<IVideoCategory | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  videoCategories: IVideoCategory[] = [];
  filteredVideoCategories!: Observable<IVideoCategory[]>;
  @Input() required!: boolean;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;

  constructor(protected videoCategoryService: VideoCategoryService) {}

  displayName(agency: IVideoCategory | null): string {
    return agency?.title ?? '';
  }

  onBlur(): void {
    // while the panel is open the user may be clicking an option; the (closed) handler resolves the text instead
    if (!this.autocompleteTrigger?.panelOpen) {
      this.resolveTypedText();
    }
  }

  resolveTypedText(): void {
    resolveTypedSelection(this.control, this.videoCategories, category => category.title);
  }

  ngOnInit(): void {
    this.control.addValidators(selectionRequiredValidator);
    this.control.updateValueAndValidity({ emitEvent: false });
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.videoCategoryService
      .query()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe({
        next: (res: VideoCategoryArrayResponseType) => {
          this.videoCategories = res.body ?? [];
          this.filteredVideoCategories = this.control.valueChanges.pipe(
            startWith(''),
            map(value => {
              const name = typeof value === 'string' ? value : value?.title;
              return name ? this._filter(name) : this.videoCategories.slice();
            }),
          );
        },
      });
  }

  private disableControl(value: boolean): void {
    if (value) {
      this.control.disable({ emitEvent: false });
    } else {
      this.control.enable({ emitEvent: false });
    }
  }

  private _filter(name: string): IVideoCategory[] {
    const value = name.toLowerCase();
    return this.videoCategories.filter(agency => agency.title?.toLowerCase().includes(value));
  }
}
