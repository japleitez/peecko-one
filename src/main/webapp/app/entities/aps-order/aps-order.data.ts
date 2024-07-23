import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApsOrderData {

  id = signal<number | undefined | null>(null);

  setId(update: number | undefined | null): void {
    this.id.set(update);
  }

  getId() {
    return this.id();
  }

}


