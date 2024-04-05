import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleCategorySelectComponent } from './article-category-select.component';

describe('ArticleCategorySelectComponent', () => {
  let component: ArticleCategorySelectComponent;
  let fixture: ComponentFixture<ArticleCategorySelectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleCategorySelectComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArticleCategorySelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
