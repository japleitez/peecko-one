import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideoCategorySelectComponent } from './video-category-select.component';

describe('VideoCategorySelectComponent', () => {
  let component: VideoCategorySelectComponent;
  let fixture: ComponentFixture<VideoCategorySelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoCategorySelectComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VideoCategorySelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
