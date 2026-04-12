package co.edu.unbosque.proyectomodulo.exceptions;

/**
 * Excepción personalizada que se lanza cuando el tipo de paquete ingresado
 * no cumple con los criterios de validación requeridos.
 *
 * <p>Los tipos de paquete válidos son: carta, alimenticio y no alimenticio.</p>
 *
 * <p>Extiende de {@link Exception} para ser tratada como una
 * excepción verificada en tiempo de compilación.</p>
 *
 * @version 1.0
 */
public class TipoPaqueteException extends Exception {

	/**
	 * Constructor por defecto de {@code TipoPaqueteException}.
	 * Inicializa la excepción con el mensaje {@code "Ingrese un tipo de paquete válido"}.
	 */
	public TipoPaqueteException() {
		super("Ingrese un tipo de paquete válido");
	}
}