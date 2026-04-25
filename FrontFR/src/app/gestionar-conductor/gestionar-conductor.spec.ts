import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarConductor } from './gestionar-conductor';

describe('GestionarConductor', () => {
  let component: GestionarConductor;
  let fixture: ComponentFixture<GestionarConductor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GestionarConductor],
    }).compileComponents();

    fixture = TestBed.createComponent(GestionarConductor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
