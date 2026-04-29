package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.dto.TipoCliente;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ClienteService;

/**
 * Clase de pruebas unitarias para el servicio {@link ClienteService}.
 * 
 * <p>Esta clase contiene casos de prueba que validan el comportamiento
 * de los métodos de gestión de clientes, incluyendo creación, consulta,
 * eliminación, actualización, autenticación y control de sesión.</p>
 * 
 * <p>Las pruebas se ejecutan en un contexto transaccional de Spring Boot
 * para garantizar aislamiento entre casos de prueba y la reversión
 * automática de los cambios realizados en la base de datos.</p>
 * 
 * <p>Algunas pruebas requieren autenticación previa como administrador,
 * para lo cual se utiliza el método auxiliar {@link #loginAdminTemp()}.</p>
 */
@SpringBootTest
@Transactional
class ClienteServiceTest {

	/**
	 * Servicio de clientes inyectado para realizar las pruebas.
	 */
	@Autowired
	private ClienteService clienteService;

	/**
	 * Servicio de administración inyectado para pruebas que requieren
	 * autenticación de administrador.
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * Método de configuración ejecutado antes de cada prueba.
	 * 
	 * <p>Cierra todas las sesiones activas tanto de clientes como de
	 * administradores para garantizar que cada caso de prueba comience
	 * desde un estado inicial limpio y predecible.</p>
	 */
	@BeforeEach
	void limpiarSesiones() {
		clienteService.logout();
		adminService.logoutadmin();
	}

	/**
	 * Crea un objeto {@link ClienteDTO} con datos válidos predefinidos para pruebas.
	 * 
	 * <p>Los datos incluyen:
	 * <ul>
	 *   <li>Usuario: "clienteTest"</li>
	 *   <li>Contraseña: "pass"</li>
	 *   <li>Cédula: "1234567890"</li>
	 *   <li>Tipo de cliente: {@link TipoCliente#NORMAL}</li>
	 * </ul></p>
	 * 
	 * @return instancia de {@code ClienteDTO} con datos de prueba válidos
	 */
	public ClienteDTO crearClienteValido() {
		ClienteDTO dto = new ClienteDTO();
		dto.setUsuario("clienteTest");
		dto.setContrasenia("pass");
		dto.setCedula("1234567890"); 
		dto.setTipoCliente(TipoCliente.NORMAL);
		return dto;
	}

	/**
	 * Crea un administrador temporal con un nombre de usuario único y
	 * autentica la sesión como administrador.
	 * 
	 * <p>Este método auxiliar se utiliza en pruebas que requieren permisos
	 * de administrador. El nombre de usuario se genera utilizando
	 * {@link System#nanoTime()} para garantizar su unicidad en cada
	 * invocación y evitar conflictos entre pruebas concurrentes.</p>
	 * 
	 * <p>Las credenciales del administrador temporal son:
	 * <ul>
	 *   <li>Usuario: "admin_" + nanosegundos actuales</li>
	 *   <li>Contraseña: "admin123"</li>
	 *   <li>Código de administrador: "admin123"</li>
	 * </ul></p>
	 */
	public void loginAdminTemp() {
		String uniqueUser = "admin_" + System.nanoTime();
		AdminDTO admin = new AdminDTO();
		admin.setUsuario(uniqueUser);
		admin.setContrasenia("admin123");
		admin.setCodigoAdmin("admin123");
		adminService.create(admin);
		adminService.loginadmin(uniqueUser, "admin123", "admin123");
	}

	/**
	 * Prueba la creación exitosa de un cliente con datos válidos.
	 * 
	 * <p>Verifica que el método {@code create} retorne {@code 0} cuando
	 * se proporcionan datos de cliente válidos y completos.</p>
	 */
	@Test
	void testCreateExitoso() {
		int resultado = clienteService.create(crearClienteValido());
		assertEquals(0, resultado);
	}

