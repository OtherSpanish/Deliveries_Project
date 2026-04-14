package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.entity.Cliente;
import co.edu.unbosque.proyectomodulo.exceptions.CedulaException;
import co.edu.unbosque.proyectomodulo.exceptions.LanzadorException;
import co.edu.unbosque.proyectomodulo.repository.ClienteRepository;

/**
 * Servicio que gestiona las operaciones CRUD y de autenticación
 * para la entidad {@link Cliente}. Permite el registro, inicio y
 * cierre de sesión, así como la administración de los datos de clientes.
 *
 * @version 1.0
 */
@Service
public class ClienteService implements CRUDOPERATION<ClienteDTO> {

	/** Repositorio para acceso a datos de clientes. */
	@Autowired
	private ClienteRepository clienteRep;

	/** Mapper para conversión entre entidades y DTOs. */
	@Autowired
	private ModelMapper mapper;

	/** Servicio de administrador para validar permisos. */
	@Autowired
	private AdminService aService;

	/** Cliente actualmente autenticado en el sistema. */
	private Cliente clienteLogueado;

	/**
	 * Constructor por defecto.
	 */
	public ClienteService() {
	}

	/**
	 * Inicia sesión de un cliente validando sus credenciales.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @return {@code 0} éxito, {@code 1} credenciales inválidas
	 */
	public int login(String usuario, String contrasenia) {
		Optional<Cliente> encontrado = clienteRep.findByUsuario(usuario);

		if (encontrado.isPresent()
				&& encontrado.get().getContrasenia().equals(contrasenia)) {

			clienteLogueado = encontrado.get();
			return 0;
		}
		return 1;
	}

	/**
	 * Cierra la sesión del cliente actual.
	 */
	public void logout() {
		if (clienteLogueado != null) {
			clienteLogueado = null;
		}
	}

	/**
	 * Verifica si hay un cliente autenticado.
	 *
	 * @return {@code true} si hay sesión activa
	 */
	public boolean isLogged() {
		return clienteLogueado != null;
	}

	/**
	 * Crea un nuevo cliente validando su cédula y unicidad de usuario.
	 *
	 * @param data datos del cliente
	 * @return {@code 0} creado, {@code 1} error de validación
	 */
	@Override
	public int create(ClienteDTO data) {
		Optional<Cliente> encontrado = clienteRep.findByUsuario(data.getUsuario());

		if (encontrado.isPresent()) {
			return 1;
		}

		try {
			LanzadorException.verificarCedulaValida(data.getCedula());
		} catch (CedulaException e) {
			return 1;
		}

		Cliente entity = mapper.map(data, Cliente.class);
		clienteRep.save(entity);
		return 0;
	}

	/**
	 * Obtiene todos los clientes registrados.
	 * Requiere que haya un administrador autenticado.
	 *
	 * @return lista de clientes o {@code null} si no hay permisos
	 */
	@Override
	public List<ClienteDTO> getAll() {
		if (!aService.isLoggedadmin()) {
			return null;
		}

		List<Cliente> entityList = (List<Cliente>) clienteRep.findAll();
		List<ClienteDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity ->
			dtoList.add(mapper.map(entity, ClienteDTO.class))
		);

