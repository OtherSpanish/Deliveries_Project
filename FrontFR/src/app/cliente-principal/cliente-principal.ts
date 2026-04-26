import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { PaqueteService, Paquete } from '../paquete.service';

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

  cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', descripcion: '' };
  alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
  noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };

  constructor(private paqueteService: PaqueteService) {}

  get misPedidos(): Paquete[] {
    return this.paqueteService.getAll().filter(p => p.cliente === this.nombreCliente);
  }

  ngOnInit() {
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  setVista(vista: VistaCliente): void {
    this.vistaActiva = vista;
    this.mensajeExito = '';
    this.cartaForm         = { remitente: '', destinatario: '', direccionEnvio: '', descripcion: '' };
    this.alimenticioForm   = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
    this.noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };
  }

  enviarCarta(): void {
    if (!this.cartaForm.remitente || !this.cartaForm.destinatario || !this.cartaForm.direccionEnvio) {
      alert('Por favor completa todos los campos obligatorios.');
      return;
    }
    const nuevo = this.paqueteService.agregar({
      tipoPaquete:    'Carta',
      contenido:      'Carta de ' + this.cartaForm.remitente + ' para ' + this.cartaForm.destinatario,
      direccionEnvio: this.cartaForm.direccionEnvio,
      cliente:        this.nombreCliente,
    });
    this.mensajeExito = 'Carta registrada correctamente. Tu pedido ' + nuevo.id + ' ya está en el sistema.';
    this.cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', descripcion: '' };
  }

  enviarAlimenticio(): void {
    if (!this.alimenticioForm.contenido || !this.alimenticioForm.direccionEnvio || this.alimenticioForm.pesoKg <= 0) {
      alert('Por favor completa todos los campos obligatorios.');
      return;
    }
    const nuevo = this.paqueteService.agregar({
      tipoPaquete:    'Alimenticio',
      contenido:      this.alimenticioForm.contenido,
      direccionEnvio: this.alimenticioForm.direccionEnvio,
      cliente:        this.nombreCliente,
    });
    this.mensajeExito = 'Paquete alimenticio registrado. Tu pedido ' + nuevo.id + ' ya está en el sistema.';
    this.alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
  }

  enviarNoAlimenticio(): void {
    if (!this.noAlimenticioForm.contenido || !this.noAlimenticioForm.direccionEnvio || this.noAlimenticioForm.pesoKg <= 0) {
      alert('Por favor completa todos los campos obligatorios.');
      return;
    }
    const nuevo = this.paqueteService.agregar({
      tipoPaquete:    'No Alimenticio',
      contenido:      this.noAlimenticioForm.contenido,
      direccionEnvio: this.noAlimenticioForm.direccionEnvio,
      cliente:        this.nombreCliente,
    });
    this.mensajeExito = 'Paquete no alimenticio registrado. Tu pedido ' + nuevo.id + ' ya está en el sistema.';
    this.noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };
  }

  logout(): void {
    this.cerrarSesionEvent.emit('login');
  }
}
