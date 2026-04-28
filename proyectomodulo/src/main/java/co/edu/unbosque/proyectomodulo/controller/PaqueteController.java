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

	/**
	 * Servicio de lógica de negocio para la gestión de paquetes.
	 */
	@Autowired
	private PaqueteService paqueteService;

	/**
	 * Servicio de lógica de negocio para la gestión de clientes.
	 */
	@Autowired
	private ClienteService clienteService;

	/**
	 * Crea un nuevo paquete para el cliente actualmente logueado. Calcula el precio
	 * y el tiempo de entrega automáticamente según el tipo de paquete y el tipo de cliente.
	 * <p>
	 * Endpoint: {@code POST /paquete/crear}
	 * </p>
	 * <p>
	 * Lógica de tiempo de envío por tipo de paquete:
	 * <ul>
	 *   <li>{@code CARTA} – Entrega al día siguiente a las 08:00. Fragil y refrigeración
	 *       se ignoran y se fijan en {@code false}.</li>
	 *   <li>{@code ALIMENTICIO} – Entrega en 6 horas desde el momento actual (minutos en 0).
	 *       Si la hora actual supera las 22:00, se ajusta a las 08:00.</li>
	 *   <li>{@code NO_ALIMENTICIO} – Entrega al día siguiente a las 08:00.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Descuentos aplicados según tipo de cliente:
	 * <ul>
	 *   <li>{@code NORMAL} – Sin descuento.</li>
	 *   <li>{@code PREMIUM} – 30% de descuento sobre el precio base.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Los códigos de estado devueltos por el servicio se interpretan así:
	 * <ul>
	 *   <li>{@code 0} – Paquete creado exitosamente.</li>
	 *   <li>{@code 1} – Tipo de paquete inválido.</li>
	 *   <li>{@code 2} – No hay sesión de cliente activa.</li>
	 *   <li>Cualquier otro valor – Error genérico.</li>
	 * </ul>
	 * </p>
	 *
	 * @param tipoPaquete           tipo del paquete ({@code CARTA}, {@code ALIMENTICIO}
	 *                              o {@code NO_ALIMENTICIO}).
	 * @param contenido             descripción del contenido del paquete.
	 * @param direccionAEnviar      dirección de destino del envío.
	 * @param destinatario          nombre del destinatario del paquete.
	 * @param pesoKg                peso del paquete en kilogramos.
	 * @param esFragil              indica si el paquete es frágil (ignorado para {@code CARTA}).
	 * @param requiereRefrigeracion indica si el paquete requiere refrigeración
	 *                              (ignorado para {@code CARTA}).
	 * @return {@link ResponseEntity} con un mensaje descriptivo y el código HTTP
	 *         correspondiente al resultado de la operación.
	 */
	@PostMapping("/crear")
	public ResponseEntity<String> crearPaquete(@RequestParam TipoPaquete tipoPaquete, @RequestParam String contenido,
			@RequestParam String direccionAEnviar, @RequestParam String destinatario, @RequestParam float pesoKg,
			@RequestParam boolean esFragil, @RequestParam boolean requiereRefrigeracion) {
		if (clienteService.getClienteLogueado() == null) {
			return new ResponseEntity<>("Se debe ingresar un usuario", HttpStatus.UNAUTHORIZED);
		}

		// Bug fix: "mm" para minutos
		LocalDateTime ahora = LocalDateTime.now();
		int anio = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("uuuu")));
		int mes = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("MM")));
		int dia = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("dd")));
		int hora = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("HH")));
		int minuto = Integer.parseInt(ahora.format(DateTimeFormatter.ofPattern("mm")));
		int precio;

		if (hora > 22) {
			hora = 8;			
		}

		switch (tipoPaquete) {
		case CARTA:{
			dia += 1;
			hora = 8;
			minuto = 0;
			precio = 10000;
			break;
		}
			
		case ALIMENTICIO: {
			hora += 6;
			minuto = 0;
			precio = 20000;
			break;			
		}
		case NO_ALIMENTICIO: {
			dia += 1;
			hora = 8;
			minuto = 0;
			precio = 30000;
			break;			
		}
		default:
			return new ResponseEntity<>("Ingrese un tipo de paquete valido (CARTA, ALIMENTICIO, NO_ALIMENTICIO)",
					HttpStatus.BAD_REQUEST);
		}

		LocalDateTime tiempoDeEnvio = LocalDateTime.of(anio, mes, dia, hora, minuto);
		EstadoPaquete estadoPaquete = EstadoPaquete.EN_BODEGA;
		String clientePaquete = clienteService.getClienteLogueado().getUsuario();
		String precioEnvio;

		switch (clienteService.getClienteLogueado().getTipoCliente()) {
		case NORMAL: {
			precioEnvio = String.valueOf(precio);
			break;			
		}
		case PREMIUM: {
			precioEnvio = String.valueOf(precio - (precio * 0.30));
			break;
		}
		default:
			return new ResponseEntity<>("Tipo de usuario invalido", HttpStatus.BAD_REQUEST);
		}
		int status = 0;
		switch (tipoPaquete) {
		case CARTA: {
			boolean esFragilCarta = false;
			boolean requiereRefrigeracionCarta = false;

			PaqueteDTO nuevo = new PaqueteDTO(tipoPaquete, contenido, direccionAEnviar, tiempoDeEnvio, precioEnvio,
					estadoPaquete, clientePaquete, destinatario, pesoKg, esFragilCarta, requiereRefrigeracionCarta);
			int status1 = paqueteService.create(nuevo);
			switch (status1) {
			case 0: {
				return new ResponseEntity<>("Paquete creado", HttpStatus.CREATED);				
			}
			case 1: {
				return new ResponseEntity<>("Tipo invalido", HttpStatus.BAD_REQUEST);				
			}
			case 2: {
				return new ResponseEntity<>("Debe iniciar sesion", HttpStatus.UNAUTHORIZED);				
			}
			default:
				return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
			}
		}
		case ALIMENTICIO: {
			PaqueteDTO nuevo = new PaqueteDTO(tipoPaquete, contenido, direccionAEnviar, tiempoDeEnvio, precioEnvio,
					estadoPaquete, clientePaquete, destinatario, pesoKg, esFragil, requiereRefrigeracion);
			int status1 = paqueteService.create(nuevo);
			switch (status1) {
			case 0: {
				return new ResponseEntity<>("Paquete creado", HttpStatus.CREATED);				
			}
			case 1: {
				return new ResponseEntity<>("Tipo invalido", HttpStatus.BAD_REQUEST);				
			}
			case 2: {
				return new ResponseEntity<>("Debe iniciar sesion", HttpStatus.UNAUTHORIZED);
			}
			default:
				return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
			}
		}
		case NO_ALIMENTICIO: {
			PaqueteDTO nuevo = new PaqueteDTO(tipoPaquete, contenido, direccionAEnviar, tiempoDeEnvio, precioEnvio,
					estadoPaquete, clientePaquete, destinatario, pesoKg, esFragil, requiereRefrigeracion);
			int status1 = paqueteService.create(nuevo);
			switch (status1) {
			case 0: {
				return new ResponseEntity<>("Paquete creado", HttpStatus.CREATED);				
			}
			case 1: {
				return new ResponseEntity<>("Tipo invalido", HttpStatus.BAD_REQUEST);				
			}
			case 2: {
				return new ResponseEntity<>("Debe iniciar sesion", HttpStatus.UNAUTHORIZED);				
			}
			default:
				return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
			}
		}

		default:
			break;
		}
		return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
	}

	/**
	 * Retorna los tiempos de entrega estimados por tipo de paquete.
	 * <p>
	 * Endpoint: {@code GET /paquete/tiempopaquete}
	 * </p>
	 *
	 * @return {@link ResponseEntity} con el mensaje
	 *         {@code "Alimenticios: máximo 6 horas | No Alimenticios: máximo 24 horas | Carta: máximo 72 horas"}
	 *         y estado {@code 200 OK}.
	 */
	@GetMapping("/tiempopaquete")
	public ResponseEntity<String> demoraDePaquete() {
		return new ResponseEntity<>(
				"Alimenticios: máximo 6 horas | No Alimenticios: máximo 24 horas | Carta: máximo 72 horas",
				HttpStatus.OK);
	}
}