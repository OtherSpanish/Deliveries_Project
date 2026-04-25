import { Component, EventEmitter, Output } from '@angular/core';

export interface Pedido {
  numero: number;
  id: string;
  cliente: string;
  direccionOrigen: string;
  direccionDestino: string;
  tipoPaquete: string;
  estado: string;
}

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

  pedidos: Pedido[] = [
    { numero: 1,  id: 'PED-001', cliente: 'jperez',     direccionOrigen: 'Cra 7 # 32-18, Bogotá',        direccionDestino: 'Cl 100 # 15-45, Medellín',    tipoPaquete: 'Frágil',      estado: 'En camino'  },
    { numero: 2,  id: 'PED-002', cliente: 'mgarcia',    direccionOrigen: 'Cl 45 # 22-10, Cali',           direccionDestino: 'Cra 15 # 80-60, Bogotá',      tipoPaquete: 'Normal',      estado: 'Pendiente'  },
    { numero: 3,  id: 'PED-003', cliente: 'lrodriguez', direccionOrigen: 'Av 68 # 55-30, Bogotá',         direccionDestino: 'Cl 13 # 74-25, Barranquilla',  tipoPaquete: 'Pesado',      estado: 'Entregado'  },
    { numero: 4,  id: 'PED-004', cliente: 'cmartinez',  direccionOrigen: 'Cra 30 # 10-55, Bucaramanga',   direccionDestino: 'Av El Dorado # 85-36, Bogotá', tipoPaquete: 'Refrigerado', estado: 'En camino'  },
    { numero: 5,  id: 'PED-005', cliente: 'alopez',     direccionOrigen: 'Cl 72 # 50-40, Bogotá',         direccionDestino: 'Cl 80 # 60-20, Pereira',       tipoPaquete: 'Normal',      estado: 'Pendiente'  },
    { numero: 6,  id: 'PED-006', cliente: 'rherrera',   direccionOrigen: 'Cra 50 # 25-15, Manizales',     direccionDestino: 'Zona Franca, Cartagena',        tipoPaquete: 'Pesado',      estado: 'Entregado'  },
    { numero: 7,  id: 'PED-007', cliente: 'storres',    direccionOrigen: 'Cl 13 # 74-25, Barranquilla',   direccionDestino: 'Cra 7 # 32-18, Bogotá',        tipoPaquete: 'Frágil',      estado: 'En camino'  },
    { numero: 8,  id: 'PED-008', cliente: 'dflores',    direccionOrigen: 'Zona Franca, Cartagena',         direccionDestino: 'Av 68 # 55-30, Bogotá',        tipoPaquete: 'Normal',      estado: 'Pendiente'  },
  ];

  get pedidosPendientes(): number {
    return this.pedidos.filter(p => p.estado === 'Pendiente').length;
  }

  get pedidosEnCamino(): number {
    return this.pedidos.filter(p => p.estado === 'En camino').length;
  }

  get pedidosEntregados(): number {
    return this.pedidos.filter(p => p.estado === 'Entregado').length;
  }

  setVista(vista: 'ninguna' | 'pedidos'): void {
    this.vistaActiva = vista;
    this.mensajeExito = '';
  }

  marcarEntregado(pedido: Pedido): void {
    const idx = this.pedidos.findIndex(p => p.id === pedido.id);
    if (idx !== -1 && this.pedidos[idx].estado !== 'Entregado') {
      this.pedidos[idx].estado = 'Entregado';
      this.mensajeExito = `Pedido ${pedido.id} marcado como entregado.`;
    }
  }

  logout(): void {
    this.cerrarSesionEvent.emit('login');
  }
}
