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

	/** Tipo de cliente (normal, premium, concurrente). */
	@Enumerated(EnumType.STRING)
	private TipoCliente tipoCliente;

	/**
	 * Constructor por defecto de {@code ClienteDTO}.
	 */
	public ClienteDTO() {
	}

	public ClienteDTO(String usuario, String contrasenia, String cedula, TipoCliente tipoCliente) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	@Override
	public String toString() {
		return "ClienteDTO [id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", cedula=" + cedula
				+ ", tipoCliente=" + tipoCliente + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cedula, contrasenia, id, tipoCliente, usuario);
	}

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
