package co.edu.unbosque.proyectomodulo.dto;

import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Admin. Se utiliza para
 * transportar información de administradores entre las capas de la aplicación
 * sin exponer directamente la entidad.
 *
 * @version 1.0
 */
public class AdminDTO {

	/** Identificador único del administrador. */
	private long id;

	/** Nombre de usuario del administrador. */
	private String usuario;

	/** Contraseña del administrador. */
	private String contrasenia;

	/** Código de verificación del administrador. */
	private String codigoAdmin;

	/**
	 * Constructor por defecto de {@code AdminDTO}.
	 */
	public AdminDTO() {
	}

	/**
	 * Constructor con los campos básicos de autenticación.
	 *
	 * @param usuario     nombre de usuario del administrador.
	 * @param contrasenia contraseña del administrador.
	 */
	public AdminDTO(String usuario, String contrasenia) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene el identificador único del administrador.
	 *
	 * @return id del administrador.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del administrador.
	 *
	 * @param id nuevo id del administrador.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de usuario del administrador.
	 *
	 * @return nombre de usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Establece el nombre de usuario del administrador.
	 *
	 * @param usuario nuevo nombre de usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtiene la contraseña del administrador.
	 *
	 * @return contraseña del administrador.
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * Establece la contraseña del administrador.
	 *
	 * @param contrasenia nueva contraseña.
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene el código de verificación del administrador.
	 *
	 * @return código de administrador.
	 */
	public String getCodigoAdmin() {
		return codigoAdmin;
	}

	/**
	 * Establece el código de verificación del administrador.
	 *
	 * @param codigoAdmin nuevo código de administrador.
	 */
	public void setCodigoAdmin(String codigoAdmin) {
		this.codigoAdmin = codigoAdmin;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code AdminDTO}.
	 *
	 * @return cadena con los datos del administrador formateados.
	 */
	@Override
	public String toString() {
		return " === |AdminDTO| === \n Id: " + id + "\n - Usuario: " + usuario + "\n - Contraseña: " + contrasenia
				+ "\n - Código de admin: " + codigoAdmin + "\n================\n";
	}

	/**
	 * Calcula el código hash del objeto {@code AdminDTO} basado en todos sus
	 * campos.
	 *
	 * @return valor hash calculado a partir de {@code codigoAdmin},
	 *         {@code contrasenia}, {@code id} y {@code usuario}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigoAdmin, contrasenia, id, usuario);
	}

	/**
	 * Compara este objeto {@code AdminDTO} con otro para determinar si son iguales.
	 * Dos instancias son iguales si todos sus campos coinciden.
	 *
	 * @param obj objeto a comparar.
	 * @return {@code true} si los objetos son iguales, {@code false} en caso
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
		AdminDTO other = (AdminDTO) obj;
		return Objects.equals(codigoAdmin, other.codigoAdmin) && Objects.equals(contrasenia, other.contrasenia)
				&& id == other.id && Objects.equals(usuario, other.usuario);
	}
}