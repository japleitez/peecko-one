import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApsPlanSelectComponent } from './aps-plan-select.component';

describe('ApsPlanSelectComponent', () => {
  let component: ApsPlanSelectComponent;
  let fixture: ComponentFixture<ApsPlanSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsPlanSelectComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ApsPlanSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
