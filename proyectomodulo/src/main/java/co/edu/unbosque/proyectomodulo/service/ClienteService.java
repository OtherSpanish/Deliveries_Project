package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.entity.Cliente;
import co.edu.unbosque.proyectomodulo.exceptions.CedulaException;
import co.edu.unbosque.proyectomodulo.exceptions.LanzadorException;
import co.edu.unbosque.proyectomodulo.repository.ClienteRepository;

/**
 * Servicio que gestiona las operaciones CRUD y autenticación para
 * {@link Cliente}.
 *
 * @version 1.0
 */
@Service
public class ClienteService implements CRUDOPERATION<ClienteDTO> {

	/**
	 * Repositorio JPA para el acceso a datos de la entidad {@link Cliente}.
	 */
	@Autowired
	private ClienteRepository clienteRep;

	/**
	 * Mapper utilizado para convertir entre entidades {@link Cliente} y
	 * objetos de transferencia {@link ClienteDTO}.
	 */
	@Autowired
	private ModelMapper mapper;

	/**
	 * Servicio de administración, utilizado para verificar si hay un admin
	 * con sesión activa antes de ejecutar operaciones restringidas.
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * Referencia al cliente que tiene sesión activa en el sistema.
	 * Es {@code null} cuando no hay ningún cliente autenticado.
	 */
	private Cliente clienteLogueado;

	/**
	 * Constructor por defecto de {@code ClienteService}.
	 */
	public ClienteService() {
	}

	/**
	 * Autentica a un cliente verificando su nombre de usuario y contraseña.
	 * Si las credenciales son correctas, establece al cliente como logueado.
	 *
	 * @param usuario     nombre de usuario del cliente.
	 * @param contrasenia contraseña del cliente.
	 * @return {@code 0} si el login fue exitoso; {@code 1} si las credenciales
	 *         son incorrectas o el usuario no existe.
	 */
	public int login(String usuario, String contrasenia) {
		Optional<Cliente> encontrado = clienteRep.findByUsuario(usuario);
		if (encontrado.isPresent() && encontrado.get().getContrasenia().equals(contrasenia)) {
			clienteLogueado = encontrado.get();
			return 0;
		}
		return 1;
	}

	/**
	 * Cierra la sesión del cliente actualmente autenticado,
	 * estableciendo {@code clienteLogueado} en {@code null}.
	 */
	public void logout() {
		clienteLogueado = null;
	}

	/**
	 * Indica si hay un cliente con sesión activa en el sistema.
	 *
	 * @return {@code true} si hay un cliente logueado; {@code false} en caso contrario.
	 */
	public boolean isLogged() {
		return clienteLogueado != null;
	}

	/**
	 * Registra un nuevo cliente en el sistema. Valida que el nombre de usuario
	 * no esté en uso y que la cédula tenga un formato válido.
	 *
	 * @param data {@link ClienteDTO} con los datos del cliente a registrar.
	 * @return {@code 0} si el cliente fue creado exitosamente;
	 *         {@code 1} si la cédula es inválida;
	 *         {@code 2} si el nombre de usuario ya está registrado.
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
		return 0;
	}

	/**
	 * Retorna la lista completa de clientes registrados en formato JSON.
	 * Solo disponible si hay un administrador con sesión activa.
	 *
	 * @return cadena JSON con todos los clientes, o {@code null} si no hay
	 *         un administrador autenticado.
	 */
	@Override
	public String getAll() {
		if (!adminService.isLoggedadmin()) {
			return null;			
		}
		Gson gson = new Gson();
		List<Cliente> entityList = (List<Cliente>) clienteRep.findAll();
		List<ClienteDTO> dtoList = new ArrayList<>();
		entityList.forEach(entity -> dtoList.add(mapper.map(entity, ClienteDTO.class)));
		return gson.toJson(dtoList);
	}

	/**
	 * Elimina un cliente del sistema por su identificador.
	 * Solo disponible si hay un administrador con sesión activa.
	 *
	 * @param id identificador único del cliente a eliminar.
	 * @return {@code 0} si el cliente fue eliminado exitosamente;
	 *         {@code 1} si no se encontró ningún cliente con ese ID;
	 *         {@code 2} si no hay un administrador autenticado.
	 */
	@Override
	public int deleteById(Long id) {
		if (!adminService.isLoggedadmin()) {
			return 2;			
		}
		if (clienteRep.existsById(id)) {
			clienteRep.deleteById(id);
			return 0;
		}
		return 1;
	}

	/**
	 * Actualiza los datos de un cliente existente identificado por su ID.
	 * Solo disponible si hay un administrador con sesión activa. Valida que
	 * el nuevo usuario y cédula no estén ya en uso por otro cliente.
	 *
	 * @param id    identificador único del cliente a actualizar.
	 * @param dataC {@link ClienteDTO} con los nuevos datos del cliente.
	 * @return {@code 0} si el cliente fue actualizado exitosamente;
	 *         {@code 1} si la cédula es inválida;
	 *         {@code 2} si no hay un administrador autenticado;
	 *         {@code 3} si el nombre de usuario ya está en uso;
	 *         {@code 4} si la cédula ya está en uso;
	 *         {@code 5} si tanto el usuario como la cédula ya están en uso;
	 *         {@code 6} si no se encontró ningún cliente con ese ID.
	 */
	@Override
	public int updateById(Long id, ClienteDTO dataC) {
		if (!adminService.isLoggedadmin())
			return 2;
		Optional<Cliente> encontradoID = clienteRep.findById(id);
		Optional<Cliente> encontradoUsuario = clienteRep.findByUsuario(dataC.getUsuario());
		Optional<Cliente> encontradoCedula = clienteRep.findByCedula(dataC.getCedula());

		if (encontradoUsuario.isPresent() && encontradoCedula.isPresent())
			return 5;
		if (encontradoUsuario.isPresent())
			return 3;
		if (encontradoCedula.isPresent())
			return 4;

		if (encontradoID.isPresent()) {
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
	 * Retorna el número total de clientes registrados en el sistema.
	 *
	 * @return cantidad de clientes en la base de datos.
	 */
	@Override
	public long count() {
		return clienteRep.count();
	}

	/**
	 * Verifica si existe un cliente con el identificador dado.
	 *
	 * @param id identificador único a verificar.
	 * @return {@code true} si existe un cliente con ese ID; {@code false} en caso contrario.
	 */
	@Override
	public boolean exist(Long id) {
		return clienteRep.existsById(id);
	}

	/**
	 * Retorna el cliente que tiene sesión activa en el sistema.
	 *
	 * @return instancia de {@link Cliente} logueado, o {@code null} si no hay
	 *         ningún cliente autenticado.
	 */
	public Cliente getClienteLogueado() {
		return clienteLogueado;
	}

	/**
	 * Retorna el nombre de usuario del cliente identificado por su ID.
	 * Se utiliza para obtener el estado (tipo) del cliente al calcular precios de envío.
	 *
	 * @param id identificador único del cliente.
	 * @return nombre de usuario del cliente encontrado.
	 */
	public String getClienteStatus(Long id) {
		Optional<Cliente> encontrado = clienteRep.findById(id);
		return encontrado.get().getUsuario();
	}

	/**
	 * Retorna el nombre de usuario del cliente identificado por su ID.
	 * Se utiliza para asociar el nombre del cliente a un paquete en el momento de su creación.
	 *
	 * @param id identificador único del cliente.
	 * @return nombre de usuario del cliente encontrado.
	 */
	public String getClienteNombre(Long id) {
		Optional<Cliente> encontrado = clienteRep.findById(id);
		return encontrado.get().getUsuario();
	}

}