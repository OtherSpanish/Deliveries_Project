package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import co.edu.unbosque.proyectomodulo.dto.PaqueteDTO;
import co.edu.unbosque.proyectomodulo.entity.Paquete;
import co.edu.unbosque.proyectomodulo.exceptions.DireccionException;
import co.edu.unbosque.proyectomodulo.exceptions.LanzadorException;
import co.edu.unbosque.proyectomodulo.repository.PaqueteRepository;

/**
 * Servicio que gestiona las operaciones CRUD y consultas para la entidad
 * {@link Paquete}. Controla el acceso según el tipo de usuario autenticado
 * (administrador, cliente, conductor o manipulador de paquetes).
 *
 * @version 1.0
 */
@Service
public class PaqueteService implements CRUDOPERATION<PaqueteDTO> {

	/** Repositorio para acceso a datos de paquetes. */
	@Autowired
	private PaqueteRepository paqueteRep;

	/** Mapper para conversión entre entidades y DTOs. */
	@Autowired
	private ModelMapper mapper;

	/** Servicio de cliente para validación de sesión. */
	@Autowired
	private ClienteService clienteService;

	/** Servicio de administrador para validación de sesión. */
	@Autowired
	private AdminService adminService;

	/** Servicio de conductor para validación de sesión. */
	@Autowired
	private ConductorService conductorService;

	/** Servicio de manipulador de paquetes para validación de sesión. */
	@Autowired
	private ManipuladorPaquetesService mService;

	/**
	 * Constructor por defecto de {@code PaqueteService}.
	 */
	public PaqueteService() {
		super();
	}

	/**
	 * Crea un nuevo paquete en el sistema. Requiere que el cliente tenga sesión
	 * activa y valida el tipo de paquete y la dirección de envío antes de
	 * persistir.
	 *
	 * @param data objeto {@link PaqueteDTO} con los datos del paquete a crear
	 * @return {@code 0} si el paquete fue creado exitosamente, {@code 1} si los
	 *         datos no pasan la validación, {@code 2} si el cliente no tiene sesión
	 *         activa
	 */
	@Override
	public int create(PaqueteDTO data) {
		if (!clienteService.isLogged()) {
			return 2;
		}

		try {
			LanzadorException.verificarDireccion(data.getDireccionDeEnvio());
		} catch (DireccionException e) {
			return 1;
		}

		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		Paquete entity = mapper.map(data, Paquete.class);
		paqueteRep.save(entity);
		PaqueteDTO dto = mapper.map(entity, PaqueteDTO.class);
		String json = gson.toJson(dto);
		return 0;
	}

