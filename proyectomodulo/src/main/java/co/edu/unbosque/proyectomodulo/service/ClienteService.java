package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;


import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.dto.TipoCliente;
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
	private AdminService adminService;

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
			return 2;
		}
		try {
			LanzadorException.verificarCedulaValida(data.getCedula());
		} catch (CedulaException e) {
			return 1;
		}
		Cliente entity = mapper.map(data, Cliente.class);
		clienteRep.save(entity);
		ClienteDTO dto = mapper.map(entity, ClienteDTO.class);
        Gson gson = new Gson();
        String json = gson.toJson(dto);
		return 0;
	}

	/**
	 * Obtiene todos los clientes registrados.
	 * Requiere que haya un administrador autenticado.
	 *
	 * @return lista de clientes o {@code null} si no hay permisos
	 */
	@Override
	public String getAll() {
		Gson gson = new Gson();
		if (!adminService.isLoggedadmin()) {
			return null;
		}

		List<Cliente> entityList = (List<Cliente>) clienteRep.findAll();
		List<ClienteDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity ->
			dtoList.add(mapper.map(entity, ClienteDTO.class))
		);
		return gson.toJson(dtoList);
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
		if (!adminService.isLoggedadmin()) {
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
	public int updateById(Long id, ClienteDTO dataC) {

		Optional<Cliente> encontradoID = clienteRep.findById(id);
		Optional<Cliente> encontradoUsuario = clienteRep.findByUsuario(dataC.getUsuario());
		Optional<Cliente> encontradoCedula = clienteRep.findByCedula(dataC.getCedula());
		
		if(!adminService.isLoggedadmin()) {
			return 2;
		}
		if (encontradoUsuario.isPresent()) {
			return 3;
		} else if (encontradoCedula.isPresent()) {
			return 4;
		} else if (encontradoID.isPresent() && encontradoUsuario.isPresent() && encontradoCedula.isPresent()) {
			return 5;
		}

		if (encontradoID.isPresent() && !(encontradoUsuario.isPresent() && encontradoCedula.isPresent())) {

			ClienteDTO temp = mapper.map(encontradoID.get(), ClienteDTO.class);

			temp.setUsuario(dataC.getUsuario());
			temp.setContrasenia(dataC.getContrasenia());
			temp.setCedula(dataC.getCedula());
			temp.setTipoCliente(dataC.getTipoCliente());

			try {
				LanzadorException.verificarCedulaValida(dataC.getCedula());
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
	 * Verifica si existen clientes en el sistema.
	 *
	 * @return {@code 0} hay clientes,
	 *         {@code 2} lista vacía o sin permisos
	 */
	public int ObtenerTodo() {
		return 0;
	}

	/**
	 * Obtiene el cliente actualmente autenticado.
	 *
	 * @return cliente logueado o {@code null}
	 */
	public Cliente getClienteLogueado() {
		return clienteLogueado;
	}

	public String getClienteStatus(Long id) {
		Optional<Cliente> encontrado = clienteRep.findById(id);
		ClienteDTO temp = mapper.map(encontrado.get(), ClienteDTO.class);
		TipoCliente status = temp.getTipoCliente();
		return "" + status;
	}
	public String getClienteNombre(Long id) {
		Optional<Cliente> encontrado = clienteRep.findById(id);
		ClienteDTO temp = mapper.map(encontrado.get(), ClienteDTO.class);
		String status = temp.getUsuario();
		return status;
	}
}