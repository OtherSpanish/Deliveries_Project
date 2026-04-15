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

import co.edu.unbosque.proyectomodulo.dto.PaqueteDTO;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador REST para la gestión de operaciones de manipuladores de paquetes.
 * Proporciona endpoints para autenticación y consulta de paquetes
 * disponibles para manipulación.
 *
 * <p>
 * Todos los endpoints están bajo el prefijo {@code /manipuladorpaquete} y permiten
 * solicitudes desde cualquier origen.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/manipuladorpaquete")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class ManipuladorPaqueteController {

	/** Servicio para operaciones relacionadas con manipuladores de paquetes. */
	@Autowired
	private ManipuladorPaquetesService manipuladorService;

	/** Servicio para operaciones relacionadas con paquetes. */
	@Autowired
	private PaqueteService pService;

	/**
	 * Inicia sesión como manipulador de paquetes en el sistema.
	 *
	 * @param usuario     nombre de usuario del manipulador.
	 * @param contrasenia contraseña del manipulador.
	 * @return {@code 200 OK} si el login es exitoso,
	 *         {@code 401 Unauthorized} si las credenciales son incorrectas.
	 */
	@PostMapping("/loginmanipulador")
	public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contrasenia) {
		int status = manipuladorService.login(usuario, contrasenia);
		if (status == 0) {
			return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
		}
		return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Cierra la sesión del manipulador de paquetes actualmente logueado.
	 *
	 * @return {@code 200 OK} confirmando el cierre de sesión,
	 *         {@code 400 Bad Request} si no hay sesión activa.
	 */
	@PostMapping("/logoutmanipulador")
	public ResponseEntity<String> logout() {
		if (manipuladorService.isLogged() == false) {
			return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
		}
		manipuladorService.logout();
		return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}

	/**
	 * Obtiene la lista de todos los paquetes registrados en el sistema.
	 *
	 * @return {@code 202 Accepted} con la lista de paquetes,
	 *         {@code 204 No Content} si no hay paquetes registrados,
	 *         {@code 401 Unauthorized} si no hay manipulador o admin logueado.
	 */
	@GetMapping("/mostrarpaquete")
	public ResponseEntity<String> mostrarPaquetes() {
		String paquetes = pService.getAllManipuladorPaquetes();
		if (paquetes == null) {
			return new ResponseEntity<>("Se necesita ingresar un manipulador de paquetes o un admin", HttpStatus.UNAUTHORIZED);
		}else {
			return new ResponseEntity<>("" + paquetes, HttpStatus.ACCEPTED);
		}
	}
}