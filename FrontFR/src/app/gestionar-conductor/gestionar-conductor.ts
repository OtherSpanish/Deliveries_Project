import { Component, EventEmitter, Output } from '@angular/core';
import { ApiService } from '../services/api.service';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar';

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
  editForm = { usuario: '', contrasena: '', tipoVehiculo: '' };
  mensajeExito = '';

  conductores: Conductor[] = [];

  constructor(private api: ApiService) {}

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.conductorEditando = null;
    this.mensajeExito = '';
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
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.conductorEditando) return;
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
      error: (err) => alert(err?.error || 'Error al actualizar conductor'),
    });
  }

  cancelarEdicion(): void {
    this.conductorEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarConductor(conductor: Conductor): void {
    if (confirm(`¿Eliminar al conductor ${conductor.id} (${conductor.usuario})?`)) {
      this.api.adminBorrarConductor(Number(conductor.id)).subscribe({
        next: () => {
          this.conductores = this.conductores.filter(c => c.id !== conductor.id);
          this.mensajeExito = `Conductor ${conductor.id} eliminado.`;
        },
        error: (err) => alert(err?.error || 'Error al eliminar conductor'),
      });
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
