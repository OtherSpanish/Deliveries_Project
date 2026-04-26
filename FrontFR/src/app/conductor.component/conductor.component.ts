import { Component, EventEmitter, Output } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';

@Component({
  selector: 'app-conductor',
  standalone: false,
  templateUrl: './conductor.component.html',
  styleUrl: './conductor.component.css',
})
export class ConductorComponent {

  @Output() cerrarSesionEvent = new EventEmitter<string>();

  vistaActiva: 'ninguna' | 'pedidos' = 'ninguna';
  mensajeExito = '';

  constructor(private paqueteService: PaqueteService) {}

  // El conductor ve los paquetes despachados y en camino (los que le corresponde entregar)
  get pedidos(): Paquete[] {
    return this.paqueteService.getAll().filter(
      p => p.estado === 'Despachado' || p.estado === 'En camino' || p.estado === 'Entregado'
    );
  }



  get pedidosEnCamino(): number {
    return this.paqueteService.getAll().filter(p => p.estado === 'En camino' || p.estado === 'Despachado').length;
  }

  get pedidosEntregados(): number {
    return this.paqueteService.getAll().filter(p => p.estado === 'Entregado').length;
  }

  setVista(vista: 'ninguna' | 'pedidos'): void {
    this.vistaActiva = vista;
    this.mensajeExito = '';
  }

  marcarEntregado(paquete: Paquete): void {
    if (paquete.estado !== 'Entregado') {
      this.paqueteService.actualizar(paquete.id, { estado: 'Entregado' });
      this.mensajeExito = 'Pedido ' + paquete.id + ' marcado como entregado.';
    }
  }

  logout(): void {
    this.cerrarSesionEvent.emit('login');
  }
}
