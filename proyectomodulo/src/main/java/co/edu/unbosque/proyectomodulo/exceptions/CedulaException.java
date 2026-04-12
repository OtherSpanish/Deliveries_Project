package co.edu.unbosque.proyectomodulo.exceptions;

/**
 * Excepción personalizada que se lanza cuando la cédula ingresada
 * no cumple con el formato o los criterios de validación requeridos.
 *
 * <p>Extiende de {@link Exception} para ser tratada como una
 * excepción verificada en tiempo de compilación.</p>
 *
 * @version 1.0
 */
public class CedulaException extends Exception {

	/**
	 * Constructor por defecto de {@code CedulaException}.
	 * Inicializa la excepción con el mensaje {@code "Ingrese una cédula válida"}.
	 */
	public CedulaException() {
		super("Ingrese una cédula válida");
	}
}