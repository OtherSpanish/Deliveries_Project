package co.edu.unbosque.proyectomodulo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.proyectomodulo.entity.Admin;

/**
 * Repositorio JPA para la entidad {@link Admin}.
 * Extiende de {@link CrudRepository} proporcionando operaciones CRUD básicas
 * y agrega consultas personalizadas para la gestión de administradores.
 *
 * @version 1.0
 */
public interface AdminRepository extends CrudRepository<Admin, Long> {

	/**
	 * Busca un administrador por su nombre de usuario.
	 *
	 * @param usuario nombre de usuario a buscar.
	 * @return un {@link Optional} con el administrador encontrado,
	 *         o vacío si no existe ningún administrador con ese usuario.
	 */
	Optional<Admin> findByUsuario(String usuario);
}