import { Injectable } from '@angular/core';

export interface Paquete {
  numero: number;
  id: string;
  tipoPaquete: 'Carta' | 'Alimenticio' | 'No Alimenticio';
  contenido: string;
  direccionEnvio: string;
  estado: 'Pendiente' | 'En bodega' | 'Despachado' | 'En camino' | 'Entregado';
  cliente: string;
}

@Injectable({
  providedIn: 'root',
})
export class PaqueteService {

  private paquetes: Paquete[] = [
    { numero: 1,  id: 'PKT-001', tipoPaquete: 'Carta',          contenido: 'Documentos legales',       direccionEnvio: 'Cl 100 # 15-45, Medellín',     estado: 'En camino',  cliente: 'jperez'     },
    { numero: 2,  id: 'PKT-002', tipoPaquete: 'Alimenticio',    contenido: 'Frutas tropicales',         direccionEnvio: 'Cra 7 # 32-18, Bogotá',        estado: 'En bodega',  cliente: 'mgarcia'    },
    { numero: 3,  id: 'PKT-003', tipoPaquete: 'No Alimenticio', contenido: 'Herramientas eléctricas',   direccionEnvio: 'Av 68 # 55-30, Bogotá',        estado: 'Entregado',  cliente: 'lrodriguez' },
    { numero: 4,  id: 'PKT-004', tipoPaquete: 'Carta',          contenido: 'Contrato notarial',         direccionEnvio: 'Cl 45 # 22-10, Cali',          estado: 'En camino',  cliente: 'cmartinez'  },
    { numero: 5,  id: 'PKT-005', tipoPaquete: 'Alimenticio',    contenido: 'Productos lácteos',         direccionEnvio: 'Cra 15 # 80-60, Bogotá',       estado: 'Pendiente',  cliente: 'alopez'     },
    { numero: 6,  id: 'PKT-006', tipoPaquete: 'No Alimenticio', contenido: 'Calzado deportivo',         direccionEnvio: 'Cl 13 # 74-25, Barranquilla',  estado: 'En bodega',  cliente: 'rherrera'   },
    { numero: 7,  id: 'PKT-007', tipoPaquete: 'Carta',          contenido: 'Carta certificada',         direccionEnvio: 'Cra 30 # 10-55, Bucaramanga',  estado: 'Despachado', cliente: 'storres'    },
    { numero: 8,  id: 'PKT-008', tipoPaquete: 'No Alimenticio', contenido: 'Partes de computador',      direccionEnvio: 'Cl 72 # 50-40, Bogotá',        estado: 'En bodega',  cliente: 'dflores'    },
    { numero: 9,  id: 'PKT-009', tipoPaquete: 'Alimenticio',    contenido: 'Conservas artesanales',     direccionEnvio: 'Av El Dorado # 85-36, Bogotá', estado: 'Despachado', cliente: 'ncastillo'  },
    { numero: 10, id: 'PKT-010', tipoPaquete: 'No Alimenticio', contenido: 'Juguetes infantiles',       direccionEnvio: 'Cl 80 # 60-20, Pereira',       estado: 'Pendiente',  cliente: 'emoreno'    },
    { numero: 11, id: 'PKT-011', tipoPaquete: 'Alimenticio',    contenido: 'Medicamentos naturales',    direccionEnvio: 'Cra 50 # 25-15, Manizales',    estado: 'Entregado',  cliente: 'jperez'     },
    { numero: 12, id: 'PKT-012', tipoPaquete: 'No Alimenticio', contenido: 'Maquinaria industrial',     direccionEnvio: 'Zona Franca, Cartagena',        estado: 'Despachado', cliente: 'mgarcia'    },
  ];

  getAll(): Paquete[] {
    return this.paquetes;
  }

  agregar(p: Omit<Paquete, 'numero' | 'id' | 'estado'>): Paquete {
    const nuevoNumero = this.paquetes.length
      ? Math.max(...this.paquetes.map(x => x.numero)) + 1
      : 1;
    const nuevo: Paquete = {
      numero:        nuevoNumero,
      id:            'PKT-' + String(nuevoNumero).padStart(3, '0'),
      estado:        'Pendiente',
      ...p,
    };
    this.paquetes.push(nuevo);
    return nuevo;
  }

  actualizar(id: string, cambios: Partial<Paquete>): void {
    const idx = this.paquetes.findIndex(p => p.id === id);
    if (idx !== -1) {
      this.paquetes[idx] = { ...this.paquetes[idx], ...cambios };
    }
  }

  eliminar(id: string): void {
    this.paquetes = this.paquetes.filter(p => p.id !== id);
  }
}
