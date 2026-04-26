package co.edu.unbosque.proyectomodulo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.proyectomodulo.entity.ManipuladorPaquete;


/**
 * Repositorio JPA para la entidad {@link ManipuladorPaquete}.
 * Extiende de {@link CrudRepository}, proporcionando operaciones CRUD básicas
 * y consultas derivadas para la gestión de manipuladores.
 *
 * @version 1.0
 */
public interface ManipuladorPaqueteRepository extends CrudRepository<ManipuladorPaquete, Long> {

	/**
	 * Busca un manipulador de paquetes por su nombre de usuario.
	 *
	 * @param usuario nombre de usuario
	 * @return {@link Optional} con el manipulador encontrado, o vacío si no existe
	 */
	Optional<ManipuladorPaquete> findByUsuario(String usuario);
	
	

}