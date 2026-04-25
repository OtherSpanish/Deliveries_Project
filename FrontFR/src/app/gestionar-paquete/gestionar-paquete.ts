import { Component, EventEmitter, Output } from '@angular/core';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar' | 'crear' | 'tiempo';

export interface Paquete {
  numero: number;
  id: string;
  tipoPaquete: string;
  contenido: string;
  direccionEnvio: string;
}

@Component({
  selector: 'app-gestionar-paquete',
  standalone: false,
  templateUrl: './gestionar-paquete.html',
  styleUrl: './gestionar-paquete.css',
})
export class GestionarPaquete {

  @Output() cerrarSesionEvent = new EventEmitter<string>();

  vistaActiva: VistaActiva = 'ninguna';
  paqueteEditando: Paquete | null = null;
  editForm   = { tipoPaquete: '', contenido: '', direccionEnvio: '' };
  crearForm  = { tipoPaquete: 'Normal', contenido: '', direccionEnvio: '' };
  tiempoForm = { tipoPaquete: 'Normal', distanciaKm: 0 };
  tiempoResultado: string | null = null;
  mensajeExito = '';

  paquetes: Paquete[] = [
    { numero: 1,  id: 'PKT-001', tipoPaquete: 'Frágil',      contenido: 'Vajilla de porcelana',   direccionEnvio: 'Cra 7 # 32-18, Bogotá'       },
    { numero: 2,  id: 'PKT-002', tipoPaquete: 'Normal',      contenido: 'Ropa deportiva',          direccionEnvio: 'Cl 100 # 15-45, Medellín'     },
    { numero: 3,  id: 'PKT-003', tipoPaquete: 'Pesado',      contenido: 'Herramientas eléctricas', direccionEnvio: 'Av 68 # 55-30, Bogotá'        },
    { numero: 4,  id: 'PKT-004', tipoPaquete: 'Frágil',      contenido: 'Monitor 27"',             direccionEnvio: 'Cl 45 # 22-10, Cali'          },
    { numero: 5,  id: 'PKT-005', tipoPaquete: 'Normal',      contenido: 'Libros universitarios',   direccionEnvio: 'Cra 15 # 80-60, Bogotá'       },
    { numero: 6,  id: 'PKT-006', tipoPaquete: 'Refrigerado', contenido: 'Productos lácteos',       direccionEnvio: 'Cl 13 # 74-25, Barranquilla'  },
    { numero: 7,  id: 'PKT-007', tipoPaquete: 'Normal',      contenido: 'Calzado deportivo',       direccionEnvio: 'Cra 30 # 10-55, Bucaramanga'  },
    { numero: 8,  id: 'PKT-008', tipoPaquete: 'Pesado',      contenido: 'Partes de computador',    direccionEnvio: 'Cl 72 # 50-40, Bogotá'        },
    { numero: 9,  id: 'PKT-009', tipoPaquete: 'Frágil',      contenido: 'Instrumentos musicales',  direccionEnvio: 'Av El Dorado # 85-36, Bogotá' },
    { numero: 10, id: 'PKT-010', tipoPaquete: 'Normal',      contenido: 'Juguetes infantiles',     direccionEnvio: 'Cl 80 # 60-20, Pereira'       },
    { numero: 11, id: 'PKT-011', tipoPaquete: 'Refrigerado', contenido: 'Medicamentos',            direccionEnvio: 'Cra 50 # 25-15, Manizales'    },
    { numero: 12, id: 'PKT-012', tipoPaquete: 'Pesado',      contenido: 'Maquinaria industrial',   direccionEnvio: 'Zona Franca, Cartagena'        },
  ];

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.paqueteEditando = null;
    this.mensajeExito = '';
    this.tiempoResultado = null;
    this.crearForm = { tipoPaquete: 'Normal', contenido: '', direccionEnvio: '' };
    this.tiempoForm = { tipoPaquete: 'Normal', distanciaKm: 0 };
  }

  iniciarEdicion(paquete: Paquete): void {
    this.paqueteEditando = { ...paquete };
    this.editForm = {
      tipoPaquete: paquete.tipoPaquete,
      contenido: paquete.contenido,
      direccionEnvio: paquete.direccionEnvio,
    };
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.paqueteEditando) return;
    const idx = this.paquetes.findIndex(p => p.id === this.paqueteEditando!.id);
    if (idx !== -1) {
      this.paquetes[idx] = {
        ...this.paquetes[idx],
        tipoPaquete:    this.editForm.tipoPaquete,
        contenido:      this.editForm.contenido,
        direccionEnvio: this.editForm.direccionEnvio,
      };
    }
    this.mensajeExito = `Paquete ${this.paqueteEditando.id} actualizado correctamente.`;
    this.paqueteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  cancelarEdicion(): void {
    this.paqueteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarPaquete(paquete: Paquete): void {
    if (confirm(`¿Eliminar el paquete ${paquete.id}?`)) {
      this.paquetes = this.paquetes.filter(p => p.id !== paquete.id);
      this.mensajeExito = `Paquete ${paquete.id} eliminado.`;
    }
  }

  // ── CREAR ──────────────────────────────────────────────
  crearPaquete(): void {
    if (!this.crearForm.contenido.trim() || !this.crearForm.direccionEnvio.trim()) return;

    const nuevoNumero = this.paquetes.length
      ? Math.max(...this.paquetes.map(p => p.numero)) + 1
      : 1;

    const nuevoId = `PKT-${String(nuevoNumero).padStart(3, '0')}`;

    this.paquetes.push({
      numero:        nuevoNumero,
      id:            nuevoId,
      tipoPaquete:   this.crearForm.tipoPaquete,
      contenido:     this.crearForm.contenido.trim(),
      direccionEnvio: this.crearForm.direccionEnvio.trim(),
    });

    this.mensajeExito = `Paquete ${nuevoId} creado correctamente.`;
    this.crearForm = { tipoPaquete: 'Normal', contenido: '', direccionEnvio: '' };
  }

  // ── TIEMPO DE ENTREGA ──────────────────────────────────
  calcularTiempo(): void {
    if (!this.tiempoForm.distanciaKm || this.tiempoForm.distanciaKm <= 0) return;

    // Velocidad base según tipo (km/h)
    const velocidades: Record<string, number> = {
      Normal:      80,
      Frágil:      60,
      Pesado:      50,
      Refrigerado: 70,
    };

    const velocidad = velocidades[this.tiempoForm.tipoPaquete] ?? 80;
    const horasViaje = this.tiempoForm.distanciaKm / velocidad;

    // Tiempo extra de preparación/manipulación según tipo (horas)
    const extras: Record<string, number> = {
      Normal:      1,
      Frágil:      3,
      Pesado:      2,
      Refrigerado: 2,
    };

    const totalHoras = horasViaje + (extras[this.tiempoForm.tipoPaquete] ?? 1);
    const dias   = Math.floor(totalHoras / 24);
    const horas  = Math.round(totalHoras % 24);

    if (dias > 0) {
      this.tiempoResultado = `${dias} día${dias > 1 ? 's' : ''} y ${horas} hora${horas !== 1 ? 's' : ''}`;
    } else {
      this.tiempoResultado = `${horas} hora${horas !== 1 ? 's' : ''}`;
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
