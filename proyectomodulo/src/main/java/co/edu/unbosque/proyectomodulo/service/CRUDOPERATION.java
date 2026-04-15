package co.edu.unbosque.proyectomodulo.service;

/**
 * Interfaz genérica que define las operaciones CRUD estándar
 * para los servicios de la aplicación.
 * <p>
 * Cualquier servicio que implemente esta interfaz deberá proveer
 * la lógica correspondiente para crear, leer, actualizar y eliminar
 * entidades del tipo {@code T}.
 * </p>
 * 
 * 
 * @version 1.0
 * 
 *
 * @param <T> el tipo de dato (generalmente un DTO) sobre el cual se realizan las operaciones.
 */
public interface CRUDOPERATION<T> {

    /**
     * Crea y persiste un nuevo registro a partir de los datos proporcionados.
     *
     * @param data el objeto de tipo {@code T} con la información a registrar.
     * @return un código entero que indica el resultado de la operación
     *         ({@code 0} para éxito, valores distintos para diferentes tipos de error).
     */
    public int create(T data);

    /**
     * Obtiene todos los registros existentes del tipo {@code T}.
     *
     * @return una lista de objetos de tipo {@code T} con todos los registros encontrados,
     *         o una lista vacía si no hay registros disponibles.
     */
    public String getAll();

    /**
     * Elimina un registro identificado por su id.
     *
     * @param id el identificador único del registro a eliminar.
     * @return un código entero que indica el resultado de la operación
     *         ({@code 0} para éxito, valores distintos para diferentes tipos de error).
     */
    public int deleteById(Long id);

    /**
     * Retorna el número total de registros existentes del tipo {@code T}.
     *
     * @return la cantidad total de registros almacenados.
     */
    public long count();

    /**
     * Verifica si existe un registro con el identificador proporcionado.
     *
     * @param id el identificador único del registro a verificar.
     * @return {@code true} si el registro existe, {@code false} en caso contrario.
     */
    public boolean exist(Long id);
    
	public int updateById(Long id, T data);

	/**
	 * Actualiza un cliente existente validando datos y restricciones.
	 *
	 * @param id   identificador del cliente
	 * @param data nuevos datos
	 * @return códigos de estado:
	 *         {@code 0} éxito,
	 *         {@code 1} cédula inválida,
	 *         {@code 3-6} conflictos de datos
	 */

}