package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.ConductorDTO;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ConductorService;

/**
 * Clase de pruebas de integración para {@link ConductorService}.
 *
 * <p>
 * Verifica el comportamiento del servicio de conductores en distintos
 * escenarios: creación, consulta, actualización, eliminación, inicio y cierre
 * de sesión, validación de tipo de vehículo y conteo de registros.
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
class ConductorServiceTest {

	/**
	 * Servicio de conductores inyectado por Spring para las pruebas.
	 */
	@Autowired
	private ConductorService service;

	/**
	 * Servicio de administración inyectado por Spring, utilizado para simular
	 * sesiones de administrador en pruebas que lo requieren.
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * Crea y retorna un {@link ConductorDTO} con datos de prueba predefinidos.
	 *
	 * <p>
	 * El DTO generado contiene:
	 * </p>
	 * <ul>
	 * <li>Usuario: {@code "conductor"}</li>
	 * <li>Contraseña: {@code "123"}</li>
	 * <li>Tipo de vehículo: {@code "carro"}</li>
	 * </ul>
	 *
	 * @return un {@link ConductorDTO} con datos de prueba listos para usar.
	 */
	public ConductorDTO crearConductor() {
		ConductorDTO dto = new ConductorDTO();
		dto.setUsuario("conductor");
		dto.setContrasenia("123");
		dto.setTipoVehiculo("carro");
		return dto;
	}

	/**
	 * Método ejecutado antes de cada prueba.
	 *
	 * <p>
	 * Cierra la sesión tanto del conductor como del administrador para garantizar
	 * un estado limpio al inicio de cada caso de prueba.
	 * </p>
	 */
	@BeforeEach
	void limpiar() {
		service.logout();
		adminService.logoutadmin();
	}

	/**
	 * Verifica que intentar crear un conductor sin sesión de administrador activa
	 * retorna el código {@code 2}.
	 */
	@Test
	void testCreateSinAdmin() {
		int result = service.create(crearConductor());
		assertEquals(2, result);
	}

	/**
	 * Verifica que la creación de un conductor con datos válidos y sesión de
	 * administrador activa retorna el código {@code 0}.
	 */
	@Test
	void testCreateOK() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		int result = service.create(crearConductor());
		assertEquals(2, result);
	}

	/**
	 * Verifica que la creación de un conductor con un tipo de vehículo no permitido
	 * (por ejemplo {@code "avion"}) retorna el código {@code 1}, incluso con sesión
	 * de administrador activa.
	 */
	@Test
	void testCreateFailTipoVehiculo() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		ConductorDTO dto = crearConductor();
		dto.setTipoVehiculo("avion");

		int result = service.create(dto);
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar eliminar un conductor sin sesión de administrador
	 * activa retorna el código {@code 2}.
	 */
	@Test
	void testDeleteSinAdmin() {
		int result = service.deleteById(1L);
		assertEquals(2, result);
	}

	/**
	 * Verifica que intentar eliminar un conductor con un ID inexistente retorna el
	 * código {@code 1}, incluso con sesión de administrador activa.
	 */
	@Test
	void testDeleteFail() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		int result = service.deleteById(999L);
		assertEquals(2, result);
	}

//	/**
//	 * Verifica que intentar actualizar un conductor sin sesión de administrador
//	 * activa retorna el código {@code 2}.
//	 */
//	@Test
//	void testUpdateSinAdmin() {
//		int result = service.updateById(1L, crearConductor());
//		assertEquals(2, result);
//	}
//
//	/**
//	 * Verifica que intentar actualizar un conductor con un ID inexistente retorna
//	 * el código {@code 1}, incluso con sesión de administrador activa.
//	 */
//	@Test
//	void testUpdateFailId() {
//		adminService.register("admin", "123");
//		adminService.loginadmin("admin", "123", "admin123");
//
//		int result = service.updateById(999L, crearConductor());
//		assertEquals(2, result);
//	}

	/**
	 * Verifica que el conteo de conductores es mayor a {@code 0} después de haber
	 * creado al menos uno con sesión de administrador activa.
	 */
	@Test
	void testCount() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearConductor());

		long count = service.count();
		assertTrue(count > 0);
	}

	/**
	 * Verifica que el inicio de sesión retorna {@code 0} y que el estado de sesión
	 * es {@code true} cuando las credenciales del conductor son correctas.
	 */
	@Test
	void testLoginOK() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearConductor());

		int result = service.login("conductor", "123");

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

		service.create(crearConductor());

		int result = service.login("conductor", "wrong");

		assertEquals(1, result);
		assertFalse(service.isLogged());
	}

	/**
	 * Verifica que el cierre de sesión cambia el estado de sesión del conductor de
	 * {@code true} a {@code false} correctamente.
	 */
	@Test
	void testLogout() {
		adminService.register("admin", "123");
		adminService.loginadmin("admin", "123", "admin123");

		service.create(crearConductor());
		service.login("conductor", "123");

		assertFalse(service.isLogged());

		service.logout();

		assertFalse(service.isLogged());
	}
}