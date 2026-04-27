import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login implements OnInit {

  @Output() cambiarPantallaEvent = new EventEmitter<string>();

  imagenes: string[] = ['login_pic_1.png', 'login_pic_2.png'];
  indiceImagen: number = 0;

  usuario: string = '';
  password: string = '';
  codigo: string = '';
  rol: string = 'cliente';
  modo: string = 'login';

  nombre: string = '';
  direccion: string = '';
  cedula: string = '';
  tipoCliente: string = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  cambiarRol(nuevoRol: string) {
    this.rol = nuevoRol;
  }

  cambiarModo(nuevoModo: string) {
    this.modo = nuevoModo;
  }

  login() {
    if (this.rol === 'admin') {
      this.api.adminLogin(this.usuario, this.password, this.codigo).subscribe({
        next: () => this.cambiarPantallaEvent.emit('admin'),
        error: () => alert('Credenciales de administrador incorrectas'),
      });
      return;
    }
    if (this.rol === 'cliente') {
      this.api.clienteLogin(this.usuario, this.password).subscribe({
        next: () => this.cambiarPantallaEvent.emit('cliente-principal'),
        error: () => alert('Credenciales incorrectas'),
      });
      return;
    }
    if (this.rol === 'conductor') {
      this.api.conductorLogin(this.usuario, this.password).subscribe({
        next: () => this.cambiarPantallaEvent.emit('conductor.component'),
        error: () => alert('Credenciales incorrectas'),
      });
      return;
    }
    if (this.rol === 'manipulador') {
      this.api.manipuladorLogin(this.usuario, this.password).subscribe({
        next: () => this.cambiarPantallaEvent.emit('manipulador.component'),
        error: () => alert('Credenciales incorrectas'),
      });
      return;
    }
  }

  registrar() {
    if (!this.usuario || !this.password || !this.nombre || !this.cedula || !this.tipoCliente) {
      alert('Completa todos los campos');
      return;
    }
    // El backend espera NORMAL o PREMIUM en mayúsculas
    const tipo = this.tipoCliente.toUpperCase();
    this.api.clienteRegistrar(this.usuario, this.password, this.cedula, tipo).subscribe({
      next: () => {
        alert('Usuario registrado correctamente');
        this.usuario = ''; this.password = ''; this.nombre = '';
        this.direccion = ''; this.cedula = ''; this.tipoCliente = '';
        this.modo = 'login';
      },
      error: (err) => alert(err?.error || 'Error al registrar'),
    });
  }
}
