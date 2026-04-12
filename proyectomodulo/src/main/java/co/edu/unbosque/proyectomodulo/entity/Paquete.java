package co.edu.unbosque.proyectomodulo.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un paquete en el sistema.
 * Contiene información como tipo, contenido, dirección de envío,
 * tiempo estimado de entrega y precio.
 *
 * <p>Se encuentra mapeada a la tabla {@code paquete} en la base de datos.</p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "paquete")
public class Paquete {

	/** Identificador único del paquete, generado automáticamente. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/** Tipo de paquete (carta, alimenticios, no alimenticios). */
	private String tipoPaquete;

	/** Contenido del paquete. */
	private String contenido;

	/** Dirección de destino. */
	private String direccionDeEnvio;

	/** Tiempo estimado de entrega. */
	private String tiempoDeEnvio;

	/** Precio de envío. */
	private String precioEnvio;

	/**
	 * Constructor por defecto.
	 */
	public Paquete() {
	}

	/**
	 * Constructor que inicializa los atributos principales del paquete.
	 *
	 * @param tipoPaquete      tipo de paquete
	 * @param contenido        contenido
	 * @param direccionDeEnvio dirección de destino
	 * @param tiempoDeEnvio    tiempo estimado de entrega
	 * @param precioEnvio      precio de envío
	 */
	public Paquete(String tipoPaquete, String contenido, String direccionDeEnvio, String tiempoDeEnvio,
			String precioEnvio) {
		this.tipoPaquete = tipoPaquete;
		this.contenido = contenido;
		this.direccionDeEnvio = direccionDeEnvio;
		this.tiempoDeEnvio = tiempoDeEnvio;
		this.precioEnvio = precioEnvio;
	}

	/**
	 * Obtiene el identificador del paquete.
	 *
	 * @return identificador único
	 */
	public long getId() {
		return id;
	}

	/**
	 * Establece el identificador del paquete.
	 *
	 * @param id nuevo identificador
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Obtiene el tipo de paquete.
	 *
	 * @return tipo de paquete
	 */
	public String getTipoPaquete() {
		return tipoPaquete;
	}

	/**
	 * Establece el tipo de paquete.
	 *
	 * @param tipoPaquete nuevo tipo
	 */
	public void setTipoPaquete(String tipoPaquete) {
		this.tipoPaquete = tipoPaquete;
	}

	/**
	 * Obtiene el contenido del paquete.
	 *
	 * @return contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Establece el contenido del paquete.
	 *
	 * @param contenido nuevo contenido
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * Obtiene la dirección de envío.
	 *
	 * @return dirección de destino
	 */
	public String getDireccionDeEnvio() {
		return direccionDeEnvio;
	}

	/**
	 * Establece la dirección de envío.
	 *
	 * @param direccionDeEnvio nueva dirección
	 */
	public void setDireccionDeEnvio(String direccionDeEnvio) {
		this.direccionDeEnvio = direccionDeEnvio;
	}

	/**
	 * Obtiene el tiempo estimado de entrega.
	 *
	 * @return tiempo de entrega
	 */
	public String getTiempoDeEnvio() {
		return tiempoDeEnvio;
	}

	/**
	 * Establece el tiempo estimado de entrega.
	 *
	 * @param tiempoDeEnvio nuevo tiempo
	 */
	public void setTiempoDeEnvio(String tiempoDeEnvio) {
		this.tiempoDeEnvio = tiempoDeEnvio;
	}

	/**
	 * Obtiene el precio de envío.
	 *
	 * @return precio de envío
	 */
	public String getPrecioEnvio() {
		return precioEnvio;
	}

	/**
	 * Establece el precio de envío.
	 *
	 * @param precioEnvio nuevo precio
	 */
	public void setPrecioEnvio(String precioEnvio) {
		this.precioEnvio = precioEnvio;
	}

	/**
	 * Calcula el código hash basado en el identificador único.
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
		Paquete other = (Paquete) obj;
		return id == other.id;
	}

	/**
	 * Devuelve una representación en cadena del objeto.
	 *
	 * @return cadena con los datos del paquete
	 */
	@Override
	public String toString() {
		return "=== |Paquete| ===\n"
				+ " - ID: " + id + "\n"
				+ " - Tipo: " + tipoPaquete + "\n"
				+ " - Contenido: " + contenido + "\n"
				+ " - Dirección: " + direccionDeEnvio + "\n"
				+ " - Tiempo: " + tiempoDeEnvio + "\n"
				+ " - Precio: " + precioEnvio + "\n"
				+ "================\n";
	}
}