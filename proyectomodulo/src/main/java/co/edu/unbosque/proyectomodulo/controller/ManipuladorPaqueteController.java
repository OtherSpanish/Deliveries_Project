package co.edu.unbosque.proyectomodulo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para operaciones de manipuladores de paquetes.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/manipuladorpaquete")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class ManipuladorPaqueteController {

    @Autowired private ManipuladorPaquetesService manipuladorService;
    @Autowired private PaqueteService pService;

    @PostMapping("/loginmanipulador")
    public ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String contrasenia) {
        int status = manipuladorService.login(usuario, contrasenia);
        if (status == 0) return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
        return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/logoutmanipulador")
    public ResponseEntity<String> logout() {
        if (!manipuladorService.isLogged()) return new ResponseEntity<>("No hay sesion iniciada", HttpStatus.BAD_REQUEST);
        manipuladorService.logout();
        return new ResponseEntity<>("Sesion cerrada", HttpStatus.OK);
    }

    @GetMapping("/mostrarpaquete")
    public ResponseEntity<String> mostrarPaquetes() {
        String paquetes = pService.getAllManipuladorPaquetes();
        if (paquetes == null) return new ResponseEntity<>("Se necesita ingresar un manipulador de paquetes o un admin", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(paquetes, HttpStatus.OK);
    }
}
