package co.edu.unbosque.proyectomodulo.exceptions;

/**
 * Excepción personalizada que se lanza cuando la dirección ingresada
 * no cumple con el formato o los criterios de validación requeridos.
 *
 * <p>Extiende de {@link Exception} para ser tratada como una
 * excepción verificada en tiempo de compilación.</p>
 *
 * @version 1.0
 */
public class DireccionException extends Exception {

	/**
	 * Constructor por defecto de {@code DireccionException}.
	 * Inicializa la excepción con el mensaje {@code "Ingrese una dirección válida"}.
	 */
	public DireccionException() {
		super("Ingrese una dirección válida");
	}
}