	/**
	 * Retorna la lista de todos los paquetes registrados en el sistema. Solo
	 * accesible para administradores, conductores o manipuladores de paquetes con
	 * sesión activa.
	 *
	 * @return lista de {@link PaqueteDTO} con todos los paquetes serializada en
	 *         formato JSON, o {@code null} si ningún usuario autorizado tiene sesión
	 *         activa
	 */
	@Override
	public String getAll() {

		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
		if (!(adminService.isLoggedadmin() || conductorService.isLogged() || mService.isLogged())) {
			return null;
		}

		List<Paquete> entityList = (List<Paquete>) paqueteRep.findAll();
		List<PaqueteDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> dtoList.add(mapper.map(entity, PaqueteDTO.class)));
		return gson.toJson(dtoList);
	}

	/**
	 * Retorna la lista de todos los paquetes para uso exclusivo de manipuladores de
	 * paquetes o administradores con sesión activa.
	 *
	 * @return lista de {@link PaqueteDTO} con todos los paquetes serializada en
	 *         formato JSON, o {@code null} si el usuario no tiene los permisos
	 *         requeridos
	 */
	public String getAllManipuladorPaquetes() {
		Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();

		if (!(mService.isLogged() || adminService.isLoggedadmin())) {
			return null;
		}

		List<Paquete> entityList = (List<Paquete>) paqueteRep.findAll();
		List<PaqueteDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> dtoList.add(mapper.map(entity, PaqueteDTO.class)));

		return gson.toJson(dtoList);
	}

	/**
	 * Elimina un paquete del sistema por su identificador. Requiere autenticación
	 * de administrador.
	 *
	 * @param id identificador único del paquete a eliminar
	 * @return {@code 0} si el paquete fue eliminado exitosamente, {@code 1} si no
	 *         se encontró el paquete con el ID indicado, {@code 2} si el
	 *         administrador no tiene sesión activa
	 */
	@Override
	public int deleteById(Long id) {

		if (!adminService.isLoggedadmin()) {
			return 2;
		}

		Optional<Paquete> encontrado = paqueteRep.findById(id);

		if (encontrado.isPresent()) {
			paqueteRep.delete(encontrado.get());
			return 0;
		}

		return 1;
	}

	/**
	 * Actualiza los datos de un paquete existente identificado por su ID. Requiere
	 * sesión activa simultánea de cliente y administrador, además de que los nuevos
	 * datos superen la validación de tipo y dirección.
	 *
	 * <p>Códigos de retorno:
	 * <ul>
	 *   <li>{@code 0} — paquete actualizado exitosamente</li>
	 *   <li>{@code 1} — paquete no encontrado o dirección de envío inválida</li>
	 *   <li>{@code 2} — cliente y administrador no tienen sesión activa simultáneamente</li>
	 *   <li>{@code 3} — el cliente no tiene sesión activa</li>
	 *   <li>{@code 4} — el administrador no tiene sesión activa</li>
	 * </ul>
	 *
	 * @param id   identificador único del paquete a actualizar
	 * @param data objeto {@link PaqueteDTO} con los nuevos datos del paquete
	 * @return código entero que indica el resultado de la operación según la tabla
	 *         descrita anteriormente
	 */
	public int updateById(Long id, PaqueteDTO data) {

		if (!(adminService.isLoggedadmin() && clienteService.isLogged())) {
			return 2;
		}
		if (!clienteService.isLogged()) {
			return 3;
		}
		if (!adminService.isLoggedadmin()) {
			return 4;
		}

		Optional<Paquete> encontrado = paqueteRep.findById(id);

		if (encontrado.isPresent()) {

			PaqueteDTO temp = mapper.map(encontrado.get(), PaqueteDTO.class);
			temp.setTipoPaquete(data.getTipoPaquete());
			temp.setContenido(data.getContenido());
			temp.setDireccionDeEnvio(data.getDireccionDeEnvio());
			temp.setTiempoDeEnvio(data.getTiempoDeEnvio());
			temp.setPrecioEnvio(data.getPrecioEnvio());

			try {
				LanzadorException.verificarDireccion(data.getDireccionDeEnvio());
			} catch (DireccionException e) {
				return 1;
			}

			Paquete entity = mapper.map(temp, Paquete.class);
			entity.setId(id);
			paqueteRep.save(entity);

			return 0;
		}

		return 1;
	}

	/**
	 * Retorna la cantidad total de paquetes registrados en el sistema.
	 *
	 * @return número de registros de paquetes existentes
	 */
	@Override
	public long count() {
		return paqueteRep.count();
	}

	/**
	 * Verifica si existe un paquete con el identificador indicado.
	 *
	 * @param id identificador único del paquete a verificar
	 * @return {@code true} si el paquete existe, {@code false} en caso contrario
	 */
	@Override
	public boolean exist(Long id) {
		return paqueteRep.existsById(id);
	}

	/**
	 * Busca y retorna todos los paquetes que coincidan con el tipo especificado.
	 * Los tipos válidos son: {@code "carta"}, {@code "alimenticios"} y
	 * {@code "no alimenticios"}.
	 *
	 * @param tipoPaquete cadena que representa el tipo de paquete a buscar
	 * @return lista de {@link PaqueteDTO} que coinciden con el tipo indicado, lista
	 *         vacía si no hay coincidencias, o {@code null} si el tipo
	 *         proporcionado no es válido
	 */
	public List<PaqueteDTO> findByTipoPaquete(String tipoPaquete) {

		Optional<List<Paquete>> encontrados = paqueteRep.findByTipoPaquete(tipoPaquete);

		if (!(tipoPaquete.equals("carta") || tipoPaquete.equals("alimenticios")
				|| tipoPaquete.equals("no alimenticios"))) {
			return null;
		}

		List<PaqueteDTO> dtoList = new ArrayList<>();

		if (encontrados.isPresent() && !encontrados.get().isEmpty()) {
			encontrados.get().forEach(entity -> dtoList.add(mapper.map(entity, PaqueteDTO.class)));
		}

		return dtoList;
	}

	/**
	 * Retorna la lista de paquetes asociados a un cliente específico, identificado
	 * por su nombre o identificador de cliente.
	 *
	 * @param clientePaquete nombre o identificador del cliente cuyos paquetes se
	 *                       desean consultar
	 * @return lista de {@link Paquete} pertenecientes al cliente indicado; lista
	 *         vacía si no se encontraron registros asociados
	 */
	public List<Paquete> paquetesPorCliente(String clientePaquete) {
	    return paqueteRep.findByClientePaquete(clientePaquete);
	}


}