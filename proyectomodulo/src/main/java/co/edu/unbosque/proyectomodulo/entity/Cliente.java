package co.edu.unbosque.proyectomodulo.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
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
	private TipoCliente tipoCliente;


	public Cliente() {
		super();
	}

	public Cliente(String cedula, TipoCliente tipoCliente) {
		super();
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	public Cliente(String usuario, String contrasenia, String cedula, TipoCliente tipoCliente) {
		super(usuario, contrasenia);
		this.cedula = cedula;
		this.tipoCliente = tipoCliente;
	}

	public Cliente(String usuario, String contrasenia) {
		super(usuario, contrasenia);

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
		return "Cliente [cedula=" + cedula + ", tipoCliente=" + tipoCliente + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(cedula, tipoCliente);
		return result;
	}

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