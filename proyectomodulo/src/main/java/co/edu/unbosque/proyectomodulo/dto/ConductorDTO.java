package co.edu.unbosque.proyectomodulo.dto;

import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) para la entidad Conductor. Se utiliza
 * para transportar información de conductores entre las capas de la aplicación
 * sin exponer directamente la entidad.
 *
 * @version 1.0
 */
public class ConductorDTO {

	/** Identificador único del conductor. */
	private long id;

	/** Nombre de usuario del conductor. */
	private String usuario;

	/** Contraseña del conductor. */
	private String contrasenia;

	/** Tipo de vehículo del conductor (carro, moto, camion). */
	private String tipoVehiculo;

	/**
	 * Constructor por defecto de {@code ConductorDTO}.
	 */
	public ConductorDTO() {
	}

	/**
	 * Constructor con todos los campos del conductor.
	 *
	 * @param usuario      nombre de usuario del conductor.
	 * @param contrasenia  contraseña del conductor.
	 * @param tipoVehiculo tipo de vehículo (carro, moto, camion).
	 */
	public ConductorDTO(String usuario, String contrasenia, String tipoVehiculo) {
		super();
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.tipoVehiculo = tipoVehiculo;
	}

	/**
	 * Obtiene el identificador único del conductor.
	 *
	 * @return id del conductor.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del conductor.
	 *
	 * @param id nuevo id del conductor.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de usuario del conductor.
	 *
	 * @return nombre de usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Establece el nombre de usuario del conductor.
	 *
	 * @param usuario nuevo nombre de usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtiene la contraseña del conductor.
	 *
	 * @return contraseña del conductor.
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * Establece la contraseña del conductor.
	 *
	 * @param contrasenia nueva contraseña.
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene el tipo de vehículo del conductor.
	 *
	 * @return tipo de vehículo (carro, moto, camion).
	 */
	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	/**
	 * Establece el tipo de vehículo del conductor.
	 *
	 * @param tipoVehiculo nuevo tipo de vehículo (carro, moto, camion).
	 */
	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code ConductorDTO}.
	 *
	 * @return cadena con los datos del conductor formateados.
	 */
	@Override
	public String toString() {
		return "=== |ConductorDTO| === \n - Id: " + id + "\n - Usuario: " + usuario + "\n - Contraseña: " + contrasenia
				+ "\n - Tipo de vehículo: " + tipoVehiculo + "\n================\n";
	}

	/**
	 * Calcula el código hash del objeto {@code ConductorDTO} basado en todos sus
	 * campos.
	 *
	 * @return valor hash calculado a partir de {@code contrasenia}, {@code id},
	 *         {@code tipoVehiculo} y {@code usuario}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contrasenia, id, tipoVehiculo, usuario);
	}

	/**
	 * Compara este objeto {@code ConductorDTO} con otro para determinar si son
	 * iguales. Dos instancias son iguales si todos sus campos coinciden.
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
		ConductorDTO other = (ConductorDTO) obj;
		return Objects.equals(contrasenia, other.contrasenia) && id == other.id
				&& Objects.equals(tipoVehiculo, other.tipoVehiculo) && Objects.equals(usuario, other.usuario);
	}
}
