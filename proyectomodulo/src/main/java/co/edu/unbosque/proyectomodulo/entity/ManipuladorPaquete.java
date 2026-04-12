package co.edu.unbosque.proyectomodulo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un manipulador de paquetes del sistema.
 * Extiende de {@link Persona}, heredando los atributos de autenticación
 * como usuario y contraseña, e incorpora el tiempo de trabajo.
 *
 * <p>Se encuentra mapeada a la tabla {@code manipulador} en la base de datos.</p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "manipulador")
public class ManipuladorPaquete extends Persona {

	/** Tiempo de trabajo en horas. */
	private int tiempoDeTrabajo;

	/**
	 * Constructor por defecto.
	 */
	public ManipuladorPaquete() {
		super();
	}

	/**
	 * Constructor que inicializa el tiempo de trabajo.
	 *
	 * @param tiempoDeTrabajo tiempo de trabajo en horas
	 */
	public ManipuladorPaquete(int tiempoDeTrabajo) {
		super();
		this.tiempoDeTrabajo = tiempoDeTrabajo;
	}

	/**
	 * Constructor que inicializa todos los atributos del manipulador.
	 *
	 * @param usuario         nombre de usuario
	 * @param contrasenia     contraseña
	 * @param tiempoDeTrabajo tiempo de trabajo en horas
	 */
	public ManipuladorPaquete(String usuario, String contrasenia, int tiempoDeTrabajo) {
		super(usuario, contrasenia);
		this.tiempoDeTrabajo = tiempoDeTrabajo;
	}

	/**
	 * Constructor que inicializa los datos de autenticación heredados.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 */
	public ManipuladorPaquete(String usuario, String contrasenia) {
		super(usuario, contrasenia);
	}

	/**
	 * Obtiene el tiempo de trabajo.
	 *
	 * @return tiempo de trabajo en horas
	 */
	public int getTiempoDeTrabajo() {
		return tiempoDeTrabajo;
	}

	/**
	 * Establece el tiempo de trabajo.
	 *
	 * @param tiempoDeTrabajo nuevo tiempo de trabajo
	 */
	public void setTiempoDeTrabajo(int tiempoDeTrabajo) {
		this.tiempoDeTrabajo = tiempoDeTrabajo;
	}

	/**
	 * Devuelve una representación en cadena del objeto,
	 * incluyendo los atributos heredados de {@link Persona}.
	 *
	 * @return cadena con los datos del manipulador
	 */
	@Override
	public String toString() {
		return super.toString() + " |Manipulador| \n - Tiempo de trabajo: " + tiempoDeTrabajo;
	}
}