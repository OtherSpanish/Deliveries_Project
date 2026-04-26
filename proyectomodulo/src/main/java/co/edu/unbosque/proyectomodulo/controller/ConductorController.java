package co.edu.unbosque.proyectomodulo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectomodulo.service.ConductorService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para la gestión de operaciones de conductores. Proporciona
 * endpoints para autenticación y consulta de pedidos disponibles para entrega.
 *
 * <p>
 * Todos los endpoints están bajo el prefijo {@code /conductor} y permiten
 * solicitudes desde cualquier origen.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/conductor")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class ConductorController {

	/** Servicio para operaciones relacionadas con paquetes. */
	@Autowired
	private PaqueteService pService;

	/** Servicio para operaciones relacionadas con conductores. */
	@Autowired
	private ConductorService cService;

	/**
	 * Inicia sesión como conductor en el sistema.
	 *
	 * @param usuario     nombre de usuario del conductor.
	 * @param contrasenia contraseña del conductor.
	 * @return {@code 201 Created} si el login es exitoso, {@code 400 Bad Request}
	 *         si las credenciales son inválidas.
	 */
	@PostMapping("/login")
	public ResponseEntity<String> loginConductor(@RequestParam String usuario, @RequestParam String contrasenia) {
		int status = cService.login(usuario, contrasenia);
		if (status == 0) {
			return new ResponseEntity<>("Conductor ingresado", HttpStatus.OK);
		} else if (status == 1) {
			return new ResponseEntity<>("Credenciales invalidas", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}
	/**
	 * Cierra la sesión del administrador actualmente logueado.
	 *
	 * @return {@code 200 OK} confirmando el cierre de sesión.
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		if (cService.isLogged() == false) {
	        return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
	    }
	    cService.logout();
	    return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
	}
	

	/**
	 * Obtiene la lista de todos los pedidos (paquetes) disponibles.
	 *
	 * @return {@code 202 Accepted} con la lista de paquetes, o
	 *         {@code 204 No Content} si no hay paquetes registrados,
	 *         {@Code 401 Unauthorized} si no se ha ingresado el conductor. 
	 */
	@GetMapping("/mostrarpedidos")
	public ResponseEntity<String> mostrarTodo() {
		String paquetes = pService.getAll();
		if (paquetes == null) {
			return new ResponseEntity<>("Se necesita ingresar un conductor o un admin", HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("" + paquetes, HttpStatus.ACCEPTED);
		}
	}
}