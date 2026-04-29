package co.edu.unbosque.proyectomodulo.entity;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
/**
 * Entidad JPA que representa un cliente del sistema. Extiende de
 * {@link Persona}, heredando los atributos de autenticación como usuario y
 * contraseña, e incorpora información propia como la cédula y el tipo de
 * cliente.
 *
 * <p>
 * Se encuentra mapeada a la tabla {@code cliente} en la base de datos.
 * </p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

	/** Cédula de identidad del cliente. */
	private String cedula;

	/** Tipo de cliente (normal, premium, concurrente). */
	@Enumerated(EnumType.STRING)
	private TipoCliente tipoCliente;

	/**
	 * Constructor por defecto de {@code Cliente}.
	 */
	public Cliente() {
		super();
	}

	/**
	 * Constructor con cédula y tipo de cliente.
	 *
	 * @param cedula      cédula de identidad del cliente.
	 * @param tipoCliente tipo de cliente ({@code NORMAL}, {@code PREMIUM} o {@code CONCURRENTE}).
	 */
	public Cliente(String cedula, TipoCliente tipoCliente) {
		super();
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Constructor completo con todos los atributos del cliente.
	 *
	 * @param usuario     nombre de usuario del cliente.
	 * @param contrasenia contraseña del cliente.
	 * @param cedula      cédula de identidad del cliente.
	 * @param tipoCliente tipo de cliente ({@code NORMAL}, {@code PREMIUM} o {@code CONCURRENTE}).
	 */
	public Cliente(String usuario, String contrasenia, String cedula, TipoCliente tipoCliente) {
		super(usuario, contrasenia);
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Constructor con credenciales de autenticación heredadas de {@link Persona}.
	 *
	 * @param usuario     nombre de usuario del cliente.
	 * @param contrasenia contraseña del cliente.
	 */
	public Cliente(String usuario, String contrasenia) {
		super(usuario, contrasenia);
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
	 * @return tipo de cliente ({@code NORMAL}, {@code PREMIUM} o {@code CONCURRENTE}).
	 */
	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * Establece el tipo de cliente.
	 *
	 * @param tipoCliente nuevo tipo de cliente ({@code NORMAL}, {@code PREMIUM} o {@code CONCURRENTE}).
	 */
	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code Cliente},
	 * incluyendo la cédula y el tipo de cliente.
	 *
	 * @return cadena con los valores de los atributos propios de la entidad.
	 */
	@Override
	public String toString() {
		return "Cliente [cedula=" + cedula + ", tipoCliente=" + tipoCliente + "]";
	}

	/**
	 * Calcula el código hash del objeto combinando el hash de la superclase
	 * {@link Persona} con el de los atributos propios ({@code cedula} y
	 * {@code tipoCliente}).
	 *
	 * @return código hash del {@code Cliente}.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(cedula, tipoCliente);
		return result;
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales. Dos instancias
	 * de {@code Cliente} son iguales si la superclase {@link Persona} los considera
	 * iguales y además comparten la misma {@code cedula} y {@code tipoCliente}.
	 *
	 * @param obj objeto a comparar.
	 * @return {@code true} si los objetos son iguales; {@code false} en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(cedula, other.cedula) && Objects.equals(tipoCliente, other.tipoCliente);
	}
}