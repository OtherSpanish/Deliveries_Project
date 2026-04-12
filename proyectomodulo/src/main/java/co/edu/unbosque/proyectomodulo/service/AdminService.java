package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.entity.Admin;
import co.edu.unbosque.proyectomodulo.repository.AdminRepository;

/**
 * Servicio que implementa las operaciones CRUD y de autenticación
 * para la entidad {@link Admin}. Gestiona el inicio y cierre de sesión,
 * así como el registro y administración de datos de administradores.
 *
 * @version 1.0
 */
@Service
public class AdminService implements CRUDOPERATION<AdminDTO> {

	/** Repositorio para acceso a datos de administradores. */
	@Autowired
	private AdminRepository aRep;

	/** Mapper para conversión entre entidades y DTOs. */
	@Autowired
	private ModelMapper mapper;

	/** Administrador actualmente autenticado en el sistema. */
	private Admin adminLogueado;

	/**
	 * Constructor por defecto.
	 */
	public AdminService() {
	}

	/**
	 * Crea un nuevo administrador a partir de un DTO.
	 *
	 * @param data datos del administrador a crear
	 * @return {@code 0} si la operación fue exitosa
	 */
	@Override
	public int create(AdminDTO data) {
		Admin entity = mapper.map(data, Admin.class);
		aRep.save(entity);
		return 0;
	}

	/**
	 * Obtiene todos los administradores registrados.
	 * Requiere que exista un administrador autenticado.
	 *
	 * @return lista de administradores o {@code null} si no hay sesión activa
	 */
	@Override
	public List<AdminDTO> getAll() {
		if (adminLogueado == null) {
			return null;
		}
		List<Admin> entityList = (List<Admin>) aRep.findAll();
		List<AdminDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> {
			AdminDTO dto = mapper.map(entity, AdminDTO.class);
			dtoList.add(dto);
		});

		return dtoList;
	}

	/**
	 * Elimina un administrador por su ID.
	 * Requiere autenticación previa.
	 *
	 * @param id identificador del administrador
	 * @return {@code 0} eliminado, {@code 1} no encontrado,
	 *         {@code 2} sin sesión activa
	 */
	@Override
	public int deleteById(Long id) {
		if (adminLogueado == null) {
			return 2;
		}

		Optional<Admin> encontrado = aRep.findById(id);
		if (encontrado.isPresent()) {
			aRep.delete(encontrado.get());
			return 0;
		}
		return 1;
	}

	/**
	 * Actualiza un administrador existente.
	 * Requiere autenticación previa.
	 *
	 * @param id   identificador del administrador
	 * @param data nuevos datos del administrador
	 * @return {@code 0} actualizado, {@code 1} no encontrado,
	 *         {@code 2} sin sesión activa, {@code 3} usuario duplicado
	 */
	@Override
	public int updateById(Long id, AdminDTO data) {
		if (adminLogueado == null) {
			return 2;
		}

		Optional<Admin> encontradoID = aRep.findById(id);
		Optional<Admin> encontradoUsuario = aRep.findByUsuario(data.getUsuario());

		if (encontradoID.isPresent() && encontradoUsuario.isPresent()) {
			return 3;
		} else if (encontradoID.isPresent() && !encontradoUsuario.isPresent()) {

			AdminDTO temp = mapper.map(encontradoID.get(), AdminDTO.class);
			temp.setUsuario(data.getUsuario());
			temp.setContrasenia(data.getContrasenia());

			Admin entity = mapper.map(temp, Admin.class);
			entity.setId(id);
			aRep.save(entity);

			return 0;
		}
		return 1;
	}

	/**
	 * Obtiene la cantidad total de administradores.
	 *
	 * @return número de administradores registrados
	 */
	@Override
	public long count() {
		return aRep.count();
	}

	/**
	 * Verifica si existe un administrador por su ID.
	 *
	 * @param id identificador del administrador
	 * @return {@code true} si existe, {@code false} en caso contrario
	 */
	@Override
	public boolean exist(Long id) {
		return aRep.existsById(id);
	}

	/**
	 * Inicia sesión de administrador validando credenciales y código.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @param codigoadmin código de verificación
	 * @return {@code 0} éxito, {@code 1} credenciales inválidas
	 */
	public int loginadmin(String usuario, String contrasenia, String codigoadmin) {

		Optional<Admin> encontrado = aRep.findByUsuario(usuario);

		if (encontrado.isPresent()
				&& encontrado.get().getContrasenia().equals(contrasenia)
				&& codigoadmin.equals("admin123")
				&& encontrado.get().getCodigoAdmin().equals("admin123")) {

			adminLogueado = encontrado.get();
			return 0;
		}
		return 1;
	}

	/**
	 * Registra un nuevo administrador.
	 * Requiere que exista un administrador autenticado.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @return {@code 0} registrado, {@code 1} usuario existente,
	 *         {@code 2} sin sesión activa
	 */
	public int register(String usuario, String contrasenia) {

		if (adminLogueado == null) {
			return 2;
		}

		String codigo = "admin123";
		Optional<Admin> encontrado = aRep.findByUsuario(usuario);

		if (encontrado.isPresent()) {
			return 1;
		}

		Admin nuevo = new Admin(usuario, contrasenia, codigo);
		aRep.save(nuevo);

		return 0;
	}

	/**
	 * Cierra la sesión actual del administrador.
	 */
	public void logoutadmin() {
		adminLogueado = null;
	}

	/**
	 * Verifica si hay un administrador autenticado.
	 *
	 * @return {@code true} si hay sesión activa
	 */
	public boolean isLoggedadmin() {
		return adminLogueado != null;
	}
}