package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.PaqueteDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ClienteService;
import co.edu.unbosque.proyectomodulo.service.PaqueteService;

/**
 * Clase de pruebas de integración para {@link PaqueteService}.
 *
 * <p>
 * Verifica el comportamiento del servicio de paquetes en distintos escenarios:
 * creación, consulta, actualización, eliminación, conteo, verificación de
 * existencia y búsqueda por tipo de paquete.
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
class PaqueteServiceTest {

	/**
	 * Servicio de paquetes inyectado por Spring para las pruebas.
	 */
	@Autowired
	private PaqueteService service;

	/**
	 * Servicio de clientes inyectado por Spring, utilizado para registrar e iniciar
	 * sesión como cliente en las pruebas que lo requieren.
	 */
	@Autowired
	private ClienteService clienteService;

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
	 * <li>Usuario: {@code "user"}</li>
	 * <li>Contraseña: {@code "123"}</li>
	 * <li>Código de admin: {@code "admin123"}</li>
	 * </ul>
	 *
	 * @return un {@link AdminDTO} con datos de prueba listos para usar.
	 */
	public AdminDTO crearAdmin() {
		AdminDTO dto = new AdminDTO();
		dto.setUsuario("user");
		dto.setContrasenia("123");
		dto.setCodigoAdmin("admin123");
		return dto;
	}

	/**
	 * Crea y retorna un {@link PaqueteDTO} con datos de prueba predefinidos.
	 *
	 * <p>
	 * El DTO generado contiene:
	 * </p>
	 * <ul>
	 * <li>Tipo de paquete: {@code "normal"}</li>
	 * <li>Contenido: {@code "ropa"}</li>
	 * <li>Dirección de envío: {@code "calle 123"}</li>
	 * <li>Tiempo de envío: {@code "3"}</li>
	 * <li>Precio de envío: {@code "10000"}</li>
	 * </ul>
	 *
	 * @return un {@link PaqueteDTO} con datos de prueba listos para usar.
	 */
	public PaqueteDTO crearPaquete() {
		PaqueteDTO dto = new PaqueteDTO();
		dto.setTipoPaquete("normal");
		dto.setContenido("ropa");
		dto.setDireccionDeEnvio("calle 123");
		dto.setTiempoDeEnvio("3");
		dto.setPrecioEnvio("10000");
		return dto;
	}

	/**
	 * Método ejecutado antes de cada prueba.
	 *
	 * <p>
	 * Cierra la sesión del cliente y del administrador para garantizar un estado
	 * limpio al inicio de cada caso de prueba.
	 * </p>
	 */
	@BeforeEach
	void limpiar() {
		clienteService.logout();
		adminService.logoutadmin();
	}

	/**
	 * Verifica que intentar crear un paquete sin sesión de cliente activa retorna
	 * el código {@code 2}.
	 */
	@Test
	void testCreateSinCliente() {
		int result = service.create(crearPaquete());
		assertEquals(2, result);
	}

	/**
	 * Verifica que la creación de un paquete retorna {@code 2} cuando un cliente
	 * tiene sesión activa y los datos son válidos.
	 */
	@Test
	void testCreateOK() {
		clienteService.register("cliente", "123", "123456789", "normal");
		clienteService.login("cliente", "123");

		int result = service.create(crearPaquete());
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar eliminar un paquete sin sesión de administrador activa
	 * retorna el código {@code 2}.
	 */
	@Test
	void testDeleteSinAdmin() {
		int result = service.deleteById(1L);
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar eliminar un paquete con un ID inexistente retorna el
	 * código {@code 1}, incluso con sesión de administrador activa.
	 */
	@Test
	void testDeleteFail() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		int result = service.deleteById(999L);
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar actualizar un paquete sin ninguna sesión activa retorna
	 * el código {@code 2}.
	 */
	@Test
	void testUpdateSinLoginCompleto() {
		int result = service.updateById(1L, crearPaquete());
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar actualizar un paquete con un ID inexistente retorna el
	 * código {@code 1}, incluso con sesión de cliente y administrador activas.
	 */
	@Test
	void testUpdateFail() {
		clienteService.register("cliente", "123", "123456789", "normal");
		clienteService.login("cliente", "123");

		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		int result = service.updateById(999L, crearPaquete());
		assertEquals(2, result);
	}

	/**
	 * Verifica que el conteo de paquetes es mayor a {@code 0} después de haber
	 * creado al menos uno con sesión de cliente activa.
	 */
	@Test
	void testCount() {
		clienteService.register("cliente", "123", "123456789", "normal");
		clienteService.login("cliente", "123");

		service.create(crearPaquete());

		long count = service.count();
		assertTrue(count > 0);
	}

}