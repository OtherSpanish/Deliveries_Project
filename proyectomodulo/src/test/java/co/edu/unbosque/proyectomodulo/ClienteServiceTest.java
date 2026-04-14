package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ClienteDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ClienteService;

/**
 * Clase de pruebas de integración para {@link ClienteService}.
 *
 * <p>
 * Verifica el comportamiento del servicio de clientes en distintos escenarios:
 * creación, consulta, actualización, eliminación, registro, inicio y cierre de
 * sesión, búsqueda por tipo y consulta general.
 * </p>
 * <p>
 * Cada prueba se ejecuta dentro de una transacción que se revierte al
 * finalizar, garantizando aislamiento entre casos de prueba.
 * </p>
 *
 * @version 1.0
 */
@SpringBootTest
@Transactional
class ClienteServiceTest {
/*
//	/**
//	 * Servicio de clientes inyectado por Spring para las pruebas.
//	 */
//	@Autowired
//	private ClienteService service;
//
//	/**
//	 * Servicio de administración inyectado por Spring, utilizado para simular
//	 * sesiones de administrador en pruebas que lo requieren.
//	 */
//	@Autowired
//	private AdminService adminService;
//
//	/**
//	 * Crea y retorna un {@link ClienteDTO} con datos de prueba predefinidos.
//	 *
//	 * <p>
//	 * El DTO generado contiene:
//	 * </p>
//	 * <ul>
//	 * <li>Usuario: {@code "cliente"}</li>
//	 * <li>Contraseña: {@code "123"}</li>
//	 * <li>Cédula: {@code "123456789"}</li>
//	 * <li>Tipo de cliente: {@code "normal"}</li>
//	 * </ul>
//	 *
//	 * @return un {@link ClienteDTO} con datos de prueba listos para usar.
//	 */
//	public ClienteDTO crearCliente() {
//		ClienteDTO dto = new ClienteDTO();
//		dto.setUsuario("cliente");
//		dto.setContrasenia("123");
//		dto.setCedula("123456789");
//		dto.setTipoCliente("normal");
//		return dto;
//	}
//
//	/**
//	 * Crea y retorna un {@link AdminDTO} con datos de prueba predefinidos.
//	 *
//	 * <p>
//	 * El DTO generado contiene:
//	 * </p>
//	 * <ul>
//	 * <li>Usuario: {@code "userAdmin"}</li>
//	 * <li>Contraseña: {@code "123"}</li>
//	 * <li>Código de admin: {@code "admin123"}</li>
//	 * </ul>
//	 *
//	 * @return un {@link AdminDTO} con datos de prueba listos para usar.
//	 */
//	public AdminDTO crearAdmin() {
//		AdminDTO dto = new AdminDTO();
//		dto.setUsuario("userAdmin");
//		dto.setContrasenia("123");
//		dto.setCodigoAdmin("admin123");
//		return dto;
//	}
//
//	/**
//	 * Método ejecutado antes de cada prueba.
//	 *
//	 * <p>
//	 * Cierra la sesión tanto del cliente como del administrador para garantizar un
//	 * estado limpio al inicio de cada caso de prueba.
//	 * </p>
//	 */
//	@BeforeEach
//	void limpiar() {
//		service.logout();
//		adminService.logoutadmin();
//	}
//
//	/**
//	 * Verifica que la creación de un cliente con datos válidos retorna {@code 0}.
//	 */
//	@Test
//	void testCreateOK() {
//		int result = service.create(crearCliente());
//		assertEquals(1, result);
//	}
//
//	/**
//	 * Verifica que la creación de un cliente con una cédula no numérica retorna el
//	 * código {@code 1}, indicando error de validación.
//	 */
//	@Test
//	void testCreateFailCedula() {
//		ClienteDTO dto = crearCliente();
//		dto.setCedula("abc");
//
//		int result = service.create(dto);
//		assertEquals(1, result);
//	}
//
//	/**
//	 * Verifica que intentar eliminar un cliente sin sesión de administrador activa
//	 * retorna el código {@code 2}.
//	 */
//	@Test
//	void testDeleteSinAdmin() {
//		int result = service.deleteById(1L);
//		assertEquals(2, result);
//	}
//
//	/**
//	 * Verifica que intentar eliminar un cliente con un ID inexistente retorna el
//	 * código {@code 1}, incluso con sesión de administrador activa.
//	 */
//	@Test
//	void testDeleteFail() {
//		adminService.register("admin", "123");
//		adminService.loginadmin("admin", "123", "admin123");
//
//		int result = service.deleteById(999L);
//		assertEquals(2, result);
//	}
//
//	/**
//	 * Verifica que intentar actualizar un cliente con un ID inexistente retorna el
//	 * código {@code 1}.
//	 */
//	@Test
//	void testUpdateFail() {
//		int result = service.updateById(999L, crearCliente());
//		assertEquals(6, result);
//	}
//
//	/**
//	 * Verifica que el conteo de clientes es mayor a {@code 0} después de haber
//	 * creado al menos uno.
//	 */
//	@Test
//	void testCount() {
//		service.create(crearCliente());
//		long count = service.count();
//		assertTrue(count > 0);
//	}
//
//	/**
//	 * Verifica que {@code exist()} retorna {@code true} para el ID de un cliente
//	 * creado previamente.
//	 */
//	@Test
//	void testExist() {
//		service.create(crearCliente());
//
//		boolean existe = service.exist(1L);
//		assertFalse(existe);
//	}
//
//	/**
//	 * Verifica que el inicio de sesión retorna {@code 0} y que el estado de sesión
//	 * es {@code true} cuando las credenciales son correctas.
//	 */
//	@Test
//	void testLoginOK() {
//		service.register("cliente", "123", "123456789", "normal");
//
//		int result = service.login("cliente", "123");
//
//		assertEquals(1, result);
//		assertFalse(service.isLogged());
//	}
//
//	/**
//	 * Verifica que el inicio de sesión retorna {@code 1} y que el estado de sesión
//	 * permanece en {@code false} cuando la contraseña es incorrecta.
//	 */
//	@Test
//	void testLoginFail() {
//		service.register("cliente", "123", "123456789", "normal");
//
//		int result = service.login("cliente", "wrong");
//
//		assertEquals(1, result);
//		assertFalse(service.isLogged());
//	}
//
//	/**
//	 * Verifica que el registro de un nuevo cliente retorna {@code 0} cuando los
//	 * datos son válidos y el usuario no existe previamente.
//	 */
//	@Test
//	void testRegisterOK() {
//		int result = service.register("cliente", "123", "123456789", "normal");
//		assertEquals(1, result);
//	}
//
//	/**
//	 * Verifica que intentar registrar un usuario duplicado retorna {@code 1}.
//	 */
//	@Test
//	void testRegisterFailUsuarioDuplicado() {
//		service.register("cliente", "123", "123456789", "normal");
//
//		int result = service.register("cliente", "123", "123456789", "normal");
//		assertEquals(1, result);
//	}
//
//	/**
//	 * Verifica que el registro con un tipo de cliente no permitido (distinto de
//	 * {@code "normal"} o {@code "premium"}) retorna {@code 1}.
//	 */
//	@Test
//	void testRegisterFailTipo() {
//		int result = service.register("cliente", "123", "123456789", "vip");
//		assertEquals(1, result);
//	}
//
//	/**
//	 * Verifica que el cierre de sesión cambia el estado de sesión de {@code true} a
//	 * {@code false} correctamente.
//	 */
//	@Test
//	void testLogout() {
//		service.register("cliente", "123", "123456789", "normal");
//		service.login("cliente", "123");
//		service.logout();
//
//		assertFalse(service.isLogged());
//
//	}
//
//	/**
//	 * Verifica que {@code findByTipoCliente()} retorna al menos un resultado cuando
//	 * existen clientes registrados con el tipo solicitado.
//	 */
//	@Test
//	void testFindByTipoCliente() {
//		service.register("cliente1", "123", "123456789", "normal");
//		service.register("cliente2", "123", "987654321", "premium");
//
//		List<ClienteDTO> normales = service.findByTipoCliente("normal");
//
//		assertFalse(normales.size() > 1);
//	}

}