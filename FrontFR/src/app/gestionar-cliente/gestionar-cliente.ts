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
  editForm = { usuario: '', contrasena: '', cedula: '', tipoCliente: '' };
  mensajeExito = '';

  clientes: Cliente[] = [];

  constructor(private api: ApiService) {}

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.clienteEditando = null;
    this.mensajeExito = '';
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
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.clienteEditando) return;
    this.api.adminActualizarCliente(
      Number(this.clienteEditando.id),
      this.editForm.usuario,
      this.editForm.contrasena,
      this.editForm.cedula,
      this.editForm.tipoCliente.toUpperCase()
    ).subscribe({
      next: (msg) => {
        const idx = this.clientes.findIndex(c => c.id === this.clienteEditando!.id);
        if (idx !== -1) {
          this.clientes[idx] = { ...this.clientes[idx], ...this.editForm, contrasena: this.editForm.contrasena };
        }
        this.mensajeExito = `Cliente ${this.clienteEditando!.id} actualizado correctamente.`;
        this.clienteEditando = null;
        this.vistaActiva = 'actualizar';
      },
      error: (err) => alert(err?.error || 'Error al actualizar cliente'),
    });
  }

  cancelarEdicion(): void {
    this.clienteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarCliente(cliente: Cliente): void {
    if (confirm(`¿Eliminar al cliente ${cliente.id} (${cliente.usuario})?`)) {
      this.api.adminBorrarCliente(Number(cliente.id)).subscribe({
        next: () => {
          this.clientes = this.clientes.filter(c => c.id !== cliente.id);
          this.mensajeExito = `Cliente ${cliente.id} eliminado.`;
        },
        error: (err) => alert(err?.error || 'Error al eliminar cliente'),
      });
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
