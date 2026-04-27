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
 * Servicio que gestiona las operaciones CRUD y autenticación para
 * {@link Cliente}.
 *
 * @version 1.0
 */
@Service
public class ClienteService implements CRUDOPERATION<ClienteDTO> {

	@Autowired
	private ClienteRepository clienteRep;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private AdminService adminService;

	private Cliente clienteLogueado;

	public ClienteService() {
	}

	public int login(String usuario, String contrasenia) {
		Optional<Cliente> encontrado = clienteRep.findByUsuario(usuario);
		if (encontrado.isPresent() && encontrado.get().getContrasenia().equals(contrasenia)) {
			clienteLogueado = encontrado.get();
			return 0;
		}
		return 1;
	}

	public void logout() {
		clienteLogueado = null;
	}

	public boolean isLogged() {
		return clienteLogueado != null;
	}

	@Override
	public int create(ClienteDTO data) {
		Optional<Cliente> encontrado = clienteRep.findByUsuario(data.getUsuario());
		if (encontrado.isPresent())
			return 2;
		try {
			LanzadorException.verificarCedulaValida(data.getCedula());
		} catch (CedulaException e) {
			return 1;
		}
		Cliente entity = mapper.map(data, Cliente.class);
		clienteRep.save(entity);
		return 0;
	}

	@Override
	public String getAll() {
		if (!adminService.isLoggedadmin())
			return null;
		Gson gson = new Gson();
		List<Cliente> entityList = (List<Cliente>) clienteRep.findAll();
		List<ClienteDTO> dtoList = new ArrayList<>();
		entityList.forEach(entity -> dtoList.add(mapper.map(entity, ClienteDTO.class)));
		return gson.toJson(dtoList);
	}

	@Override
	public int deleteById(Long id) {
		if (!adminService.isLoggedadmin())
			return 2;
		// Bug fix: eliminar directamente por id, sin conversión innecesaria
		if (clienteRep.existsById(id)) {
			clienteRep.deleteById(id);
			return 0;
		}
		return 1;
	}

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

	@Override
	public long count() {
		return clienteRep.count();
	}

	@Override
	public boolean exist(Long id) {
		return clienteRep.existsById(id);
	}

	public Cliente getClienteLogueado() {
		return clienteLogueado;
	}

	public String getClienteStatus(Long id) {
		Optional<Cliente> encontrado = clienteRep.findById(id);
		if (encontrado.isEmpty())
			return "NORMAL";
		return mapper.map(encontrado.get(), ClienteDTO.class).getTipoCliente().toString();
	}

	public String getClienteNombre(Long id) {
		Optional<Cliente> encontrado = clienteRep.findById(id);
		if (encontrado.isEmpty())
			return "";
		return encontrado.get().getUsuario();
	}

}
