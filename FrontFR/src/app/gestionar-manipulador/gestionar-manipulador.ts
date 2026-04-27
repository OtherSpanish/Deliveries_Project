import { Component, EventEmitter, Output } from '@angular/core';
import { ApiService } from '../services/api.service';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar';

export interface Manipulador {
  numero: number;
  id: string;
  usuario: string;
  contrasena: string;
  tiempoTrabajo: number;
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

  manipuladores: Manipulador[] = [];

  constructor(private api: ApiService) {}

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.manipuladorEditando = null;
    this.mensajeExito = '';
    if (vista === 'mostrar' || vista === 'actualizar' || vista === 'borrar') {
      this.cargarManipuladores();
    }
  }

  cargarManipuladores(): void {
    this.api.adminMostrarManipuladores().subscribe({
      next: (data) => {
        try {
          const lista = JSON.parse(data);
          this.manipuladores = lista.map((m: any, i: number) => ({
            numero: i + 1,
            id: String(m.id),
            usuario: m.usuario,
            contrasena: m.contrasenia,
            tiempoTrabajo: m.tiempoDeTrabajo,
          }));
        } catch { this.manipuladores = []; }
      },
      error: () => { this.manipuladores = []; }
    });
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
    this.api.adminActualizarManipulador(
      Number(this.manipuladorEditando.id),
      this.editForm.usuario,
      this.editForm.contrasena,
      this.editForm.tiempoTrabajo
    ).subscribe({
      next: () => {
        const idx = this.manipuladores.findIndex(m => m.id === this.manipuladorEditando!.id);
        if (idx !== -1) {
          this.manipuladores[idx] = { ...this.manipuladores[idx], ...this.editForm };
        }
        this.mensajeExito = `Manipulador ${this.manipuladorEditando!.id} actualizado correctamente.`;
        this.manipuladorEditando = null;
        this.vistaActiva = 'actualizar';
      },
      error: (err) => alert(err?.error || 'Error al actualizar manipulador'),
    });
  }

  cancelarEdicion(): void {
    this.manipuladorEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarManipulador(manipulador: Manipulador): void {
    if (confirm(`¿Eliminar al manipulador ${manipulador.id} (${manipulador.usuario})?`)) {
      this.api.adminBorrarManipulador(Number(manipulador.id)).subscribe({
        next: () => {
          this.manipuladores = this.manipuladores.filter(m => m.id !== manipulador.id);
          this.mensajeExito = `Manipulador ${manipulador.id} eliminado.`;
        },
        error: (err) => alert(err?.error || 'Error al eliminar manipulador'),
      });
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
