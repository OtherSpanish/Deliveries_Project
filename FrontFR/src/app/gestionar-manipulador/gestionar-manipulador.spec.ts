import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarManipulador } from './gestionar-manipulador';

describe('GestionarManipulador', () => {
  let component: GestionarManipulador;
  let fixture: ComponentFixture<GestionarManipulador>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GestionarManipulador],
    }).compileComponents();

    fixture = TestBed.createComponent(GestionarManipulador);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
