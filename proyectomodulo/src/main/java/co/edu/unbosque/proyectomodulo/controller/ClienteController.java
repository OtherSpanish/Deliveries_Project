package co.edu.unbosque.proyectomodulo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador REST para la gestión de operaciones de clientes. Proporciona
 * endpoints para autenticación, registro y consulta de información relacionada
 * con clientes y sus tipos.
 *
 * <p>
 * Todos los endpoints están bajo el prefijo {@code /cliente} y permiten
 * solicitudes desde cualquier origen.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class ClienteController {

	/** Servicio para operaciones relacionadas con clientes. */
	@Autowired
	private ClienteService clienteService;

	/**
	 * Inicia sesión como cliente en el sistema.
	 *
	 * @param usuario     nombre de usuario del cliente.
	 * @param contrasenia contraseña del cliente.
	 * @return {@code 200 OK} si el login es exitoso,
	 *         {@code 401 Unauthorized} si las credenciales son incorrectas.
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
	 * Cierra la sesión del cliente actualmente logueado.
	 *
	 * @return {@code 200 OK} confirmando el cierre de sesión,
	 *         {@code 400 Bad Request} si no hay sesión activa.
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (clienteService.isLogged() == false) {
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		}
		clienteService.logout();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

	/**
	 * Registra un nuevo cliente en el sistema.
	 *
	 * @param usuario     nombre de usuario del nuevo cliente.
	 * @param contrasenia contraseña del nuevo cliente.
	 * @param cedula      cédula del nuevo cliente.
	 * @param tipoCliente tipo de cliente (normal, premium, concurrente).
	 * @return {@code 201 Created} si el registro es exitoso,
	 *         {@code 400 Bad Request} si el nombre de usuario ya existe, hay un
	 *         usuario ingresado en el momento o ocurrió un error al crear.
	 */
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestParam String usuario, @RequestParam String contrasenia,
			@RequestParam String cedula, @RequestParam String tipoCliente) {
		int status = clienteService.register(usuario, contrasenia, cedula, tipoCliente);
		if (status == 0) {
			return new ResponseEntity<>("Usuario registrado", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ingrese correctamente el tipo de usuario (normal, premium)", HttpStatus.CREATED);
		} else if (status == 2) {
			return new ResponseEntity<>("El Nombre de usuario ya se encuentra registrado, intenta de nuevo", HttpStatus.BAD_REQUEST);
		} else if (status == 3) {
			return new ResponseEntity<>("Ningun usuario puede estar ingresado en este momento, intenta de nuevo", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Error al crear", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Consulta el descuento asociado a un tipo de cliente específico.
	 *
	 * @param tipoCliente tipo de cliente a consultar (normal, premium, concurrente).
	 * @return {@code 200 OK} con el porcentaje de descuento del tipo de cliente,
	 *         {@code 404 Not Found} si no existe un cliente de ese tipo,
	 *         {@code 400 Bad Request} si el tipo de cliente no es válido.
	 */
	@GetMapping("/tipocliente")
	public ResponseEntity<String> tipoDeClienteSoy(@RequestParam String tipoCliente) {
		List<ClienteDTO> encontrados = clienteService.findByTipoCliente(tipoCliente);
		if (encontrados.isEmpty()) {
			return new ResponseEntity<>("No existe un cliente de ese tipo", HttpStatus.NOT_FOUND);
		}
		String descuento;
		switch (tipoCliente.toLowerCase()) {
		case "normal":
			descuento = "0%";
			break;
		case "premium":
			descuento = "30%";
			break;
		case "concurrente":
			descuento = "10%";
			break;
		default:
			return new ResponseEntity<>("Tipo de paquete no válido", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(
				"El cliente de tipo " + tipoCliente + " tiene: " + descuento + " de descuento en todos sus pedidos.",
				HttpStatus.OK);
	}

	/**
	 * Calcula y muestra el precio final de un producto según el tipo de cliente,
	 * aplicando el descuento correspondiente.
	 *
	 * @param producto    tipo de producto a consultar (carta, alimenticios, no alimenticios).
	 * @param tipoCliente tipo de cliente (normal, premium, concurrente).
	 * @return {@code 200 OK} con el precio base, descuento y precio final del producto,
	 *         {@code 400 Bad Request} si el producto o tipo de cliente no es válido.
	 */
	@GetMapping("/precioproducto")
	public ResponseEntity<String> mostrarPrecio(@RequestParam String producto, @RequestParam String tipoCliente) {
		double precioBase;
		switch (producto.toLowerCase()) {
		case "carta":
			precioBase = 5000;
			break;
		case "alimenticios":
			precioBase = 10000;
			break;
		case "no alimenticios":
			precioBase = 8000;
			break;
		default:
			return new ResponseEntity<>("Producto no válido", HttpStatus.BAD_REQUEST);
		}
		double descuento;
		switch (tipoCliente.toLowerCase()) {
		case "normal":
			descuento = 0.0;
			break;
		case "premium":
			descuento = 0.30;
			break;
		case "concurrente":
			descuento = 0.10;
			break;
		default:
			return new ResponseEntity<>("Tipo de cliente no válido", HttpStatus.BAD_REQUEST);
		}
		double precioFinal = precioBase - (precioBase * descuento);
		return new ResponseEntity<>("Producto: " + producto + " | Precio base: $" + precioBase + " | Descuento: "
				+ (descuento * 100) + "%" + " | Precio final: $" + precioFinal, HttpStatus.OK);
	}
}