import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { IVideoCategory } from '../../video-category/video-category.model';
import { ArticleCategoryService, CategoryArrayResponseType } from '../service/article-category.service';
import { IArticleCategory } from '../article-category.model';

@Component({
  selector: 'article-category-select',
  standalone: true,
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './article-category-select.component.html',
  styleUrl: './article-category-select.component.scss'
})
export class ArticleCategorySelectComponent {
  @Input() control: FormControl<IVideoCategory | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  categories!: IVideoCategory[];
  filteredCategories!: Observable<IVideoCategory[]>;
  @Input() required!: boolean;


  constructor(protected articleCategoryService: ArticleCategoryService) {}

  ngOnInit(): void {
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.articleCategoryService.query().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: CategoryArrayResponseType) => {
        this.categories = res.body ?? [];
        this.filteredCategories = this.control.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.title;
            return name ? this._filter(name as string) : this.categories.slice();
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

  displayName(category: IArticleCategory): string {
    return category && category.title? category.title : '';
  }

  private _filter(name: string): IArticleCategory[] {
    const value = name.toLowerCase();
    return this.categories.filter(category => {
      return category.title?.toLowerCase().includes(value);
    });
  }

}
