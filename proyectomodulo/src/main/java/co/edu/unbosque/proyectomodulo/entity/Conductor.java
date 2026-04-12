package co.edu.unbosque.proyectomodulo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un conductor del sistema.
 * Extiende de {@link Persona}, heredando los atributos de autenticación
 * como usuario y contraseña, e incorpora el tipo de vehículo que maneja.
 *
 * <p>Se encuentra mapeada a la tabla {@code conductor} en la base de datos.</p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "conductor")
public class Conductor extends Persona {

	/** Tipo de vehículo del conductor (carro, moto, camión). */
	private String tipoVehiculo;

	/**
	 * Constructor por defecto.
	 */
	public Conductor() {
	}

	/**
	 * Constructor que inicializa el tipo de vehículo.
	 *
	 * @param tipoVehiculo tipo de vehículo
	 */
	public Conductor(String tipoVehiculo) {
		super();
		this.tipoVehiculo = tipoVehiculo;
	}

	/**
	 * Constructor que inicializa todos los atributos del conductor.
	 *
	 * @param usuario      nombre de usuario
	 * @param contrasenia  contraseña
	 * @param tipoVehiculo tipo de vehículo
	 */
	public Conductor(String usuario, String contrasenia, String tipoVehiculo) {
		super(usuario, contrasenia);
		this.tipoVehiculo = tipoVehiculo;
	}

	/**
	 * Constructor que inicializa los datos de autenticación heredados.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 */
	public Conductor(String usuario, String contrasenia) {
		super(usuario, contrasenia);
	}

	/**
	 * Obtiene el tipo de vehículo del conductor.
	 *
	 * @return tipo de vehículo
	 */
	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	/**
	 * Establece el tipo de vehículo del conductor.
	 *
	 * @param tipoVehiculo nuevo tipo de vehículo
	 */
	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	/**
	 * Devuelve una representación en cadena del objeto,
	 * incluyendo los atributos heredados de {@link Persona}.
	 *
	 * @return cadena con los datos del conductor
	 */
	@Override
	public String toString() {
		return super.toString() + " |Conductor| Tipo de vehículo: " + tipoVehiculo;
	}
}