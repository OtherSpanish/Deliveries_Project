import { Component, EventEmitter, Output } from '@angular/core';
import { ApiService } from '../services/api.service';

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
  editForm = { usuario: '', contrasena: '', cedula: '', tipoCliente: 'NORMAL' };
  mensajeExito = '';
  mensajeError = '';

  clientes: Cliente[] = [];

  constructor(private api: ApiService) {}

  private mostrarError(err: any, fallback: string): void {
    this.mensajeError = err?.error || fallback;
  }

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.clienteEditando = null;
    this.mensajeExito = '';
    this.mensajeError = '';
    if (vista === 'mostrar' || vista === 'actualizar' || vista === 'borrar') {
      this.cargarClientes();
    }
  }

  cargarClientes(): void {
    this.api.adminMostrarClientes().subscribe({
      next: (data) => {
        try {
          const lista = JSON.parse(data);
          this.clientes = lista.map((c: any, i: number) => ({
            numero: i + 1,
            id: String(c.id),
            usuario: c.usuario,
            contrasena: c.contrasenia,
            cedula: c.cedula,
            tipoCliente: c.tipoCliente,
          }));
        } catch { this.clientes = []; }
      },
      error: () => { this.clientes = []; }
    });
  }

  iniciarEdicion(cliente: Cliente): void {
    this.clienteEditando = { ...cliente };
    this.editForm = {
      usuario: cliente.usuario,
      contrasena: cliente.contrasena,
      cedula: cliente.cedula,
      tipoCliente: cliente.tipoCliente,
    };
    this.mensajeError = '';
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.clienteEditando) return;
    this.mensajeError = '';
    // Validación de cédula: 10 dígitos, no empieza en 0 (igual al backend)
    const cedula = this.editForm.cedula.trim();
    if (cedula.length !== 10 || cedula[0] === '0' || !/^\d+$/.test(cedula)) {
      this.mensajeError = 'Ingrese una cédula válida (10 dígitos numéricos, no empieza por 0).';
      return;
    }
    this.api.adminActualizarCliente(
      Number(this.clienteEditando.id),
      this.editForm.usuario,
      this.editForm.contrasena,
      cedula,
      this.editForm.tipoCliente.toUpperCase()
    ).subscribe({
      next: () => {
        const idx = this.clientes.findIndex(c => c.id === this.clienteEditando!.id);
        if (idx !== -1) {
          this.clientes[idx] = { ...this.clientes[idx], ...this.editForm };
        }
        this.mensajeExito = `Cliente ${this.clienteEditando!.id} actualizado correctamente.`;
        this.clienteEditando = null;
        this.vistaActiva = 'actualizar';
      },
      error: (err) => this.mostrarError(err, 'Error al actualizar el cliente'),
    });
  }

  cancelarEdicion(): void {
    this.clienteEditando = null;
    this.mensajeError = '';
    this.vistaActiva = 'actualizar';
  }

  borrarCliente(cliente: Cliente): void {
    if (confirm(`¿Eliminar al cliente ${cliente.id} (${cliente.usuario})?`)) {
      this.mensajeError = '';
      this.api.adminBorrarCliente(Number(cliente.id)).subscribe({
        next: () => {
          this.clientes = this.clientes.filter(c => c.id !== cliente.id);
          this.mensajeExito = `Cliente ${cliente.id} eliminado.`;
        },
        error: (err) => this.mostrarError(err, 'Error al eliminar el cliente'),
      });
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
