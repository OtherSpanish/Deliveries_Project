package co.edu.unbosque.proyectomodulo.dto;

/**
 * Enumeración que representa los tipos de paquete disponibles en el sistema.
 * El tipo de paquete determina el precio base del envío, el tiempo estimado
 * de entrega y las condiciones de manejo aplicables.
 *
 * @version 1.0
 */
public enum TipoPaquete {

	/**
	 * Paquete de tipo carta. Precio base: $10.000. Entrega al día siguiente a las
	 * 08:00. No aplica fragilidad ni refrigeración.
	 */
	CARTA,

	/**
	 * Paquete de tipo alimenticio. Precio base: $20.000. Entrega en un máximo de 6
	 * horas desde el momento del envío. Puede requerir refrigeración o manejo
	 * especial por fragilidad.
	 */
	ALIMENTICIO,

	/**
	 * Paquete de tipo no alimenticio. Precio base: $30.000. Entrega al día
	 * siguiente a las 08:00. Puede requerir refrigeración o manejo especial por
	 * fragilidad.
	 */
	NO_ALIMENTICIO
}