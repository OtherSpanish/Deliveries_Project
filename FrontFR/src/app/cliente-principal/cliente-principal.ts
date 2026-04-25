import { Component, EventEmitter, Output, OnInit } from '@angular/core';

@Component({
  selector: 'app-cliente-principal',
  standalone: false,
  templateUrl: './cliente-principal.html',
  styleUrls: ['./cliente-principal.css'],
})
export class ClientePrincipal implements OnInit {
  // EVENTO para volver al login
  @Output() cerrarSesionEvent = new EventEmitter<string>();

  nombreCliente: string = 'Cliente';

  imagenes: string[] = ['paquete_carta.png', 'paquete_balon.png'];

  indiceImagen: number = 0;

  ngOnInit() {
    setInterval(() => {
      this.indiceImagen = (this.indiceImagen + 1) % this.imagenes.length;
    }, 5000);
  }

  pedir(tipo: string) {
    console.log('Pedir:', tipo);
    alert('Entrando a pedido de ' + tipo);
  }

  // LOGOUT
  logout() {
    this.cerrarSesionEvent.emit('login');
  }
}

