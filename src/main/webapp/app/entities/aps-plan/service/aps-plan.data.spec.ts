import { TestBed } from '@angular/core/testing';
import { ApsPlanData } from './aps-plan.data';

describe('CustomerData', () => {
  let service: ApsPlanData;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsPlanData);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
