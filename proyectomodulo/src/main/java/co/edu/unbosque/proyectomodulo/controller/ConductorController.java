package co.edu.unbosque.proyectomodulo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.service.ConductorService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para operaciones de conductores. Permite autenticación
 * y consulta de pedidos asignados al conductor activo.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/conductor")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class ConductorController {

    /**
     * Servicio de lógica de negocio para la gestión de paquetes.
     */
    @Autowired 
    private PaqueteService pService;

    /**
     * Servicio de lógica de negocio para la gestión de conductores.
     */
    @Autowired 
    private ConductorService cService;

    /**
     * Autentica a un conductor con sus credenciales de acceso.
     * <p>
     * Endpoint: {@code POST /conductor/login}
     * </p>
     *
     * @param usuario    nombre de usuario del conductor.
     * @param contrasenia contraseña asociada al conductor.
     * @return {@link ResponseEntity} con mensaje {@code "Conductor ingresado"} y estado
     *         {@code 200 OK} si las credenciales son correctas, o mensaje
     *         {@code "Credenciales invalidas"} y estado {@code 400 BAD_REQUEST}
     *         en caso contrario.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginConductor(@RequestParam String usuario, @RequestParam String contrasenia) {
        int status = cService.login(usuario, contrasenia);
        if (status == 0) {
        	return new ResponseEntity<>("Conductor ingresado", HttpStatus.OK);
        }
        return new ResponseEntity<>("Credenciales invalidas", HttpStatus.BAD_REQUEST);
    }

    /**
     * Cierra la sesión del conductor actualmente autenticado.
     * <p>
     * Endpoint: {@code POST /conductor/logout}
     * </p>
     *
     * @return {@link ResponseEntity} con mensaje {@code "Sesion cerrada"} y estado
     *         {@code 200 OK} si había una sesión activa, o mensaje
     *         {@code "No hay sesion iniciada"} y estado {@code 400 BAD_REQUEST}
     *         si no existe ninguna sesión de conductor iniciada.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        if (!cService.isLogged()) {
        	return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
        }
        cService.logout();
        return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
    }

    /**
     * Retorna la lista de todos los paquetes (pedidos) disponibles en el sistema.
     * Requiere que haya un conductor o un admin con sesión activa.
     * <p>
     * Endpoint: {@code GET /conductor/mostrarpedidos}
     * </p>
     *
     * @return {@link ResponseEntity} con la cadena de paquetes y estado {@code 200 OK}
     *         si hay una sesión activa válida, o mensaje
     *         {@code "Se necesita ingresar un conductor o un admin"} y estado
     *         {@code 401 UNAUTHORIZED} si no hay ninguna sesión autorizada.
     */
    @GetMapping("/mostrarpedidos")
    public ResponseEntity<String> mostrarTodo() {
        String paquetes = pService.getAll();
        if (paquetes == null) {
        	return new ResponseEntity<>("Se necesita ingresar un conductor o un admin", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(paquetes, HttpStatus.OK);
    }
}