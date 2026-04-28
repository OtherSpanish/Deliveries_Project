import { Component, EventEmitter, Output } from '@angular/core';
import { ApiService } from '../services/api.service';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar' | 'crear';

export interface Conductor {
  numero: number;
  id: string;
  usuario: string;
  contrasena: string;
  tipoVehiculo: string;
}

@Component({
  selector: 'app-gestionar-conductor',
  standalone: false,
  templateUrl: './gestionar-conductor.html',
  styleUrl: './gestionar-conductor.css',
})
export class GestionarConductor {
  @Output() cerrarSesionEvent = new EventEmitter<string>();

  vistaActiva: VistaActiva = 'ninguna';
  conductorEditando: Conductor | null = null;
  editForm = { usuario: '', contrasena: '', tipoVehiculo: 'carro' };
  crearForm = { usuario: '', contrasena: '', tipoVehiculo: 'carro' };
  mensajeExito = '';
  mensajeError = '';

  conductores: Conductor[] = [];

  constructor(private api: ApiService) {}

  private mostrarError(err: any, fallback: string): void {
    this.mensajeError = err?.error || fallback;
  }

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.conductorEditando = null;
    this.mensajeExito = '';
    this.mensajeError = '';
    this.crearForm = { usuario: '', contrasena: '', tipoVehiculo: 'carro' };
    if (vista === 'mostrar' || vista === 'actualizar' || vista === 'borrar') {
      this.cargarConductores();
    }
  }

  cargarConductores(): void {
    this.api.adminMostrarConductores().subscribe({
      next: (data) => {
        try {
          const lista = JSON.parse(data);
          this.conductores = lista.map((c: any, i: number) => ({
            numero: i + 1,
            id: String(c.id),
            usuario: c.usuario,
            contrasena: c.contrasenia,
            tipoVehiculo: c.tipoVehiculo,
          }));
        } catch { this.conductores = []; }
      },
      error: () => { this.conductores = []; }
    });
  }

  iniciarEdicion(conductor: Conductor): void {
    this.conductorEditando = { ...conductor };
    this.editForm = {
      usuario: conductor.usuario,
      contrasena: conductor.contrasena,
      tipoVehiculo: conductor.tipoVehiculo,
    };
    this.mensajeError = '';
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.conductorEditando) return;
    this.mensajeError = '';
    this.api.adminActualizarConductor(
      Number(this.conductorEditando.id),
      this.editForm.usuario,
      this.editForm.contrasena,
      this.editForm.tipoVehiculo
    ).subscribe({
      next: () => {
        const idx = this.conductores.findIndex(c => c.id === this.conductorEditando!.id);
        if (idx !== -1) {
          this.conductores[idx] = { ...this.conductores[idx], ...this.editForm };
        }
        this.mensajeExito = `Conductor ${this.conductorEditando!.id} actualizado correctamente.`;
        this.conductorEditando = null;
        this.vistaActiva = 'actualizar';
      },
      error: (err) => this.mostrarError(err, 'Error al actualizar el conductor'),
    });
  }

  cancelarEdicion(): void {
    this.conductorEditando = null;
    this.mensajeError = '';
    this.vistaActiva = 'actualizar';
  }

  crearConductor(): void {
    this.mensajeError = '';
    if (!this.crearForm.usuario.trim() || !this.crearForm.contrasena.trim()) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios.';
      return;
    }
    this.api.adminCrearConductor(
      this.crearForm.usuario.trim(),
      this.crearForm.contrasena.trim(),
      this.crearForm.tipoVehiculo
    ).subscribe({
      next: () => {
        this.mensajeExito = `Conductor '${this.crearForm.usuario}' creado correctamente.`;
        this.crearForm = { usuario: '', contrasena: '', tipoVehiculo: 'carro' };
        this.vistaActiva = 'mostrar';
        this.cargarConductores();
      },
      error: (err) => this.mostrarError(err, 'Error al crear el conductor'),
    });
  }

  borrarConductor(conductor: Conductor): void {
    if (confirm(`¿Eliminar al conductor ${conductor.id} (${conductor.usuario})?`)) {
      this.mensajeError = '';
      this.api.adminBorrarConductor(Number(conductor.id)).subscribe({
        next: () => {
          this.conductores = this.conductores.filter(c => c.id !== conductor.id);
          this.mensajeExito = `Conductor ${conductor.id} eliminado.`;
        },
        error: (err) => this.mostrarError(err, 'Error al eliminar el conductor'),
      });
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
