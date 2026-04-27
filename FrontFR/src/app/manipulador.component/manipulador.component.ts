import { Component, EventEmitter, Output } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';
import { ApiService } from '../services/api.service';

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

  constructor(private paqueteService: PaqueteService, private api: ApiService) {}

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
    if (vista === 'paquetes') {
      this.api.manipuladorMostrarPaquetes().subscribe({
        next: (data) => {
          try {
            const lista = JSON.parse(data);
            lista.forEach((p: any) => {
              if (!this.paqueteService.getAll().find(x => x.id === String(p.id))) {
                this.paqueteService.agregar({
                  tipoPaquete: p.tipoPaquete === 'CARTA' ? 'Carta' : p.tipoPaquete === 'ALIMENTICIO' ? 'Alimenticio' : 'No Alimenticio',
                  contenido: p.contenido,
                  direccionEnvio: p.direccionDeEnvio,
                  cliente: p.clientePaquete,
                });
              }
            });
          } catch { /* usa datos locales */ }
        },
        error: () => { /* usa datos locales */ }
      });
    }
  }

  marcarDespachado(paquete: Paquete): void {
    if (paquete.estado === 'En bodega') {
      this.paqueteService.actualizar(paquete.id, { estado: 'Despachado' });
      this.mensajeExito = 'Paquete ' + paquete.id + ' marcado como despachado.';
    }
  }

  logout(): void {
    this.api.manipuladorLogout().subscribe({ error: () => {} });
    this.cerrarSesionEvent.emit('login');
  }
}
