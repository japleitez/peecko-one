import { Component, Input } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { ICustomer } from '../../customer/customer.model';
import { ICoach } from '../coach.model';
import { CoachArrayResponseType, CoachService } from '../service/coach.service';

@Component({
  selector: 'coach-select',
  standalone: true,
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './coach-select.component.html',
  styleUrl: './coach-select.component.scss'
})
export class CoachSelectComponent {
  @Input() control: FormControl<ICoach | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  coaches!: ICoach[];
  filteredCoaches!: Observable<ICoach[]>;
  @Input() required!: boolean;

  constructor(protected coachService: CoachService) {}

  ngOnInit(): void {
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.coachService.query().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: CoachArrayResponseType) => {
        this.coaches = res.body ?? [];
        this.filteredCoaches = this.control.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.name;
            return name ? this._filter(name as string) : this.coaches.slice();
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

  displayName(coach: ICoach): string {
    return coach && coach.name? coach.name : '';
  }

  private _filter(name: string): ICustomer[] {
    const value = name.toLowerCase();
    return this.coaches.filter(agency => {
      return agency.name?.toLowerCase().includes(value);
    });
  }

}
