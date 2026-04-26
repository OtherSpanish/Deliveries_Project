package co.edu.unbosque.proyectomodulo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Objeto de Transferencia de Datos (DTO) para la entidad {@code Paquete}.
 * Permite transportar la información de los paquetes entre las capas de la
 * aplicación sin exponer directamente la entidad del modelo.
 *
 * @version 1.0
 */
public class PaqueteDTO {

	/** Identificador único del paquete. */
	private Long id;

	/** Tipo de paquete (carta, alimenticios, no alimenticios). */
	@Enumerated(EnumType.STRING)
	private TipoPaquete tipoPaquete;

	/** Descripción del contenido del paquete. */
	private String contenido;

	/** Dirección de destino del paquete. */
	private String direccionDeEnvio;

	/** Tiempo estimado de entrega. */
	private LocalDateTime tiempoDeEnvio;

	/** Precio de envío con descuento aplicado. */
	private String precioEnvio;
	
	@Enumerated(EnumType.STRING)
	private EstadoPaquete estadoPaquete;

	private String clientePaquete;

	public PaqueteDTO() {
		super();
	}

	public PaqueteDTO(TipoPaquete tipoPaquete, String contenido, String direccionDeEnvio, LocalDateTime tiempoDeEnvio,
			String precioEnvio, EstadoPaquete estadoPaquete, String clientePaquete) {
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

	@Override
	public String toString() {
		return "PaqueteDTO [id=" + id + ", tipoPaquete=" + tipoPaquete + ", contenido=" + contenido
				+ ", direccionDeEnvio=" + direccionDeEnvio + ", tiempoDeEnvio=" + tiempoDeEnvio + ", precioEnvio="
				+ precioEnvio + ", estadoPaquete=" + estadoPaquete + ", clientePaquete=" + clientePaquete + ", getId()="
				+ getId() + ", getTipoPaquete()=" + getTipoPaquete() + ", getContenido()=" + getContenido()
				+ ", getDireccionDeEnvio()=" + getDireccionDeEnvio() + ", getTiempoDeEnvio()=" + getTiempoDeEnvio()
				+ ", getPrecioEnvio()=" + getPrecioEnvio() + ", getEstadoPaquete()=" + getEstadoPaquete()
				+ ", getClientePaquete()=" + getClientePaquete() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
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
		PaqueteDTO other = (PaqueteDTO) obj;
		return Objects.equals(clientePaquete, other.clientePaquete) && Objects.equals(contenido, other.contenido)
				&& Objects.equals(direccionDeEnvio, other.direccionDeEnvio)
				&& Objects.equals(estadoPaquete, other.estadoPaquete) && Objects.equals(id, other.id)
				&& Objects.equals(precioEnvio, other.precioEnvio) && Objects.equals(tiempoDeEnvio, other.tiempoDeEnvio)
				&& Objects.equals(tipoPaquete, other.tipoPaquete);
	}

}