		return dtoList;
	}

	/**
	 * Elimina un cliente por su ID.
	 * Requiere autenticación de administrador.
	 *
	 * @param id identificador del cliente
	 * @return {@code 0} eliminado, {@code 1} no encontrado,
	 *         {@code 2} sin permisos
	 */
	@Override
	public int deleteById(Long id) {
		if (!aService.isLoggedadmin()) {
			return 2;
		}

		Optional<Cliente> encontrado = clienteRep.findById(id);

		if (encontrado.isPresent()) {
			ClienteDTO dto = mapper.map(encontrado.get(), ClienteDTO.class);
			Cliente entity = mapper.map(dto, Cliente.class);
			clienteRep.delete(entity);
			return 0;
		}
		return 1;
	}

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
	@Override
	public int updateById(Long id, ClienteDTO data) {

		Optional<Cliente> encontradoID = clienteRep.findById(id);
		Optional<Cliente> encontradoNombre = clienteRep.findByUsuario(data.getUsuario());
		Optional<Cliente> encontradoCedula = clienteRep.findByCedula(data.getCedula());

		if (encontradoNombre.isPresent() && !encontradoCedula.isPresent()) {
			return 3;
		} else if (!encontradoNombre.isPresent() && encontradoCedula.isPresent()) {
			return 4;
		} else if (encontradoID.isPresent()
				&& encontradoNombre.isPresent()
				&& encontradoCedula.isPresent()) {
			return 5;
		}

		if (encontradoID.isPresent()
				&& !(encontradoNombre.isPresent() && encontradoCedula.isPresent())) {

			ClienteDTO temp = mapper.map(encontradoID.get(), ClienteDTO.class);

			temp.setUsuario(data.getUsuario());
			temp.setContrasenia(data.getContrasenia());
			temp.setCedula(data.getCedula());
			temp.setTipoCliente(data.getTipoCliente());

			try {
				LanzadorException.verificarCedulaValida(data.getCedula());
			} catch (CedulaException e) {
				return 1;
			}

			Cliente entity = mapper.map(temp, Cliente.class);
			entity.setId(id);
			clienteRep.save(entity);

			return 0;
		}

		return 6;
	}

	/**
	 * Obtiene la cantidad total de clientes.
	 *
	 * @return número de clientes
	 */
	@Override
	public long count() {
		return clienteRep.count();
	}

	/**
	 * Verifica si existe un cliente por su ID.
	 *
	 * @param id identificador
	 * @return {@code true} si existe
	 */
	@Override
	public boolean exist(Long id) {
		return clienteRep.existsById(id);
	}

	/**
	 * Registra un nuevo cliente validando cédula, tipo y duplicados.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @param cedula      documento de identidad
	 * @param tipoCliente tipo (normal, premium)
	 * @return {@code 0} éxito,
	 *         {@code 1} datos inválidos,
	 *         {@code 2} usuario existente,
	 *         {@code 3} cliente ya logueado
	 */
	public int register(String usuario, String contrasenia, String cedula, String tipoCliente) {

		if (clienteLogueado != null) return 3;

		try {
			LanzadorException.verificarCedulaValida(cedula);
		} catch (CedulaException e) {
			return 1;
		}

		if (!(tipoCliente.equals("normal") || tipoCliente.equals("premium"))) {
			return 1;
		}

		Optional<Cliente> encontrado = clienteRep.findByUsuario(usuario);
		if (encontrado.isPresent()) return 2;

		Cliente nuevo = new Cliente(usuario, contrasenia, cedula, tipoCliente);
		clienteRep.save(nuevo);

		return 0;
	}

	/**
	 * Verifica si existen clientes en el sistema.
	 *
	 * @return {@code 0} hay clientes,
	 *         {@code 2} lista vacía o sin permisos
	 */
	public int ObtenerTodo() {
		List<ClienteDTO> encontrado = getAll();

		if (encontrado == null || encontrado.isEmpty()) {
			return 2;
		}
		return 0;
	}

	/**
	 * Busca clientes por tipo.
	 *
	 * @param tipoCliente tipo de cliente
	 * @return lista de clientes encontrados
	 */
	public List<ClienteDTO> findByTipoCliente(String tipoCliente) {

		Optional<List<Cliente>> encontrados = clienteRep.findByTipoCliente(tipoCliente);
		List<ClienteDTO> dtoList = new ArrayList<>();

		if (encontrados.isPresent() && !encontrados.get().isEmpty()) {
			encontrados.get().forEach(entity ->
				dtoList.add(mapper.map(entity, ClienteDTO.class))
			);
		}

		return dtoList;
	}

	/**
	 * Obtiene el cliente actualmente autenticado.
	 *
	 * @return cliente logueado o {@code null}
	 */
	public Cliente getClienteLogueado() {
		return clienteLogueado;
	}
}