import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { IApsPlan } from '../aps-plan.model';

@Injectable({
  providedIn: 'root'
})
export class ApsPlanData {

  protected _data: BehaviorSubject<IApsPlan>;

  private NEW_APS_PLAN: IApsPlan = { id: 0, contract: ''};

  constructor() {
    this._data = new BehaviorSubject<IApsPlan>(this.NEW_APS_PLAN);
  }

  setValue(apsPlan: IApsPlan): void {
    this._data.next(apsPlan);
    setTimeout(() => {this._data.next(this.NEW_APS_PLAN)}, 100);
  }

  getValue(): Observable<IApsPlan> {
    return this._data.asObservable();
  }

}
