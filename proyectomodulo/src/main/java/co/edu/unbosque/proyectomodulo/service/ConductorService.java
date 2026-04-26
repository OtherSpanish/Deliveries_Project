package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.proyectomodulo.dto.ConductorDTO;
import co.edu.unbosque.proyectomodulo.dto.ManipuladorPaqueteDTO;
import co.edu.unbosque.proyectomodulo.entity.Conductor;
import co.edu.unbosque.proyectomodulo.exceptions.LanzadorException;
import co.edu.unbosque.proyectomodulo.exceptions.TipoVehiculoException;
import co.edu.unbosque.proyectomodulo.repository.ConductorRepository;

/**
 * Servicio que gestiona las operaciones CRUD y autenticación para la entidad
 * {@link Conductor}. Permite la creación, actualización, eliminación y consulta
 * de conductores, así como el manejo de sesión.
 *
 * @version 1.0
 */
@Service
public class ConductorService implements CRUDOPERATION<ConductorDTO> {

	/** Repositorio para acceso a datos de conductores. */
	@Autowired
	private ConductorRepository conductorRep;

	/** Servicio de administrador para validación de permisos. */
	@Autowired
	private AdminService adminService;

	/** Mapper para conversión entre entidades y DTOs. */
	@Autowired
	private ModelMapper mapper;

	/** Conductor actualmente autenticado en el sistema. */
	private Conductor conductorLogueado;

	/**
	 * Constructor por defecto.
	 */
	public ConductorService() {
	}

	/**
	 * Crea un nuevo conductor. Requiere autenticación de administrador y validación
	 * del tipo de vehículo.
	 *
	 * @param data datos del conductor
	 * @return {@code 0} creado, {@code 1} tipo de vehículo inválido, {@code 2} sin
	 *         permisos, {@code 3} usuario existente
	 */
	@Override
	public int create(ConductorDTO data) {
		Optional<Conductor> encontrado = conductorRep.findByUsuario(data.getUsuario());
		if (!adminService.isLoggedadmin()) {
			return 2;
		}

		if (encontrado.isPresent()) {
			return 3;
		}
		try {
			LanzadorException.verificarTipoVehiculo(data.getTipoVehiculo());
		} catch (TipoVehiculoException e) {
			return 1;
		}

		Conductor entity = mapper.map(data, Conductor.class);
		conductorRep.save(entity);
		ManipuladorPaqueteDTO dto = mapper.map(entity, ManipuladorPaqueteDTO.class);
		Gson gson = new Gson();
		String json = gson.toJson(dto);
		return 0;

	}

	/**
	 * Obtiene todos los conductores registrados. Requiere autenticación de
	 * administrador.
	 *
	 * @return lista de conductores o {@code null} si no hay permisos
	 */
	@Override
	public String getAll() {
		Gson gson = new Gson();
		if (!adminService.isLoggedadmin()) {
			return null;
		}

		List<Conductor> entityList = (List<Conductor>) conductorRep.findAll();
		List<ConductorDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> dtoList.add(mapper.map(entity, ConductorDTO.class)));
		return gson.toJson(dtoList);
	}

	/**
	 * Elimina un conductor por su ID. Requiere autenticación de administrador.
	 *
	 * @param id identificador del conductor
	 * @return {@code 0} eliminado, {@code 1} no encontrado, {@code 2} sin permisos
	 */
	@Override
	public int deleteById(Long id) {

		if (!adminService.isLoggedadmin()) {
			return 2;
		}

		Optional<Conductor> encontrado = conductorRep.findById(id);

		if (encontrado.isPresent()) {
			conductorRep.delete(encontrado.get());
			return 0;
		}

		return 1;
	}

	/**
	 * Actualiza un conductor existente. Requiere autenticación de administrador y
	 * validación del tipo de vehículo.
	 *
	 * @param id   identificador del conductor
	 * @param data nuevos datos
	 * @return {@code 0} actualizado, {@code 1} error de validación o no encontrado,
	 *         {@code 2} sin permisos, {@code 3} usuario duplicado
	 */
	@Override
	public int updateById(Long id, ConductorDTO dataConductor) {
		if (!adminService.isLoggedadmin()) {
			return 2;
		}

		Optional<Conductor> encontradoID = conductorRep.findById(id);
		Optional<Conductor> encontradoUsuario = conductorRep.findByUsuario(dataConductor.getUsuario());

		if (encontradoID.isPresent() && encontradoUsuario.isPresent()) {
			return 3;
		}

		if (encontradoID.isPresent()) {

			ConductorDTO temp = mapper.map(encontradoID.get(), ConductorDTO.class);
			temp.setUsuario(dataConductor.getUsuario());
			temp.setContrasenia(dataConductor.getContrasenia());
			temp.setTipoVehiculo(dataConductor.getTipoVehiculo());

			try {
				LanzadorException.verificarTipoVehiculo(dataConductor.getTipoVehiculo());
			} catch (TipoVehiculoException e) {
				return 1;
			}

			Conductor entity = mapper.map(temp, Conductor.class);
			entity.setId(id);
			conductorRep.save(entity);

			return 0;
		}

		return 1;
	}

	/**
	 * Obtiene la cantidad total de conductores.
	 *
	 * @return número de conductores registrados
	 */
	@Override
	public long count() {
		return conductorRep.count();
	}

	/**
	 * Verifica si existe un conductor por su ID.
	 *
	 * @param id identificador
	 * @return {@code true} si existe
	 */
	@Override
	public boolean exist(Long id) {
		return conductorRep.existsById(id);
	}

	/**
	 * Inicia sesión de un conductor.
	 *
	 * @param usuario     nombre de usuario
	 * @param contrasenia contraseña
	 * @return {@code 0} éxito, {@code 1} credenciales inválidas
	 */
	public int login(String usuario, String contrasenia) {

		Optional<Conductor> encontrado = conductorRep.findByUsuario(usuario);

		if (encontrado.isPresent() && encontrado.get().getContrasenia().equals(contrasenia)) {

			conductorLogueado = encontrado.get();
			return 0;
		}

		return 1;
	}

	/**
	 * Cierra la sesión del conductor actual.
	 */
	public void logout() {
		conductorLogueado = null;
	}

	/**
	 * Verifica si hay un conductor autenticado.
	 *
	 * @return {@code true} si hay sesión activa
	 */
	public boolean isLogged() {
		return conductorLogueado != null;
	}


}