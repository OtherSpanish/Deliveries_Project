import { Component, EventEmitter, Output } from '@angular/core';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar';

export interface Cliente {
  numero: number;
  id: string;
  usuario: string;
  contrasena: string;
  cedula: string;
  tipoCliente: string;
}

@Component({
  selector: 'app-gestionar-cliente',
  standalone: false,
  templateUrl: './gestionar-cliente.html',
  styleUrl: './gestionar-cliente.css',
})
export class GestionarCliente {

  @Output() cerrarSesionEvent = new EventEmitter<string>();

  vistaActiva: VistaActiva = 'ninguna';
  clienteEditando: Cliente | null = null;
  editForm = { usuario: '', contrasena: '', cedula: '', tipoCliente: '' };
  mensajeExito = '';

  clientes: Cliente[] = [
    { numero: 1,  id: 'CLI-001', usuario: 'jperez',     contrasena: 'pass1234', cedula: '10234567',  tipoCliente: 'Premium'   },
    { numero: 2,  id: 'CLI-002', usuario: 'mgarcia',    contrasena: 'garcia99', cedula: '20345678',  tipoCliente: 'Estándar'  },
    { numero: 3,  id: 'CLI-003', usuario: 'lrodriguez', contrasena: 'lrod2024', cedula: '30456789',  tipoCliente: 'Empresarial'},
    { numero: 4,  id: 'CLI-004', usuario: 'cmartinez',  contrasena: 'cmart456', cedula: '40567890',  tipoCliente: 'Premium'   },
    { numero: 5,  id: 'CLI-005', usuario: 'alopez',     contrasena: 'lopez789', cedula: '50678901',  tipoCliente: 'Estándar'  },
    { numero: 6,  id: 'CLI-006', usuario: 'rherrera',   contrasena: 'rherr321', cedula: '60789012',  tipoCliente: 'Empresarial'},
    { numero: 7,  id: 'CLI-007', usuario: 'storres',    contrasena: 'storr654', cedula: '70890123',  tipoCliente: 'Estándar'  },
    { numero: 8,  id: 'CLI-008', usuario: 'dflores',    contrasena: 'dflor987', cedula: '80901234',  tipoCliente: 'Premium'   },
    { numero: 9,  id: 'CLI-009', usuario: 'ncastillo',  contrasena: 'ncast111', cedula: '90123456',  tipoCliente: 'Estándar'  },
    { numero: 10, id: 'CLI-010', usuario: 'emoreno',    contrasena: 'emor222',  cedula: '10012345',  tipoCliente: 'Empresarial'},
  ];

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.clienteEditando = null;
    this.mensajeExito = '';
  }

  iniciarEdicion(cliente: Cliente): void {
    this.clienteEditando = { ...cliente };
    this.editForm = {
      usuario: cliente.usuario,
      contrasena: cliente.contrasena,
      cedula: cliente.cedula,
      tipoCliente: cliente.tipoCliente,
    };
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.clienteEditando) return;
    const idx = this.clientes.findIndex(c => c.id === this.clienteEditando!.id);
    if (idx !== -1) {
      this.clientes[idx] = {
        ...this.clientes[idx],
        usuario: this.editForm.usuario,
        contrasena: this.editForm.contrasena,
        cedula: this.editForm.cedula,
        tipoCliente: this.editForm.tipoCliente,
      };
    }
    this.mensajeExito = `Cliente ${this.clienteEditando.id} actualizado correctamente.`;
    this.clienteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  cancelarEdicion(): void {
    this.clienteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarCliente(cliente: Cliente): void {
    if (confirm(`¿Eliminar al cliente ${cliente.id} (${cliente.usuario})?`)) {
      this.clientes = this.clientes.filter(c => c.id !== cliente.id);
      this.mensajeExito = `Cliente ${cliente.id} eliminado.`;
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
