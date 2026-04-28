package co.edu.unbosque.proyectomodulo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.dto.*;
import co.edu.unbosque.proyectomodulo.service.*;

/**
 * Controlador REST para operaciones administrativas. Permite gestionar
 * conductores, clientes, paquetes, manipuladores y admins.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class AdminController {

	/**
	 * Servicio de lógica de negocio para la gestión de administradores.
	 */
	@Autowired
	private AdminService aService;

	/**
	 * Servicio de lógica de negocio para la gestión de conductores.
	 */
	@Autowired
	private ConductorService cService;

	/**
	 * Servicio de lógica de negocio para la gestión de clientes.
	 */
	@Autowired
	private ClienteService clienteService;

	/**
	 * Servicio de lógica de negocio para la gestión de paquetes.
	 */
	@Autowired
	private PaqueteService pService;

	/**
	 * Servicio de lógica de negocio para la gestión de manipuladores de paquetes.
	 */
	@Autowired
	private ManipuladorPaquetesService mService;

	/**
	 * Retorna la lista de todos los conductores registrados en el sistema.
	 * <p>
	 * Endpoint: {@code GET /admin/mostrarconductores}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con la cadena de conductores y estado
	 *         {@code 200 OK} si hay un admin autenticado, o mensaje
	 *         {@code "Admin no ingresado"} y estado {@code 401 UNAUTHORIZED}
	 *         si no hay sesión de admin activa.
	 */
	@GetMapping("/mostrarconductores")
	public ResponseEntity<String> mostrarConductores() {
		String conductores = cService.getAll();
		if (conductores == null)
			return new ResponseEntity<>("Admin no ingresado", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(conductores, HttpStatus.OK);
	}

	/**
	 * Retorna la lista de todos los administradores registrados en el sistema.
	 * <p>
	 * Endpoint: {@code GET /admin/mostraradmin}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con la cadena de admins y estado
	 *         {@code 200 OK} si hay un admin autenticado, o mensaje
	 *         {@code "Admin no ingresado"} y estado {@code 401 UNAUTHORIZED}
	 *         si no hay sesión de admin activa.
	 */
	@GetMapping("/mostraradmin")
	public ResponseEntity<String> mostrarAdmins() {
		String admins = aService.getAll();
		if (admins == null)
			return new ResponseEntity<>("Admin no ingresado", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(admins, HttpStatus.OK);
	}

	/**
	 * Retorna la lista de todos los clientes registrados en el sistema.
	 * <p>
	 * Endpoint: {@code GET /admin/mostrarcliente}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con la cadena de clientes y estado
	 *         {@code 200 OK} si hay un admin autenticado, o mensaje
	 *         {@code "Se necesita ingresar un admin"} y estado
	 *         {@code 401 UNAUTHORIZED} si no hay sesión de admin activa.
	 */
	@GetMapping("/mostrarcliente")
	public ResponseEntity<String> mostrarClientes() {
		String clientes = clienteService.getAll();
		if (clientes == null)
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	/**
	 * Retorna la lista de todos los paquetes registrados en el sistema.
	 * <p>
	 * Endpoint: {@code GET /admin/mostrarpaquete}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con la cadena de paquetes y estado
	 *         {@code 200 OK} si hay un admin autenticado, o mensaje
	 *         {@code "Se necesita ingresar un admin"} y estado
	 *         {@code 401 UNAUTHORIZED} si no hay sesión de admin activa.
	 */
	@GetMapping("/mostrarpaquete")
	public ResponseEntity<String> mostrarPaquetes() {
		String paquetes = pService.getAll();
		if (paquetes == null)
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(paquetes, HttpStatus.OK);
	}

	/**
	 * Retorna la lista de todos los manipuladores de paquetes registrados en el sistema.
	 * <p>
	 * Endpoint: {@code GET /admin/mostrarmanipulador}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con la cadena de manipuladores y estado
	 *         {@code 200 OK} si hay un admin autenticado, o mensaje
	 *         {@code "Se necesita ingresar un admin"} y estado
	 *         {@code 401 UNAUTHORIZED} si no hay sesión de admin activa.
	 */
	@GetMapping("/mostrarmanipulador")
	public ResponseEntity<String> mostrarManipulador() {
		String manipuladores = mService.getAll();
		if (manipuladores == null)
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(manipuladores, HttpStatus.OK);
	}

	/**
	 * Autentica a un administrador con sus credenciales y código de verificación.
	 * <p>
	 * Endpoint: {@code POST /admin/login}
	 * </p>
	 *
	 * @param usuarioAdmin          nombre de usuario del administrador.
	 * @param contraseniaAdmin      contraseña del administrador.
	 * @param codigoAdminverificar  código de verificación adicional requerido para el login.
	 * @return {@link ResponseEntity} con mensaje {@code "Admin ingresado"} y estado
	 *         {@code 200 OK} si las credenciales son correctas, o mensaje
	 *         {@code "Credenciales invalidas"} y estado {@code 400 BAD_REQUEST}
	 *         en caso contrario.
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String usuarioAdmin, @RequestParam String contraseniaAdmin,
			@RequestParam String codigoAdminverificar) {
		int status = aService.loginadmin(usuarioAdmin, contraseniaAdmin, codigoAdminverificar);
		if (status == 0)
			return new ResponseEntity<>("Admin ingresado", HttpStatus.OK);
		return new ResponseEntity<>("Credenciales invalidas", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Cierra la sesión del administrador actualmente autenticado.
	 * <p>
	 * Endpoint: {@code POST /admin/logout}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con mensaje {@code "Sesion cerrada"} y estado
	 *         {@code 200 OK} si había una sesión activa, o mensaje
	 *         {@code "No hay sesion iniciada"} y estado {@code 400 BAD_REQUEST}
	 *         si no existe ninguna sesión de admin iniciada.
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (!aService.isLoggedadmin())
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		aService.logoutadmin();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

	/**
	 * Crea un nuevo conductor en el sistema. Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code POST /admin/crearconductor}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Conductor creado exitosamente.</li>
	 *   <li>{@code 1} – Tipo de vehículo inválido; debe ser {@code carro}, {@code moto} o {@code camion}.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>{@code 3} – El nombre de usuario ya está en uso.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param usuario      nombre de usuario del nuevo conductor.
	 * @param contrasenia  contraseña del nuevo conductor.
	 * @param tipoVehiculo tipo de vehículo asignado ({@code carro}, {@code moto} o {@code camion}).
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PostMapping("/crearconductor")
	public ResponseEntity<String> crearConductor(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam String tipoVehiculo) {
		int status = cService.create(new ConductorDTO(usuario, contrasenia, tipoVehiculo));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Conductor creado", HttpStatus.CREATED);
		}
		case 1: {
			return new ResponseEntity<>("Tipo de vehiculo invalido (carro, moto, camion)", HttpStatus.BAD_REQUEST);
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		case 3: {
			return new ResponseEntity<>("El nombre de usuario ya esta en uso", HttpStatus.BAD_REQUEST);
		}
		default: {
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	  }
	}

	/**
	 * Crea un nuevo manipulador de paquetes en el sistema. Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code POST /admin/crearmanipulador}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Manipulador creado exitosamente.</li>
	 *   <li>{@code 1} – Ingreso de datos inválido.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>{@code 3} – El nombre de usuario ya existe.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param usuario          nombre de usuario del nuevo manipulador.
	 * @param contrasenia      contraseña del nuevo manipulador.
	 * @param tiempoDeTrabajo  tiempo de trabajo del manipulador en horas.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PostMapping("/crearmanipulador")
	public ResponseEntity<String> crearManipulador(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam int tiempoDeTrabajo) {
		int status = mService.create(new ManipuladorPaqueteDTO(usuario, contrasenia, tiempoDeTrabajo));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Manipulador creado", HttpStatus.CREATED);			
		}
		case 1:{
			return new ResponseEntity<>("Ingreso invalido", HttpStatus.BAD_REQUEST);			
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);			
		}
		case 3: {
			return new ResponseEntity<>("Usuario Ya Existente", HttpStatus.BAD_REQUEST);			
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Actualiza los datos de un cliente existente identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code PUT /admin/actualizarcliente}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Cliente actualizado exitosamente.</li>
	 *   <li>{@code 1} – No se pudo actualizar el cliente.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>{@code 3} – El nombre de usuario ya existe.</li>
	 *   <li>{@code 4} – La cédula ya existe.</li>
	 *   <li>{@code 5} – El usuario y la cédula ya existen.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id          identificador único del cliente a actualizar.
	 * @param usuario     nuevo nombre de usuario del cliente.
	 * @param contrasenia nueva contraseña del cliente.
	 * @param cedula      nueva cédula del cliente.
	 * @param tipoCliente nuevo tipo de cliente ({@code NORMAL} o {@code PREMIUM}).
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PutMapping("/actualizarcliente")
	public ResponseEntity<String> actualizarCliente(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String cedula, @RequestParam TipoCliente tipoCliente) {
		int status = clienteService.updateById(id, new ClienteDTO(usuario, contrasenia, cedula, tipoCliente));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Cliente Actualizado", HttpStatus.OK);			
		}
		case 1: {
			return new ResponseEntity<>("No se pudo actualizar el cliente", HttpStatus.BAD_REQUEST);			
		}
		case 2: {			
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		case 3: {			
			return new ResponseEntity<>("El Usuario ya existe", HttpStatus.BAD_REQUEST);
		}
		case 4: {
			return new ResponseEntity<>("La cedula ya existe", HttpStatus.BAD_REQUEST);			
		}
		case 5: {
			return new ResponseEntity<>("El usuario y la cedula ya existen", HttpStatus.BAD_REQUEST);			
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Actualiza los datos de un paquete existente identificado por el ID del cliente.
	 * Requiere que haya un cliente con sesión activa. El tiempo de envío y el precio
	 * se calculan automáticamente según el tipo de paquete y el tipo de cliente.
	 * <p>
	 * Endpoint: {@code PUT /admin/actualizarpaquete}
	 * </p>
	 * <p>
	 * Lógica de tiempo de envío por tipo de paquete:
	 * <ul>
	 *   <li>{@code CARTA} – Entrega al día siguiente a las 08:00.</li>
	 *   <li>{@code ALIMENTICIO} – Entrega en 6 horas desde el momento actual.</li>
	 *   <li>{@code NO_ALIMENTICIO} – Entrega al día siguiente a las 08:00.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Descuentos aplicados según tipo de cliente:
	 * <ul>
	 *   <li>{@code NORMAL} – Sin descuento.</li>
	 *   <li>{@code CONCURRENTE} – 10% de descuento.</li>
	 *   <li>{@code PREMIUM} – 30% de descuento.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Paquete actualizado exitosamente.</li>
	 *   <li>{@code 1} – No se pudo actualizar el paquete.</li>
	 *   <li>{@code 2} – Se requiere sesión de admin y de cliente.</li>
	 *   <li>{@code 3} – Se requiere sesión de cliente.</li>
	 *   <li>{@code 4} – Se requiere sesión de admin.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id                    identificador del cliente asociado al paquete.
	 * @param tipoPaquete           tipo del paquete ({@code CARTA}, {@code ALIMENTICIO} o {@code NO_ALIMENTICIO}).
	 * @param contenido             descripción del contenido del paquete.
	 * @param direccionDeEnvio      dirección de destino del paquete.
	 * @param destinatario          nombre del destinatario del paquete.
	 * @param pesoKg                peso del paquete en kilogramos.
	 * @param esFragil              indica si el paquete es frágil.
	 * @param requiereRefrigeracion indica si el paquete requiere refrigeración.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PutMapping("/actualizarpaquete")
	public ResponseEntity<String> actualizarPaquete(@RequestParam Long id, @RequestParam TipoPaquete tipoPaquete,
			@RequestParam String contenido, @RequestParam String direccionDeEnvio, @RequestParam String destinatario, @RequestParam float pesoKg, @RequestParam boolean esFragil, @RequestParam boolean requiereRefrigeracion) {
		if (!clienteService.isLogged()) {
			return new ResponseEntity<>("Se necesita un cliente logueado", HttpStatus.BAD_REQUEST);
		}
		// Bug fix: "mm" para minutos, no "HH"
		LocalDateTime ahora = LocalDateTime.now();
		int anio = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("uuuu")));
		int mes = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("MM")));
		int dia = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("dd")));
		int hora = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("HH")));
		int minuto = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("mm")));
		int precio;
		switch (tipoPaquete) {
		case CARTA: {
			dia += 1;
			hora = 8;
			minuto = 0;
			precio = 10000;
			break;			
		}
		case ALIMENTICIO: {
			hora += 6;
			minuto = 0;
			precio = 20000;
			break;			
		}
		case NO_ALIMENTICIO: {
			dia += 1;
			hora = 8;
			minuto = 0;
			precio = 30000;
			break;			
		}
		default:
			return new ResponseEntity<>("Tipo de paquete invalido", HttpStatus.BAD_REQUEST);
		}
		LocalDateTime tiempoDeEnvio = LocalDateTime.of(anio, mes, dia, hora, minuto);
		String estadoCliente = clienteService.getClienteStatus(id).toUpperCase();
		String nameCliente = clienteService.getClienteNombre(id);
		String precioEnvio;
		switch (estadoCliente) {
		case "NORMAL": {
			precioEnvio = String.valueOf(precio);
			break;			
		}
		case "CONCURRENTE": {
			precioEnvio = String.valueOf(precio - (precio * 0.10));
			break;			
		}
		case "PREMIUM": {
			precioEnvio = String.valueOf(precio - (precio * 0.30));
			break;			
		}
		default:
			return new ResponseEntity<>("Tipo de cliente no valido", HttpStatus.BAD_REQUEST);
		}
		int status = pService.updateById(id, new PaqueteDTO(tipoPaquete, contenido, direccionDeEnvio, tiempoDeEnvio,
				precioEnvio, EstadoPaquete.DESPACHADO, nameCliente, destinatario, pesoKg, esFragil, requiereRefrigeracion));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Paquete actualizado", HttpStatus.OK);			
		}
		case 1: {
			return new ResponseEntity<>("No se pudo actualizar el paquete", HttpStatus.BAD_REQUEST);			
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin y un cliente", HttpStatus.UNAUTHORIZED);			
		}
		case 3: {
			return new ResponseEntity<>("Debe iniciar sesion un cliente", HttpStatus.UNAUTHORIZED);			
		}
		case 4: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);			
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Actualiza los datos de un conductor existente identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code PUT /admin/actualizarconductor}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Conductor actualizado exitosamente.</li>
	 *   <li>{@code 1} – No se pudo actualizar el conductor.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>{@code 3} – El nombre de usuario ya existe.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id             identificador único del conductor a actualizar.
	 * @param usuario        nuevo nombre de usuario del conductor.
	 * @param contrasenia    nueva contraseña del conductor.
	 * @param tipoDeVehiculo nuevo tipo de vehículo asignado al conductor.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PutMapping("/actualizarconductor")
	public ResponseEntity<String> actualizarConductor(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String tipoDeVehiculo) {
		int status = cService.updateById(id, new ConductorDTO(usuario, contrasenia, tipoDeVehiculo));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Conductor Actualizado", HttpStatus.OK);			
		}
		case 1: {			
			return new ResponseEntity<>("No se pudo actualizar el conductor", HttpStatus.BAD_REQUEST);
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);			
		}
		case 3: {
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Actualiza los datos de un manipulador de paquetes existente identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code PUT /admin/actualizarmanipulador}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Manipulador actualizado exitosamente.</li>
	 *   <li>{@code 1} – No se pudo actualizar el manipulador.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>{@code 3} – El nombre de usuario ya existe.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id              identificador único del manipulador a actualizar.
	 * @param usuario         nuevo nombre de usuario del manipulador.
	 * @param contrasenia     nueva contraseña del manipulador.
	 * @param tiempoDeTrabajo nuevo tiempo de trabajo del manipulador en horas.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PutMapping("/actualizarmanipulador")
	public ResponseEntity<String> actualizarManipulador(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam int tiempoDeTrabajo) {
		int status = mService.updateById(id, new ManipuladorPaqueteDTO(usuario, contrasenia, tiempoDeTrabajo));
		switch (status) {
		case 0: {			
			return new ResponseEntity<>("Manipulador Actualizado", HttpStatus.OK);
		}
		case 1: {			
			return new ResponseEntity<>("No se pudo actualizar el manipulador", HttpStatus.BAD_REQUEST);
		}
		case 2: {			
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		case 3: {			
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.CONFLICT);
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Elimina un conductor del sistema identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code DELETE /admin/borrarconductor}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Conductor eliminado exitosamente.</li>
	 *   <li>{@code 1} – Conductor no encontrado.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id identificador único del conductor a eliminar.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	// Bug fix: deletes devuelven 200 OK, no 201 Created
	@DeleteMapping("/borrarconductor")
	public ResponseEntity<String> borrarConductor(@RequestParam Long id) {
		int status = cService.deleteById(id);
		switch (status) {
		case 0:
			return new ResponseEntity<>("Conductor borrado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("Conductor no encontrado", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Elimina un cliente del sistema identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code DELETE /admin/borrarcliente}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Cliente eliminado exitosamente.</li>
	 *   <li>{@code 1} – Cliente no encontrado.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id identificador único del cliente a eliminar.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@DeleteMapping("/borrarcliente")
	public ResponseEntity<String> borrarCliente(@RequestParam Long id) {
		int status = clienteService.deleteById(id);
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Cliente borrado", HttpStatus.OK);			
		}
		case 1: {
			return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);			
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);			
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Elimina un paquete del sistema identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code DELETE /admin/borrarpaquete}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Paquete eliminado exitosamente.</li>
	 *   <li>{@code 1} – Paquete no encontrado.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id identificador único del paquete a eliminar.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@DeleteMapping("/borrarpaquete")
	public ResponseEntity<String> borrarPaquete(@RequestParam Long id) {
		int status = pService.deleteById(id);
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Paquete eliminado", HttpStatus.OK);			
		}
		case 1: {
			return new ResponseEntity<>("Paquete no encontrado", HttpStatus.NOT_FOUND);			
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);			
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Elimina un manipulador de paquetes del sistema identificado por su ID.
	 * Requiere sesión de admin activa.
	 * <p>
	 * Endpoint: {@code DELETE /admin/borrarmanipulador}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Manipulador eliminado exitosamente.</li>
	 *   <li>{@code 1} – Manipulador no encontrado.</li>
	 *   <li>{@code 2} – No hay sesión de admin activa.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param id identificador único del manipulador a eliminar.
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@DeleteMapping("/borrarmanipulador")
	public ResponseEntity<String> borrarManipulador(@RequestParam Long id) {
		int status = mService.deleteById(id);
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Manipulador eliminado", HttpStatus.OK);			
		}
		case 1: {
			return new ResponseEntity<>("Manipulador no encontrado", HttpStatus.NOT_FOUND);			
		}
		case 2: {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);			
		}
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}
}