import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { resolveTypedSelection, selectionRequiredValidator } from 'app/shared/autocomplete/autocomplete-selection';
import { ICoach } from '../coach.model';
import { CoachArrayResponseType, CoachService } from '../service/coach.service';

@Component({
  selector: 'jhi-coach-select',
  standalone: true,
  imports: [AsyncPipe, ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './coach-select.component.html',
  styleUrl: './coach-select.component.scss',
})
export class CoachSelectComponent implements OnInit {
  @Input() control: FormControl<ICoach | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  coaches: ICoach[] = [];
  filteredCoaches!: Observable<ICoach[]>;
  @Input() required!: boolean;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;

  constructor(protected coachService: CoachService) {}

  displayName(coach: ICoach | null): string {
    return coach?.name ?? '';
  }

  onBlur(): void {
    // while the panel is open the user may be clicking an option; the (closed) handler resolves the text instead
    if (!this.autocompleteTrigger?.panelOpen) {
      this.resolveTypedText();
    }
  }

  resolveTypedText(): void {
    resolveTypedSelection(this.control, this.coaches, coach => coach.name);
  }

  ngOnInit(): void {
    this.control.addValidators(selectionRequiredValidator);
    this.control.updateValueAndValidity({ emitEvent: false });
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.coachService
      .query()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe({
        next: (res: CoachArrayResponseType) => {
          this.coaches = res.body ?? [];
          this.filteredCoaches = this.control.valueChanges.pipe(
            startWith(''),
            map(value => {
              const name = typeof value === 'string' ? value : value?.name;
              return name ? this._filter(name) : this.coaches.slice();
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

  private _filter(name: string): ICoach[] {
    const value = name.toLowerCase();
    return this.coaches.filter(agency => agency.name?.toLowerCase().includes(value));
  }
}
