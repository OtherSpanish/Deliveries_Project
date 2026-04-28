package co.edu.unbosque.proyectomodulo.exceptions;

/**
 * Clase utilitaria que centraliza la validación de datos del sistema y lanza
 * las excepciones personalizadas correspondientes cuando los datos ingresados
 * no cumplen con los criterios requeridos.
 *
 * <p>
 * Todos los métodos son estáticos y no requieren instanciar la clase.
 * </p>
 *
 * @version 1.0
 */
public class LanzadorException {

	/**
	 * Verifica que la cédula ingresada sea válida. Una cédula válida debe tener
	 * exactamente 10 dígitos, no comenzar con '0' y contener solo caracteres
	 * numéricos.
	 *
	 * @param entrada cédula a validar.
	 * @throws CedulaException si la cédula es nula, no tiene 10 dígitos, comienza
	 *                         con '0' o contiene caracteres no numéricos.
	 */
	public static void verificarCedulaValida(String entrada) throws CedulaException {
		if (entrada == null || entrada.length() != 10) {
			throw new CedulaException();
		}
		if (entrada.charAt(0) == '0') {
			throw new CedulaException();
		}
		for (char c : entrada.toCharArray()) {
			if (!Character.isDigit(c)) {
				throw new CedulaException();
			}
		}
	}

	/**
	 * Verifica que el tipo de paquete ingresado sea válido. Los tipos válidos son:
	 * carta, alimenticio y no alimenticio.
	 *
	 * @param tipo tipo de paquete a validar.
	 * @throws TipoPaqueteException si el tipo es nulo o no corresponde a ninguno de
	 *                              los tipos permitidos.
	 */
	public static void verificarTipoPaquete(String tipo) throws TipoPaqueteException {
		if (tipo == null) {
			throw new TipoPaqueteException();
		}
		String t = tipo.trim().toLowerCase();
		if (!(t.equals("carta") || t.equals("alimenticios") || t.equals("no alimenticios") || t.equals("no alimenticio")
				|| t.equals("alimenticio"))) {
			throw new TipoPaqueteException();
		}
	}

	/**
	 * Verifica que el tipo de vehículo ingresado sea válido. Los tipos válidos son:
	 * carro, moto y camion.
	 *
	 * @param tipo tipo de vehículo a validar.
	 * @throws TipoVehiculoException si el tipo es nulo o no corresponde a ninguno
	 *                               de los tipos permitidos.
	 */
	public static void verificarTipoVehiculo(String tipo) throws TipoVehiculoException {
		if (tipo == null) {
			throw new TipoVehiculoException();
		}
		String t = tipo.trim().toLowerCase();
		if (!(t.equals("carro") || t.equals("moto") || t.equals("camion") || t.equals("camión"))) {
			throw new TipoVehiculoException();
		}
	}

	/**
	 * Verifica que la dirección ingresada sea válida según el formato colombiano
	 * estándar. Para ser considerada válida, la dirección debe cumplir todas las
	 * siguientes condiciones:
	 * <ul>
	 *   <li>No ser nula ni estar vacía.</li>
	 *   <li>Comenzar con una nomenclatura reconocida: {@code calle}, {@code carrera},
	 *       {@code avenida}, {@code cl}, {@code cra}, {@code av} o {@code cll}.</li>
	 *   <li>Contener al menos un dígito numérico.</li>
	 *   <li>Contener los caracteres {@code #} y {@code -}, propios del formato de
	 *       dirección colombiana (ej. {@code Calle 10 #5-20}).</li>
	 * </ul>
	 *
	 * @param direccion dirección a validar.
	 * @throws DireccionException si la dirección es nula, está vacía, no inicia con
	 *                            una nomenclatura válida, no contiene dígitos, o no
	 *                            incluye los caracteres {@code #} y {@code -}.
	 */
	public static void verificarDireccion(String direccion) throws DireccionException {
	    if (direccion == null || direccion.trim().isEmpty()) {
	        throw new DireccionException();
	    }

	    String t = direccion.trim().toLowerCase();

	    if (!(t.startsWith("calle") || t.startsWith("carrera") || t.startsWith("avenida") || t.startsWith("cl")
	            || t.startsWith("cra") || t.startsWith("av") || t.startsWith("cll"))) {
	        throw new DireccionException();
	    }

	    boolean tieneNumero = false;
	    for (char c : t.toCharArray()) {
	        if (Character.isDigit(c)) {
	            tieneNumero = true;
	            break;
	        }
	    }

	    if (!tieneNumero) {
	        throw new DireccionException();
	    }

	    if (!t.contains("#") || !t.contains("-")) {
	        throw new DireccionException();
	    }
	}

	/**
	 * Verifica que las horas de trabajo ingresadas estén dentro del rango
	 * permitido. El rango válido es de mínimo 1 hora y máximo 8 horas.
	 *
	 * @param horaTrabajo cantidad de horas de trabajo a validar.
	 * @throws HoraTrabajoException si las horas son mayores o iguales a 9 o menores
	 *                              o iguales a 0.
	 */
	public static void verificarHoraDeTrabajo(int horasTrabajo) throws HoraTrabajoException {
		if (horasTrabajo >= 9 || horasTrabajo < 2) {
			throw new HoraTrabajoException();
		}
	}
}
