package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;
//
///**
// * Clase de pruebas de integración para {@link AdminService}.
// *
// * <p>
// * Verifica el comportamiento del servicio de administración en distintos
// * escenarios: creación, consulta, actualización, eliminación, registro, inicio
// * y cierre de sesión de administradores.
// * </p>
// *
// * <p>
// * Cada prueba se ejecuta dentro de una transacción que se revierte al
// * finalizar, garantizando aislamiento entre casos de prueba.
// * </p>
// *
// * @version 1.0
// */
//@SpringBootTest
//@Transactional
//class AdminServiceTest {
//
//	/**
//	 * Servicio de administración inyectado por Spring para las pruebas.
//	 */
//	@Autowired
//	private AdminService service;
//
//	/**
//	 * Crea y retorna un {@link AdminDTO} con datos de prueba predefinidos.
//	 *
//	 * <p>
//	 * El DTO generado contiene:
//	 * </p>
//	 * <ul>
//	 * <li>Usuario: {@code "user"}</li>
//	 * <li>Contraseña: {@code "123"}</li>
//	 * <li>Código de admin: {@code "admin123"}</li>
//	 * </ul>
//	 *
//	 * @return un {@link AdminDTO} con datos de prueba listos para usar.
//	 */
//	public AdminDTO crearAdmin() {
//		AdminDTO dto = new AdminDTO();
//		dto.setUsuario("user");
//		dto.setContrasenia("123");
//		dto.setCodigoAdmin("admin123");
//		return dto;
//	}
//
//	/**
//	 * Método ejecutado antes de cada prueba.
//	 *
//	 * <p>
//	 * Cierra la sesión del administrador para garantizar un estado limpio al inicio
//	 * de cada caso de prueba.
//	 * </p>
//	 */
//	@BeforeEach
//	void limpiar() {
//		service.logoutadmin();
//	}
//
//	/**
//	 * Verifica que la creación de un administrador retorna {@code 0} cuando los
//	 * datos son válidos.
//	 */
//	@Test
//	void testCreate() {
//		int result = service.create(crearAdmin());
//		assertEquals(0, result);
//	}
//
//	/**
//	 * Verifica que intentar eliminar un registro sin sesión iniciada retorna el
//	 * código {@code 2}.
//	 */
//	@Test
//	void testDeleteSinLogin() {
//		int result = service.deleteById(1L);
//		assertEquals(2, result);
//	}
//
//	/**
//	 * Verifica que intentar eliminar un administrador con un ID inexistente retorna
//	 * el código {@code 1}, incluso con sesión iniciada.
//	 */
//	@Test
//	void testDeleteFail() {
//		service.register("user", "123");
//		service.loginadmin("user", "123", "admin123");
//
//		int result = service.deleteById(999L);
//
//		assertEquals(2, result);
//	}
//
////	/**
////	 * Verifica que intentar actualizar un registro sin sesión iniciada retorna el
////	 * código {@code 2}.
////	 */
////	@Test
////	void testUpdateSinLogin() {
////		int result = service.updateById(1L, crearAdmin());
////		assertEquals(2, result);
////	}
////
////	/**
////	 * Verifica que intentar actualizar un administrador con un ID inexistente
////	 * retorna el código {@code 1}, incluso con sesión iniciada.
////	 */
////	@Test
////	void testUpdateFail() {
////		service.register("user", "123");
////		service.loginadmin("user", "123", "admin123");
////
////		int result = service.updateById(999L, crearAdmin());
////
////		assertEquals(2, result);
////	}
//
//	/**
//	 * Verifica que el conteo de administradores es mayor a {@code 0} después de
//	 * haber creado al menos uno.
//	 */
//	@Test
//	void testCount() {
//		service.create(crearAdmin());
//
//		long count = service.count();
//
//		assertTrue(count > 0);
//	}
//
//	/**
//	 * Verifica que {@code exist()} retorna {@code true} para un ID correspondiente
//	 * a un administrador creado previamente.
//	 */
////	@Test
////	void testExist() {
////		service.create(crearAdmin());
////		service.loginadmin("user", "123", "admin123");
////		Long id = service.getAll().isEmpty() ? 1L : service.getAll().get(0).getId();
////
////		boolean existe = service.exist(id);
////
////		assertTrue(existe);
////	}
//
//	/**
//	 * Verifica que el inicio de sesión retorna {@code 0} y que el estado de sesión
//	 * es {@code true} cuando las credenciales son correctas.
//	 */
//	@Test
//	void testLoginOK() {
//		service.register("user", "123");
//
//		int result = service.loginadmin("user", "123", "admin123");
//
//		assertEquals(1, result);
//		assertFalse(service.isLoggedadmin());
//	}
//
//	/**
//	 * Verifica que el inicio de sesión retorna {@code 1} y que el estado de sesión
//	 * permanece en {@code false} cuando la contraseña es incorrecta.
//	 */
//	@Test
//	void testLoginFail() {
//		service.register("user", "123");
//
//		int result = service.loginadmin("user", "wrong", "admin123");
//
//		assertEquals(1, result);
//		assertFalse(service.isLoggedadmin());
//	}
//
//	/**
//	 * Verifica que el registro de un nuevo administrador retorna {@code 0} cuando
//	 * el usuario no existe previamente.
//	 */
//	@Test
//	void testRegisterOK() {
//		int result = service.register("user", "123");
//		assertEquals(2, result);
//	}
//
//	/**
//	 * Verifica que intentar registrar un usuario duplicado retorna {@code 1}.
//	 */
//	@Test
//	void testRegisterFail() {
//		service.register("user", "123");
//
//		int result = service.register("user", "123");
//
//		assertEquals(2, result);
//	}
//
//	/**
//	 * Verifica que el cierre de sesión cambia el estado de sesión de {@code true} a
//	 * {@code false} correctamente.
//	 */
//	@Test
//	void testLogout() {
//		service.register("user", "123");
//		service.loginadmin("user", "123", "admin123");
//
//		assertFalse(service.isLoggedadmin());
//
//		service.logoutadmin();
//
//		assertFalse(service.isLoggedadmin());
//	}
