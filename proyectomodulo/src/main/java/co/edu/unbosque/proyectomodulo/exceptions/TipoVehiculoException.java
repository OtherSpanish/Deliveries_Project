package co.edu.unbosque.proyectomodulo.exceptions;

/**
 * Excepción personalizada que se lanza cuando el tipo de vehículo ingresado
 * no cumple con los criterios de validación requeridos.
 *
 * <p>Los tipos de vehículo válidos son: carro, moto y camion.</p>
 *
 * <p>Extiende de {@link Exception} para ser tratada como una
 * excepción verificada en tiempo de compilación.</p>
 *
 * @version 1.0
 */
public class TipoVehiculoException extends Exception {

	/**
	 * Constructor por defecto de {@code TipoVehiculoException}.
	 * Inicializa la excepción con el mensaje {@code "Ingrese un tipo de vehículo válido"}.
	 */
	public TipoVehiculoException() {
		super("Ingrese un tipo de vehículo válido");
	}
}