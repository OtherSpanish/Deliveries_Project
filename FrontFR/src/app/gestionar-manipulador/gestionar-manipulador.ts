import { Component, EventEmitter, Output } from '@angular/core';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar';

export interface Manipulador {
  numero: number;
  id: string;
  usuario: string;
  contrasena: string;
  tiempoTrabajo: number; // en horas
}

@Component({
  selector: 'app-gestionar-manipulador',
  standalone: false,
  templateUrl: './gestionar-manipulador.html',
  styleUrl: './gestionar-manipulador.css',
})
export class GestionarManipulador {

  @Output() cerrarSesionEvent = new EventEmitter<string>();

  vistaActiva: VistaActiva = 'ninguna';
  manipuladorEditando: Manipulador | null = null;
  editForm = { usuario: '', contrasena: '', tiempoTrabajo: 0 };
  mensajeExito = '';

  manipuladores: Manipulador[] = [
    { numero: 1,  id: 'MAN-001', usuario: 'jperez',    contrasena: 'pass1234',  tiempoTrabajo: 160 },
    { numero: 2,  id: 'MAN-002', usuario: 'mgarcia',   contrasena: 'garcia99',  tiempoTrabajo: 200 },
    { numero: 3,  id: 'MAN-003', usuario: 'lrodriguez', contrasena: 'lrod2024', tiempoTrabajo: 180 },
    { numero: 4,  id: 'MAN-004', usuario: 'cmartinez', contrasena: 'cmart456',  tiempoTrabajo: 220 },
    { numero: 5,  id: 'MAN-005', usuario: 'alopez',    contrasena: 'lopez789',  tiempoTrabajo: 140 },
    { numero: 6,  id: 'MAN-006', usuario: 'rherrera',  contrasena: 'rherr321',  tiempoTrabajo: 190 },
    { numero: 7,  id: 'MAN-007', usuario: 'storres',   contrasena: 'storr654',  tiempoTrabajo: 175 },
    { numero: 8,  id: 'MAN-008', usuario: 'dflores',   contrasena: 'dflor987',  tiempoTrabajo: 210 },
    { numero: 9,  id: 'MAN-009', usuario: 'ncastillo', contrasena: 'ncast111',  tiempoTrabajo: 155 },
    { numero: 10, id: 'MAN-010', usuario: 'emoreno',   contrasena: 'emor222',   tiempoTrabajo: 230 },
  ];

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.manipuladorEditando = null;
    this.mensajeExito = '';
  }

  iniciarEdicion(manipulador: Manipulador): void {
    this.manipuladorEditando = { ...manipulador };
    this.editForm = {
      usuario: manipulador.usuario,
      contrasena: manipulador.contrasena,
      tiempoTrabajo: manipulador.tiempoTrabajo,
    };
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.manipuladorEditando) return;
    const idx = this.manipuladores.findIndex(m => m.id === this.manipuladorEditando!.id);
    if (idx !== -1) {
      this.manipuladores[idx] = {
        ...this.manipuladores[idx],
        usuario: this.editForm.usuario,
        contrasena: this.editForm.contrasena,
        tiempoTrabajo: this.editForm.tiempoTrabajo,
      };
    }
    this.mensajeExito = `Manipulador ${this.manipuladorEditando.id} actualizado correctamente.`;
    this.manipuladorEditando = null;
    this.vistaActiva = 'actualizar';
  }

  cancelarEdicion(): void {
    this.manipuladorEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarManipulador(manipulador: Manipulador): void {
    if (confirm(`¿Eliminar al manipulador ${manipulador.id} (${manipulador.usuario})?`)) {
      this.manipuladores = this.manipuladores.filter(m => m.id !== manipulador.id);
      this.mensajeExito = `Manipulador ${manipulador.id} eliminado.`;
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
