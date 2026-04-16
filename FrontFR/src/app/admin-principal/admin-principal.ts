import { Component, EventEmitter, Output, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-principal',
  standalone: false,
  templateUrl: './admin-principal.html',
  styleUrls: ['./admin-principal.css'],
})
export class AdminPrincipal implements OnInit {

  // EVENTO para volver al login
  @Output() cerrarSesionEvent = new EventEmitter<string>();

  nombreAdmin: string = 'Admin';

  imagenes: string[] = [
    'paquete_carta.png',
    'paquete_balon.png'
  ];

  indiceImagen: number = 0;

  ngOnInit() {
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  gestionar(tipo: string) {
    console.log("Gestionar:", tipo);
    alert("Entrando a gestión de " + tipo);
  }

  // LOGOUT
  logout() {
    this.cerrarSesionEvent.emit('login');
  }
}
