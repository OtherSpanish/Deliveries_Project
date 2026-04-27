import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Servicio centralizado para todas las llamadas HTTP al backend Spring Boot.
 * URL base: http://localhost:8080
 */
@Injectable({
  providedIn: 'root',
})
export class ApiService {

  private readonly BASE = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // ── ADMIN ─────────────────────────────────────────────────────────────────

  adminLogin(usuarioAdmin: string, contraseniaAdmin: string, codigoAdminverificar: string): Observable<string> {
    const params = new HttpParams()
      .set('usuarioAdmin', usuarioAdmin)
      .set('contraseniaAdmin', contraseniaAdmin)
      .set('codigoAdminverificar', codigoAdminverificar);
    return this.http.post(`${this.BASE}/admin/login`, null, { params, responseType: 'text' });
  }

  adminLogout(): Observable<string> {
    return this.http.post(`${this.BASE}/admin/logout`, null, { responseType: 'text' });
  }

  adminMostrarConductores(): Observable<string> {
    return this.http.get(`${this.BASE}/admin/mostrarconductores`, { responseType: 'text' });
  }

  adminMostrarClientes(): Observable<string> {
    return this.http.get(`${this.BASE}/admin/mostrarcliente`, { responseType: 'text' });
  }

  adminMostrarPaquetes(): Observable<string> {
    return this.http.get(`${this.BASE}/admin/mostrarpaquete`, { responseType: 'text' });
  }

  adminMostrarManipuladores(): Observable<string> {
    return this.http.get(`${this.BASE}/admin/mostrarmanipulador`, { responseType: 'text' });
  }

  adminCrearConductor(usuario: string, contrasenia: string, tipoVehiculo: string): Observable<string> {
    const params = new HttpParams()
      .set('usuario', usuario).set('contrasenia', contrasenia).set('tipoVehiculo', tipoVehiculo);
    return this.http.post(`${this.BASE}/admin/crearconductor`, null, { params, responseType: 'text' });
  }

  adminCrearManipulador(usuario: string, contrasenia: string, tiempoDeTrabajo: number): Observable<string> {
    const params = new HttpParams()
      .set('usuario', usuario).set('contrasenia', contrasenia).set('tiempoDeTrabajo', tiempoDeTrabajo);
    return this.http.post(`${this.BASE}/admin/crearmanipulador`, null, { params, responseType: 'text' });
  }

  adminActualizarConductor(id: number, usuario: string, contrasenia: string, tipoDeVehiculo: string): Observable<string> {
    const params = new HttpParams()
      .set('id', id).set('usuario', usuario).set('contrasenia', contrasenia).set('tipoDeVehiculo', tipoDeVehiculo);
    return this.http.put(`${this.BASE}/admin/actualizarconductor`, null, { params, responseType: 'text' });
  }

  adminActualizarManipulador(id: number, usuario: string, contrasenia: string, tiempoDeTrabajo: number): Observable<string> {
    const params = new HttpParams()
      .set('id', id).set('usuario', usuario).set('contrasenia', contrasenia).set('tiempoDeTrabajo', tiempoDeTrabajo);
    return this.http.put(`${this.BASE}/admin/actualizarmanipulador`, null, { params, responseType: 'text' });
  }

  adminActualizarCliente(id: number, usuario: string, contrasenia: string, cedula: string, tipoCliente: string): Observable<string> {
    const params = new HttpParams()
      .set('id', id).set('usuario', usuario).set('contrasenia', contrasenia)
      .set('cedula', cedula).set('tipoCliente', tipoCliente);
    return this.http.put(`${this.BASE}/admin/actualizarcliente`, null, { params, responseType: 'text' });
  }

  adminActualizarPaquete(id: number, tipoPaquete: string, contenido: string, direccionDeEnvio: string): Observable<string> {
    const params = new HttpParams()
      .set('id', id).set('tipoPaquete', tipoPaquete)
      .set('contenido', contenido).set('direccionDeEnvio', direccionDeEnvio);
    return this.http.put(`${this.BASE}/admin/actualizarpaquete`, null, { params, responseType: 'text' });
  }

  adminBorrarConductor(id: number): Observable<string> {
    return this.http.delete(`${this.BASE}/admin/borrarconductor`,
      { params: new HttpParams().set('id', id), responseType: 'text' });
  }

  adminBorrarCliente(id: number): Observable<string> {
    return this.http.delete(`${this.BASE}/admin/borrarcliente`,
      { params: new HttpParams().set('id', id), responseType: 'text' });
  }

  adminBorrarPaquete(id: number): Observable<string> {
    return this.http.delete(`${this.BASE}/admin/borrarpaquete`,
      { params: new HttpParams().set('id', id), responseType: 'text' });
  }

  adminBorrarManipulador(id: number): Observable<string> {
    return this.http.delete(`${this.BASE}/admin/borrarmanipulador`,
      { params: new HttpParams().set('id', id), responseType: 'text' });
  }

  // ── CLIENTE ───────────────────────────────────────────────────────────────

  clienteLogin(usuario: string, contrasenia: string): Observable<string> {
    const params = new HttpParams().set('usuario', usuario).set('contrasenia', contrasenia);
    return this.http.post(`${this.BASE}/cliente/login`, null, { params, responseType: 'text' });
  }

  clienteLogout(): Observable<string> {
    return this.http.post(`${this.BASE}/cliente/logout`, null, { responseType: 'text' });
  }

  clienteRegistrar(usuario: string, contrasenia: string, cedula: string, tipoCliente: string): Observable<string> {
    const params = new HttpParams()
      .set('usuario', usuario).set('contrasenia', contrasenia)
      .set('cedula', cedula).set('tipoCliente', tipoCliente);
    return this.http.post(`${this.BASE}/cliente/register`, null, { params, responseType: 'text' });
  }

  // ── CONDUCTOR ─────────────────────────────────────────────────────────────

  conductorLogin(usuario: string, contrasenia: string): Observable<string> {
    const params = new HttpParams().set('usuario', usuario).set('contrasenia', contrasenia);
    return this.http.post(`${this.BASE}/conductor/login`, null, { params, responseType: 'text' });
  }

  conductorLogout(): Observable<string> {
    return this.http.post(`${this.BASE}/conductor/logout`, null, { responseType: 'text' });
  }

  conductorMostrarPedidos(): Observable<string> {
    return this.http.get(`${this.BASE}/conductor/mostrarpedidos`, { responseType: 'text' });
  }

  // ── MANIPULADOR ───────────────────────────────────────────────────────────

  manipuladorLogin(usuario: string, contrasenia: string): Observable<string> {
    const params = new HttpParams().set('usuario', usuario).set('contrasenia', contrasenia);
    return this.http.post(`${this.BASE}/manipuladorpaquete/loginmanipulador`, null, { params, responseType: 'text' });
  }

  manipuladorLogout(): Observable<string> {
    return this.http.post(`${this.BASE}/manipuladorpaquete/logoutmanipulador`, null, { responseType: 'text' });
  }

  manipuladorMostrarPaquetes(): Observable<string> {
    return this.http.get(`${this.BASE}/manipuladorpaquete/mostrarpaquete`, { responseType: 'text' });
  }

  // ── PAQUETE ───────────────────────────────────────────────────────────────

  paqueteCrear(tipoPaquete: string, contenido: string, direccionAEnviar: string): Observable<string> {
    const params = new HttpParams()
      .set('tipoPaquete', tipoPaquete)
      .set('contenido', contenido)
      .set('direccionAEnviar', direccionAEnviar);
    return this.http.post(`${this.BASE}/paquete/crear`, null, { params, responseType: 'text' });
  }
}
