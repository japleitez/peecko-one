import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApsOrderMembersComponent } from './aps-order-members.component';

describe('ApsOrderMembersComponent', () => {
  let component: ApsOrderMembersComponent;
  let fixture: ComponentFixture<ApsOrderMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsOrderMembersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ApsOrderMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
