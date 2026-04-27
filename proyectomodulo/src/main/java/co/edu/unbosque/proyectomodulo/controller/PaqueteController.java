package co.edu.unbosque.proyectomodulo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unbosque.proyectomodulo.dto.*;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para la gestión de paquetes. Proporciona endpoints para
 * crear paquetes y consultar tiempos de entrega según tipo de paquete.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/paquete")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080", "*" })
public class PaqueteController {

    @Autowired private PaqueteService paqueteService;
    @Autowired private ClienteService clienteService;

    /**
     * Crea un nuevo paquete para el cliente actualmente logueado.
     * Calcula precio y tiempo de entrega según tipo de paquete y tipo de cliente.
     */
    @PostMapping("/crear")
    public ResponseEntity<String> crearPaquete(@RequestParam TipoPaquete tipoPaquete,
            @RequestParam String contenido, @RequestParam String direccionAEnviar) {
        if (clienteService.getClienteLogueado() == null) {
            return new ResponseEntity<>("Se debe ingresar un usuario", HttpStatus.UNAUTHORIZED);
        }

        // Bug fix: "mm" para minutos
        LocalDateTime ahora = LocalDateTime.now();
        int anio   = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("uuuu")));
        int mes    = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("MM")));
        int dia    = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("dd")));
        int hora   = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("HH")));
        int minuto = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("mm")));
        int precio;

        if (hora > 22) hora = 8;

        switch (tipoPaquete) {
            case CARTA:
                dia += 1; hora = 8; minuto = 0; precio = 10000;
                break;
            case ALIMENTICIO:
                hora += 6; minuto = 0; precio = 20000;
                break;
            case NO_ALIMENTICIO:
                dia += 1; hora = 8; minuto = 0; precio = 30000;
                break;
            default:
                return new ResponseEntity<>("Ingrese un tipo de paquete valido (CARTA, ALIMENTICIO, NO_ALIMENTICIO)",
                        HttpStatus.BAD_REQUEST);
        }

        LocalDateTime tiempoDeEnvio = LocalDateTime.of(anio, mes, dia, hora, minuto);
        EstadoPaquete estadoPaquete  = EstadoPaquete.EN_BODEGA;
        String clientePaquete        = clienteService.getClienteLogueado().getUsuario();
        String precioEnvio;

        // Bug fix: añadir caso CONCURRENTE
        switch (clienteService.getClienteLogueado().getTipoCliente()) {
            case NORMAL:
                precioEnvio = String.valueOf(precio);
                break;
            case PREMIUM:
                precioEnvio = String.valueOf(precio - (precio * 0.30));
                break;
            default:
                return new ResponseEntity<>("Tipo de usuario invalido", HttpStatus.BAD_REQUEST);
        }

        PaqueteDTO nuevo = new PaqueteDTO(tipoPaquete, contenido, direccionAEnviar,
                tiempoDeEnvio, precioEnvio, estadoPaquete, clientePaquete);
        int status = paqueteService.create(nuevo);
        switch (status) {
            case 0: return new ResponseEntity<>("Paquete creado", HttpStatus.CREATED);
            case 1: return new ResponseEntity<>("Tipo invalido", HttpStatus.BAD_REQUEST);
            case 2: return new ResponseEntity<>("Debe iniciar sesion", HttpStatus.UNAUTHORIZED);
            default: return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
        }
    }

    /**
     * Retorna los tiempos de entrega estimados por tipo de paquete.
     */
    @GetMapping("/tiempopaquete")
    public ResponseEntity<String> demoraDePaquete() {
        return new ResponseEntity<>(
                "Alimenticios: máximo 6 horas | No Alimenticios: máximo 24 horas | Carta: máximo 72 horas",
                HttpStatus.OK);
    }
}
