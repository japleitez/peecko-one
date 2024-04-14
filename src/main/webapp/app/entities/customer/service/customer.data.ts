import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ICustomer, NewCustomer } from '../customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerData {

  private NEW_CUSTOMER: ICustomer = { id: 0, code: ''};

  protected _data: BehaviorSubject<ICustomer>;

  constructor() {
    this._data = new BehaviorSubject<ICustomer>(this.NEW_CUSTOMER);
  }

  setValue(customer: ICustomer) {
    this._data.next(customer);
  }

  getValue(): Observable<ICustomer> {
    return this._data.asObservable();
  }

}
