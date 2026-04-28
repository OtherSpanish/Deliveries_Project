package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ManipuladorPaqueteDTO;
import co.edu.unbosque.proyectomodulo.entity.ManipuladorPaquete;
import co.edu.unbosque.proyectomodulo.repository.ManipuladorPaqueteRepository;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ManipuladorPaquetesService;

/**
 * Clase de pruebas unitarias para el servicio {@link ManipuladorPaquetesService}.
 * 
 * <p>Esta clase contiene casos de prueba que validan el comportamiento
 * de los métodos de gestión de manipuladores de paquetes, incluyendo
 * creación, consulta, eliminación, actualización, autenticación y
 * control de sesión.</p>
 * 
 * <p>Las pruebas se ejecutan en un contexto transaccional de Spring Boot
 * para garantizar aislamiento entre casos de prueba y la reversión
 * automática de los cambios realizados en la base de datos.</p>
 * 
 * <p>La mayoría de las operaciones requieren autenticación previa como
 * administrador, para lo cual se utiliza el método auxiliar
 * {@link #loginAdminTemp()}. El repositorio
 * {@link ManipuladorPaqueteRepository} se utiliza para operaciones de
 * verificación directa cuando es necesario.</p>
 */
@SpringBootTest
@Transactional
class ManipuladorPaquetesServiceTest {

	/**
	 * Servicio de manipuladores de paquetes inyectado para realizar las pruebas.
	 */
	@Autowired
	private ManipuladorPaquetesService manipuladorService;

	/**
	 * Servicio de administración inyectado para pruebas que requieren
	 * autenticación de administrador.
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * Repositorio de manipuladores de paquetes inyectado para operaciones
	 * de verificación directa en la base de datos durante las pruebas.
	 */
	@Autowired
	private ManipuladorPaqueteRepository manipuladorRepository;

	/**
	 * Método de configuración ejecutado antes de cada prueba.
	 * 
	 * <p>Cierra todas las sesiones activas tanto de administradores como
	 * de manipuladores de paquetes para garantizar que cada caso de prueba
	 * comience desde un estado inicial limpio y predecible.</p>
	 */
	@BeforeEach
	void limpiarSesiones() {
		adminService.logoutadmin();
		manipuladorService.logout();
	}

