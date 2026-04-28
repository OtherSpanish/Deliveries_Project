package co.edu.unbosque.proyectomodulo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.dto.TipoCliente;
import co.edu.unbosque.proyectomodulo.entity.Paquete;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para operaciones de clientes. Permite autenticación,
 * registro y consulta de información de clientes.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class ClienteController {

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
	 * Autentica a un cliente con sus credenciales de acceso.
	 * <p>
	 * Endpoint: {@code POST /cliente/login}
	 * </p>
	 *
	 * @param usuario    nombre de usuario del cliente.
	 * @param contrasenia contraseña asociada al usuario.
	 * @return {@link ResponseEntity} con mensaje {@code "Login exitoso"} y estado
	 *         {@code 200 OK} si las credenciales son correctas, o mensaje
	 *         {@code "Credenciales incorrectas"} y estado {@code 401 UNAUTHORIZED}
	 *         en caso contrario.
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contrasenia) {
		int status = clienteService.login(usuario, contrasenia);
		if (status == 0) {
			return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
		}
		return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Cierra la sesión del cliente actualmente autenticado.
	 * <p>
	 * Endpoint: {@code POST /cliente/logout}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con mensaje {@code "Sesion cerrada"} y estado
	 *         {@code 200 OK} si había una sesión activa, o mensaje
	 *         {@code "No hay sesion iniciada"} y estado {@code 400 BAD_REQUEST}
	 *         si no existe ninguna sesión iniciada.
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (!clienteService.isLogged()) {
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		}
		clienteService.logout();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

	/**
	 * Registra un nuevo cliente en el sistema.
	 * <p>
	 * Endpoint: {@code POST /cliente/register}
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Registro exitoso.</li>
	 *   <li>{@code 1} – Tipo de cliente inválido; debe ser {@code NORMAL} o {@code PREMIUM}.</li>
	 *   <li>{@code 2} – El nombre de usuario ya está registrado.</li>
	 *   <li>{@code 3} – No puede haber un usuario con sesión activa al momento del registro.</li>
	 *   <li>Cualquier otro valor – Error genérico al crear el usuario.</li>
	 * </ul>
	 * </p>
	 *
	 * @param usuario     nombre de usuario deseado para el nuevo cliente.
	 * @param contrasenia contraseña para el nuevo cliente.
	 * @param cedula      número de cédula del cliente.
	 * @param tipoCliente tipo de cliente ({@code NORMAL} o {@code PREMIUM}).
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam String cedula, @RequestParam TipoCliente tipoCliente) {
		int status = clienteService.create(new ClienteDTO(usuario, contrasenia, cedula, tipoCliente));
		switch (status) {
		case 0: {
			return new ResponseEntity<>("Usuario registrado", HttpStatus.CREATED);
		}

		case 1: {
			return new ResponseEntity<>("Ingrese correctamente el tipo de usuario (NORMAL, PREMIUM)",
					HttpStatus.BAD_REQUEST);
		}

		case 2: {
			return new ResponseEntity<>("El Nombre de usuario ya se encuentra registrado", HttpStatus.BAD_REQUEST);
		}
		case 3: {
			return new ResponseEntity<>("Ningun usuario puede estar ingresado en este momento", HttpStatus.BAD_REQUEST);
		}
		default:
			return new ResponseEntity<>("Error al crear", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Devuelve información sobre los tipos de cliente disponibles y sus descuentos.
	 * <p>
	 * Endpoint: {@code GET /cliente/tipocliente}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con el mensaje
	 *         {@code "NORMAL: 0% descuento | PREMIUM: 30% descuento"} y estado
	 *         {@code 200 OK}.
	 */
	@GetMapping("/tipocliente")
	public ResponseEntity<String> tipoDeClienteSoy() {
		return new ResponseEntity<>("NORMAL: 0% descuento | PREMIUM: 30% descuento", HttpStatus.OK);
	}

	/**
	 * Devuelve la carta de precios de los productos disponibles.
	 * <p>
	 * Endpoint: {@code GET /cliente/precioproducto}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con el mensaje
	 *         {@code "Carta: $10.000 | Alimenticios: $20.000 | No Alimenticios: $30.000"}
	 *         y estado {@code 200 OK}.
	 */
	@GetMapping("/precioproducto")
	public ResponseEntity<String> mostrarPrecio() {
		return new ResponseEntity<>("Carta: $10.000 | Alimenticios: $20.000 | No Alimenticios: $30.000", HttpStatus.OK);
	}

	/**
	 * Retorna la lista de paquetes asociados al cliente que tiene sesión activa.
	 * <p>
	 * Endpoint: {@code GET /cliente/clientePaquete}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con la lista de {@link Paquete} pertenecientes
	 *         al cliente logueado y estado {@code 200 OK}.
	 */
	@GetMapping("/clientePaquete")
	public ResponseEntity<List<Paquete>> clientePorPaquete() {
		String clientePorPaquete =  clienteService.getClienteLogueado().getUsuario();
		return new ResponseEntity<>(pService.paquetesPorCliente(clientePorPaquete), HttpStatus.OK);
	}

}