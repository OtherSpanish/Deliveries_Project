import { Component, EventEmitter, Output } from '@angular/core';

export interface PaqueteManipulador {
  numero: number;
  id: string;
  tipoPaquete: string;
  contenido: string;
  direccionEnvio: string;
  estado: string;
}

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

  paquetes: PaqueteManipulador[] = [
    { numero: 1,  id: 'PKT-001', tipoPaquete: 'Frágil',      contenido: 'Vajilla de porcelana',   direccionEnvio: 'Cra 7 # 32-18, Bogotá',        estado: 'En bodega'    },
    { numero: 2,  id: 'PKT-002', tipoPaquete: 'Normal',      contenido: 'Ropa deportiva',          direccionEnvio: 'Cl 100 # 15-45, Medellín',      estado: 'Despachado'   },
    { numero: 3,  id: 'PKT-003', tipoPaquete: 'Pesado',      contenido: 'Herramientas eléctricas', direccionEnvio: 'Av 68 # 55-30, Bogotá',         estado: 'En bodega'    },
    { numero: 4,  id: 'PKT-004', tipoPaquete: 'Frágil',      contenido: 'Monitor 27"',             direccionEnvio: 'Cl 45 # 22-10, Cali',           estado: 'Entregado'    },
    { numero: 5,  id: 'PKT-005', tipoPaquete: 'Normal',      contenido: 'Libros universitarios',   direccionEnvio: 'Cra 15 # 80-60, Bogotá',        estado: 'Despachado'   },
    { numero: 6,  id: 'PKT-006', tipoPaquete: 'Refrigerado', contenido: 'Productos lácteos',       direccionEnvio: 'Cl 13 # 74-25, Barranquilla',   estado: 'En bodega'    },
    { numero: 7,  id: 'PKT-007', tipoPaquete: 'Normal',      contenido: 'Calzado deportivo',       direccionEnvio: 'Cra 30 # 10-55, Bucaramanga',   estado: 'Entregado'    },
    { numero: 8,  id: 'PKT-008', tipoPaquete: 'Pesado',      contenido: 'Partes de computador',    direccionEnvio: 'Cl 72 # 50-40, Bogotá',         estado: 'En bodega'    },
    { numero: 9,  id: 'PKT-009', tipoPaquete: 'Frágil',      contenido: 'Instrumentos musicales',  direccionEnvio: 'Av El Dorado # 85-36, Bogotá',  estado: 'Despachado'   },
    { numero: 10, id: 'PKT-010', tipoPaquete: 'Normal',      contenido: 'Juguetes infantiles',     direccionEnvio: 'Cl 80 # 60-20, Pereira',        estado: 'En bodega'    },
    { numero: 11, id: 'PKT-011', tipoPaquete: 'Refrigerado', contenido: 'Medicamentos',            direccionEnvio: 'Cra 50 # 25-15, Manizales',     estado: 'Entregado'    },
    { numero: 12, id: 'PKT-012', tipoPaquete: 'Pesado',      contenido: 'Maquinaria industrial',   direccionEnvio: 'Zona Franca, Cartagena',         estado: 'Despachado'   },
  ];

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

  marcarDespachado(paquete: PaqueteManipulador): void {
    const idx = this.paquetes.findIndex(p => p.id === paquete.id);
    if (idx !== -1 && this.paquetes[idx].estado === 'En bodega') {
      this.paquetes[idx].estado = 'Despachado';
      this.mensajeExito = `Paquete ${paquete.id} marcado como despachado.`;
    }
  }

  logout(): void {
    this.cerrarSesionEvent.emit('login');
  }
}
