package co.edu.unbosque.proyectomodulo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.service.ConductorService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para operaciones de conductores.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/conductor")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class ConductorController {

    @Autowired private PaqueteService pService;
    @Autowired private ConductorService cService;

    @PostMapping("/login")
    public ResponseEntity<String> loginConductor(@RequestParam String usuario, @RequestParam String contrasenia) {
        int status = cService.login(usuario, contrasenia);
        if (status == 0) {
        	return new ResponseEntity<>("Conductor ingresado", HttpStatus.OK);
        }        	
        return new ResponseEntity<>("Credenciales invalidas", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        if (!cService.isLogged()) {
        	return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
        }        	
        cService.logout();
        return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
    }

    @GetMapping("/mostrarpedidos")
    public ResponseEntity<String> mostrarTodo() {
        String paquetes = pService.getAll();
        if (paquetes == null) {
        	return new ResponseEntity<>("Se necesita ingresar un conductor o un admin", HttpStatus.UNAUTHORIZED);
        }
        	
        return new ResponseEntity<>(paquetes, HttpStatus.OK);
    }
}
