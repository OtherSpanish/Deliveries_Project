package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;

/**
 * Clase de pruebas unitarias para el servicio {@link AdminService}.
 * 
 * <p>Esta clase contiene casos de prueba que validan el comportamiento
 * de los métodos de administración, incluyendo creación, eliminación,
 * actualización, autenticación y control de sesión de administradores.</p>
 * 
 * <p>Se ejecuta en un contexto transaccional de Spring Boot para garantizar
 * que cada prueba se realice de forma aislada y los cambios se reviertan
 * automáticamente.</p>
 */
@SpringBootTest
@Transactional
class AdminServiceTest {

	/**
	 * Servicio de administración inyectado para realizar las pruebas.
	 */
	@Autowired
	private AdminService service;

	/**
	 * Crea un objeto {@link AdminDTO} con datos válidos predefinidos para pruebas.
	 * 
	 * <p>Los datos incluyen:
	 * <ul>
	 *   <li>Usuario: "adminPrueba"</li>
	 *   <li>Contraseña: "123"</li>
	 *   <li>Código de administrador: "admin123"</li>
	 * </ul></p>
	 * 
	 * @return instancia de {@code AdminDTO} con datos de prueba válidos
	 */
	private AdminDTO crearAdminValido() {
		AdminDTO dto = new AdminDTO();
		dto.setUsuario("adminPrueba");
		dto.setContrasenia("123");
		dto.setCodigoAdmin("admin123");
		return dto;
	}

	/**
	 * Método de configuración ejecutado antes de cada prueba.
	 * 
	 * <p>Limpia cualquier sesión activa de administrador para garantizar
	 * que cada caso de prueba comience en un estado inicial conocido.</p>
	 */
	@BeforeEach
	void limpiarSesion() {
		service.logoutadmin();
	}

	/**
	 * Prueba la creación exitosa de un administrador con datos válidos.
	 * 
	 * <p>Verifica que el método {@code create} retorne {@code 0} cuando
	 * se proporcionan datos de administrador válidos.</p>
	 */
	@Test
	void testCreate() {
		int resultado = service.create(crearAdminValido());
		assertEquals(0, resultado);
	}

	/**
	 * Prueba la eliminación de un administrador sin haber iniciado sesión.
	 * 
	 * <p>Verifica que el método {@code deleteById} retorne {@code 2} 
	 * indicando que no hay una sesión activa de administrador.</p>
	 */
	@Test
	void testDeleteSinLogin() {
		int resultado = service.deleteById(1L);
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la eliminación de un administrador con un ID inexistente.
	 * 
	 * <p>Crea un administrador, inicia sesión y luego intenta eliminar
	 * un registro con ID que no existe. Verifica que el método retorne
	 * {@code 1} indicando que no se encontró el administrador.</p>
	 */
	@Test
	void testDeleteConIdInexistente() {
		service.create(crearAdminValido());
		service.loginadmin("adminPrueba", "123", "admin123");
		int resultado = service.deleteById(999L);
		assertEquals(1, resultado);
	}

	/**
	 * Prueba la actualización de un administrador sin haber iniciado sesión.
	 * 
	 * <p>Verifica que el método {@code updateById} retorne {@code 2}
	 * cuando no hay una sesión activa de administrador.</p>
	 */
	@Test
	void testUpdateSinLogin() {
		int resultado = service.updateById(1L, crearAdminValido());
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la actualización de un administrador con un ID inexistente.
	 * 
	 * <p>Crea un administrador, inicia sesión y luego intenta actualizar
	 * un registro con ID que no existe. Verifica que el método retorne
	 * {@code 1} indicando que no se encontró el administrador.</p>
	 */
	@Test
	void testUpdateConIdInexistente() {
		service.create(crearAdminValido());
		service.loginadmin("adminPrueba", "123", "admin123");
		int resultado = service.updateById(999L, crearAdminValido());
		assertEquals(1, resultado);
	}

	/**
	 * Prueba el conteo de administradores registrados.
	 * 
	 * <p>Compara el número de administradores antes y después de crear
	 * uno nuevo para verificar que el contador se incremente correctamente.</p>
	 */
	@Test
	void testCount() {
		long antes = service.count();
		service.create(crearAdminValido());
		assertEquals(antes + 1, service.count());
	}

	/**
	 * Prueba la verificación de existencia de un administrador con ID inexistente.
	 * 
	 * <p>Verifica que el método {@code exist} retorne {@code false} para
	 * un ID que no corresponde a ningún administrador registrado.</p>
	 */
	@Test
	void testExist() {
		assertFalse(service.exist(999L));
	}

	/**
	 * Prueba el inicio de sesión exitoso con credenciales válidas.
	 * 
	 * <p>Crea un administrador, intenta iniciar sesión con las credenciales
	 * correctas y verifica que el estado de autenticación sea verdadero.</p>
	 */
	@Test
	void testLoginExitoso() {
		service.create(crearAdminValido());
		int resultado = service.loginadmin("adminPrueba", "123", "admin123");
		assertEquals(0, resultado);
		assertTrue(service.isLoggedadmin());
	}

	/**
	 * Prueba el inicio de sesión con contraseña incorrecta.
	 * 
	 * <p>Crea un administrador e intenta iniciar sesión con una contraseña
	 * incorrecta. Verifica que el método retorne {@code 1} y que el estado
	 * de autenticación permanezca en falso.</p>
	 */
	@Test
	void testLoginContraseniaIncorrecta() {
		service.create(crearAdminValido());
		int resultado = service.loginadmin("adminPrueba", "incorrecta", "admin123");
		assertEquals(1, resultado);
		assertFalse(service.isLoggedadmin());
	}

	/**
	 * Prueba el cierre de sesión de un administrador autenticado.
	 * 
	 * <p>Inicia sesión correctamente, verifica el estado de autenticación,
	 * cierra la sesión y confirma que el estado de autenticación cambie
	 * a falso después del cierre.</p>
	 */
	@Test
	void testLogout() {
		service.create(crearAdminValido());
		service.loginadmin("adminPrueba", "123", "admin123");
		assertTrue(service.isLoggedadmin());
		service.logoutadmin();
		assertFalse(service.isLoggedadmin());
	}
}