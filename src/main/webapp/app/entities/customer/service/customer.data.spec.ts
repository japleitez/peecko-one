import { TestBed } from '@angular/core/testing';

import { CustomerData } from './customer.data';

describe('CustomerData', () => {
  let service: CustomerData;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerData);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
