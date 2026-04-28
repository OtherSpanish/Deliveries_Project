package co.edu.unbosque.proyectomodulo.dto;

/**
 * Enumeración que representa los posibles estados de un paquete
 * a lo largo de su ciclo de vida en el sistema de envíos.
 *
 * @version 1.0
 */
public enum EstadoPaquete {

	/** El paquete se encuentra almacenado en bodega, pendiente de despacho. */
	EN_BODEGA,

	/** El paquete ha sido despachado desde la bodega y está listo para su transporte. */
	DESPACHADO,

	/** El paquete está en camino hacia su destino final. */
	EN_CAMINO,

	/** El paquete ha sido entregado exitosamente al destinatario. */
	ENTREGADO
}