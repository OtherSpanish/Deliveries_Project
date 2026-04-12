package co.edu.unbosque.proyectomodulo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un administrador del sistema.
 * Extiende de {@link Persona}, heredando los atributos de autenticación
 * como usuario y contraseña, e incorpora un código de verificación propio.
 *
 * <p>Se encuentra mapeada a la tabla {@code admin} en la base de datos.</p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "admin")
public class Admin extends Persona {

	/** Código de verificación único del administrador. */
	private String codigoAdmin;

	/**
	 * Constructor por defecto.
	 */
	public Admin() {
	}

	/**
	 * Constructor que inicializa el código de administrador.
	 *
	 * @param codigoAdmin código de verificación
	 */
	public Admin(String codigoAdmin) {
		super();
		this.codigoAdmin = codigoAdmin;
	}

	/**
	 * Constructor que inicializa todos los atributos del administrador.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @param codigoAdmin código de verificación
	 */
	public Admin(String usuario, String contrasenia, String codigoAdmin) {
		super(usuario, contrasenia);
		this.codigoAdmin = codigoAdmin;
	}

	/**
	 * Constructor que inicializa los datos de autenticación heredados.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 */
	public Admin(String usuario, String contrasenia) {
		super(usuario, contrasenia);
	}

	/**
	 * Obtiene el código de verificación del administrador.
	 *
	 * @return código de administrador
	 */
	public String getCodigoAdmin() {
		return codigoAdmin;
	}

	/**
	 * Establece el código de verificación del administrador.
	 *
	 * @param codigoAdmin nuevo código de verificación
	 */
	public void setCodigoAdmin(String codigoAdmin) {
		this.codigoAdmin = codigoAdmin;
	}

	/**
	 * Devuelve una representación en cadena del objeto,
	 * incluyendo los atributos heredados de {@link Persona}.
	 *
	 * @return cadena con los datos del administrador
	 */
	@Override
	public String toString() {
		return super.toString() + " |Admin| Código: " + codigoAdmin;
	}
}