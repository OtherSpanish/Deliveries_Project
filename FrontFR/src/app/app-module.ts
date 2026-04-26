import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './login/login';
import { AdminPrincipal } from './admin-principal/admin-principal';
import { ClientePrincipal } from './cliente-principal/cliente-principal';
import { GestionarPaquete } from './gestionar-paquete/gestionar-paquete';
import { GestionarManipulador } from './gestionar-manipulador/gestionar-manipulador';
import { GestionarConductor } from './gestionar-conductor/gestionar-conductor';
import { GestionarCliente } from './gestionar-cliente/gestionar-cliente';
import { ConductorComponent } from './conductor.component/conductor.component';
import { ManipuladorComponent } from './manipulador.component/manipulador.component';

@NgModule({
  declarations: [
    App,
    Login,
    AdminPrincipal,
    ClientePrincipal,
    GestionarPaquete,
    GestionarManipulador,
    GestionarConductor,
    GestionarCliente,
    ConductorComponent,
    ManipuladorComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule, HttpClientModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
