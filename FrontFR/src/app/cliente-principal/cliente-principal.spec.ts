import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientePrincipal } from './cliente-principal';

describe('ClientePrincipal', () => {
  let component: ClientePrincipal;
  let fixture: ComponentFixture<ClientePrincipal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientePrincipal],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientePrincipal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
