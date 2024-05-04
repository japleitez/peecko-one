import { Injectable, signal } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ICustomer } from '../customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerData {

  code = signal<string | undefined | null>('');

  setCode(update: string | undefined | null): void {
    this.code.set(update);
  }

  getCode() {
    return this.code();
  }

}
