import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { resolveTypedSelection, selectionRequiredValidator } from 'app/shared/autocomplete/autocomplete-selection';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ArticleCategoryService, CategoryArrayResponseType } from '../service/article-category.service';
import { IArticleCategory } from '../article-category.model';

@Component({
  selector: 'jhi-article-category-select',
  standalone: true,
  imports: [AsyncPipe, ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './article-category-select.component.html',
  styleUrl: './article-category-select.component.scss',
  // the global preserveWhitespaces option would keep the whitespace around <mat-error> inside @if as extra nodes,
  // which stops mat-form-field from projecting the error into its error slot (NG8011)
  preserveWhitespaces: false,
})
export class ArticleCategorySelectComponent implements OnInit {
  @Input() control: FormControl<IArticleCategory | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  categories: IArticleCategory[] = [];
  filteredCategories!: Observable<IArticleCategory[]>;
  @Input() required!: boolean;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;

  constructor(protected articleCategoryService: ArticleCategoryService) {}

  displayName(category: IArticleCategory | null): string {
    return category?.title ?? '';
  }

  onBlur(): void {
    // while the panel is open the user may be clicking an option; the (closed) handler resolves the text instead
    if (!this.autocompleteTrigger?.panelOpen) {
      this.resolveTypedText();
    }
  }

  resolveTypedText(): void {
    resolveTypedSelection(this.control, this.categories, category => category.title);
  }

  ngOnInit(): void {
    this.control.addValidators(selectionRequiredValidator);
    this.control.updateValueAndValidity({ emitEvent: false });
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.articleCategoryService
      .query()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe({
        next: (res: CategoryArrayResponseType) => {
          this.categories = res.body ?? [];
          this.filteredCategories = this.control.valueChanges.pipe(
            startWith(''),
            map(value => {
              const name = typeof value === 'string' ? value : value?.title;
              return name ? this._filter(name) : this.categories.slice();
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

  private _filter(name: string): IArticleCategory[] {
    const value = name.toLowerCase();
    return this.categories.filter(category => category.title?.toLowerCase().includes(value));
  }
}
