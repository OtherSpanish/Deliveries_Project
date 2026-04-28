package co.edu.unbosque.proyectomodulo.entity;

/**
 * Enumeración que representa los tipos de cliente disponibles en el sistema.
 * El tipo de cliente determina el porcentaje de descuento aplicado al precio
 * de envío de sus paquetes.
 *
 * @version 1.0
 */
public enum TipoCliente {

	/** Cliente estándar. No aplica ningún descuento sobre el precio de envío. */
	NORMAL,

	/** Cliente premium. Aplica un descuento del 30% sobre el precio de envío. */
	PREMIUM
}