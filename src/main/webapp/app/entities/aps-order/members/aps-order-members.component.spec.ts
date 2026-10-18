import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { ApsOrderMembersComponent } from './aps-order-members.component';

describe('ApsOrderMembersComponent', () => {
  let component: ApsOrderMembersComponent;
  let fixture: ComponentFixture<ApsOrderMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, ApsOrderMembersComponent],
      providers: [
        { provide: MatDialogRef, useValue: { close: jest.fn() } },
        { provide: MAT_DIALOG_DATA, useValue: { id: 123 } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ApsOrderMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
