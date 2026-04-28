package co.edu.unbosque.proyectomodulo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para operaciones de manipuladores de paquetes. Permite
 * autenticación y consulta de paquetes disponibles para el manipulador activo.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/manipuladorpaquete")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class ManipuladorPaqueteController {

    /**
     * Servicio de lógica de negocio para la gestión de manipuladores de paquetes.
     */
    @Autowired
    private ManipuladorPaquetesService manipuladorService;

    /**
     * Servicio de lógica de negocio para la gestión de paquetes.
     */
    @Autowired
    private PaqueteService pService;

    /**
     * Autentica a un manipulador de paquetes con sus credenciales de acceso.
     * <p>
     * Endpoint: {@code POST /manipuladorpaquete/loginmanipulador}
     * </p>
     *
     * @param usuario     nombre de usuario del manipulador.
     * @param contrasenia contraseña asociada al manipulador.
     * @return {@link ResponseEntity} con mensaje {@code "Login exitoso"} y estado
     *         {@code 200 OK} si las credenciales son correctas, o mensaje
     *         {@code "Credenciales incorrectas"} y estado {@code 400 BAD_REQUEST}
     *         en caso contrario.
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
     * Cierra la sesión del manipulador de paquetes actualmente autenticado.
     * <p>
     * Endpoint: {@code POST /manipuladorpaquete/logoutmanipulador}
     * </p>
     *
     * @return {@link ResponseEntity} con mensaje {@code "Sesion cerrada"} y estado
     *         {@code 200 OK} si había una sesión activa, o mensaje
     *         {@code "No hay sesion iniciada"} y estado {@code 400 BAD_REQUEST}
     *         si no existe ninguna sesión de manipulador iniciada.
     */
    @PostMapping("/logoutmanipulador")
    public ResponseEntity<String> logout() {
        if (!manipuladorService.isLogged()) {
        	return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
        }
        manipuladorService.logout();
        return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
    }

    /**
     * Retorna la lista de paquetes disponibles para ser gestionados por el manipulador.
     * Requiere que haya un manipulador de paquetes o un admin con sesión activa.
     * <p>
     * Endpoint: {@code GET /manipuladorpaquete/mostrarpaquete}
     * </p>
     *
     * @return {@link ResponseEntity} con la cadena de paquetes y estado {@code 200 OK}
     *         si hay una sesión activa válida, o mensaje
     *         {@code "Se necesita ingresar un manipulador de paquetes o un admin"} y estado
     *         {@code 401 UNAUTHORIZED} si no hay ninguna sesión autorizada.
     */
    @GetMapping("/mostrarpaquete")
    public ResponseEntity<String> mostrarPaquetes() {
        String paquetes = pService.getAllManipuladorPaquetes();
        if (paquetes == null) {
        	return new ResponseEntity<>("Se necesita ingresar un manipulador de paquetes o un admin", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(paquetes, HttpStatus.OK);
    }
}