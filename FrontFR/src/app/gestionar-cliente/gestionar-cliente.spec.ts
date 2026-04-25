import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarCliente } from './gestionar-cliente';

describe('GestionarCliente', () => {
  let component: GestionarCliente;
  let fixture: ComponentFixture<GestionarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GestionarCliente],
    }).compileComponents();

    fixture = TestBed.createComponent(GestionarCliente);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