	/**
	 * Prueba la creación de un cliente con un nombre de usuario duplicado.
	 * 
	 * <p>Intenta crear dos clientes con el mismo nombre de usuario y verifica
	 * que el segundo intento retorne {@code 2}, indicando que el usuario
	 * ya existe en el sistema.</p>
	 */
	@Test
	void testCreateUsuarioDuplicado() {
		clienteService.create(crearClienteValido());
		int resultado = clienteService.create(crearClienteValido());
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la creación de un cliente con una cédula de formato inválido.
	 * 
	 * <p>Modifica el DTO de cliente para establecer una cédula demasiado corta
	 * y verifica que el método {@code create} retorne {@code 1}, indicando
	 * un error de validación en los datos proporcionados.</p>
	 */
	@Test
	void testCreateCedulaInvalida() {
		ClienteDTO dto = crearClienteValido();
		dto.setCedula("123");
		int resultado = clienteService.create(dto);
		assertEquals(1, resultado);
	}

	/**
	 * Prueba la obtención de todos los clientes sin autenticación de administrador.
	 * 
	 * <p>Verifica que el método {@code getAll} retorne {@code null} cuando
	 * no hay una sesión de administrador activa, indicando que se requiere
	 * autenticación para acceder a este recurso.</p>
	 */
	@Test
	void testGetAllSinAdmin() {
		String json = clienteService.getAll();
		assertNull(json);
	}

	/**
	 * Prueba la obtención de todos los clientes con autenticación de administrador.
	 * 
	 * <p>Crea un cliente, inicia sesión como administrador y verifica que
	 * el método {@code getAll} retorne un JSON no nulo que contiene los
	 * datos del cliente creado.</p>
	 */
	@Test
	void testGetAllConAdmin() {
		clienteService.create(crearClienteValido());
		loginAdminTemp();
		String json = clienteService.getAll();
		assertNotNull(json);
		assertTrue(json.contains("clienteTest"));
	}

	/**
	 * Prueba la eliminación de un cliente sin autenticación de administrador.
	 * 
	 * <p>Verifica que el método {@code deleteById} retorne {@code 2} cuando
	 * no hay una sesión de administrador activa, indicando que se requiere
	 * autenticación para realizar esta operación.</p>
	 */
	@Test
	void testDeleteByIdSinAdmin() {
		int resultado = clienteService.deleteById(1L);
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la eliminación de un cliente con un ID inexistente.
	 * 
	 * <p>Inicia sesión como administrador e intenta eliminar un cliente
	 * con un ID que no existe en el sistema. Verifica que el método retorne
	 * {@code 1}, indicando que el cliente no fue encontrado.</p>
	 */
	@Test
	void testDeleteByIdInexistenteConAdmin() {
		loginAdminTemp();
		int resultado = clienteService.deleteById(999L);
		assertEquals(1, resultado);
	}

	/**
	 * Prueba la actualización de un cliente sin autenticación de administrador.
	 * 
	 * <p>Verifica que el método {@code updateById} retorne {@code 2} cuando
	 * no hay una sesión de administrador activa, indicando que se requiere
	 * autenticación para realizar esta operación.</p>
	 */
	@Test
	void testUpdateByIdSinAdmin() {
		int resultado = clienteService.updateById(1L, crearClienteValido());
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la actualización de un cliente que no existe en el sistema.
	 * 
	 * <p>Inicia sesión como administrador e intenta actualizar un cliente
	 * con un ID inexistente. Verifica que el método {@code updateById}
	 * retorne {@code 6}, indicando que el cliente no fue encontrado.</p>
	 */
	@Test
	void testUpdateByIdClienteNoExistente() {
		loginAdminTemp();
		int resultado = clienteService.updateById(999L, crearClienteValido());
		assertEquals(6, resultado);
	}

	/**
	 * Prueba el inicio de sesión exitoso de un cliente.
	 * 
	 * <p>Crea un cliente, inicia sesión con las credenciales correctas y
	 * verifica que el método {@code login} retorne {@code 0} y que el
	 * estado de autenticación del cliente sea verdadero.</p>
	 */
	@Test
	void testLoginExitoso() {
		clienteService.create(crearClienteValido());
		int resultado = clienteService.login("clienteTest", "pass");
		assertEquals(0, resultado);
		assertTrue(clienteService.isLogged());
	}

	/**
	 * Prueba el inicio de sesión de un cliente con contraseña incorrecta.
	 * 
	 * <p>Crea un cliente e intenta iniciar sesión con una contraseña
	 * incorrecta. Verifica que el método retorne {@code 1} y que el
	 * estado de autenticación permanezca en falso.</p>
	 */
	@Test
	void testLoginFallido() {
		clienteService.create(crearClienteValido());
		int resultado = clienteService.login("clienteTest", "wrong");
		assertEquals(1, resultado);
		assertFalse(clienteService.isLogged());
	}

	/**
	 * Prueba el cierre de sesión de un cliente autenticado.
	 * 
	 * <p>Inicia sesión correctamente como cliente, verifica el estado de
	 * autenticación, cierra la sesión y confirma que el estado cambie
	 * a falso después del cierre.</p>
	 */
	@Test
	void testLogout() {
		clienteService.create(crearClienteValido());
		clienteService.login("clienteTest", "pass");
		assertTrue(clienteService.isLogged());
		clienteService.logout();
		assertFalse(clienteService.isLogged());
	}

	/**
	 * Prueba el conteo de clientes registrados en el sistema.
	 * 
	 * <p>Compara el número de clientes antes y después de crear uno nuevo
	 * para verificar que el contador se incremente correctamente en una
	 * unidad.</p>
	 */
	@Test
	void testCount() {
		long antes = clienteService.count();
		clienteService.create(crearClienteValido());
		assertEquals(antes + 1, clienteService.count());
	}

	/**
	 * Prueba la verificación de existencia de un cliente con un ID inexistente.
	 * 
	 * <p>Verifica que el método {@code exist} retorne {@code false} para
	 * un ID que no corresponde a ningún cliente registrado en el sistema.</p>
	 */
	@Test
	void testExist() {
		assertFalse(clienteService.exist(999L));
	}

	/**
	 * Prueba la obtención del cliente actualmente autenticado.
	 * 
	 * <p>Crea un cliente, inicia sesión y verifica que el método
	 * {@code getClienteLogueado} retorne un objeto no nulo con el
	 * nombre de usuario correcto del cliente autenticado.</p>
	 */
	@Test
	void testGetClienteLogueado() {
		clienteService.create(crearClienteValido());
		clienteService.login("clienteTest", "pass");
		assertNotNull(clienteService.getClienteLogueado());
		assertEquals("clienteTest", clienteService.getClienteLogueado().getUsuario());
	}

}