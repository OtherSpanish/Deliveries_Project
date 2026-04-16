import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPrincipal } from './admin-principal';

describe('AdminPrincipal', () => {
  let component: AdminPrincipal;
  let fixture: ComponentFixture<AdminPrincipal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminPrincipal],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminPrincipal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
