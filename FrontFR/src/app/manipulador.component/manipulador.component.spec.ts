import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManipuladorComponent } from './manipulador.component';

describe('ManipuladorComponent', () => {
  let component: ManipuladorComponent;
  let fixture: ComponentFixture<ManipuladorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManipuladorComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ManipuladorComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
