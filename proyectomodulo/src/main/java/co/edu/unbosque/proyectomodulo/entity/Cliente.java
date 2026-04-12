package co.edu.unbosque.proyectomodulo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un cliente del sistema.
 * Extiende de {@link Persona}, heredando los atributos de autenticación
 * como usuario y contraseña, e incorpora información propia como la cédula
 * y el tipo de cliente.
 *
 * <p>Se encuentra mapeada a la tabla {@code cliente} en la base de datos.</p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

	/** Cédula de identidad del cliente. */
	private String cedula;

	/** Tipo de cliente (normal, premium, concurrente). */
	private String tipoCliente;

	/**
	 * Constructor por defecto.
	 */
	public Cliente() {
	}

	/**
	 * Constructor que inicializa la cédula y el tipo de cliente.
	 *
	 * @param cedula      cédula de identidad
	 * @param tipoCliente tipo de cliente
	 */
	public Cliente(String cedula, String tipoCliente) {
		super();
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Constructor que inicializa todos los atributos del cliente.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @param cedula      cédula de identidad
	 * @param tipoCliente tipo de cliente
	 */
	public Cliente(String usuario, String contrasenia, String cedula, String tipoCliente) {
		super(usuario, contrasenia);
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Obtiene la cédula del cliente.
	 *
	 * @return cédula de identidad
	 */
	public String getCedula() {
		return cedula;
	}

	/**
	 * Establece la cédula del cliente.
	 *
	 * @param cedula nueva cédula
	 */
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	/**
	 * Obtiene el tipo de cliente.
	 *
	 * @return tipo de cliente
	 */
	public String getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * Establece el tipo de cliente.
	 *
	 * @param tipoCliente nuevo tipo de cliente
	 */
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Devuelve una representación en cadena del objeto,
	 * incluyendo los atributos heredados de {@link Persona}.
	 *
	 * @return cadena con los datos del cliente
	 */
	@Override
	public String toString() {
		return super.toString() + " |Cliente| \n - Cedula: " + cedula + "\n - Tipo de cliente: " + tipoCliente;
	}
}