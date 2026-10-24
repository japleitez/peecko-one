import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { AgencySelectComponent } from './agency-select.component';

describe('AgencySelectComponent', () => {
  let component: AgencySelectComponent;
  let fixture: ComponentFixture<AgencySelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, AgencySelectComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AgencySelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
