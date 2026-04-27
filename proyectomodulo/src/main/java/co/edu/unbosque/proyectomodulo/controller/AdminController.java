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

	@Autowired
	private AdminService aService;
	@Autowired
	private ConductorService cService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private PaqueteService pService;
	@Autowired
	private ManipuladorPaquetesService mService;

	@GetMapping("/mostrarconductores")
	public ResponseEntity<String> mostrarConductores() {
		String conductores = cService.getAll();
		if (conductores == null)
			return new ResponseEntity<>("Admin no ingresado", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(conductores, HttpStatus.OK);
	}

	@GetMapping("/mostraradmin")
	public ResponseEntity<String> mostrarAdmins() {
		String admins = aService.getAll();
		if (admins == null)
			return new ResponseEntity<>("Admin no ingresado", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(admins, HttpStatus.OK);
	}

	@GetMapping("/mostrarcliente")
	public ResponseEntity<String> mostrarClientes() {
		String clientes = clienteService.getAll();
		if (clientes == null)
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	@GetMapping("/mostrarpaquete")
	public ResponseEntity<String> mostrarPaquetes() {
		String paquetes = pService.getAll();
		if (paquetes == null)
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(paquetes, HttpStatus.OK);
	}

	@GetMapping("/mostrarmanipulador")
	public ResponseEntity<String> mostrarManipulador() {
		String manipuladores = mService.getAll();
		if (manipuladores == null)
			return new ResponseEntity<>("Se necesita ingresar un admin", HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(manipuladores, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String usuarioAdmin, @RequestParam String contraseniaAdmin,
			@RequestParam String codigoAdminverificar) {
		int status = aService.loginadmin(usuarioAdmin, contraseniaAdmin, codigoAdminverificar);
		if (status == 0)
			return new ResponseEntity<>("Admin ingresado", HttpStatus.OK);
		return new ResponseEntity<>("Credenciales invalidas", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (!aService.isLoggedadmin())
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		aService.logoutadmin();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

	@PostMapping("/crearconductor")
	public ResponseEntity<String> crearConductor(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam String tipoVehiculo) {
		int status = cService.create(new ConductorDTO(usuario, contrasenia, tipoVehiculo));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Conductor creado", HttpStatus.CREATED);
		}
		case 1: {

		}
			return new ResponseEntity<>("Tipo de vehiculo invalido (carro, moto, camion)", HttpStatus.BAD_REQUEST);
		case 2: {

		}
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		case 3: {
			return new ResponseEntity<>("El nombre de usuario ya esta en uso", HttpStatus.BAD_REQUEST);
		}
		default: {
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
		}
	}

	@PostMapping("/crearmanipulador")
	public ResponseEntity<String> crearManipulador(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam int tiempoDeTrabajo) {
		int status = mService.create(new ManipuladorPaqueteDTO(usuario, contrasenia, tiempoDeTrabajo));
		switch (status) {
		case 0:
			return new ResponseEntity<>("Manipulador creado", HttpStatus.CREATED);
		case 1:
			return new ResponseEntity<>("Ingreso invalido", HttpStatus.BAD_REQUEST);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		case 3:
			return new ResponseEntity<>("Usuario Ya Existente", HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/actualizarcliente")
	public ResponseEntity<String> actualizarCliente(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String cedula, @RequestParam TipoCliente tipoCliente) {
		int status = clienteService.updateById(id, new ClienteDTO(usuario, contrasenia, cedula, tipoCliente));
		switch (status) {
		case 0:
			return new ResponseEntity<>("Cliente Actualizado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("No se pudo actualizar el cliente", HttpStatus.BAD_REQUEST);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		case 3:
			return new ResponseEntity<>("El Usuario ya existe", HttpStatus.BAD_REQUEST);
		case 4:
			return new ResponseEntity<>("La cedula ya existe", HttpStatus.BAD_REQUEST);
		case 5:
			return new ResponseEntity<>("El usuario y la cedula ya existen", HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/actualizarpaquete")
	public ResponseEntity<String> actualizarPaquete(@RequestParam Long id, @RequestParam TipoPaquete tipoPaquete,
			@RequestParam String contenido, @RequestParam String direccionDeEnvio) {
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
		case CARTA:
			dia += 1;
			hora = 8;
			minuto = 0;
			precio = 10000;
			break;
		case ALIMENTICIO:
			hora += 6;
			minuto = 0;
			precio = 20000;
			break;
		case NO_ALIMENTICIO:
			dia += 1;
			hora = 8;
			minuto = 0;
			precio = 30000;
			break;
		default:
			return new ResponseEntity<>("Tipo de paquete invalido", HttpStatus.BAD_REQUEST);
		}
		LocalDateTime tiempoDeEnvio = LocalDateTime.of(anio, mes, dia, hora, minuto);
		String estadoCliente = clienteService.getClienteStatus(id).toUpperCase();
		String nameCliente = clienteService.getClienteNombre(id);
		String precioEnvio;
		switch (estadoCliente) {
		case "NORMAL":
			precioEnvio = String.valueOf(precio);
			break;
		case "CONCURRENTE":
			precioEnvio = String.valueOf(precio - (precio * 0.10));
			break;
		case "PREMIUM":
			precioEnvio = String.valueOf(precio - (precio * 0.30));
			break;
		default:
			return new ResponseEntity<>("Tipo de cliente no valido", HttpStatus.BAD_REQUEST);
		}
		int status = pService.updateById(id, new PaqueteDTO(tipoPaquete, contenido, direccionDeEnvio, tiempoDeEnvio,
				precioEnvio, EstadoPaquete.DESPACHADO, nameCliente));
		switch (status) {
		case 0:
			return new ResponseEntity<>("Paquete actualizado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("No se pudo actualizar el paquete", HttpStatus.BAD_REQUEST);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin y un cliente", HttpStatus.UNAUTHORIZED);
		case 3:
			return new ResponseEntity<>("Debe iniciar sesion un cliente", HttpStatus.UNAUTHORIZED);
		case 4:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/actualizarconductor")
	public ResponseEntity<String> actualizarConductor(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam String tipoDeVehiculo) {
		int status = cService.updateById(id, new ConductorDTO(usuario, contrasenia, tipoDeVehiculo));
		switch (status) {
		case 0:
			return new ResponseEntity<>("Conductor Actualizado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("No se pudo actualizar el conductor", HttpStatus.BAD_REQUEST);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		case 3:
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/actualizarmanipulador")
	public ResponseEntity<String> actualizarManipulador(@RequestParam Long id, @RequestParam String usuario,
			@RequestParam String contrasenia, @RequestParam int tiempoDeTrabajo) {
		int status = mService.updateById(id, new ManipuladorPaqueteDTO(usuario, contrasenia, tiempoDeTrabajo));
		switch (status) {
		case 0:
			return new ResponseEntity<>("Manipulador Actualizado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("No se pudo actualizar el manipulador", HttpStatus.BAD_REQUEST);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		case 3:
			return new ResponseEntity<>("El usuario ya existe", HttpStatus.CONFLICT);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

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

	@DeleteMapping("/borrarcliente")
	public ResponseEntity<String> borrarCliente(@RequestParam Long id) {
		int status = clienteService.deleteById(id);
		switch (status) {
		case 0:
			return new ResponseEntity<>("Cliente borrado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/borrarpaquete")
	public ResponseEntity<String> borrarPaquete(@RequestParam Long id) {
		int status = pService.deleteById(id);
		switch (status) {
		case 0:
			return new ResponseEntity<>("Paquete eliminado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("Paquete no encontrado", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/borrarmanipulador")
	public ResponseEntity<String> borrarManipulador(@RequestParam Long id) {
		int status = mService.deleteById(id);
		switch (status) {
		case 0:
			return new ResponseEntity<>("Manipulador eliminado", HttpStatus.OK);
		case 1:
			return new ResponseEntity<>("Manipulador no encontrado", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Debe iniciar sesion un admin", HttpStatus.UNAUTHORIZED);
		default:
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}
}
