import { Component, EventEmitter, Output } from '@angular/core';

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

  conductores: Conductor[] = [
    { numero: 1,  id: 'CON-001', usuario: 'jperez',     contrasena: 'pass1234',  tipoVehiculo: 'Camión'      },
    { numero: 2,  id: 'CON-002', usuario: 'mgarcia',    contrasena: 'garcia99',  tipoVehiculo: 'Moto'        },
    { numero: 3,  id: 'CON-003', usuario: 'lrodriguez', contrasena: 'lrod2024',  tipoVehiculo: 'Furgoneta'   },
    { numero: 4,  id: 'CON-004', usuario: 'cmartinez',  contrasena: 'cmart456',  tipoVehiculo: 'Camión'      },
    { numero: 5,  id: 'CON-005', usuario: 'alopez',     contrasena: 'lopez789',  tipoVehiculo: 'Automóvil'   },
    { numero: 6,  id: 'CON-006', usuario: 'rherrera',   contrasena: 'rherr321',  tipoVehiculo: 'Moto'        },
    { numero: 7,  id: 'CON-007', usuario: 'storres',    contrasena: 'storr654',  tipoVehiculo: 'Furgoneta'   },
    { numero: 8,  id: 'CON-008', usuario: 'dflores',    contrasena: 'dflor987',  tipoVehiculo: 'Camión'      },
    { numero: 9,  id: 'CON-009', usuario: 'ncastillo',  contrasena: 'ncast111',  tipoVehiculo: 'Automóvil'   },
    { numero: 10, id: 'CON-010', usuario: 'emoreno',    contrasena: 'emor222',   tipoVehiculo: 'Furgoneta'   },
  ];

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.conductorEditando = null;
    this.mensajeExito = '';
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
    const idx = this.conductores.findIndex(c => c.id === this.conductorEditando!.id);
    if (idx !== -1) {
      this.conductores[idx] = {
        ...this.conductores[idx],
        usuario: this.editForm.usuario,
        contrasena: this.editForm.contrasena,
        tipoVehiculo: this.editForm.tipoVehiculo,
      };
    }
    this.mensajeExito = `Conductor ${this.conductorEditando.id} actualizado correctamente.`;
    this.conductorEditando = null;
    this.vistaActiva = 'actualizar';
  }

  cancelarEdicion(): void {
    this.conductorEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarConductor(conductor: Conductor): void {
    if (confirm(`¿Eliminar al conductor ${conductor.id} (${conductor.usuario})?`)) {
      this.conductores = this.conductores.filter(c => c.id !== conductor.id);
      this.mensajeExito = `Conductor ${conductor.id} eliminado.`;
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
