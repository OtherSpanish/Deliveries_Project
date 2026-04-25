import { Component, Output, EventEmitter, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login implements OnInit {

  // EVENTO para cambiar pantalla
  @Output() cambiarPantallaEvent = new EventEmitter<string>();

  // CARRUSEL
  imagenes: string[] = [
    'login_pic_1.png',
    'login_pic_2.png'
  ];

  indiceImagen: number = 0;

  // LOGIN
  usuario: string = '';
  password: string = '';
  codigo: string = '';

  rol: string = 'cliente'; // por defecto

  // MODO (login / register)
  modo: string = 'login';

  // REGISTER
  nombre: string = '';
  direccion: string = '';
  cedula: string = '';
  tipoCliente: string = '';

  // (carrusel automático)
  ngOnInit() {
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  // Cambiar rol
  cambiarRol(nuevoRol: string) {
    this.rol = nuevoRol;
  }

  // Cambiar entre login y register
  cambiarModo(nuevoModo: string) {
    this.modo = nuevoModo;
  }

  // LOGIN
  login() {
    console.log("Rol:", this.rol);
    console.log("Usuario:", this.usuario);

    // ADMIN
    if (this.rol === 'admin') {
      if (this.codigo === '1234') {
        alert('Admin verificado');
        this.cambiarPantallaEvent.emit('admin');
        return;
      } else {
        alert('Código incorrecto');
        return;
      }
    }

    // OTROS ROLES
    if (this.rol === 'cliente') {
      this.cambiarPantallaEvent.emit('cliente-principal');
    }

    if (this.rol === 'conductor') {
      this.cambiarPantallaEvent.emit('conductor.component');
    }

    if (this.rol === 'manipulador') {
      this.cambiarPantallaEvent.emit('manipulador.component');
    }

    alert('Login como ' + this.rol);
  }

  // REGISTER
  registrar() {
    console.log("Registro:");
    console.log("Usuario:", this.usuario);
    console.log("Nombre:", this.nombre);
    console.log("Dirección:", this.direccion);
    console.log("Cédula:", this.cedula);
    console.log("Tipo:", this.tipoCliente);

    // Validación básica
    if (
      !this.usuario ||
      !this.password ||
      !this.nombre ||
      !this.direccion ||
      !this.cedula ||
      !this.tipoCliente
    ) {
      alert('Completa todos los campos');
      return;
    }

    alert('Usuario registrado correctamente (simulado)');

    // Reset
    this.usuario = '';
    this.password = '';
    this.nombre = '';
    this.direccion = '';
    this.cedula = '';
    this.tipoCliente = '';

    // Volver a login
    this.modo = 'login';
  }
}
