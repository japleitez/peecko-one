import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ICustomer } from '../customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerData {

  protected _data: BehaviorSubject<ICustomer>;

  private NEW_CUSTOMER: ICustomer = { id: 0, code: ''};

  constructor() {
    this._data = new BehaviorSubject<ICustomer>(this.NEW_CUSTOMER);
  }

  setValue(customer: ICustomer): void {
    this._data.next(customer);
  }

  getValue(): Observable<ICustomer> {
    return this._data.asObservable();
  }

}
