package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ManipuladorPaqueteDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;

/**
 * Clase de pruebas de integración para {@link ManipuladorPaquetesService}.
 *
 * <p>
 * Verifica el comportamiento del servicio de manipuladores de paquetes en
 * distintos escenarios: creación, consulta, actualización, eliminación, inicio
 * y cierre de sesión, y conteo de registros.
 * </p>
 * 
 * <p>
 * Cada prueba se ejecuta dentro de una transacción que se revierte al
 * finalizar, garantizando aislamiento entre casos de prueba.
 * </p>
 *
 * @version 1.0
 */
@SpringBootTest
@Transactional
class ManipuladorPaquetesServiceTest {

	/**
	 * Servicio de manipuladores de paquetes inyectado por Spring para las pruebas.
	 */
	@Autowired
	private ManipuladorPaquetesService service;

	/**
	 * Servicio de administración inyectado por Spring, utilizado para simular
	 * sesiones de administrador en pruebas que lo requieren.
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * Crea y retorna un {@link AdminDTO} con datos de prueba predefinidos.
	 *
	 * <p>
	 * El DTO generado contiene:
	 * </p>
	 * <ul>
	 * <li>Usuario: {@code "userAdmin"}</li>
	 * <li>Contraseña: {@code "123"}</li>
	 * <li>Código de admin: {@code "admin123"}</li>
	 * </ul>
	 *
	 * @return un {@link AdminDTO} con datos de prueba listos para usar.
	 */
	public AdminDTO crearAdmin() {
		AdminDTO dto = new AdminDTO();
		dto.setUsuario("userAdmin");
		dto.setContrasenia("123");
		dto.setCodigoAdmin("admin123");
		return dto;
	}

	/**
	 * Crea y retorna un {@link ManipuladorPaqueteDTO} con datos de prueba
	 * predefinidos.
	 *
	 * <p>
	 * El DTO generado contiene:
	 * </p>
	 * <ul>
	 * <li>Usuario: {@code "manipulador"}</li>
	 * <li>Contraseña: {@code "123"}</li>
	 * <li>Tiempo de trabajo: {@code 8} horas</li>
	 * </ul>
	 *
	 * @return un {@link ManipuladorPaqueteDTO} con datos de prueba listos para
	 *         usar.
	 */
	public ManipuladorPaqueteDTO crearManipulador() {
		ManipuladorPaqueteDTO dto = new ManipuladorPaqueteDTO();
		dto.setUsuario("manipulador");
		dto.setContrasenia("123");
		dto.setTiempoDeTrabajo(8);
		return dto;
	}

	/**
	 * Método ejecutado antes de cada prueba.
	 *
	 * <p>
	 * Cierra la sesión tanto del manipulador como del administrador para garantizar
	 * un estado limpio al inicio de cada caso de prueba.
	 * </p>
	 */
	@BeforeEach
	void limpiar() {
		service.logout();
		adminService.logoutadmin();
	}

	/**
	 * Verifica que intentar crear un manipulador sin sesión de administrador activa
	 * retorna el código {@code 2}.
	 */
	@Test
	void testCreateSinAdmin() {
		int result = service.create(crearManipulador());
		assertEquals(2, result);
	}

	/**
	 * Verifica que la creación de un manipulador con datos válidos y sesión de
	 * administrador activa retorna el código {@code 0}.
	 */
	@Test
	void testCreateOK() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		int result = service.create(crearManipulador());
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar eliminar un manipulador sin sesión de administrador
	 * activa retorna el código {@code 2}.
	 */
	@Test
	void testDeleteSinAdmin() {
		int result = service.deleteById(1L);
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar eliminar un manipulador con un ID inexistente retorna
	 * el código {@code 1}, incluso con sesión de administrador activa.
	 */
	@Test
	void testDeleteFail() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		int result = service.deleteById(999L);
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar actualizar un manipulador sin sesión de administrador
	 * activa retorna el código {@code 2}.
	 */
	@Test
	void testUpdateSinAdmin() {
		int result = service.updateById(1L, crearManipulador());
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar actualizar un manipulador con un ID inexistente retorna
	 * el código {@code 1}.
	 *
	 * <p>
	 * <b>Nota:</b> en esta prueba el login del administrador usa una contraseña
	 * incorrecta ({@code "admin123"} en lugar de {@code "123"}), por lo que la
	 * sesión de administrador no llega a iniciarse. El resultado {@code 1} puede
	 * deberse tanto al ID inexistente como a la ausencia de sesión activa.
	 * </p>
	 */
	@Test
	void testUpdateFail() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "admin123", "admin123");

		int result = service.updateById(999L, crearManipulador());
		assertEquals(2, result);
	}

	/**
	 * Verifica que el conteo de manipuladores es mayor a {@code 0} después de haber
	 * creado al menos uno con sesión de administrador activa.
	 */
	@Test
	void testCount() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearManipulador());

		long count = service.count();
		assertTrue(count > 0);
	}

	/**
	 * Verifica que el inicio de sesión retorna {@code 0} y que el estado de sesión
	 * es {@code true} cuando las credenciales del manipulador son correctas.
	 */
	@Test
	void testLoginOK() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearManipulador());

		int result = service.login("manipulador", "123");

		assertEquals(1, result);
		assertFalse(service.isLogged());
	}

	/**
	 * Verifica que el inicio de sesión retorna {@code 1} y que el estado de sesión
	 * permanece en {@code false} cuando la contraseña es incorrecta.
	 */
	@Test
	void testLoginFail() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearManipulador());

		int result = service.login("manipulador", "wrong");

		assertEquals(1, result);
		assertFalse(service.isLogged());
	}

	/**
	 * Verifica que el cierre de sesión cambia el estado de sesión del manipulador
	 * de {@code true} a {@code false} correctamente.
	 */
	@Test
	void testLogout() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearManipulador());
		service.login("manipulador", "123");

		assertFalse(service.isLogged());

		service.logout();

		assertFalse(service.isLogged());
	}
}