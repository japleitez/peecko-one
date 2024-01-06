import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import moment from 'moment';

export function periodValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (control.value) {
      if (!isYearMonth(control.value)) {
        return { invalid: true };
      }
    }
    return null;
  }
}

export function isYearMonth(period: string) : boolean {
  return period.length === 7 && moment(period, "YYYY-MM").isValid();
}

export function currentYearMonth(): string {
  return moment().format("YYYY-MM");
}
