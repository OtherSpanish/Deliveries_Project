package co.edu.unbosque.proyectomodulo.dto;

import java.util.Objects;

/**
 * Objeto de Transferencia de Datos (DTO) para la entidad {@code Paquete}.
 * Permite transportar la información de los paquetes entre las capas de la
 * aplicación sin exponer directamente la entidad del modelo.
 *
 * @version 1.0
 */
public class PaqueteDTO {

	/** Identificador único del paquete. */
	private long id;

	/** Tipo de paquete (carta, alimenticios, no alimenticios). */
	private String tipoPaquete;

	/** Descripción del contenido del paquete. */
	private String contenido;

	/** Dirección de destino del paquete. */
	private String direccionDeEnvio;

	/** Tiempo estimado de entrega. */
	private String tiempoDeEnvio;

	/** Precio de envío con descuento aplicado. */
	private String precioEnvio;

	/**
	 * Constructor por defecto.
	 */
	public PaqueteDTO() {
	}

	/**
	 * Constructor que inicializa los atributos principales del paquete.
	 *
	 * @param tipoPaquete      tipo de paquete
	 * @param contenido        descripción del contenido
	 * @param direccionDeEnvio dirección de destino
	 * @param tiempoDeEnvio    tiempo estimado de entrega
	 * @param precioEnvio      precio de envío
	 */
	public PaqueteDTO(String tipoPaquete, String contenido, String direccionDeEnvio, String tiempoDeEnvio,
			String precioEnvio) {
		super();
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
	 * @param tipoPaquete nuevo tipo de paquete
	 */
	public void setTipoPaquete(String tipoPaquete) {
		this.tipoPaquete = tipoPaquete;
	}

	/**
	 * Obtiene el contenido del paquete.
	 *
	 * @return descripción del contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Establece el contenido del paquete.
	 *
	 * @param contenido nueva descripción
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
	 * @param tiempoDeEnvio nuevo tiempo de entrega
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
	 * Devuelve una representación en cadena del objeto.
	 *
	 * @return cadena con los datos del paquete formateados
	 */
	@Override
	public String toString() {
		return "=== |PaqueteDTO| === \n - id: " + id + "\n - Tipo de paquete: " + tipoPaquete + "\n - Contenido: "
				+ contenido + "\n - Direccion a enviar: " + direccionDeEnvio + "\n - Tiempo De Entrega :"
				+ tiempoDeEnvio + "\n - Precio de envio: " + precioEnvio + "\n================\n";
	}

	/**
	 * Calcula el código hash del objeto basado en sus atributos.
	 *
	 * @return valor hash del objeto
	 */
	@Override
	public int hashCode() {
		return Objects.hash(contenido, direccionDeEnvio, id, precioEnvio, tiempoDeEnvio, tipoPaquete);
	}

	/**
	 * Compara este objeto con otro para determinar si son iguales.
	 *
	 * @param obj objeto a comparar
	 * @return {@code true} si los objetos son iguales, {@code false} en caso contrario
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
		return Objects.equals(contenido, other.contenido) && Objects.equals(direccionDeEnvio, other.direccionDeEnvio)
				&& id == other.id && Objects.equals(precioEnvio, other.precioEnvio)
				&& Objects.equals(tiempoDeEnvio, other.tiempoDeEnvio) && Objects.equals(tipoPaquete, other.tipoPaquete);
	}
}