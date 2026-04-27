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

  cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', contenido: '' };
  alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
  noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };

  constructor(private paqueteService: PaqueteService, private api: ApiService) {}

  get misPedidos(): Paquete[] {
    return this.paqueteService.getAll().filter(p => p.cliente === this.nombreCliente);
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

  setVista(vista: VistaCliente): void {
    this.vistaActiva = vista;
    this.mensajeExito = '';
    this.cartaForm         = { remitente: '', destinatario: '', direccionEnvio: '', contenido: '' };
    this.alimenticioForm   = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
    this.noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };
  }

  enviarCarta(): void {
    if (!this.cartaForm.remitente || !this.cartaForm.destinatario || !this.cartaForm.direccionEnvio || !this.cartaForm.contenido) {
      alert('Por favor completa todos los campos obligatorios.');
      return;
    }
    const contenido = this.cartaForm.contenido;
    this.api.paqueteCrear('CARTA', contenido, this.cartaForm.direccionEnvio).subscribe({
      next: (msg) => {
        // Tambien agregar al servicio local para que mis-pedidos lo muestre
        const nuevo = this.paqueteService.agregar({
          tipoPaquete: 'Carta', contenido, direccionEnvio: this.cartaForm.direccionEnvio, cliente: this.nombreCliente,
        });
        this.mensajeExito = 'Carta registrada correctamente. Tu pedido ' + nuevo.id + ' ya está en el sistema.';
        this.cartaForm = { remitente: '', destinatario: '', direccionEnvio: '', contenido: '' };
      },
      error: (err) => alert(err?.error || 'Error al registrar la carta'),
    });
  }

  enviarAlimenticio(): void {
    if (!this.alimenticioForm.contenido || !this.alimenticioForm.direccionEnvio || this.alimenticioForm.pesoKg <= 0) {
      alert('Por favor completa todos los campos obligatorios.');
      return;
    }
    this.api.paqueteCrear('ALIMENTICIO', this.alimenticioForm.contenido, this.alimenticioForm.direccionEnvio).subscribe({
      next: (msg) => {
        const nuevo = this.paqueteService.agregar({
          tipoPaquete: 'Alimenticio', contenido: this.alimenticioForm.contenido,
          direccionEnvio: this.alimenticioForm.direccionEnvio, cliente: this.nombreCliente,
        });
        this.mensajeExito = 'Paquete alimenticio registrado. Tu pedido ' + nuevo.id + ' ya está en el sistema.';
        this.alimenticioForm = { contenido: '', pesoKg: 0, requiereRefrigeracion: false, direccionEnvio: '' };
      },
      error: (err) => alert(err?.error || 'Error al registrar el paquete'),
    });
  }

  enviarNoAlimenticio(): void {
    if (!this.noAlimenticioForm.contenido || !this.noAlimenticioForm.direccionEnvio || this.noAlimenticioForm.pesoKg <= 0) {
      alert('Por favor completa todos los campos obligatorios.');
      return;
    }
    this.api.paqueteCrear('NO_ALIMENTICIO', this.noAlimenticioForm.contenido, this.noAlimenticioForm.direccionEnvio).subscribe({
      next: (msg) => {
        const nuevo = this.paqueteService.agregar({
          tipoPaquete: 'No Alimenticio', contenido: this.noAlimenticioForm.contenido,
          direccionEnvio: this.noAlimenticioForm.direccionEnvio, cliente: this.nombreCliente,
        });
        this.mensajeExito = 'Paquete no alimenticio registrado. Tu pedido ' + nuevo.id + ' ya está en el sistema.';
        this.noAlimenticioForm = { contenido: '', pesoKg: 0, esFragil: false, direccionEnvio: '' };
      },
      error: (err) => alert(err?.error || 'Error al registrar el paquete'),
    });
  }

  logout(): void {
    this.api.clienteLogout().subscribe({ error: () => {} });
    localStorage.removeItem('usuario_logueado');
    this.cerrarSesionEvent.emit('login');
  }
}
