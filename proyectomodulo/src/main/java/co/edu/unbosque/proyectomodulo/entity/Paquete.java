package co.edu.unbosque.proyectomodulo.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa un paquete en el sistema. Contiene información
 * como tipo, contenido, dirección de envío, tiempo estimado de entrega y
 * precio.
 *
 * <p>
 * Se encuentra mapeada a la tabla {@code paquete} en la base de datos.
 * </p>
 *
 * @version 1.0
 */
@Entity
@Table(name = "paquete")
public class Paquete {

	/** Identificador único del paquete, generado automáticamente. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Tipo de paquete (carta, alimenticios, no alimenticios). */
	private TipoPaquete tipoPaquete;

	/** Contenido del paquete. */
	private String contenido;

	/** Dirección de destino. */
	private String direccionDeEnvio;

	/** Tiempo estimado de entrega. */
	private LocalDateTime tiempoDeEnvio;

	/** Precio de envío. */
	private String precioEnvio;

	private EstadoPaquete estadoPaquete;

	private String clientePaquete;


	public Paquete() {
		super();
	}

	public Paquete(TipoPaquete tipoPaquete, String contenido, String direccionDeEnvio,
			LocalDateTime tiempoDeEnvio, String precioEnvio, EstadoPaquete estadoPaquete, String clientePaquete) {
		super();
		this.tipoPaquete = tipoPaquete;
		this.contenido = contenido;
		this.direccionDeEnvio = direccionDeEnvio;
		this.tiempoDeEnvio = tiempoDeEnvio;
		this.precioEnvio = precioEnvio;
		this.estadoPaquete = estadoPaquete;
		this.clientePaquete = clientePaquete;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoPaquete getTipoPaquete() {
		return tipoPaquete;
	}

	public void setTipoPaquete(TipoPaquete tipoPaquete) {
		this.tipoPaquete = tipoPaquete;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getDireccionDeEnvio() {
		return direccionDeEnvio;
	}

	public void setDireccionDeEnvio(String direccionDeEnvio) {
		this.direccionDeEnvio = direccionDeEnvio;
	}

	public LocalDateTime getTiempoDeEnvio() {
		return tiempoDeEnvio;
	}

	public void setTiempoDeEnvio(LocalDateTime tiempoDeEnvio) {
		this.tiempoDeEnvio = tiempoDeEnvio;
	}

	public String getPrecioEnvio() {
		return precioEnvio;
	}

	public void setPrecioEnvio(String precioEnvio) {
		this.precioEnvio = precioEnvio;
	}

	public EstadoPaquete getEstadoPaquete() {
		return estadoPaquete;
	}

	public void setEstadoPaquete(EstadoPaquete estadoPaquete) {
		this.estadoPaquete = estadoPaquete;
	}

	public String getClientePaquete() {
		return clientePaquete;
	}

	public void setClientePaquete(String clientePaquete) {
		this.clientePaquete = clientePaquete;
	}

	/**
	 * Devuelve una representación en cadena del objeto.
	 *
	 * @return cadena con los datos del paquete
	 */
	@Override
	public String toString() {
		return "=== |Paquete| ===\n" + " - ID: " + id + "\n" + " - Tipo: " + tipoPaquete + "\n" + " - Contenido: "
				+ contenido + "\n" + " - Dirección: " + direccionDeEnvio + "\n" + " - Tiempo: " + tiempoDeEnvio + "\n"
				+ " - Precio: " + precioEnvio + "\n" + "================\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientePaquete, contenido, direccionDeEnvio, estadoPaquete, id, precioEnvio, tiempoDeEnvio,
				tipoPaquete);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paquete other = (Paquete) obj;
		return Objects.equals(clientePaquete, other.clientePaquete) && Objects.equals(contenido, other.contenido)
				&& Objects.equals(direccionDeEnvio, other.direccionDeEnvio)
				&& Objects.equals(estadoPaquete, other.estadoPaquete) && Objects.equals(id, other.id)
				&& Objects.equals(precioEnvio, other.precioEnvio) && Objects.equals(tiempoDeEnvio, other.tiempoDeEnvio)
				&& Objects.equals(tipoPaquete, other.tipoPaquete);
	}

	
	
}