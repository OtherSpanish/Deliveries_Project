package co.edu.unbosque.proyectomodulo.dto;

import java.util.Objects;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

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
	/** Tipo de cliente (normal, premium). */
	@Enumerated(EnumType.STRING)
	private TipoCliente tipoCliente;

	/**
	 * Constructor por defecto de {@code ClienteDTO}.
	 */
	public ClienteDTO() {
	}

	/**
	 * Constructor con parámetros de {@code ClienteDTO}.
	 *
	 * @param usuario     nombre de usuario del cliente.
	 * @param contrasenia contraseña del cliente.
	 * @param cedula      cédula de identidad del cliente.
	 * @param tipoCliente tipo de cliente ({@code NORMAL}, {@code PREMIUM} o
	 *                    {@code CONCURRENTE}).
	 */
	public ClienteDTO(String usuario, String contrasenia, String cedula, TipoCliente tipoCliente) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Retorna el identificador único del cliente.
	 *
	 * @return identificador del cliente.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del cliente.
	 *
	 * @param id nuevo identificador del cliente.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Retorna el nombre de usuario del cliente.
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
	 * Retorna la contraseña del cliente.
	 *
	 * @return contraseña del cliente.
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * Establece la contraseña del cliente.
	 *
	 * @param contrasenia nueva contraseña del cliente.
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * Retorna la cédula de identidad del cliente.
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
	 * Retorna el tipo de cliente.
	 *
	 * @return tipo de cliente ({@code NORMAL} o {@code PREMIUM})
	 *        
	 */
	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * Establece el tipo de cliente.
	 *
	 * @param tipoCliente nuevo tipo de cliente ({@code NORMAL} o {@code PREMIUM})
	 *                   
	 */
	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code ClienteDTO},
	 * incluyendo todos sus campos.
	 *
	 * @return cadena con los valores de los atributos del DTO.
	 */
	@Override
	public String toString() {
		return "ClienteDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", cedula=" + cedula
				+ ", tipoCliente=" + tipoCliente + "]";
	}

	/**
	 * Calcula el código hash del objeto a partir de sus atributos principales.
	 *
	 * @return código hash del {@code ClienteDTO}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(cedula, contrasenia, id, tipoCliente, usuario);
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales. Dos instancias
	 * de {@code ClienteDTO} son iguales si todos sus campos ({@code id},
	 * {@code usuario}, {@code contrasenia}, {@code cedula} y {@code tipoCliente})
	 * tienen el mismo valor.
	 *
	 * @param obj objeto a comparar.
	 * @return {@code true} si los objetos son iguales; {@code false} en caso
	 *         contrario.
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