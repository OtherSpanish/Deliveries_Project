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

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private PaqueteService pService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contrasenia) {
		int status = clienteService.login(usuario, contrasenia);
		if (status == 0) {
			return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
		}
		return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (!clienteService.isLogged()) {
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		}
		clienteService.logout();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

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

	@GetMapping("/tipocliente")
	public ResponseEntity<String> tipoDeClienteSoy() {
		return new ResponseEntity<>("NORMAL: 0% descuento | PREMIUM: 30% descuento", HttpStatus.OK);
	}

	@GetMapping("/precioproducto")
	public ResponseEntity<String> mostrarPrecio() {
		return new ResponseEntity<>("Carta: $10.000 | Alimenticios: $20.000 | No Alimenticios: $30.000", HttpStatus.OK);
	}

	@GetMapping("/clientePaquete")
	public ResponseEntity<List<Paquete>> clientePorPaquete() {
		String clientePorPaquete = clienteService.getClienteLogueado().getUsuario();
		return new ResponseEntity<>(pService.paquetesPorCliente(clientePorPaquete), HttpStatus.OK);
	}

}
