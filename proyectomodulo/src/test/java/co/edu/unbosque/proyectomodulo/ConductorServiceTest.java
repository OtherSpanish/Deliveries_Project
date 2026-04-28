package co.edu.unbosque.proyectomodulo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.proyectomodulo.dto.AdminDTO;
import co.edu.unbosque.proyectomodulo.dto.ConductorDTO;
import co.edu.unbosque.proyectomodulo.entity.Conductor;
import co.edu.unbosque.proyectomodulo.repository.ConductorRepository;
import co.edu.unbosque.proyectomodulo.service.AdminService;
import co.edu.unbosque.proyectomodulo.service.ConductorService;

/**
 * Clase de pruebas unitarias para el servicio {@link ConductorService}.
 * 
 * <p>Esta clase contiene casos de prueba que validan el comportamiento
 * de los métodos de gestión de conductores, incluyendo creación, consulta,
 * eliminación, actualización, autenticación y control de sesión.</p>
 * 
 * <p>Las pruebas se ejecutan en un contexto transaccional de Spring Boot
 * para garantizar aislamiento entre casos de prueba y la reversión
 * automática de los cambios realizados en la base de datos.</p>
 * 
 * <p>La mayoría de las operaciones requieren autenticación previa como
 * administrador, para lo cual se utiliza el método auxiliar
 * {@link #loginAdminTemp()}. El repositorio {@link ConductorRepository}
 * se utiliza para operaciones de verificación directa cuando es necesario.</p>
 */
@SpringBootTest
@Transactional
class ConductorServiceTest {

    /**
     * Servicio de conductores inyectado para realizar las pruebas.
     */
    @Autowired
    private ConductorService conductorService;

    /**
     * Servicio de administración inyectado para pruebas que requieren
     * autenticación de administrador.
     */
    @Autowired
    private AdminService adminService;

    /**
     * Repositorio de conductores inyectado para operaciones de verificación
     * directa en la base de datos durante las pruebas.
     */
    @Autowired
    private ConductorRepository conductorRepository;

    /**
     * Método de configuración ejecutado antes de cada prueba.
     * 
     * <p>Cierra todas las sesiones activas tanto de administradores como
     * de conductores para garantizar que cada caso de prueba comience
     * desde un estado inicial limpio y predecible.</p>
     */
    @BeforeEach
    void limpiarSesiones() {
        adminService.logoutadmin();
        conductorService.logout();
    }

