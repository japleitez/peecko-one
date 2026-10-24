import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { ArticleCategorySelectComponent } from './article-category-select.component';

describe('ArticleCategorySelectComponent', () => {
  let component: ArticleCategorySelectComponent;
  let fixture: ComponentFixture<ArticleCategorySelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NoopAnimationsModule, ArticleCategorySelectComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleCategorySelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
