import { Component, EventEmitter, Output } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';
import { ApiService } from '../services/api.service';

export type VistaActiva =
  | 'ninguna'
  | 'mostrar'
  | 'actualizar'
  | 'borrar'
  | 'editar'
  | 'crear'
  | 'tiempo';

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
  editForm = { tipoPaquete: '' as Paquete['tipoPaquete'], contenido: '', direccionEnvio: '' };
  crearForm = {
    tipoPaquete: 'Carta' as Paquete['tipoPaquete'],
    contenido: '',
    direccionEnvio: '',
    destinatario: '',
    pesoKg: 0,
    esFragil: false,
    requiereRefrigeracion: false,
  };
  tiempoForm = { tipoPaquete: 'Carta' as Paquete['tipoPaquete'], distanciaKm: 0 };
  tiempoResultado: string | null = null;
  mensajeExito = '';
  mensajeError = '';

  constructor(
    private paqueteService: PaqueteService,
    private api: ApiService,
  ) {}

  get paquetes(): Paquete[] {
    return this.paqueteService.getAll();
  }

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.paqueteEditando = null;
    this.mensajeExito = '';
    this.mensajeError = '';
    this.tiempoResultado = null;
    this.crearForm = {
      tipoPaquete: 'Carta',
      contenido: '',
      direccionEnvio: '',
      destinatario: '',
      pesoKg: 0,
      esFragil: false,
      requiereRefrigeracion: false,
    };
    this.tiempoForm = { tipoPaquete: 'Carta', distanciaKm: 0 };
    if (vista === 'mostrar' || vista === 'actualizar' || vista === 'borrar') {
      this.cargarPaquetes();
    }
  }

  private mostrarError(err: any, fallback: string): void {
    const msg = err?.error || fallback;
    this.mensajeError = msg;
  }

  cargarPaquetes(): void {
    this.api.adminMostrarPaquetes().subscribe({
      next: (data) => {
        try {
          const lista = JSON.parse(data);
          const nuevos: Paquete[] = lista.map((p: any, i: number) => ({
            numero: i + 1,
            id: String(p.id),
            tipoPaquete:
              p.tipoPaquete === 'CARTA'
                ? 'Carta'
                : p.tipoPaquete === 'ALIMENTICIO'
                  ? 'Alimenticio'
                  : 'No Alimenticio',
            contenido: p.contenido,
            direccionEnvio: p.direccionDeEnvio,
            estado:
              p.estadoPaquete === 'EN_BODEGA'
                ? 'En bodega'
                : p.estadoPaquete === 'DESPACHADO'
                  ? 'Despachado'
                  : p.estadoPaquete === 'EN_CAMINO'
                    ? 'En camino'
                    : 'Entregado',
            cliente: p.clientePaquete,
          }));
          this.paqueteService.setPaquetes(nuevos);
        } catch {
          /* mantiene datos locales */
        }
      },
      error: (err) => {
        // 401 = no hay sesión de admin, no mostrar error en pantalla de lista
        if (err?.status !== 401) {
          this.mensajeError = err?.error || 'No se pudieron cargar los paquetes';
        }
      },
    });
  }

  iniciarEdicion(paquete: Paquete): void {
    this.paqueteEditando = { ...paquete };
    this.editForm = {
      tipoPaquete: paquete.tipoPaquete,
      contenido: paquete.contenido,
      direccionEnvio: paquete.direccionEnvio,
    };
    this.mensajeError = '';
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.paqueteEditando) return;
    this.mensajeError = '';
    const tipoBE =
      this.editForm.tipoPaquete === 'Carta'
        ? 'CARTA'
        : this.editForm.tipoPaquete === 'Alimenticio'
          ? 'ALIMENTICIO'
          : 'NO_ALIMENTICIO';
    this.api
      .adminActualizarPaquete(
        Number(this.paqueteEditando.id),
        tipoBE,
        this.editForm.contenido,
        this.editForm.direccionEnvio,
      )
      .subscribe({
        next: () => {
          this.paqueteService.actualizar(this.paqueteEditando!.id, {
            tipoPaquete: this.editForm.tipoPaquete,
            contenido: this.editForm.contenido,
            direccionEnvio: this.editForm.direccionEnvio,
          });
          this.mensajeExito = 'Paquete ' + this.paqueteEditando!.id + ' actualizado correctamente.';
          this.paqueteEditando = null;
          this.vistaActiva = 'actualizar';
        },
        error: (err) => this.mostrarError(err, 'Error al actualizar el paquete'),
      });
  }

  cancelarEdicion(): void {
    this.paqueteEditando = null;
    this.mensajeError = '';
    this.vistaActiva = 'actualizar';
  }

  borrarPaquete(paquete: Paquete): void {
    if (confirm('¿Eliminar el paquete ' + paquete.id + '?')) {
      this.mensajeError = '';
      this.api.adminBorrarPaquete(Number(paquete.id)).subscribe({
        next: () => {
          this.paqueteService.eliminar(paquete.id);
          this.mensajeExito = 'Paquete ' + paquete.id + ' eliminado.';
        },
        error: (err) => this.mostrarError(err, 'Error al eliminar el paquete'),
      });
    }
  }

  crearPaquete(): void {
    this.mensajeError = '';
    if (
      !this.crearForm.contenido.trim() ||
      !this.crearForm.direccionEnvio.trim() ||
      !this.crearForm.destinatario.trim()
    ) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios.';
      return;
    }
    if (this.crearForm.tipoPaquete !== 'Carta' && this.crearForm.pesoKg <= 0) {
      this.mensajeError = 'El peso debe ser mayor a 0 kg.';
      return;
    }
    const tipoBE =
      this.crearForm.tipoPaquete === 'Carta'
        ? 'CARTA'
        : this.crearForm.tipoPaquete === 'Alimenticio'
          ? 'ALIMENTICIO'
          : 'NO_ALIMENTICIO';
    this.api
      .paqueteCrear(
        tipoBE,
        this.crearForm.contenido.trim(),
        this.crearForm.direccionEnvio.trim(),
        this.crearForm.destinatario.trim(),
        this.crearForm.pesoKg,
        this.crearForm.esFragil,
        this.crearForm.requiereRefrigeracion,
      )
      .subscribe({
        next: () => {
          const nuevo = this.paqueteService.agregar({
            tipoPaquete: this.crearForm.tipoPaquete,
            contenido: this.crearForm.contenido.trim(),
            direccionEnvio: this.crearForm.direccionEnvio.trim(),
            cliente: 'admin',
          });
          this.mensajeExito = 'Paquete ' + nuevo.id + ' creado correctamente.';
          this.crearForm = {
            tipoPaquete: 'Carta',
            contenido: '',
            direccionEnvio: '',
            destinatario: '',
            pesoKg: 0,
            esFragil: false,
            requiereRefrigeracion: false,
          };
        },
        error: (err) => this.mostrarError(err, 'Error al crear el paquete'),
      });
  }

  calcularTiempo(): void {
    this.mensajeError = '';
    if (!this.tiempoForm.distanciaKm || this.tiempoForm.distanciaKm <= 0) {
      this.mensajeError = 'Ingresa una distancia mayor a 0 km.';
      return;
    }
    const velocidades: Record<string, number> = {
      Carta: 80,
      Alimenticio: 60,
      'No Alimenticio': 70,
    };
    const extras: Record<string, number> = {
      Carta: 1,
      Alimenticio: 2,
      'No Alimenticio': 1,
    };

    const velocidad = velocidades[this.tiempoForm.tipoPaquete] ?? 80;
    const totalHoras =
      this.tiempoForm.distanciaKm / velocidad + (extras[this.tiempoForm.tipoPaquete] ?? 1);
    const dias = Math.floor(totalHoras / 24);
    const horas = Math.round(totalHoras % 24);

    if (dias > 0) {
      this.tiempoResultado =
        dias + ' día' + (dias > 1 ? 's' : '') + ' y ' + horas + ' hora' + (horas !== 1 ? 's' : '');
    } else {
      this.tiempoResultado = horas + ' hora' + (horas !== 1 ? 's' : '');
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