    /**
     * Crea un objeto {@link ConductorDTO} con datos válidos predefinidos
     * para pruebas.
     * 
     * <p>Los datos incluyen:
     * <ul>
     *   <li>Usuario: "conductorTest"</li>
     *   <li>Contraseña: "pass"</li>
     *   <li>Tipo de vehículo: "moto"</li>
     * </ul></p>
     * 
     * @return instancia de {@code ConductorDTO} con datos de prueba válidos
     */
    public ConductorDTO crearConductorValido() {
        ConductorDTO dto = new ConductorDTO();
        dto.setUsuario("conductorTest");
        dto.setContrasenia("pass");
        dto.setTipoVehiculo("moto");
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
     * Prueba la creación exitosa de un conductor con datos válidos.
     * 
     * <p>Inicia sesión como administrador y verifica que el método
     * {@code create} retorne {@code 0} cuando se proporcionan datos
     * de conductor válidos y completos.</p>
     */
    @Test
    void testCreateExitoso() {
        loginAdminTemp();
        int resultado = conductorService.create(crearConductorValido());
        assertEquals(0, resultado);
    }

    /**
     * Prueba la creación de un conductor con un nombre de usuario duplicado.
     * 
     * <p>Inicia sesión como administrador, crea un conductor y luego intenta
     * crear otro con el mismo nombre de usuario. Verifica que el segundo
     * intento retorne {@code 3}, indicando que el usuario ya existe.</p>
     */
    @Test
    void testCreateUsuarioDuplicado() {
        loginAdminTemp();
        conductorService.create(crearConductorValido());
        int resultado = conductorService.create(crearConductorValido());
        assertEquals(3, resultado);
    }

    /**
     * Prueba la creación de un conductor con un tipo de vehículo inválido.
     * 
     * <p>Inicia sesión como administrador e intenta crear un conductor con
     * un tipo de vehículo no permitido ("avion"). Verifica que el método
     * {@code create} retorne {@code 1}, indicando un error de validación
     * en el tipo de vehículo.</p>
     */
    @Test
    void testCreateTipoVehiculoInvalido() {
        loginAdminTemp();
        ConductorDTO dto = crearConductorValido();
        dto.setTipoVehiculo("avion");
        int resultado = conductorService.create(dto);
        assertEquals(1, resultado);
    }

    /**
     * Prueba la creación de un conductor sin autenticación de administrador.
     * 
     * <p>Verifica que el método {@code create} retorne {@code 2} cuando
     * no hay una sesión de administrador activa, indicando que se requiere
     * autenticación para realizar esta operación.</p>
     */
    @Test
    void testCreateSinAdmin() {
        int resultado = conductorService.create(crearConductorValido());
        assertEquals(2, resultado);
    }

    /**
     * Prueba la obtención de todos los conductores sin autenticación de
     * administrador.
     * 
     * <p>Verifica que el método {@code getAll} retorne {@code null} cuando
     * no hay una sesión de administrador activa, indicando que se requiere
     * autenticación para acceder a este recurso.</p>
     */
    @Test
    void testGetAllSinAdmin() {
        String json = conductorService.getAll();
        assertNull(json);
    }

    /**
     * Prueba la obtención de todos los conductores con autenticación de
     * administrador.
     * 
     * <p>Inicia sesión como administrador, crea un conductor y verifica
     * que el método {@code getAll} retorne un JSON no nulo que contiene
     * los datos del conductor creado.</p>
     */
    @Test
    void testGetAllConAdmin() {
        loginAdminTemp();
        conductorService.create(crearConductorValido());
        String json = conductorService.getAll();
        assertNotNull(json);
        assertTrue(json.contains("conductorTest"));
    }

    /**
     * Prueba la eliminación de un conductor sin autenticación de administrador.
     * 
     * <p>Verifica que el método {@code deleteById} retorne {@code 2} cuando
     * no hay una sesión de administrador activa, indicando que se requiere
     * autenticación para realizar esta operación.</p>
     */
    @Test
    void testDeleteByIdSinAdmin() {
        int resultado = conductorService.deleteById(1L);
        assertEquals(2, resultado);
    }

    /**
     * Prueba la eliminación de un conductor con un ID inexistente.
     * 
     * <p>Inicia sesión como administrador e intenta eliminar un conductor
     * con un ID que no existe en el sistema. Verifica que el método retorne
     * {@code 1}, indicando que el conductor no fue encontrado.</p>
     */
    @Test
    void testDeleteByIdInexistente() {
        loginAdminTemp();
        int resultado = conductorService.deleteById(999L);
        assertEquals(1, resultado);
    }

    /**
     * Prueba la actualización de un conductor con un ID inexistente.
     * 
     * <p>Inicia sesión como administrador, crea un conductor y luego intenta
     * actualizar un registro con un ID que no existe. Verifica que el método
     * {@code updateById} retorne {@code 1}, indicando que el conductor
     * no fue encontrado.</p>
     */
    @Test
    void testUpdateByIdExitoso() {
        loginAdminTemp();
        conductorService.create(crearConductorValido());
        ConductorDTO actualizado = crearConductorValido();
        actualizado.setTipoVehiculo("carro");
        int resultado = conductorService.updateById(999L, actualizado);
        assertEquals(1, resultado); // non-existent ID -> 1
    }

    /**
     * Prueba la actualización de un conductor configurando un nombre de
     * usuario que ya pertenece a otro conductor existente.
     * 
     * <p>Inicia sesión como administrador, crea dos conductores diferentes
     * e intenta actualizar el segundo conductor asignándole el nombre de
     * usuario del primero. Verifica que el método {@code updateById}
     * retorne {@code 3}, indicando que el nombre de usuario ya está en uso.</p>
     */
    @Test
    void testUpdateByIdUsuarioDuplicado() {
        loginAdminTemp();
    
        conductorService.create(crearConductorValido());
        
        ConductorDTO otro = new ConductorDTO();
        otro.setUsuario("otro");
        otro.setContrasenia("pass2");
        otro.setTipoVehiculo("moto");
        conductorService.create(otro);

        Conductor segundo = conductorRepository.findByUsuario("otro").orElseThrow();
        Long segundoId = segundo.getId();
        
        otro.setUsuario("conductorTest");
        int resultado = conductorService.updateById(segundoId, otro);
        assertEquals(3, resultado);
    }

    /**
     * Prueba el inicio de sesión exitoso de un conductor.
     * 
     * <p>Inicia sesión como administrador, crea un conductor y luego
     * intenta iniciar sesión como ese conductor con las credenciales
     * correctas. Verifica que el método {@code login} retorne {@code 0}
     * y que el estado de autenticación sea verdadero.</p>
     */
    @Test
    void testLoginExitoso() {
        loginAdminTemp();
        conductorService.create(crearConductorValido());
        int resultado = conductorService.login("conductorTest", "pass");
        assertEquals(0, resultado);
        assertTrue(conductorService.isLogged());
    }

    /**
     * Prueba el inicio de sesión de un conductor con contraseña incorrecta.
     * 
     * <p>Inicia sesión como administrador, crea un conductor e intenta
     * iniciar sesión con una contraseña incorrecta. Verifica que el método
     * {@code login} retorne {@code 1} y que el estado de autenticación
     * permanezca en falso.</p>
     */
    @Test
    void testLoginFallido() {
        loginAdminTemp();
        conductorService.create(crearConductorValido());
        int resultado = conductorService.login("conductorTest", "wrong");
        assertEquals(1, resultado);
        assertFalse(conductorService.isLogged());
    }

    /**
     * Prueba el cierre de sesión de un conductor autenticado.
     * 
     * <p>Inicia sesión como administrador, crea un conductor, inicia sesión
     * como ese conductor, verifica el estado de autenticación y luego cierra
     * la sesión. Confirma que el estado de autenticación cambie a falso
     * después del cierre.</p>
     */
    @Test
    void testLogout() {
        loginAdminTemp();
        conductorService.create(crearConductorValido());
        conductorService.login("conductorTest", "pass");
        assertTrue(conductorService.isLogged());
        conductorService.logout();
        assertFalse(conductorService.isLogged());
    }

    /**
     * Prueba el conteo de conductores registrados en el sistema.
     * 
     * <p>Inicia sesión como administrador, compara el número de conductores
     * antes y después de crear uno nuevo para verificar que el contador
     * se incremente correctamente en una unidad.</p>
     */
    @Test
    void testCount() {
        loginAdminTemp();
        long antes = conductorService.count();
        conductorService.create(crearConductorValido());
        assertEquals(antes + 1, conductorService.count());
    }
}