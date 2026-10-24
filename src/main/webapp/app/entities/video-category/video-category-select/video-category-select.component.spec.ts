import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { VideoCategorySelectComponent } from './video-category-select.component';

describe('VideoCategorySelectComponent', () => {
  let component: VideoCategorySelectComponent;
  let fixture: ComponentFixture<VideoCategorySelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, VideoCategorySelectComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VideoCategorySelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
