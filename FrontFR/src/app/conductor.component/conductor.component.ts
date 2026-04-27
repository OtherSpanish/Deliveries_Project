import { Component, EventEmitter, Output } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';
import { ApiService } from '../services/api.service';

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

  constructor(private paqueteService: PaqueteService, private api: ApiService) {}

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
    if (vista === 'pedidos') {
      // Cargar pedidos reales del backend
      this.api.conductorMostrarPedidos().subscribe({
        next: (data) => {
          try {
            const lista = JSON.parse(data);
            // Sincronizar con PaqueteService local para que los getters funcionen
            lista.forEach((p: any) => {
              if (!this.paqueteService.getAll().find(x => x.id === p.id)) {
                this.paqueteService.agregar({
                  tipoPaquete: p.tipoPaquete === 'CARTA' ? 'Carta' : p.tipoPaquete === 'ALIMENTICIO' ? 'Alimenticio' : 'No Alimenticio',
                  contenido: p.contenido,
                  direccionEnvio: p.direccionDeEnvio,
                  cliente: p.clientePaquete,
                });
              }
            });
          } catch { /* usa datos locales si falla el parseo */ }
        },
        error: () => { /* muestra datos locales */ }
      });
    }
  }

  marcarEntregado(paquete: Paquete): void {
    if (paquete.estado !== 'Entregado') {
      this.paqueteService.actualizar(paquete.id, { estado: 'Entregado' });
      this.mensajeExito = 'Pedido ' + paquete.id + ' marcado como entregado.';
    }
  }

  logout(): void {
    this.api.conductorLogout().subscribe({ error: () => {} });
    this.cerrarSesionEvent.emit('login');
  }
}
