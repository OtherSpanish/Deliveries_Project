package co.edu.unbosque.proyectomodulo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import co.edu.unbosque.proyectomodulo.service.ConductorService;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;
import co.edu.unbosque.proyectomodulo.dto.ConductorDTO;
import co.edu.unbosque.proyectomodulo.dto.EstadoPaquete;
import co.edu.unbosque.proyectomodulo.dto.ManipuladorPaqueteDTO;
import co.edu.unbosque.proyectomodulo.dto.PaqueteDTO;
import co.edu.unbosque.proyectomodulo.dto.TipoCliente;
import co.edu.unbosque.proyectomodulo.dto.TipoPaquete;

/**
 * Controlador REST para la gestión de operaciones administrativas. Proporciona
 * endpoints para administrar conductores, clientes, paquetes, manipuladores y
 * administradores del sistema.
 *
 * <p>
 * Todos los endpoints están bajo el prefijo {@code /admin} y permiten
 * solicitudes desde cualquier origen.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class AdminController {

	/** Servicio para operaciones relacionadas con administradores. */
	@Autowired
	private AdminService aService;

	/** Servicio para operaciones relacionadas con conductores. */
	@Autowired
	private ConductorService cService;

	/** Servicio para operaciones relacionadas con clientes. */
	@Autowired
	private ClienteService clienteService;

	/** Servicio para operaciones relacionadas con paquetes. */
	@Autowired
	private PaqueteService pService;

	/** Servicio para operaciones relacionadas con manipuladores de paquetes. */
	@Autowired
	private ManipuladorPaquetesService mService;

	/**
	 * Obtiene la lista de todos los conductores registrados.
	 *
	 * @return {@code 202 Accepted} con la lista de conductores,
	 *         {@code 400 Bad Request} si el admin no está ingresado o la lista está
	 *         vacía.
	 */
	@GetMapping("/mostrarConductores")
	public ResponseEntity<String> mostrarConductores() {
		String conductores = cService.getAll();
		if (conductores == null) {
			return new ResponseEntity<>("Admin no ingresado", HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("" + conductores, HttpStatus.ACCEPTED);
		}
	}

	/**
	 * Obtiene la lista de todos los administradores registrados.
	 *
	 * @return {@code 202 Accepted} con la lista de administradores,
	 *         {@code 401 Unauthorized} si el admin no está ingresado,
	 *         {@code 400 Bad Request} si la lista está vacía.
	 */
	@GetMapping("/mostrarAdmin")
	public ResponseEntity<String> mostrarAdmins() {
		String admins = aService.getAll();
		if (admins == null) {
			return new ResponseEntity<>("Admin no ingresado", HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("" + admins, HttpStatus.ACCEPTED);
		}
	}

	/**
	 * Obtiene la lista de todos los clientes registrados.
	 *
	 * @return {@code 202 Accepted} con la lista de clientes,
	 *         {@code 401 Unauthorized} si no hay admin logueado,
	 *         {@code 400 Bad Request} si la lista está vacía.
	 */
	@GetMapping("/mostrarCliente")
	public ResponseEntity<String> mostrarClientes() {
		String clientes = clienteService.getAll();
		if (clientes == null) {
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("" + clientes, HttpStatus.ACCEPTED);
		}
	}

	/**
	 * Obtiene la lista de todos los paquetes registrados.
	 *
	 * @return {@code 202 Accepted} con la lista de paquetes,
	 *         {@code 401 Unauthorized} si no hay admin logueado,
	 *         {@code 400 Bad Request} si la lista está vacía.
	 */
	@GetMapping("/mostrarPaquete")
	public ResponseEntity<String> mostrarPaquetes() {
		String paquetes = pService.getAll();
		if (paquetes == null) {
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("" + paquetes, HttpStatus.ACCEPTED);
		}
	}

	/**
	 * Obtiene la lista de todos los manipuladores de paquetes registrados.
	 *
	 * @return {@code 202 Accepted} con la lista de manipuladores,
	 *         {@code 401 Unauthorized} si no hay admin logueado,
	 *         {@code 400 Bad Request} si la lista está vacía.
	 */
	@GetMapping("/mostrarManipulador")
	public ResponseEntity<String> mostrarManipulador() {
		String manipuladores = mService.getAll();
		if (manipuladores == null) {
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("" + manipuladores, HttpStatus.ACCEPTED);
		}
	}

	/**
	 * Inicia sesión como administrador en el sistema.
	 *
	 * @param usuarioAdmin         nombre de usuario del administrador.
	 * @param contraseniaAdmin     contraseña del administrador.
	 * @param codigoAdminverificar código de verificación del administrador.
	 * @return {@code 201 Created} si el login es exitoso, {@code 400 Bad Request}
	 *         si las credenciales son inválidas.
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String usuarioAdmin, @RequestParam String contraseniaAdmin,
			@RequestParam String codigoAdminverificar) {
		int status = aService.loginadmin(usuarioAdmin, contraseniaAdmin, codigoAdminverificar);
		if (status == 0) {
			return new ResponseEntity<>("Admin ingresado", HttpStatus.OK);
		} else if (status == 1) {
			return new ResponseEntity<>("Credenciales invalido", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Cierra la sesión del administrador actualmente logueado.
	 *
	 * @return {@code 200 OK} confirmando el cierre de sesión,
	 *         {@code 400 Bad Request} si no hay sesión activa.
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (aService.isLoggedadmin() == false) {
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		}
		aService.logoutadmin();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

	/**
	 * Crea un nuevo conductor en el sistema.
	 *
	 * @param usuario      nombre de usuario del conductor.
	 * @param contrasenia  contraseña del conductor.
	 * @param tipoVehiculo tipo de vehículo del conductor (carro, moto, camion).
	 * @return {@code 201 Created} si se crea exitosamente, {@code 400 Bad Request}
	 *         si el tipo de vehículo es inválido o el usuario ya existe,
	 *         {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@PostMapping("/crearConductor")
	public ResponseEntity<String> crearConductor(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam String tipoVehiculo) {
		ConductorDTO nuevo = new ConductorDTO(usuario, contrasenia, tipoVehiculo);
		int status = cService.create(nuevo);
		if (status == 0) {
			return new ResponseEntity<>("Conductor creado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Tipo de vehiculo invalido (carro, moto, camion)", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		} else if (status == 3) {
			return new ResponseEntity<>("El nombre de usuario ya esta en uso", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Crea un nuevo manipulador de paquetes en el sistema.
	 *
	 * @param usuario         nombre de usuario del manipulador.
	 * @param contrasenia     contraseña del manipulador.
	 * @param tiempoDeTrabajo tiempo de trabajo en horas del manipulador.
	 * @return {@code 201 Created} si se crea exitosamente, {@code 400 Bad Request}
	 *         si el ingreso es inválido, {@code 401 Unauthorized} si no hay admin
	 *         logueado.
	 */
	@PostMapping("/crearManipulador")
	public ResponseEntity<String> crearManipulador(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam int tiempoDeTrabajo) {
		ManipuladorPaqueteDTO nuevo = new ManipuladorPaqueteDTO(usuario, contrasenia, tiempoDeTrabajo);
		int status = mService.create(nuevo);
		if (status == 0) {
			return new ResponseEntity<>("Manipulador creado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ingreso invalido", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		} else if (status == 3) {
			return new ResponseEntity<>("Usuario Ya Existente", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Actualiza los datos de un cliente existente.
	 *
	 * @param id          identificador del cliente a actualizar.
	 * @param usuario     nuevo nombre de usuario.
	 * @param contrasenia nueva contraseña.
	 * @param cedula      nueva cédula del cliente.
	 * @param tipoCliente nuevo tipo de cliente (normal, concurrente, premium).
	 * @return {@code 202 Accepted} si se actualiza exitosamente,
	 *         {@code 400 Bad Request} si el tipo de cliente es inválido, el usuario
	 *         o cédula ya existen, o no se pudo actualizar,
	 *         {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@PutMapping("/actualizarCliente")
	public ResponseEntity<String> actualizarCliente(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String cedula, @RequestParam TipoCliente tipoCliente) {
		ClienteDTO actualizar = new ClienteDTO(usuario, contrasenia, cedula, tipoCliente);
		switch (tipoCliente) {
		case NORMAL: {
			tipoCliente = TipoCliente.NORMAL;
			break;
		}
		case PREMIUM: {
			tipoCliente = TipoCliente.PREMIUM;
			break;
		}
		case CONCURRENTE: {
			tipoCliente = TipoCliente.CONCURRENTE;
			break;
		}
		default: {
			return new ResponseEntity<>("Tipo de cliente invalido (normal, premium, concurrente)",
					HttpStatus.BAD_REQUEST);
		}
		}
		int status = clienteService.updateById(id, actualizar);
		if (status == 0) {
			return new ResponseEntity<>("Cliente Actualizado", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("No se pudo actualizar el cliente", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		} else if (status == 3) {
			return new ResponseEntity<>("El Usuario ya existe", HttpStatus.BAD_REQUEST);
		} else if (status == 4) {
			return new ResponseEntity<>("La cedula ya existe", HttpStatus.BAD_REQUEST);
		} else if (status == 5) {
			return new ResponseEntity<>("El usuario y la cedula ya existen", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Actualiza los datos de un paquete existente. Calcula automáticamente el
	 * tiempo de entrega y precio según el tipo de paquete y el tipo de cliente
	 * actualmente logueado.
	 *
	 * @param id               identificador del paquete a actualizar.
	 * @param tipoDePaquete    nuevo tipo de paquete (carta, alimenticios, no
	 *                         alimenticios).
	 * @param contenido        nuevo contenido del paquete.
	 * @param direccionDeEnvio nueva dirección de envío.
	 * @return {@code 202 Accepted} si se actualiza exitosamente,
	 *         {@code 400 Bad Request} si el tipo de paquete es inválido, no hay
	 *         cliente logueado o no se pudo actualizar, {@code 401 Unauthorized} si
	 *         no hay admin o cliente logueado.
	 */
	@PutMapping("/actualizarPaquete")
	public ResponseEntity<String> actualizarPaquete(@RequestParam Long id, @RequestParam TipoPaquete tipoPaquete,
			@RequestParam String contenido, @RequestParam String direccionDeEnvio) {
		LocalDateTime tiempoDeEnvio2 = LocalDateTime.now();
		DateTimeFormatter formatoDias = DateTimeFormatter.ofPattern("dd");
		DateTimeFormatter formatoMeses = DateTimeFormatter.ofPattern("MM");
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH");
		DateTimeFormatter formatoMinuto = DateTimeFormatter.ofPattern("HH");
		DateTimeFormatter formatoAnio = DateTimeFormatter.ofPattern("uuuu");
		String formateadoHora = tiempoDeEnvio2.format(formatoHora);
		String formateadoDias = tiempoDeEnvio2.format(formatoDias);
		String formateadoMeses = tiempoDeEnvio2.format(formatoMeses);
		String formateadoAnio = tiempoDeEnvio2.format(formatoAnio);
		String formateadoMinuto = tiempoDeEnvio2.format(formatoMinuto);
		int anio = Integer.parseInt(formateadoAnio);
		int hora = Integer.parseInt(formateadoHora);
		int dia = Integer.parseInt(formateadoDias);
		int mes = Integer.parseInt(formateadoMeses);
		int minuto = Integer.parseInt(formateadoMinuto);
		String precioEnvio = "";
		int precio;
		if (!clienteService.isLogged()) {
			return new ResponseEntity<>("Se necesita un cliente logueado", HttpStatus.BAD_REQUEST);
		} else {
			switch (tipoPaquete.toString().toLowerCase()) {
			case "carta": {
				int dias = dia + 1;
				hora = 8;
				minuto = 00;
				dia = dias;
				precio = 10000;
				break;
			}
			case "alimenticios": {
				int entrega = 6 + hora;
				hora = entrega;
				minuto = 00;
				precio = 20000;
				break;
			}
			case "no alimenticios": {
				int dias = dia + 1;
				hora = 8;
				minuto = 00;
				dia = dias;
				precio = 30000;
				break;
			}
			default:
				return new ResponseEntity<>("Ingrese un tipo de paquete valido (carta, alimenticios, no alimenticios)",
						HttpStatus.BAD_REQUEST);
			}
			LocalDateTime tiempoDeEnvio = LocalDateTime.of(anio, mes, dia, hora, minuto);
			EstadoPaquete estadoPaquete = EstadoPaquete.DESPACHADO;
			String estadoCliente = clienteService.getClienteStatus(id).toLowerCase();
			String nameCliente = clienteService.getClienteNombre(id);
			switch (estadoCliente) {
			case "normal": {
				precioEnvio = "" + precio;
				break;
			}
			case "concurrente": {
				precioEnvio = "" + (precio - (precio * 0.10));
				break;
			}
			case "premium": {
				precioEnvio = "" + (precio - (precio * 0.30));
				break;
			}
			default: {
				return new ResponseEntity<>("Tipo de paquete no válido", HttpStatus.BAD_REQUEST);
			}
			}
			PaqueteDTO actualizar = new PaqueteDTO(tipoPaquete, contenido, direccionDeEnvio, tiempoDeEnvio, precioEnvio, estadoPaquete, nameCliente);

			int status = pService.updateById(id, actualizar);
			if (status == 0) {
				return new ResponseEntity<>("Paquete actualizado", HttpStatus.ACCEPTED);
			} else if (status == 1) {
				return new ResponseEntity<>(
						"No se pudo actualizar el paquete, verifique la direccion (mayor de 5 caracteres) y el tipo de paquete (carta, no comestibles, comestibles)",
						HttpStatus.BAD_REQUEST);
			} else if (status == 2) {
				return new ResponseEntity<>("Debe iniciar sesion un admin y un cliente", HttpStatus.UNAUTHORIZED);
			} else if (status == 3) {
				return new ResponseEntity<>("Debe iniciar sesion un cliente", HttpStatus.UNAUTHORIZED);
			} else if (status == 4) {
				return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Actualiza los datos de un conductor existente.
	 *
	 * @param id             identificador del conductor a actualizar.
	 * @param usuario        nuevo nombre de usuario.
	 * @param contrasenia    nueva contraseña.
	 * @param tipoDeVehiculo nuevo tipo de vehículo.
	 * @return {@code 202 Accepted} si se actualiza exitosamente,
	 *         {@code 400 Bad Request} si no se pudo actualizar o el usuario ya
	 *         existe, {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@PutMapping("/actualizarConductor")
	public ResponseEntity<String> actualizarConductor(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String tipoDeVehiculo) {
		ConductorDTO actualizar = new ConductorDTO(usuario, contrasenia, tipoDeVehiculo);
		int status = cService.updateById(id, actualizar);
		if (status == 0) {
			return new ResponseEntity<>("Conductor Actualizado", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("No se pudo actualizar el conductor", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		} else if (status == 3) {
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Actualiza los datos de un manipulador de paquetes existente.
	 *
	 * @param id              identificador del manipulador a actualizar.
	 * @param usuario         nuevo nombre de usuario.
	 * @param contrasenia     nueva contraseña.
	 * @param tiempoDeTrabajo nuevo tiempo de trabajo en horas.
	 * @return {@code 202 Accepted} si se actualiza exitosamente,
	 *         {@code 400 Bad Request} si no se pudo actualizar o el usuario ya
	 *         existe, {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@PutMapping("/actualizarManipulador")
	public ResponseEntity<String> actualizarManipulador(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam int tiempoDeTrabajo) {
		ManipuladorPaqueteDTO actualizar = new ManipuladorPaqueteDTO(usuario, contrasenia, tiempoDeTrabajo);
		int status = mService.updateById(id, actualizar);
		if (status == 0) {
			return new ResponseEntity<>("Manipulador Actualizado", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("No se pudo actualizar el manipulador", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		} else if (status == 3) {
			return new ResponseEntity<>("El usuario ya existe.", HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Elimina un conductor del sistema por su ID.
	 *
	 * @param id identificador del conductor a eliminar.
	 * @return {@code 201 Created} si se elimina exitosamente,
	 *         {@code 400 Bad Request} si el borrado es inválido,
	 *         {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@DeleteMapping("/borrarConductor")
	public ResponseEntity<String> borrarConductor(@RequestParam Long id) {
		int status = cService.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Conductor borrado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Borrado invalido", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Elimina un cliente del sistema por su ID.
	 *
	 * @param id identificador del cliente a eliminar.
	 * @return {@code 201 Created} si se elimina exitosamente,
	 *         {@code 400 Bad Request} si el borrado es inválido,
	 *         {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@DeleteMapping("/borrarCliente")
	public ResponseEntity<String> borrarCliente(@RequestParam Long id) {
		int status = clienteService.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Cliente borrado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Borrado invalido", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Elimina un paquete del sistema por su ID.
	 *
	 * @param id identificador del paquete a eliminar.
	 * @return {@code 201 Created} si se elimina exitosamente,
	 *         {@code 400 Bad Request} si el borrado es inválido,
	 *         {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@DeleteMapping("/borrarPaquete")
	public ResponseEntity<String> borrarPaquete(@RequestParam Long id) {
		int status = pService.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Paquete eliminado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Borrado invalido", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Elimina un manipulador de paquetes del sistema por su ID.
	 *
	 * @param id identificador del manipulador a eliminar.
	 * @return {@code 201 Created} si se elimina exitosamente,
	 *         {@code 400 Bad Request} si el borrado es inválido,
	 *         {@code 401 Unauthorized} si no hay admin logueado.
	 */
	@DeleteMapping("/borrarManipulador")
	public ResponseEntity<String> borrarManipulador(@RequestParam Long id) {
		int status = mService.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Manipulador eliminado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Borrado invalido", HttpStatus.BAD_REQUEST);
		} else if (status == 2) {
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}
	
	
}