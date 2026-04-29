package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.dto.ConductorDTO;
import co.edu.unbosque.proyectomodulo.dto.ManipuladorPaqueteDTO;
import co.edu.unbosque.proyectomodulo.dto.PaqueteDTO;
import co.edu.unbosque.proyectomodulo.dto.TipoCliente;
import co.edu.unbosque.proyectomodulo.entity.Paquete;
import co.edu.unbosque.proyectomodulo.repository.AdminRepository;
import co.edu.unbosque.proyectomodulo.repository.ClienteRepository;
import co.edu.unbosque.proyectomodulo.repository.ConductorRepository;
import co.edu.unbosque.proyectomodulo.repository.ManipuladorPaqueteRepository;
import co.edu.unbosque.proyectomodulo.repository.PaqueteRepository;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import co.edu.unbosque.proyectomodulo.service.ConductorService;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Clase de pruebas de integración para {@link PaqueteService}.
 *
 * <p>Verifica el comportamiento del servicio de paquetes bajo diferentes
 * escenarios de autenticación y validación, incluyendo creación, consulta,
 * actualización, eliminación y búsqueda por cliente.</p>
 *
 * <p>Cada test inicia con la base de datos limpia gracias al método
 * {@link #limpiar()} anotado con {@link BeforeEach}, que elimina todos los
 * registros y cierra todas las sesiones activas.</p>
 *
 * @version 1.0
 */
@SpringBootTest
class PaqueteServiceTest {

	/** Servicio principal bajo prueba. */
	@Autowired
	private PaqueteService paqueteService;

	/** Servicio de clientes utilizado para autenticación en los tests. */
	@Autowired
	private ClienteService clienteService;

	/** Servicio de administradores utilizado para autenticación en los tests. */
	@Autowired
	private AdminService adminService;

	/** Servicio de conductores utilizado para autenticación en los tests. */
	@Autowired
	private ConductorService conductorService;

	/** Servicio de manipuladores de paquetes utilizado para autenticación en los tests. */
	@Autowired
	private ManipuladorPaquetesService manipuladorService;

	/** Repositorio de paquetes para consultas directas a la base de datos. */
	@Autowired
	private PaqueteRepository paqueteRepository;

	/** Repositorio de clientes para limpieza de datos entre tests. */
	@Autowired
	private ClienteRepository clienteRepository;

	/** Repositorio de administradores para limpieza de datos entre tests. */
	@Autowired
	private AdminRepository adminRepository;

	/** Repositorio de conductores para limpieza de datos entre tests. */
	@Autowired
	private ConductorRepository conductorRepository;

	/** Repositorio de manipuladores de paquetes para limpieza de datos entre tests. */
	@Autowired
	private ManipuladorPaqueteRepository manipuladorRepository;

	/**
	 * Método ejecutado antes de cada test. Cierra todas las sesiones activas
	 * y elimina todos los registros de la base de datos para garantizar
	 * un estado limpio en cada prueba.
	 */
	@BeforeEach
	void limpiar() {
		clienteService.logout();
		adminService.logoutadmin();
		conductorService.logout();
		manipuladorService.logout();
		paqueteRepository.deleteAll();
		clienteRepository.deleteAll();
		adminRepository.deleteAll();
		conductorRepository.deleteAll();
		manipuladorRepository.deleteAll();
	}

	/**
	 * Crea un cliente con los datos proporcionados e inicia sesión con sus credenciales.
	 *
	 * @param usuario    nombre de usuario del cliente
	 * @param contrasenia contraseña del cliente
	 * @param cedula     número de cédula del cliente
	 */
	public void crearYLoggearCliente(String usuario, String contrasenia, String cedula) {
		ClienteDTO cliente = new ClienteDTO();
		cliente.setUsuario(usuario);
		cliente.setContrasenia(contrasenia);
		cliente.setCedula(cedula);
		cliente.setTipoCliente(TipoCliente.NORMAL);
		clienteService.create(cliente);
		clienteService.login(usuario, contrasenia);
	}

	/**
	 * Crea un administrador con los datos proporcionados e inicia sesión con sus credenciales.
	 *
	 * @param usuario    nombre de usuario del administrador
	 * @param contrasenia contraseña del administrador
	 */
	public void crearYLoggearAdmin(String usuario, String contrasenia) {
		AdminDTO admin = new AdminDTO();
		admin.setUsuario(usuario);
		admin.setContrasenia(contrasenia);
		admin.setCodigoAdmin("admin123");
		adminService.create(admin);
		adminService.loginadmin(usuario, contrasenia, "admin123");
	}

	/**
	 * Crea un conductor con los datos proporcionados e inicia sesión con sus credenciales.
	 *
	 * @param usuario      nombre de usuario del conductor
	 * @param contrasenia  contraseña del conductor
	 * @param tipoVehiculo tipo de vehículo asignado al conductor
	 */
	public void crearYLoggearConductor(String usuario, String contrasenia, String tipoVehiculo) {
		ConductorDTO conductor = new ConductorDTO();
		conductor.setUsuario(usuario);
		conductor.setContrasenia(contrasenia);
		conductor.setTipoVehiculo(tipoVehiculo);
		conductorService.create(conductor);
		conductorService.login(usuario, contrasenia);
	}

	/**
	 * Crea un manipulador de paquetes con los datos proporcionados e inicia sesión con sus credenciales.
	 *
	 * @param usuario    nombre de usuario del manipulador
	 * @param contrasenia contraseña del manipulador
	 * @param horas      tiempo de trabajo en horas del manipulador
	 */
	public void crearYLoggearManipulador(String usuario, String contrasenia, int horas) {
		ManipuladorPaqueteDTO manip = new ManipuladorPaqueteDTO();
		manip.setUsuario(usuario);
		manip.setContrasenia(contrasenia);
		manip.setTiempoDeTrabajo(horas);
		manipuladorService.create(manip);
		manipuladorService.login(usuario, contrasenia);
	}

	/**
	 * Construye un {@link PaqueteDTO} con datos válidos para ser usado en los tests.
	 *
	 * @param clienteUsuario nombre de usuario del cliente propietario del paquete
	 * @return {@link PaqueteDTO} con contenido, dirección y tiempo de envío válidos
	 */
	public PaqueteDTO crearPaqueteValido(String clienteUsuario) {
		PaqueteDTO dto = new PaqueteDTO();
		dto.setContenido("Documentos");
		dto.setDireccionDeEnvio("Calle 123 # 45-67");
		dto.setTiempoDeEnvio(LocalDateTime.now().plusDays(1));
		dto.setClientePaquete(clienteUsuario);
		return dto;
	}

	// ==================== Tests ====================

	/**
	 * Verifica que un cliente autenticado puede crear un paquete exitosamente.
	 * Se espera que el servicio retorne {@code 0} indicando éxito.
	 */
	@Test
	void testCreateExitoso() {
		String clienteUser = "clienteCreate";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		PaqueteDTO dto = crearPaqueteValido(clienteUser);
		int resultado = paqueteService.create(dto);
		assertEquals(0, resultado);
	}

	/**
	 * Verifica que no es posible crear un paquete sin un cliente autenticado.
	 * Se espera que el servicio retorne {@code 2} indicando sesión no activa.
	 */
	@Test
	void testCreateSinClienteLogueado() {
		PaqueteDTO dto = crearPaqueteValido("algunCliente");
		int resultado = paqueteService.create(dto);
		assertEquals(2, resultado);
	}

	/**
	 * Verifica que la creación de un paquete falla cuando la dirección de envío
	 * es inválida (vacía). Se espera que el servicio retorne {@code 1}.
	 */
	@Test
	void testCreateDireccionInvalida() {
		String clienteUser = "clienteDirInvalida";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		PaqueteDTO dto = crearPaqueteValido(clienteUser);
		dto.setDireccionDeEnvio("");
		int resultado = paqueteService.create(dto);
		assertEquals(1, resultado);
	}

	/**
	 * Verifica que {@code getAll()} solo retorna resultados cuando hay un usuario
	 * autorizado con sesión activa (administrador, conductor o manipulador).
	 * Sin sesión activa debe retornar {@code null}.
	 */
	@Test
	void testGetAllSoloParaRolesAutorizados() {
		assertNull(paqueteService.getAll());

		crearYLoggearAdmin("adminGetAll", "admin123");
		assertNotNull(paqueteService.getAll());
		adminService.logoutadmin();

		crearYLoggearConductor("conductorGetAll", "pass", "moto");
		assertNull(paqueteService.getAll());
		conductorService.logout();

		crearYLoggearManipulador("manipGetAll", "pass", 8);
		assertNull(paqueteService.getAll());
	}

	/**
	 * Verifica que {@code getAllManipuladorPaquetes()} solo retorna resultados
	 * cuando hay un administrador con sesión activa. Sin sesión activa o con
	 * un manipulador sin sesión válida debe retornar {@code null}.
	 */
	@Test
	void testGetAllManipuladorPaquetes() {
		assertNull(paqueteService.getAllManipuladorPaquetes());

		crearYLoggearAdmin("adminManip", "admin123");
		assertNotNull(paqueteService.getAllManipuladorPaquetes());
		adminService.logoutadmin();

		crearYLoggearManipulador("manipManip", "pass", 8);
		assertNull(paqueteService.getAllManipuladorPaquetes());
	}

	/**
	 * Verifica que la eliminación de un paquete solo es posible con un
	 * administrador autenticado. Sin sesión de admin debe retornar {@code 2},
	 * con admin y paquete existente debe retornar {@code 0}, y con ID inexistente
	 * debe retornar {@code 1}.
	 */
	@Test
	void testDeleteByIdSoloAdmin() {
		String clienteUser = "clienteDelete";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		paqueteService.create(crearPaqueteValido(clienteUser));
		List<Paquete> paquetes = paqueteRepository.findByClientePaquete(clienteUser);
		assertFalse(paquetes.isEmpty());
		Long id = paquetes.get(0).getId();

		clienteService.logout();
		int resultado = paqueteService.deleteById(id);
		assertEquals(2, resultado);

		crearYLoggearAdmin("adminDelete", "admin123");
		resultado = paqueteService.deleteById(id);
		assertEquals(0, resultado);

		resultado = paqueteService.deleteById(999L);
		assertEquals(1, resultado);
	}

	/**
	 * Verifica que la actualización de un paquete requiere sesión activa
	 * simultánea de cliente y administrador. Sin admin debe retornar {@code 2},
	 * con ambos autenticados y datos válidos debe retornar {@code 0}.
	 */
	@Test
	void testUpdateByIdRequiereAdminYCliente() {
		String clienteUser = "clienteUpdate";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		paqueteService.create(crearPaqueteValido(clienteUser));
		List<Paquete> paquetes = paqueteRepository.findByClientePaquete(clienteUser);
		Long id = paquetes.get(0).getId();

		PaqueteDTO actualizado = crearPaqueteValido(clienteUser);
		actualizado.setDireccionDeEnvio("Calle 456 # 78-90");

		int resultado = paqueteService.updateById(id, actualizado);
		assertEquals(2, resultado);

		crearYLoggearAdmin("adminUpdate", "admin123");
		resultado = paqueteService.updateById(id, actualizado);
		assertEquals(0, resultado);
	}

	/**
	 * Verifica que el conteo de paquetes se incrementa correctamente
	 * al crear un nuevo paquete.
	 */
	@Test
	void testCount() {
		long antes = paqueteService.count();
		String clienteUser = "clienteCount";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		paqueteService.create(crearPaqueteValido(clienteUser));
		assertEquals(antes + 1, paqueteService.count());
	}

	/**
	 * Verifica que {@code exist()} retorna {@code false} para un ID inexistente
	 * y {@code true} para un paquete recién creado.
	 */
	@Test
	void testExist() {
		assertFalse(paqueteService.exist(999L));
		String clienteUser = "clienteExist";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		paqueteService.create(crearPaqueteValido(clienteUser));
		List<Paquete> paquetes = paqueteRepository.findByClientePaquete(clienteUser);
		Long id = paquetes.get(0).getId();
		assertTrue(paqueteService.exist(id));
	}

	/**
	 * Verifica que {@code paquetesPorCliente()} retorna correctamente todos
	 * los paquetes asociados a un cliente específico.
	 */
	@Test
	void testPaquetesPorCliente() {
		String clienteUser = "clientePaquetes";
		crearYLoggearCliente(clienteUser, "pass", "1234567890");
		paqueteService.create(crearPaqueteValido(clienteUser));
		paqueteService.create(crearPaqueteValido(clienteUser));
		List<Paquete> paquetes = paqueteService.paquetesPorCliente(clienteUser);
		assertNotNull(paquetes);
		assertEquals(2, paquetes.size());
	}
}