package co.edu.unbosque.proyectomodulo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.proyectomodulo.dto.PaqueteDTO;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Controlador REST para la gestión de operaciones sobre paquetes. Proporciona
 * endpoints para la creación de paquetes y consulta de tiempos de entrega según
 * el tipo de paquete.
 *
 * <p>
 * Todos los endpoints están bajo el prefijo {@code /paquete} y permiten
 * solicitudes desde cualquier origen.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/paquete")
@CrossOrigin(origins = { "http://localhost:8080", "*" })
public class PaqueteController {

	/** Servicio para operaciones relacionadas con paquetes. */
	@Autowired
	private PaqueteService paqueteService;

	/** Servicio para operaciones relacionadas con clientes. */
	@Autowired
	private ClienteService clienteService;

	/**
	 * Crea un nuevo paquete en el sistema. Calcula automáticamente el tiempo de
	 * entrega y el precio final según el tipo de paquete y el tipo del cliente
	 * actualmente logueado, aplicando el descuento correspondiente.
	 *
	 * <ul>
	 * <li>carta: 72 horas, $10.000</li>
	 * <li>alimenticios: 6 horas, $20.000</li>
	 * <li>no alimenticios: 24 horas, $30.000</li>
	 * </ul>
	 *
	 * @param tipoPaquete      tipo de paquete (carta, alimenticios, no alimenticios).
	 * @param contenido        descripción del contenido del paquete.
	 * @param direccionAEnviar dirección de destino del paquete.
	 * @return {@code 201 Created} si el paquete se crea exitosamente,
	 *         {@code 400 Bad Request} si el tipo de paquete o tipo de cliente es
	 *         inválido, o no hay cliente logueado,
	 *         {@code 401 Unauthorized} si no se ha iniciado sesión.
	 */
	@PostMapping("/crear")
	public ResponseEntity<String> crearPaquete(@RequestParam String tipoPaquete, @RequestParam String contenido,
			@RequestParam String direccionAEnviar) {
		if (clienteService.getClienteLogueado() == null) {
			return new ResponseEntity<>("Se debe ingresar un usuario", HttpStatus.UNAUTHORIZED);
		} else {
			LocalDateTime tiempoDeEnvio2 = LocalDateTime.now();
			DateTimeFormatter formatoDias = DateTimeFormatter.ofPattern("dd");
			DateTimeFormatter formatoMeses = DateTimeFormatter.ofPattern("MM");
			DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH");
			DateTimeFormatter formatoMinuto= DateTimeFormatter.ofPattern("HH");
			DateTimeFormatter formatoAnio = DateTimeFormatter.ofPattern("uuuu");
			String formateadoHora = tiempoDeEnvio2.format(formatoHora);
			String formateadoDias = tiempoDeEnvio2.format(formatoDias);
			String formateadoMeses = tiempoDeEnvio2.format(formatoMeses);
			String formateadoAnio = tiempoDeEnvio2.format(formatoAnio);
			String formateadoMinuto = tiempoDeEnvio2.format(formatoMinuto);
			int anio = Integer.parseInt(formateadoAnio);
			int hora = Integer.parseInt(formateadoHora);
			int dia = Integer.parseInt(formateadoDias);
			int mes = Integer.parseInt(formateadoMeses);
			int minuto = Integer.parseInt(formateadoMinuto);
			int precio;
			
			if(hora > 22) {
				hora = 8;
			}
			switch (tipoPaquete.toLowerCase()) {
			case "carta": {
				int dias = dia+1;
				dia = dias;
				hora = 8;
				minuto = 00;
				precio = 10000;
				break;
			}
			case "alimenticios":{
				int entrega = 6 + hora;
				hora = entrega;
				minuto = 00;
				precio = 20000;
				break;
			}
			case "no alimenticios":{
				int dias = dia+1;
				dia = dias;
				hora = 8;
				minuto = 00;
				precio = 30000;
				break;
			}
			default:
				return new ResponseEntity<>("Ingrese un tipo de paquete valido (carta, alimenticios, no alimenticios)",
						HttpStatus.BAD_REQUEST);
			}
			LocalDateTime tiempoDeEnvio = LocalDateTime.of(anio, mes, dia, hora, minuto);
			String precioEnvio = "";

			switch (clienteService.getClienteLogueado().getTipoCliente().toLowerCase()) {
			case "normal": {
				precioEnvio = "" + precio;
				break;
			}
			case "concurrente": {
				precioEnvio = "" + (precio - (precio * 0.10));
				break;
			}
			case "premium": {
				precioEnvio = "" + (precio - (precio * 0.30));
				break;
			}
			default:
				return new ResponseEntity<>("Tipo de usuario invalido", HttpStatus.BAD_REQUEST);
			}
			PaqueteDTO nuevo = new PaqueteDTO(tipoPaquete, contenido, direccionAEnviar, tiempoDeEnvio, precioEnvio);
			int status = paqueteService.create(nuevo);
			if (status == 0) {
				return new ResponseEntity<>("Paquete creado", HttpStatus.CREATED);
			} else if (status == 1) {
				return new ResponseEntity<>("Tipo invalido", HttpStatus.BAD_REQUEST);
			} else if (status == 2) {
				return new ResponseEntity<>("Debe iniciar sesion", HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
	}

	/**
	 * Consulta el tiempo de entrega estimado para un tipo de paquete específico.
	 *
	 * <ul>
	 * <li>carta: 72 horas</li>
	 * <li>alimenticios: 6 horas</li>
	 * <li>no alimenticios: 24 horas</li>
	 * </ul>
	 *
	 * @param tipoPaquete tipo de paquete a consultar (carta, alimenticios, no alimenticios).
	 * @return {@code 200 OK} con el tiempo de entrega del tipo de paquete,
	 *         
	 */
	@GetMapping("/tiempoPaquete")
	public ResponseEntity<String> demoraDePaquete() {
			return new ResponseEntity<>("El paquete de tipo: Alimenticios se demora un tiempo máximo de: 6 horas\nEl paquete de tipo: No Alimenticios se demora un tiempo máximo de: 24 horas\nEl paquete de tipo: Carta se demora un tiempo máximo de: 72 horas", HttpStatus.OK);
		}
}
	
