package co.edu.unbosque.proyectomodulo.dto;

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

	/** Estado actual del paquete dentro de su ciclo de vida. */
	@Enumerated(EnumType.STRING)
	private EstadoPaquete estadoPaquete;

	/** Nombre de usuario del cliente propietario del paquete. */
	private String clientePaquete;

	/** Nombre del destinatario del paquete. */
	private String destinatario;

	/** Peso del paquete en kilogramos. */
	private float pesoKg;

	/** Indica si el paquete es frágil y requiere manejo especial. */
	private boolean esFragil;

	/** Indica si el paquete requiere condiciones de refrigeración durante el transporte. */
	private boolean requiereRefrigeracion;

	/**
	 * Constructor por defecto de {@code PaqueteDTO}.
	 */
	public PaqueteDTO() {
		super();
	}

	/**
	 * Constructor con parámetros de {@code PaqueteDTO}.
	 *
	 * @param tipoPaquete           tipo del paquete ({@code CARTA}, {@code ALIMENTICIO} o {@code NO_ALIMENTICIO}).
	 * @param contenido             descripción del contenido del paquete.
	 * @param direccionDeEnvio      dirección de destino del envío.
	 * @param tiempoDeEnvio         fecha y hora estimada de entrega.
	 * @param precioEnvio           precio de envío como cadena, con descuento ya aplicado.
	 * @param estadoPaquete         estado inicial del paquete ({@link EstadoPaquete}).
	 * @param clientePaquete        nombre de usuario del cliente propietario del paquete.
	 * @param destinatario          nombre del destinatario del paquete.
	 * @param pesoKg                peso del paquete en kilogramos.
	 * @param esFragil              {@code true} si el paquete requiere manejo especial por fragilidad.
	 * @param requiereRefrigeracion {@code true} si el paquete requiere refrigeración durante el transporte.
	 */
	public PaqueteDTO(TipoPaquete tipoPaquete, String contenido, String direccionDeEnvio, LocalDateTime tiempoDeEnvio,
			String precioEnvio, EstadoPaquete estadoPaquete, String clientePaquete, String destinatario, float pesoKg,
			boolean esFragil, boolean requiereRefrigeracion) {
		super();
		this.tipoPaquete = tipoPaquete;
		this.contenido = contenido;
		this.direccionDeEnvio = direccionDeEnvio;
		this.tiempoDeEnvio = tiempoDeEnvio;
		this.precioEnvio = precioEnvio;
		this.estadoPaquete = estadoPaquete;
		this.clientePaquete = clientePaquete;
		this.destinatario = destinatario;
		this.pesoKg = pesoKg;
		this.esFragil = esFragil;
		this.requiereRefrigeracion = requiereRefrigeracion;
	}

	/**
	 * Retorna el identificador único del paquete.
	 *
	 * @return identificador del paquete.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del paquete.
	 *
	 * @param id nuevo identificador del paquete.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retorna el tipo del paquete.
	 *
	 * @return tipo de paquete ({@code CARTA}, {@code ALIMENTICIO} o {@code NO_ALIMENTICIO}).
	 */
	public TipoPaquete getTipoPaquete() {
		return tipoPaquete;
	}

	/**
	 * Establece el tipo del paquete.
	 *
	 * @param tipoPaquete nuevo tipo de paquete.
	 */
	public void setTipoPaquete(TipoPaquete tipoPaquete) {
		this.tipoPaquete = tipoPaquete;
	}

	/**
	 * Retorna la descripción del contenido del paquete.
	 *
	 * @return contenido del paquete.
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Establece la descripción del contenido del paquete.
	 *
	 * @param contenido nueva descripción del contenido.
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * Retorna la dirección de destino del envío.
	 *
	 * @return dirección de envío.
	 */
	public String getDireccionDeEnvio() {
		return direccionDeEnvio;
	}

	/**
	 * Establece la dirección de destino del envío.
	 *
	 * @param direccionDeEnvio nueva dirección de envío.
	 */
	public void setDireccionDeEnvio(String direccionDeEnvio) {
		this.direccionDeEnvio = direccionDeEnvio;
	}

	/**
	 * Retorna la fecha y hora estimada de entrega del paquete.
	 *
	 * @return tiempo de envío estimado.
	 */
	public LocalDateTime getTiempoDeEnvio() {
		return tiempoDeEnvio;
	}

	/**
	 * Establece la fecha y hora estimada de entrega del paquete.
	 *
	 * @param tiempoDeEnvio nueva fecha y hora de entrega estimada.
	 */
	public void setTiempoDeEnvio(LocalDateTime tiempoDeEnvio) {
		this.tiempoDeEnvio = tiempoDeEnvio;
	}

	/**
	 * Retorna el precio de envío del paquete con el descuento ya aplicado.
	 *
	 * @return precio de envío como cadena de texto.
	 */
	public String getPrecioEnvio() {
		return precioEnvio;
	}

	/**
	 * Establece el precio de envío del paquete.
	 *
	 * @param precioEnvio nuevo precio de envío como cadena de texto.
	 */
	public void setPrecioEnvio(String precioEnvio) {
		this.precioEnvio = precioEnvio;
	}

	/**
	 * Retorna el estado actual del paquete dentro de su ciclo de vida.
	 *
	 * @return estado del paquete ({@link EstadoPaquete}).
	 */
	public EstadoPaquete getEstadoPaquete() {
		return estadoPaquete;
	}

	/**
	 * Establece el estado actual del paquete.
	 *
	 * @param estadoPaquete nuevo estado del paquete ({@link EstadoPaquete}).
	 */
	public void setEstadoPaquete(EstadoPaquete estadoPaquete) {
		this.estadoPaquete = estadoPaquete;
	}

	/**
	 * Retorna el nombre de usuario del cliente propietario del paquete.
	 *
	 * @return nombre de usuario del cliente.
	 */
	public String getClientePaquete() {
		return clientePaquete;
	}

	/**
	 * Establece el nombre de usuario del cliente propietario del paquete.
	 *
	 * @param clientePaquete nuevo nombre de usuario del cliente.
	 */
	public void setClientePaquete(String clientePaquete) {
		this.clientePaquete = clientePaquete;
	}

	/**
	 * Retorna el nombre del destinatario del paquete.
	 *
	 * @return nombre del destinatario.
	 */
	public String getDestinatario() {
		return destinatario;
	}

	/**
	 * Establece el nombre del destinatario del paquete.
	 *
	 * @param destinatario nuevo nombre del destinatario.
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * Retorna el peso del paquete en kilogramos.
	 *
	 * @return peso en kilogramos.
	 */
	public float getPesoKg() {
		return pesoKg;
	}

	/**
	 * Establece el peso del paquete en kilogramos.
	 *
	 * @param pesoKg nuevo peso en kilogramos.
	 */
	public void setPesoKg(float pesoKg) {
		this.pesoKg = pesoKg;
	}

	/**
	 * Indica si el paquete es frágil y requiere manejo especial.
	 *
	 * @return {@code true} si el paquete es frágil; {@code false} en caso contrario.
	 */
	public boolean isEsFragil() {
		return esFragil;
	}

	/**
	 * Establece si el paquete es frágil y requiere manejo especial.
	 *
	 * @param esFragil {@code true} si el paquete es frágil; {@code false} en caso contrario.
	 */
	public void setEsFragil(boolean esFragil) {
		this.esFragil = esFragil;
	}

	/**
	 * Indica si el paquete requiere condiciones de refrigeración durante el transporte.
	 *
	 * @return {@code true} si requiere refrigeración; {@code false} en caso contrario.
	 */
	public boolean isRequiereRefrigeracion() {
		return requiereRefrigeracion;
	}

	/**
	 * Establece si el paquete requiere condiciones de refrigeración durante el transporte.
	 *
	 * @param requiereRefrigeracion {@code true} si requiere refrigeración; {@code false} en caso contrario.
	 */
	public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
		this.requiereRefrigeracion = requiereRefrigeracion;
	}

	/**
	 * Retorna una representación en cadena del objeto {@code PaqueteDTO},
	 * incluyendo todos sus campos.
	 *
	 * @return cadena con los valores de los atributos del DTO.
	 */
	@Override
	public String toString() {
		return "PaqueteDTO [id=" + id + ", tipoPaquete=" + tipoPaquete + ", contenido=" + contenido
				+ ", direccionDeEnvio=" + direccionDeEnvio + ", tiempoDeEnvio=" + tiempoDeEnvio + ", precioEnvio="
				+ precioEnvio + ", estadoPaquete=" + estadoPaquete + ", clientePaquete=" + clientePaquete
				+ ", destinatario=" + destinatario + ", pesoKg=" + pesoKg + ", esFragil=" + esFragil
				+ ", requiereRefrigeracion=" + requiereRefrigeracion + "]";
	}

	/**
	 * Calcula el código hash del objeto a partir de todos sus atributos.
	 *
	 * @return código hash del {@code PaqueteDTO}.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(clientePaquete, contenido, destinatario, direccionDeEnvio, esFragil, estadoPaquete, id,
				pesoKg, precioEnvio, requiereRefrigeracion, tiempoDeEnvio, tipoPaquete);
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales. Dos instancias
	 * de {@code PaqueteDTO} son iguales si todos sus campos tienen el mismo valor.
	 * La comparación de {@code pesoKg} se realiza a nivel de bits para evitar
	 * inconsistencias propias de la aritmética de punto flotante.
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
		if (getClass() != obj.getClass())
			return false;
		PaqueteDTO other = (PaqueteDTO) obj;
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