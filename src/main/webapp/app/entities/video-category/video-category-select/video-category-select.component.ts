import { Component, Input } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { IVideoCategory } from '../video-category.model';
import { VideoCategoryArrayResponseType, VideoCategoryService } from '../service/video-category.service';

@Component({
  selector: 'video-category-select',
  standalone: true,
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './video-category-select.component.html',
  styleUrl: './video-category-select.component.scss'
})
export class VideoCategorySelectComponent {
  @Input() control: FormControl<IVideoCategory | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  videoCategories!: IVideoCategory[];
  filteredVideoCategories!: Observable<IVideoCategory[]>;
  @Input() required!: boolean;


  constructor(protected videoCategoryService: VideoCategoryService) {}

  ngOnInit(): void {
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.videoCategoryService.query().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: VideoCategoryArrayResponseType) => {
        this.videoCategories = res.body ?? [];
        this.filteredVideoCategories = this.control.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.title;
            return name ? this._filter(name as string) : this.videoCategories.slice();
          }),
        );
      },
    });
  }

  private disableControl(value: boolean) {
    if (value) {
      this.control.disable({ emitEvent: false});
    } else {
      this.control.enable({ emitEvent: false });
    }
  }

  displayName(agency: IVideoCategory): string {
    return agency && agency.title? agency.title : '';
  }

  private _filter(name: string): IVideoCategory[] {
    const value = name.toLowerCase();
    return this.videoCategories.filter(agency => {
      return agency.title?.toLowerCase().includes(value);
    });
  }

}
