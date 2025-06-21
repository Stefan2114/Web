import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainLayoputComponent } from './main-layoput.component';

describe('MainLayoputComponent', () => {
  let component: MainLayoputComponent;
  let fixture: ComponentFixture<MainLayoputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainLayoputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MainLayoputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
