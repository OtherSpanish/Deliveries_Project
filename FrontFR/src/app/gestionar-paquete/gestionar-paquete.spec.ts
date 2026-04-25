import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarPaquete } from './gestionar-paquete';

describe('GestionarPaquete', () => {
  let component: GestionarPaquete;
  let fixture: ComponentFixture<GestionarPaquete>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GestionarPaquete],
    }).compileComponents();

    fixture = TestBed.createComponent(GestionarPaquete);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
