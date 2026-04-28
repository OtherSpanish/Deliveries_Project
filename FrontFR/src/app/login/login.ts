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

  tipoCliente: string = 'NORMAL';

  mensajeError: string = '';
  mensajeExito: string = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  cambiarRol(nuevoRol: string) {
    this.rol = nuevoRol;
    this.mensajeError = '';
    this.mensajeExito = '';
  }

  cambiarModo(nuevoModo: string) {
    this.modo = nuevoModo;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.usuario = '';
    this.password = '';
    this.codigo = '';
    this.tipoCliente = 'NORMAL';
  }

  login() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.usuario.trim() || !this.password.trim()) {
      this.mensajeError = 'Por favor ingresa usuario y contraseña.';
      return;
    }

    if (this.rol === 'admin') {
      if (!this.codigo.trim()) {
        this.mensajeError = 'El código de administrador es obligatorio.';
        return;
      }
      this.api.adminLogin(this.usuario, this.password, this.codigo).subscribe({
        next: () => this.cambiarPantallaEvent.emit('admin'),
        error: (err) => {
          this.mensajeError = err?.error || 'Credenciales de administrador incorrectas.';
        },
      });
      return;
    }

    if (this.rol === 'cliente') {
      this.api.clienteLogin(this.usuario, this.password).subscribe({
        next: () => {
          localStorage.setItem('usuario_logueado', this.usuario);
          this.cambiarPantallaEvent.emit('cliente-principal');
        },
        error: (err) => {
          this.mensajeError = err?.error || 'Credenciales incorrectas.';
        },
      });
      return;
    }

    if (this.rol === 'conductor') {
      this.api.conductorLogin(this.usuario, this.password).subscribe({
        next: () => this.cambiarPantallaEvent.emit('conductor.component'),
        error: (err) => {
          this.mensajeError = err?.error || 'Credenciales incorrectas.';
        },
      });
      return;
    }

    if (this.rol === 'manipulador') {
      this.api.manipuladorLogin(this.usuario, this.password).subscribe({
        next: () => this.cambiarPantallaEvent.emit('manipulador.component'),
        error: (err) => {
          this.mensajeError = err?.error || 'Credenciales incorrectas.';
        },
      });
      return;
    }
  }

  registrar() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.usuario.trim() || !this.password.trim() || !this.tipoCliente) {
      this.mensajeError = 'Por favor completa todos los campos.';
      return;
    }

    const tipo = this.tipoCliente.toUpperCase();
    // El backend acepta NORMAL y PREMIUM
    if (tipo !== 'NORMAL' && tipo !== 'PREMIUM') {
      this.mensajeError = 'El tipo de cliente debe ser NORMAL o PREMIUM.';
      return;
    }

    // Pasamos cedula vacía — el backend en register no la valida con LanzadorException
    this.api.clienteRegistrar(this.usuario.trim(), this.password, '', tipo).subscribe({
      next: () => {
        this.mensajeExito = 'Usuario registrado correctamente. Ya puedes iniciar sesión.';
        this.usuario = '';
        this.password = '';
        this.tipoCliente = 'NORMAL';
        setTimeout(() => this.cambiarModo('login'), 1500);
      },
      error: (err) => {
        this.mensajeError = err?.error || 'Error al registrar el usuario.';
      },
    });
  }
}
