import { Injectable } from '@angular/core';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import moment from 'moment';

export const YearMonthValidator:  ValidatorFn  = (control:AbstractControl):  ValidationErrors | null  => {
  const period = control.value;
  if (period) {
    if (!isYearMonth(period)) {
      return {
        invalid: true
      }
    }
  }
  return null;
}

export function currentYearMonth(): string {
  return moment().format("YYYY-MM")
}
export function isYearMonth(period: string) : boolean {
    return period.length === 7 && moment(period, "YYYY-MM").isValid();
}

@Injectable({
  providedIn: 'root'
})

export class ValidateService {

  constructor() { }

}
