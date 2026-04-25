package co.edu.unbosque.proyectomodulo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.proyectomodulo.entity.Paquete;
import jakarta.transaction.Transactional;

/**
 * Repositorio JPA para la entidad {@link Paquete}.
 * Extiende de {@link CrudRepository} proporcionando operaciones CRUD básicas
 * y agrega consultas personalizadas para la gestión de paquetes.
 *
 * @version 1.0
 */
public interface PaqueteRepository extends CrudRepository<Paquete, Long> {

	/**
	 * Busca todos los paquetes que correspondan a un tipo de paquete específico.
	 *
	 * @param tipoPaquete tipo de paquete a buscar (carta, alimenticios, no alimenticios).
	 * @return un {@link Optional} con la lista de paquetes encontrados,
	 *         o vacío si no existe ningún paquete con ese tipo.
	 */
	Optional<List<Paquete>> findByTipoPaquete(String tipoPaquete);
}