package co.edu.unbosque.proyectomodulo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.proyectomodulo.dto.ManipuladorPaqueteDTO;
import co.edu.unbosque.proyectomodulo.entity.ManipuladorPaquete;
import co.edu.unbosque.proyectomodulo.exceptions.HoraTrabajoException;
import co.edu.unbosque.proyectomodulo.exceptions.LanzadorException;
import co.edu.unbosque.proyectomodulo.repository.ManipuladorPaqueteRepository;

/**
 * Servicio que gestiona las operaciones CRUD y autenticación
 * para la entidad {@link ManipuladorPaquete}. Permite la creación,
 * actualización, eliminación y consulta de manipuladores,
 * así como el manejo de sesión.
 *
 * @version 1.0
 */
@Service
public class ManipuladorPaquetesService implements CRUDOPERATION<ManipuladorPaqueteDTO> {

    /** Repositorio para acceso a datos de manipuladores de paquetes. */
    @Autowired
    private ManipuladorPaqueteRepository manipuladorRep;

    /** Servicio de administrador para validación de permisos. */
    @Autowired
    private AdminService adminService;

    /** Mapper para conversión entre entidades y DTOs. */
    @Autowired
    private ModelMapper mapper;

    /** Manipulador actualmente autenticado en el sistema. */
    private ManipuladorPaquete manipuladorLogueado;

    /**
     * Constructor por defecto.
     */
    public ManipuladorPaquetesService() {
        super();
    }

    /**
     * Crea un nuevo manipulador de paquetes.
     * Requiere autenticación de administrador, validación de horas de trabajo
     * y que el usuario no exista previamente.
     *
     * @param data datos del manipulador
     * @return {@code 0} creado,
     *         {@code 1} error de validación o usuario existente,
     *         {@code 2} sin permisos
     */
    @Override
    public int create(ManipuladorPaqueteDTO data) {

        if (!adminService.isLoggedadmin()) {
            return 2;
        }

        try {
            LanzadorException.verificarHoraDeTrabajo(data.getTiempoDeTrabajo());
        } catch (HoraTrabajoException e) {
            return 1;
        }

        Optional<ManipuladorPaquete> encontrado = manipuladorRep.findByUsuario(data.getUsuario());
        if (encontrado.isPresent()) {
            return 1;
        }

        ManipuladorPaquete entity = mapper.map(data, ManipuladorPaquete.class);
        manipuladorRep.save(entity);
        return 0;
    }

    /**
     * Obtiene todos los manipuladores registrados.
     * Requiere autenticación de administrador.
     *
     * @return lista de manipuladores o {@code null} si no hay permisos
     */
    @Override
    public List<ManipuladorPaqueteDTO> getAll() {

        if (!adminService.isLoggedadmin()) {
            return null;
        }

        List<ManipuladorPaquete> entityList = (List<ManipuladorPaquete>) manipuladorRep.findAll();
        List<ManipuladorPaqueteDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity ->
            dtoList.add(mapper.map(entity, ManipuladorPaqueteDTO.class))
        );

        return dtoList;
    }

    /**
     * Elimina un manipulador por su ID.
     * Requiere autenticación de administrador.
     *
     * @param id identificador del manipulador
     * @return {@code 0} eliminado,
     *         {@code 1} no encontrado,
     *         {@code 2} sin permisos
     */
    @Override
    public int deleteById(Long id) {

        if (!adminService.isLoggedadmin()) {
            return 2;
        }

        Optional<ManipuladorPaquete> encontrado = manipuladorRep.findById(id);

        if (encontrado.isPresent()) {
            manipuladorRep.delete(encontrado.get());
            return 0;
        }

        return 1;
    }

    /**
     * Actualiza un manipulador existente.
     * Requiere autenticación de administrador y validación de horas de trabajo.
     *
     * @param id   identificador del manipulador
     * @param data nuevos datos
     * @return {@code 0} actualizado,
     *         {@code 1} error de validación o no encontrado,
     *         {@code 2} sin permisos,
     *         {@code 3} usuario duplicado
     */
    @Override
    public int updateById(Long id, ManipuladorPaqueteDTO data) {

        if (!adminService.isLoggedadmin()) {
            return 2;
        }

        Optional<ManipuladorPaquete> encontradoID = manipuladorRep.findById(id);
        Optional<ManipuladorPaquete> encontradoUsuario = manipuladorRep.findByUsuario(data.getUsuario());

        if (encontradoID.isPresent() && encontradoUsuario.isPresent()) {
            return 3;
        }

        if (encontradoID.isPresent()) {

            ManipuladorPaqueteDTO temp = mapper.map(encontradoID.get(), ManipuladorPaqueteDTO.class);
            temp.setUsuario(data.getUsuario());
            temp.setContrasenia(data.getContrasenia());
            temp.setTiempoDeTrabajo(data.getTiempoDeTrabajo());

            try {
                LanzadorException.verificarHoraDeTrabajo(data.getTiempoDeTrabajo());
            } catch (HoraTrabajoException e) {
                return 1;
            }

            ManipuladorPaquete entity = mapper.map(temp, ManipuladorPaquete.class);
            entity.setId(id);
            manipuladorRep.save(entity);

            return 0;
        }

        return 1;
    }

    /**
     * Obtiene la cantidad total de manipuladores.
     *
     * @return número de registros
     */
    @Override
    public long count() {
        return manipuladorRep.count();
    }

    /**
     * Verifica si existe un manipulador por su ID.
     *
     * @param id identificador
     * @return {@code true} si existe
     */
    @Override
    public boolean exist(Long id) {
        return manipuladorRep.existsById(id);
    }

    /**
     * Inicia sesión de un manipulador.
     *
     * @param usuario     nombre de usuario
     * @param contrasenia contraseña
     * @return {@code 0} éxito, {@code 1} credenciales inválidas
     */
    public int login(String usuario, String contrasenia) {

        Optional<ManipuladorPaquete> encontrado = manipuladorRep.findByUsuario(usuario);

        if (encontrado.isPresent()
                && encontrado.get().getContrasenia().equals(contrasenia)) {

            manipuladorLogueado = encontrado.get();
            return 0;
        }

        return 1;
    }

    /**
     * Cierra la sesión del manipulador actual.
     */
    public void logout() {
        manipuladorLogueado = null;
    }

    /**
     * Verifica si hay un manipulador autenticado.
     *
     * @return {@code true} si hay sesión activa
     */
    public boolean isLogged() {
        return manipuladorLogueado != null;
    }
}