package co.edu.unbosque.proyectomodulo.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@Enumerated(EnumType.STRING)
	private TipoPaquete tipoPaquete;

	/** Contenido del paquete. */
	private String contenido;

	/** Dirección de destino. */
	private String direccionDeEnvio;

	/** Tiempo estimado de entrega. */
	private LocalDateTime tiempoDeEnvio;

	/** Precio de envío. */
	private String precioEnvio;

	private String destinatario;

	
	
	@Enumerated(EnumType.STRING)
	private EstadoPaquete estadoPaquete;

	private String clientePaquete;

	private float pesoKg;

	private boolean esFragil;

	private boolean requiereRefrigeracion;

	public Paquete() {
		super();
	}

	public Paquete(TipoPaquete tipoPaquete, String contenido, String direccionDeEnvio, LocalDateTime tiempoDeEnvio,
			String precioEnvio, String destinatario, EstadoPaquete estadoPaquete, String clientePaquete, float pesoKg,
			boolean esFragil, boolean requiereRefrigeracion) {
		super();
		this.tipoPaquete = tipoPaquete;
		this.contenido = contenido;
		this.direccionDeEnvio = direccionDeEnvio;
		this.tiempoDeEnvio = tiempoDeEnvio;
		this.precioEnvio = precioEnvio;
		this.destinatario = destinatario;
		this.estadoPaquete = estadoPaquete;
		this.clientePaquete = clientePaquete;
		this.pesoKg = pesoKg;
		this.esFragil = esFragil;
		this.requiereRefrigeracion = requiereRefrigeracion;
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

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
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

	public float getPesoKg() {
		return pesoKg;
	}

	public void setPesoKg(float pesoKg) {
		this.pesoKg = pesoKg;
	}

	public boolean isEsFragil() {
		return esFragil;
	}

	public void setEsFragil(boolean esFragil) {
		this.esFragil = esFragil;
	}

	public boolean isRequiereRefrigeracion() {
		return requiereRefrigeracion;
	}

	public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
		this.requiereRefrigeracion = requiereRefrigeracion;
	}

	@Override
	public String toString() {
		return "Paquete [id=" + id + ", tipoPaquete=" + tipoPaquete + ", contenido=" + contenido + ", direccionDeEnvio="
				+ direccionDeEnvio + ", tiempoDeEnvio=" + tiempoDeEnvio + ", precioEnvio=" + precioEnvio
				+ ", destinatario=" + destinatario + ", estadoPaquete=" + estadoPaquete + ", clientePaquete="
				+ clientePaquete + ", pesoKg=" + pesoKg + ", esFragil=" + esFragil + ", requiereRefrigeracion="
				+ requiereRefrigeracion + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientePaquete, contenido, destinatario, direccionDeEnvio, esFragil, estadoPaquete, id,
				pesoKg, precioEnvio, requiereRefrigeracion, tiempoDeEnvio, tipoPaquete);
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
				&& Objects.equals(destinatario, other.destinatario)
				&& Objects.equals(direccionDeEnvio, other.direccionDeEnvio) && esFragil == other.esFragil
				&& estadoPaquete == other.estadoPaquete && Objects.equals(id, other.id)
				&& Float.floatToIntBits(pesoKg) == Float.floatToIntBits(other.pesoKg)
				&& Objects.equals(precioEnvio, other.precioEnvio)
				&& requiereRefrigeracion == other.requiereRefrigeracion
				&& Objects.equals(tiempoDeEnvio, other.tiempoDeEnvio) && tipoPaquete == other.tipoPaquete;
	}

}