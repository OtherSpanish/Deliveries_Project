import { Component, EventEmitter, Output } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';

export type VistaActiva = 'ninguna' | 'mostrar' | 'actualizar' | 'borrar' | 'editar' | 'crear' | 'tiempo';

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
  editForm   = { tipoPaquete: '' as Paquete['tipoPaquete'], contenido: '', direccionEnvio: '' };
  crearForm  = { tipoPaquete: 'Carta' as Paquete['tipoPaquete'], contenido: '', direccionEnvio: '' };
  tiempoForm = { tipoPaquete: 'Carta' as Paquete['tipoPaquete'], distanciaKm: 0 };
  tiempoResultado: string | null = null;
  mensajeExito = '';

  constructor(private paqueteService: PaqueteService) {}

  get paquetes(): Paquete[] {
    return this.paqueteService.getAll();
  }

  setVista(vista: VistaActiva): void {
    this.vistaActiva = vista;
    this.paqueteEditando = null;
    this.mensajeExito = '';
    this.tiempoResultado = null;
    this.crearForm = { tipoPaquete: 'Carta', contenido: '', direccionEnvio: '' };
    this.tiempoForm = { tipoPaquete: 'Carta', distanciaKm: 0 };
  }

  iniciarEdicion(paquete: Paquete): void {
    this.paqueteEditando = { ...paquete };
    this.editForm = {
      tipoPaquete:    paquete.tipoPaquete,
      contenido:      paquete.contenido,
      direccionEnvio: paquete.direccionEnvio,
    };
    this.vistaActiva = 'editar';
  }

  guardarEdicion(): void {
    if (!this.paqueteEditando) return;
    this.paqueteService.actualizar(this.paqueteEditando.id, {
      tipoPaquete:    this.editForm.tipoPaquete,
      contenido:      this.editForm.contenido,
      direccionEnvio: this.editForm.direccionEnvio,
    });
    this.mensajeExito = 'Paquete ' + this.paqueteEditando.id + ' actualizado correctamente.';
    this.paqueteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  cancelarEdicion(): void {
    this.paqueteEditando = null;
    this.vistaActiva = 'actualizar';
  }

  borrarPaquete(paquete: Paquete): void {
    if (confirm('¿Eliminar el paquete ' + paquete.id + '?')) {
      this.paqueteService.eliminar(paquete.id);
      this.mensajeExito = 'Paquete ' + paquete.id + ' eliminado.';
    }
  }

  crearPaquete(): void {
    if (!this.crearForm.contenido.trim() || !this.crearForm.direccionEnvio.trim()) return;
    const nuevo = this.paqueteService.agregar({
      tipoPaquete:    this.crearForm.tipoPaquete,
      contenido:      this.crearForm.contenido.trim(),
      direccionEnvio: this.crearForm.direccionEnvio.trim(),
      cliente:        'admin',
    });
    this.mensajeExito = 'Paquete ' + nuevo.id + ' creado correctamente.';
    this.crearForm = { tipoPaquete: 'Carta', contenido: '', direccionEnvio: '' };
  }

  calcularTiempo(): void {
    if (!this.tiempoForm.distanciaKm || this.tiempoForm.distanciaKm <= 0) return;

    const velocidades: Record<string, number> = {
      'Carta':          80,
      'Alimenticio':    60,
      'No Alimenticio': 70,
    };

    const extras: Record<string, number> = {
      'Carta':          1,
      'Alimenticio':    2,
      'No Alimenticio': 1,
    };

    const velocidad   = velocidades[this.tiempoForm.tipoPaquete] ?? 80;
    const totalHoras  = (this.tiempoForm.distanciaKm / velocidad) + (extras[this.tiempoForm.tipoPaquete] ?? 1);
    const dias        = Math.floor(totalHoras / 24);
    const horas       = Math.round(totalHoras % 24);

    if (dias > 0) {
      this.tiempoResultado = dias + ' día' + (dias > 1 ? 's' : '') + ' y ' + horas + ' hora' + (horas !== 1 ? 's' : '');
    } else {
      this.tiempoResultado = horas + ' hora' + (horas !== 1 ? 's' : '');
    }
  }

  volver(): void {
    this.cerrarSesionEvent.emit('admin');
  }
}