	/**
	 * Crea un objeto {@link ManipuladorPaqueteDTO} con datos válidos
	 * predefinidos para pruebas.
	 * 
	 * <p>Los datos incluyen:
	 * <ul>
	 *   <li>Usuario: "manipTest"</li>
	 *   <li>Contraseña: "pass"</li>
	 *   <li>Tiempo de trabajo: 8 horas</li>
	 * </ul></p>
	 * 
	 * @return instancia de {@code ManipuladorPaqueteDTO} con datos de prueba
	 *         válidos
	 */
	public ManipuladorPaqueteDTO crearManipuladorValido() {
		ManipuladorPaqueteDTO dto = new ManipuladorPaqueteDTO();
		dto.setUsuario("manipTest");
		dto.setContrasenia("pass");
		dto.setTiempoDeTrabajo(8);
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
	 * Prueba la creación exitosa de un manipulador de paquetes con datos
	 * válidos.
	 * 
	 * <p>Inicia sesión como administrador y verifica que el método
	 * {@code create} retorne {@code 0} cuando se proporcionan datos
	 * de manipulador de paquetes válidos y completos.</p>
	 */
	@Test
	void testCreateExitoso() {
		loginAdminTemp();
		int resultado = manipuladorService.create(crearManipuladorValido());
		assertEquals(0, resultado);
	}

	/**
	 * Prueba la creación de un manipulador de paquetes con un nombre de
	 * usuario duplicado.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador y luego
	 * intenta crear otro con el mismo nombre de usuario. Verifica que el
	 * segundo intento retorne {@code 3}, indicando que el usuario ya existe.</p>
	 */
	@Test
	void testCreateUsuarioDuplicado() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());
		int resultado = manipuladorService.create(crearManipuladorValido());
		assertEquals(3, resultado);
	}

	/**
	 * Prueba la creación de un manipulador de paquetes con un tiempo de
	 * trabajo inválido.
	 * 
	 * <p>Inicia sesión como administrador e intenta crear un manipulador
	 * con un tiempo de trabajo negativo. Verifica que el método
	 * {@code create} retorne {@code 1}, indicando un error de validación
	 * en las horas de trabajo.</p>
	 */
	@Test
	void testCreateHorasInvalidas() {
		loginAdminTemp();
		ManipuladorPaqueteDTO dto = crearManipuladorValido();
		dto.setTiempoDeTrabajo(-10);
		int resultado = manipuladorService.create(dto);
		assertEquals(1, resultado);
	}

	/**
	 * Prueba la creación de un manipulador de paquetes sin autenticación
	 * de administrador.
	 * 
	 * <p>Verifica que el método {@code create} retorne {@code 2} cuando
	 * no hay una sesión de administrador activa, indicando que se requiere
	 * autenticación para realizar esta operación.</p>
	 */
	@Test
	void testCreateSinAdmin() {
		int resultado = manipuladorService.create(crearManipuladorValido());
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la obtención de todos los manipuladores de paquetes sin
	 * autenticación de administrador.
	 * 
	 * <p>Verifica que el método {@code getAll} retorne {@code null} cuando
	 * no hay una sesión de administrador activa, indicando que se requiere
	 * autenticación para acceder a este recurso.</p>
	 */
	@Test
	void testGetAllSinAdmin() {
		String json = manipuladorService.getAll();
		assertNull(json);
	}

	/**
	 * Prueba la obtención de todos los manipuladores de paquetes con
	 * autenticación de administrador.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador y verifica
	 * que el método {@code getAll} retorne un JSON no nulo que contiene
	 * los datos del manipulador creado.</p>
	 */
	@Test
	void testGetAllConAdmin() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());
		String json = manipuladorService.getAll();
		assertNotNull(json);
		assertTrue(json.contains("manipTest"));
	}

	/**
	 * Prueba la eliminación de un manipulador de paquetes sin autenticación
	 * de administrador.
	 * 
	 * <p>Verifica que el método {@code deleteById} retorne {@code 2} cuando
	 * no hay una sesión de administrador activa, indicando que se requiere
	 * autenticación para realizar esta operación.</p>
	 */
	@Test
	void testDeleteByIdSinAdmin() {
		int resultado = manipuladorService.deleteById(1L);
		assertEquals(2, resultado);
	}

	/**
	 * Prueba la eliminación de un manipulador de paquetes con un ID
	 * inexistente.
	 * 
	 * <p>Inicia sesión como administrador e intenta eliminar un manipulador
	 * con un ID que no existe en el sistema. Verifica que el método retorne
	 * {@code 1}, indicando que el manipulador no fue encontrado.</p>
	 */
	@Test
	void testDeleteByIdInexistente() {
		loginAdminTemp();
		int resultado = manipuladorService.deleteById(999L);
		assertEquals(1, resultado);
	}

	/**
	 * Prueba la actualización exitosa de un manipulador de paquetes existente.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador, obtiene su
	 * ID desde el repositorio y luego lo actualiza con nuevos datos válidos.
	 * Verifica que el método {@code updateById} retorne {@code 0}, indicando
	 * que la actualización se realizó correctamente.</p>
	 */
	@Test
	void testUpdateByIdExitoso() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());

		ManipuladorPaquete entity = manipuladorRepository.findByUsuario("manipTest").orElseThrow();
		Long id = entity.getId();

		ManipuladorPaqueteDTO actualizado = new ManipuladorPaqueteDTO();
		actualizado.setUsuario("manipTestUpdated");
		actualizado.setContrasenia("newPass");
		actualizado.setTiempoDeTrabajo(6);

		int resultado = manipuladorService.updateById(id, actualizado);
		assertEquals(0, resultado);
	}

	/**
	 * Prueba la actualización de un manipulador de paquetes con un tiempo
	 * de trabajo inválido.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador e intenta
	 * actualizarlo con un tiempo de trabajo fuera del rango permitido
	 * (100 horas). Verifica que el método {@code updateById} retorne
	 * {@code 4}, indicando un error de validación en el tiempo de trabajo.</p>
	 */
	@Test
	void testUpdateByIdHorasInvalidas() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());
		ManipuladorPaquete entity = manipuladorRepository.findByUsuario("manipTest").orElseThrow();
		Long id = entity.getId();
		ManipuladorPaqueteDTO actualizado = new ManipuladorPaqueteDTO();
		actualizado.setUsuario("manipTestInvalid");
		actualizado.setContrasenia("pass");
		actualizado.setTiempoDeTrabajo(100);

		int resultado = manipuladorService.updateById(id, actualizado);
		assertEquals(4, resultado);
	}

	/**
	 * Prueba el inicio de sesión exitoso de un manipulador de paquetes.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador y luego
	 * intenta iniciar sesión como ese manipulador con las credenciales
	 * correctas. Verifica que el método {@code login} retorne {@code 0}
	 * y que el estado de autenticación sea verdadero.</p>
	 */
	@Test
	void testLoginExitoso() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());
		int resultado = manipuladorService.login("manipTest", "pass");
		assertEquals(0, resultado);
		assertTrue(manipuladorService.isLogged());
	}

	/**
	 * Prueba el inicio de sesión de un manipulador de paquetes con
	 * contraseña incorrecta.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador e intenta
	 * iniciar sesión con una contraseña incorrecta. Verifica que el método
	 * {@code login} retorne {@code 1} y que el estado de autenticación
	 * permanezca en falso.</p>
	 */
	@Test
	void testLoginFallido() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());
		int resultado = manipuladorService.login("manipTest", "wrong");
		assertEquals(1, resultado);
		assertFalse(manipuladorService.isLogged());
	}

	/**
	 * Prueba el cierre de sesión de un manipulador de paquetes autenticado.
	 * 
	 * <p>Inicia sesión como administrador, crea un manipulador, inicia
	 * sesión como ese manipulador, verifica el estado de autenticación
	 * y luego cierra la sesión. Confirma que el estado de autenticación
	 * cambie a falso después del cierre.</p>
	 */
	@Test
	void testLogout() {
		loginAdminTemp();
		manipuladorService.create(crearManipuladorValido());
		manipuladorService.login("manipTest", "pass");
		assertTrue(manipuladorService.isLogged());
		manipuladorService.logout();
		assertFalse(manipuladorService.isLogged());
	}

	/**
	 * Prueba el conteo de manipuladores de paquetes registrados en el sistema.
	 * 
	 * <p>Inicia sesión como administrador, compara el número de manipuladores
	 * antes y después de crear uno nuevo para verificar que el contador
	 * se incremente correctamente en una unidad.</p>
	 */
	@Test
	void testCount() {
		loginAdminTemp();
		long antes = manipuladorService.count();
		manipuladorService.create(crearManipuladorValido());
		assertEquals(antes + 1, manipuladorService.count());
	}
}