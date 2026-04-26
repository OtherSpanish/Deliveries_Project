package co.edu.unbosque.proyectomodulo.dto;

import java.util.Objects;

/**
 * Objeto de Transferencia de Datos (DTO) para la entidad
 * {@code ManipuladorPaquete}. Permite transportar la información de los
 * manipuladores de paquetes entre las diferentes capas de la aplicación sin
 * exponer directamente la entidad del modelo.
 *
 * @version 1.0
 */
public class ManipuladorPaqueteDTO {

	/** Identificador único del manipulador de paquetes. */
	private long id;

	/** Nombre de usuario del manipulador. */
	private String usuario;

	/** Contraseña del manipulador. */
	private String contrasenia;

	/** Tiempo de trabajo del manipulador expresado en horas. */
	private int tiempoDeTrabajo;

	/**
	 * Constructor por defecto.
	 */
	public ManipuladorPaqueteDTO() {
		super();
	}

	/**
	 * Constructor que inicializa los atributos principales del manipulador.
	 *
	 * @param usuario         nombre de usuario del manipulador
	 * @param contrasenia     contraseña del manipulador
	 * @param tiempoDeTrabajo tiempo de trabajo en horas
	 */
	public ManipuladorPaqueteDTO(String usuario, String contrasenia, int tiempoDeTrabajo) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.tiempoDeTrabajo = tiempoDeTrabajo;
	}

	/**
	 * Obtiene el identificador del manipulador.
	 *
	 * @return identificador único
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador del manipulador.
	 *
	 * @param id nuevo identificador
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de usuario.
	 *
	 * @return nombre de usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Establece el nombre de usuario.
	 *
	 * @param usuario nuevo nombre de usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtiene la contraseña.
	 *
	 * @return contraseña del manipulador
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * Establece la contraseña.
	 *
	 * @param contrasenia nueva contraseña
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene el tiempo de trabajo en horas.
	 *
	 * @return tiempo de trabajo
	 */
	public int getTiempoDeTrabajo() {
		return tiempoDeTrabajo;
	}

	/**
	 * Establece el tiempo de trabajo en horas.
	 *
	 * @param tiempoDeTrabajo nuevo tiempo de trabajo
	 */
	public void setTiempoDeTrabajo(int tiempoDeTrabajo) {
		this.tiempoDeTrabajo = tiempoDeTrabajo;
	}

	/**
	 * Calcula el código hash del objeto basado en sus atributos.
	 *
	 * @return valor hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, id, tiempoDeTrabajo, usuario);
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales.
	 *
	 * @param obj objeto a comparar
	 * @return {@code true} si los objetos son iguales, {@code false} en caso
	 *         contrario
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManipuladorPaqueteDTO other = (ManipuladorPaqueteDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia) && id == other.id
				&& tiempoDeTrabajo == other.tiempoDeTrabajo && Objects.equals(usuario, other.usuario);
	}

	/**
	 * Devuelve una representación en cadena del objeto.
	 *
	 * @return cadena con los datos del manipulador formateados
	 */
	@Override
	public String toString() {
		return "=== |ManipuladorPaquetesDTO| === \n -ID: " + id + "\n - Usuario: " + usuario + "\n - Contrasenia: "
				+ contrasenia + "\n - Tiempo de trabajo: " + tiempoDeTrabajo + "horas " + "\n================\n";
	}
}
