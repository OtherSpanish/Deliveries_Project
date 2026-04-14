package co.edu.unbosque.proyectomodulo.entity;

import java.util.Objects;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Clase base abstracta que representa una persona en el sistema.
 * Actúa como superclase para las entidades {@link Admin}, {@link Cliente},
 * {@link Conductor} y {@link ManipuladorPaquete}, proporcionando atributos
 * comunes como identificador, usuario y contraseña.
 *
 * <p>Al estar anotada con {@code @MappedSuperclass}, sus atributos se heredan
 * directamente en las tablas de las subclases sin generar una tabla propia.</p>
 *
 * @version 1.0
 */
@MappedSuperclass
public abstract class Persona{

	/** Identificador único, generado automáticamente. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/** Nombre de usuario. */
	private String usuario;

	/** Contraseña. */
	private String contrasenia;

	/**
	 * Constructor por defecto.
	 */
	public Persona() {
	}

	/**
	 * Constructor que inicializa los datos de autenticación.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 */
	public Persona(String usuario, String contrasenia) {
		this.usuario = usuario;
		this.contrasenia = contrasenia;
	}

	/**
	 * Obtiene el identificador.
	 *
	 * @return identificador único
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador.
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
	 * @return contraseña
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
	 * Calcula el código hash basado en el identificador.
	 *
	 * @return valor hash
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Compara este objeto con otro basado en su identificador.
	 *
	 * @param obj objeto a comparar
	 * @return {@code true} si tienen el mismo id, {@code false} en caso contrario
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		return id == other.id;
	}

	/**
	 * Devuelve una representación en cadena del objeto.
	 *
	 * @return cadena con los datos de la persona
	 */
	@Override
	public String toString() {
		return "=== |Persona| ===\n"
				+ " - ID: " + id + "\n"
				+ " - Usuario: " + usuario + "\n"
				+ " - Contraseña: " + contrasenia + "\n";
	}
}