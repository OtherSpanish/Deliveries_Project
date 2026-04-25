import { Component, EventEmitter, Output, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-principal',
  standalone: false,
  templateUrl: './admin-principal.html',
  styleUrls: ['./admin-principal.css'],
})
export class AdminPrincipal implements OnInit {

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
    if (tipo === 'paquete') {
      this.cerrarSesionEvent.emit('gestionar-paquete');  // nombre exacto de la pantalla
    }else if(tipo === 'manipulador'){
      this.cerrarSesionEvent.emit('gestionar-manipulador');
    }else if(tipo === 'conductor'){
      this.cerrarSesionEvent.emit('gestionar-conductor');
    } else if(tipo === 'cliente'){
      this.cerrarSesionEvent.emit('gestionar-cliente');
    }else {
      console.log('Gestionar:', tipo);
    }
  }

  logout() {
    this.cerrarSesionEvent.emit('login');
  }
}
