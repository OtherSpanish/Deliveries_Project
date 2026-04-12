package co.edu.unbosque.proyectomodulo.exceptions;

/**
 * Excepción personalizada que se lanza cuando las horas de trabajo
 * ingresadas no se encuentran dentro del rango permitido.
 *
 * <p>El rango válido es de mínimo 2 horas y máximo 8 horas.</p>
 *
 * <p>Extiende de {@link Exception} para ser tratada como una
 * excepción verificada en tiempo de compilación.</p>
 *
 * @version 1.0
 */
public class HoraTrabajoException extends Exception {

	/**
	 * Constructor por defecto de {@code HorasTrabajoException}.
	 * Inicializa la excepción con el mensaje indicando el rango permitido
	 * de horas de trabajo (mínimo 2 horas, máximo 8 horas).
	 */
	public HoraTrabajoException() {
		super("No se puede trabajar más de las horas permitidas (2h minimo - 8h maximo)");
	}
}