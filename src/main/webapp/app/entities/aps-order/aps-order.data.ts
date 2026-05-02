import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ApsOrderData {
  id = signal<number | undefined | null>(null);
  period = signal<number | undefined | null>(null);
  license = signal<string | undefined | null>(null);

  setId(update: number | undefined | null): void {
    this.id.set(update);
  }

  getId() {
    return this.id();
  }

  setPeriod(update: number | undefined | null): void {
    this.period.set(update);
  }

  getPeriod() {
    return this.period();
  }

  setLicense(update: string | undefined | null): void {
    this.license.set(update);
  }

  getLicense() {
    return this.license();
  }
}
