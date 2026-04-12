package co.edu.unbosque.proyectomodulo.dto;

import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Cliente. Se utiliza
 * para transportar información de clientes entre las capas de la aplicación sin
 * exponer directamente la entidad.
 *
 * @version 1.0
 */
public class ClienteDTO {

	/** Identificador único del cliente. */
	private long id;

	/** Nombre de usuario del cliente. */
	private String usuario;

	/** Contraseña del cliente. */
	private String contrasenia;

	/** Cédula de identidad del cliente. */
	private String cedula;

	/** Tipo de cliente (normal, premium, concurrente). */
	private String tipoCliente;

	/**
	 * Constructor por defecto de {@code ClienteDTO}.
	 */
	public ClienteDTO() {
	}

	/**
	 * Constructor con todos los campos del cliente.
	 *
	 * @param usuario     nombre de usuario del cliente.
	 * @param contrasenia contraseña del cliente.
	 * @param cedula      cédula de identidad del cliente.
	 * @param tipoCliente tipo de cliente (normal, premium, concurrente).
	 */
	public ClienteDTO(String usuario, String contrasenia, String cedula, String tipoCliente) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Obtiene el identificador único del cliente.
	 *
	 * @return id del cliente.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del cliente.
	 *
	 * @param id nuevo id del cliente.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de usuario del cliente.
	 *
	 * @return nombre de usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Establece el nombre de usuario del cliente.
	 *
	 * @param usuario nuevo nombre de usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtiene la contraseña del cliente.
	 *
	 * @return contraseña del cliente.
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * Establece la contraseña del cliente.
	 *
	 * @param contrasenia nueva contraseña.
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene la cédula de identidad del cliente.
	 *
	 * @return cédula del cliente.
	 */
	public String getCedula() {
		return cedula;
	}

	/**
	 * Establece la cédula de identidad del cliente.
	 *
	 * @param cedula nueva cédula del cliente.
	 */
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	/**
	 * Obtiene el tipo de cliente.
	 *
	 * @return tipo de cliente (normal, premium, concurrente).
	 */
	public String getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * Establece el tipo de cliente.
	 *
	 * @param tipoCliente nuevo tipo de cliente (normal, premium, concurrente).
	 */
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code ClienteDTO}.
	 *
	 * @return cadena con los datos del cliente formateados.
	 */
	@Override
	public String toString() {
		return "=== |ClienteDTO| === \n - Id: " + id + "\n - Usuario: " + usuario + "\n - Contraseña: " + contrasenia
				+ "\n - Cédula: " + cedula + "\n - Tipo de cliente: " + tipoCliente + "\n================\n";
	}

	/**
	 * Calcula el código hash del objeto {@code ClienteDTO} basado en todos sus campos.
	 *
	 * @return valor hash calculado a partir de {@code cedula}, {@code contrasenia},
	 *         {@code id}, {@code tipoCliente} y {@code usuario}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(cedula, contrasenia, id, tipoCliente, usuario);
	}

	/**
	 * Compara este objeto {@code ClienteDTO} con otro para determinar si son iguales.
	 * Dos instancias son iguales si todos sus campos coinciden.
	 *
	 * @param obj objeto a comparar.
	 * @return {@code true} si los objetos son iguales, {@code false} en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteDTO other = (ClienteDTO) obj;
		return Objects.equals(cedula, other.cedula) && Objects.equals(contrasenia, other.contrasenia) && id == other.id
				&& Objects.equals(tipoCliente, other.tipoCliente) && Objects.equals(usuario, other.usuario);
	}
}