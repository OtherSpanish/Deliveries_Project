import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';
import { ApiService } from '../services/api.service';

export type VistaCliente = 'ninguna' | 'carta' | 'alimenticio' | 'no-alimenticio' | 'mis-pedidos';

@Component({
  selector: 'app-cliente-principal',
  standalone: false,
  templateUrl: './cliente-principal.html',
  styleUrls: ['./cliente-principal.css'],
})
export class ClientePrincipal implements OnInit {
  @Output() cerrarSesionEvent = new EventEmitter<string>();

  nombreCliente: string = 'Cliente';

  imagenes: string[] = ['login_pic_1.png', 'login_pic_2.png'];
  indiceImagen: number = 0;

  vistaActiva: VistaCliente = 'ninguna';
  mensajeExito: string = '';
  mensajeError: string = '';

  cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', contenido: '' };
  alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
  noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };

  constructor(
    private paqueteService: PaqueteService,
    private api: ApiService,
  ) {}

  get misPedidos(): Paquete[] {
    return this.paqueteService.getAll().filter((p) => p.cliente === this.nombreCliente);
  }

  ngOnInit() {
    const usuarioGuardado = localStorage.getItem('usuario_logueado');
    if (usuarioGuardado) {
      this.nombreCliente = usuarioGuardado;
    }
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  private mostrarError(err: any, fallback: string): void {
    this.mensajeError = err?.error || fallback;
  }

  setVista(vista: VistaCliente): void {
    this.vistaActiva = vista;
    this.mensajeExito = '';
    this.mensajeError = '';
    this.cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', contenido: '' };
    this.alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
    this.noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };
  }

  // Validación alineada con DireccionException del backend:
  // debe empezar con Calle/Carrera/Av/Cl/Cra/Av/Cll, tener número, '#' y '-'
  private validarDireccion(direccion: string): boolean {
    const d = direccion.trim().toLowerCase();
    const prefijos = ['calle', 'carrera', 'avenida', 'cl', 'cra', 'av', 'cll'];
    const tienePrefijo = prefijos.some(p => d.startsWith(p));
    if (!tienePrefijo || !d.includes('#') || !d.includes('-') || !/\d/.test(d)) {
      this.mensajeError =
        'La dirección debe tener formato colombiano válido. Ejemplo: Calle 100 # 15-45 o Cra 7 # 32-18.';
      return false;
    }
    return true;
  }

  enviarCarta(): void {
    this.mensajeError = '';
    if (!this.cartaForm.remitente || !this.cartaForm.destinatario ||
        !this.cartaForm.direccionEnvio || !this.cartaForm.contenido) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios.';
      return;
    }
    if (!this.validarDireccion(this.cartaForm.direccionEnvio)) return;
    const contenido = this.cartaForm.contenido;
    this.api.paqueteCrear(
      'CARTA',
      contenido,
      this.cartaForm.direccionEnvio.trim(),
      this.cartaForm.destinatario,
      0,
      false,
      false,
    ).subscribe({
      next: () => {
        this.mensajeExito = 'Carta enviada correctamente.';
        this.paqueteService.agregar({
          tipoPaquete: 'Carta',
          contenido,
          direccionEnvio: this.cartaForm.direccionEnvio.trim(),
          cliente: this.nombreCliente,
        });
        this.cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', contenido: '' };
      },
      error: (err) => this.mostrarError(err, 'Error al enviar la carta'),
    });
  }

  enviarAlimenticio(): void {
    this.mensajeError = '';
    if (!this.alimenticioForm.contenido || !this.alimenticioForm.direccionEnvio ||
        this.alimenticioForm.pesoKg <= 0) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios. El peso debe ser mayor a 0.';
      return;
    }
    if (!this.validarDireccion(this.alimenticioForm.direccionEnvio)) return;
    this.api.paqueteCrear(
      'ALIMENTICIO',
      this.alimenticioForm.contenido,
      this.alimenticioForm.direccionEnvio.trim(),
      this.nombreCliente,
      this.alimenticioForm.pesoKg,
      false,
      this.alimenticioForm.requiereRefrigeracion,
    ).subscribe({
      next: () => {
        this.mensajeExito = 'Paquete alimenticio enviado correctamente.';
        this.paqueteService.agregar({
          tipoPaquete: 'Alimenticio',
          contenido: this.alimenticioForm.contenido,
          direccionEnvio: this.alimenticioForm.direccionEnvio.trim(),
          cliente: this.nombreCliente,
        });
        this.alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
      },
      error: (err) => this.mostrarError(err, 'Error al enviar el paquete alimenticio'),
    });
  }

  enviarNoAlimenticio(): void {
    this.mensajeError = '';
    if (!this.noAlimenticioForm.contenido || !this.noAlimenticioForm.direccionEnvio ||
        this.noAlimenticioForm.pesoKg <= 0) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios. El peso debe ser mayor a 0.';
      return;
    }
    if (!this.validarDireccion(this.noAlimenticioForm.direccionEnvio)) return;
    this.api.paqueteCrear(
      'NO_ALIMENTICIO',
      this.noAlimenticioForm.contenido,
      this.noAlimenticioForm.direccionEnvio.trim(),
      this.nombreCliente,
      this.noAlimenticioForm.pesoKg,
      this.noAlimenticioForm.esFragil,
      false,
    ).subscribe({
      next: () => {
        this.mensajeExito = 'Paquete no alimenticio enviado correctamente.';
        this.paqueteService.agregar({
          tipoPaquete: 'No Alimenticio',
          contenido: this.noAlimenticioForm.contenido,
          direccionEnvio: this.noAlimenticioForm.direccionEnvio.trim(),
          cliente: this.nombreCliente,
        });
        this.noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };
      },
      error: (err) => this.mostrarError(err, 'Error al enviar el paquete no alimenticio'),
    });
  }

  logout(): void {
    this.api.clienteLogout().subscribe({ error: () => {} });
    localStorage.removeItem('usuario_logueado');
    this.cerrarSesionEvent.emit('login');
  }
}
