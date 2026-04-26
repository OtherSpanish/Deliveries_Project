import { Component, EventEmitter, Output } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';

@Component({
  selector: 'app-manipulador',
  standalone: false,
  templateUrl: './manipulador.component.html',
  styleUrl: './manipulador.component.css',
})
export class ManipuladorComponent {

  @Output() cerrarSesionEvent = new EventEmitter<string>();

  vistaActiva: 'ninguna' | 'paquetes' = 'ninguna';
  mensajeExito = '';

  constructor(private paqueteService: PaqueteService) {}

  get paquetes(): Paquete[] {
    return this.paqueteService.getAll();
  }

  get totalEnBodega(): number {
    return this.paquetes.filter(p => p.estado === 'En bodega').length;
  }

  get totalDespachados(): number {
    return this.paquetes.filter(p => p.estado === 'Despachado').length;
  }

  get totalEntregados(): number {
    return this.paquetes.filter(p => p.estado === 'Entregado').length;
  }

  setVista(vista: 'ninguna' | 'paquetes'): void {
    this.vistaActiva = vista;
    this.mensajeExito = '';
  }

  marcarDespachado(paquete: Paquete): void {
    if (paquete.estado === 'En bodega') {
      this.paqueteService.actualizar(paquete.id, { estado: 'Despachado' });
      this.mensajeExito = 'Paquete ' + paquete.id + ' marcado como despachado.';
    }
  }

  logout(): void {
    this.cerrarSesionEvent.emit('login');
  }
}
