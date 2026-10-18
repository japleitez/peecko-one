import { Injectable, signal, WritableSignal } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { IApsPlan } from '../aps-plan.model';

@Injectable({
  providedIn: 'root',
})
export class ApsPlanData {
  private contract = signal<string | undefined | null>('');

  setContract(update: string | null | undefined): void {
    this.contract.set(update);
  }

  getContract(): string | undefined | null {
    return this.contract();
  }
}
