import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './login/login';
import { AdminPrincipal } from './admin-principal/admin-principal';
import { ClientePrincipal } from './cliente-principal/cliente-principal';

@NgModule({
  declarations: [App, Login, AdminPrincipal, ClientePrincipal, ClientePrincipal],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
