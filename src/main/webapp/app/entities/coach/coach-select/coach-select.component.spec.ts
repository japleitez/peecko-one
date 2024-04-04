import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoachSelectComponent } from './coach-select.component';

describe('CoachSelectComponent', () => {
  let component: CoachSelectComponent;
  let fixture: ComponentFixture<CoachSelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CoachSelectComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CoachSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
