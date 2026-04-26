package co.edu.unbosque.proyectomodulo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.proyectomodulo.entity.Conductor;

/**
 * Repositorio JPA para la entidad {@link Conductor}.
 * Extiende de {@link CrudRepository} proporcionando operaciones CRUD básicas
 * y agrega consultas personalizadas para la gestión de conductores.
 *
 * @version 1.0
 */
public interface ConductorRepository extends CrudRepository<Conductor, Long> {

	/**
	 * Busca un conductor por su nombre de usuario.
	 *
	 * @param usuario nombre de usuario a buscar.
	 * @return un {@link Optional} con el conductor encontrado,
	 *         o vacío si no existe ningún conductor con ese usuario.
	 */
	Optional<Conductor> findByUsuario(String usuario);

}