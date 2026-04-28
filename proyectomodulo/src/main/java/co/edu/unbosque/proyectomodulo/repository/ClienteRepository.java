package co.edu.unbosque.proyectomodulo.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import co.edu.unbosque.proyectomodulo.entity.Cliente;
/**
 * Repositorio JPA para la entidad {@link Cliente}.
 * Extiende de {@link CrudRepository} proporcionando operaciones CRUD básicas
 * y agrega consultas personalizadas para la gestión de clientes.
 *
 * @version 1.0
 */
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
	/**
	 * Busca un cliente por su nombre de usuario.
	 *
	 * @param usuario nombre de usuario a buscar.
	 * @return un {@link Optional} con el cliente encontrado,
	 *         o vacío si no existe ningún cliente con ese usuario.
	 */
	Optional<Cliente> findByUsuario(String usuario);

	/**
	 * Busca todos los clientes que correspondan a un tipo de cliente específico.
	 *
	 * @param tipoCliente tipo de cliente a buscar (normal, premium, concurrente).
	 * @return un {@link Optional} con la lista de clientes encontrados,
	 *         o vacío si no existe ningún cliente con ese tipo.
	 */
	Optional<List<Cliente>> findByTipoCliente(String tipoCliente);

	/**
	 * Busca un cliente por su número de cédula de identidad.
	 *
	 * @param cedula número de cédula a buscar.
	 * @return un {@link Optional} con el cliente encontrado,
	 *         o vacío si no existe ningún cliente con esa cédula.
	 */
	Optional<Cliente> findByCedula(String cedula);
}