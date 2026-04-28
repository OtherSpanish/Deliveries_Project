import { Component, EventEmitter, Output } from '@angular/core';
import { ApiService } from '../services/api.service';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar' | 'crear';

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
  editForm = { usuario: '', contrasena: '', tiempoTrabajo: 4 };
  crearForm = { usuario: '', contrasena: '', tiempoTrabajo: 4 };
  mensajeExito = '';
  mensajeError = '';

  manipuladores: Manipulador[] = [];

  constructor(private api: ApiService) {}

  private mostrarError(err: any, fallback: string): void {
    this.mensajeError = err?.error || fallback;
  }

  // El backend valida: horasTrabajo >= 9 || horasTrabajo < 2  → excepción
  private validarHoras(horas: number): boolean {
    if (horas < 2 || horas > 8) {
      this.mensajeError = 'Las horas de trabajo deben estar entre 2 y 8 horas.';
      return false;
    }
    return true;
  }

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.manipuladorEditando = null;
    this.mensajeExito = '';
    this.mensajeError = '';
    this.crearForm = { usuario: '', contrasena: '', tiempoTrabajo: 4 };
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
    this.mensajeError = '';
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.manipuladorEditando) return;
    this.mensajeError = '';
    if (!this.validarHoras(this.editForm.tiempoTrabajo)) return;
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
      error: (err) => this.mostrarError(err, 'Error al actualizar el manipulador'),
    });
  }

  cancelarEdicion(): void {
    this.manipuladorEditando = null;
    this.mensajeError = '';
    this.vistaActiva = 'actualizar';
  }

  crearManipulador(): void {
    this.mensajeError = '';
    if (!this.crearForm.usuario.trim() || !this.crearForm.contrasena.trim()) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios.';
      return;
    }
    if (!this.validarHoras(this.crearForm.tiempoTrabajo)) return;
    this.api.adminCrearManipulador(
      this.crearForm.usuario.trim(),
      this.crearForm.contrasena.trim(),
      this.crearForm.tiempoTrabajo
    ).subscribe({
      next: () => {
        this.mensajeExito = `Manipulador '${this.crearForm.usuario}' creado correctamente.`;
        this.crearForm = { usuario: '', contrasena: '', tiempoTrabajo: 4 };
        this.vistaActiva = 'mostrar';
        this.cargarManipuladores();
      },
      error: (err) => this.mostrarError(err, 'Error al crear el manipulador'),
    });
  }

  borrarManipulador(manipulador: Manipulador): void {
    if (confirm(`¿Eliminar al manipulador ${manipulador.id} (${manipulador.usuario})?`)) {
      this.mensajeError = '';
      this.api.adminBorrarManipulador(Number(manipulador.id)).subscribe({
        next: () => {
          this.manipuladores = this.manipuladores.filter(m => m.id !== manipulador.id);
          this.mensajeExito = `Manipulador ${manipulador.id} eliminado.`;
        },
        error: (err) => this.mostrarError(err, 'Error al eliminar el manipulador'),
      });
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
