package controller;

import model.Servicio;
import service.ServicioService;

import java.util.List;

/**
 * Controller Servicios.
 *
 * 🔥 ERP F&B:
 * - Comunicación View ↔ Service
 * - Gestión catálogo servicios
 * - CRUD servicios
 */
public class ServicioController {

    private final ServicioService service;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ServicioController() {

        this.service =
                new ServicioService();
    }

    // =========================
    // 🔹 LISTAR ACTIVOS
    // =========================

    public List<Servicio> listarActivos() {

        return service.listarActivos();
    }

    // =========================
    // 🔹 LISTAR TODOS
    // =========================

    public List<Servicio> listarTodos() {

        return service.listarTodos();
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================

    public Servicio buscarPorId(
            int idServicio
    ) {

        return service.buscarPorId(
                idServicio
        );
    }

    // =========================
    // 🔹 GUARDAR DESDE VIEW
    // 🔥 RECIBE OBJETO SERVICIO
    // =========================

    public String guardarServicio(
            Servicio servicio
    ) {

        try {

            return service.guardarServicio(
                    servicio
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "Error registrando servicio.";
        }
    }

    // =========================
    // 🔹 GUARDAR POR CAMPOS
    // 🔥 Compatibilidad
    // =========================

    public String guardarServicio(

            String codigo,
            String nombre,
            String descripcion,
            String categoria,
            double precioBase,
            double tiempoEstimadoHoras,
            boolean estado

    ) {

        try {

            Servicio servicio =
                    new Servicio();

            servicio.setCodigo(
                    codigo
            );

            servicio.setNombre(
                    nombre
            );

            servicio.setDescripcion(
                    descripcion
            );

            servicio.setCategoria(
                    categoria
            );

            servicio.setPrecioBase(
                    precioBase
            );

            servicio.setTiempoEstimadoHoras(
                    tiempoEstimadoHoras
            );

            servicio.setEstado(
                    estado
            );

            return service.guardarServicio(
                    servicio
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "Error registrando servicio.";
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================

    public String actualizarServicio(
            Servicio servicio
    ) {

        try {

            return service.actualizarServicio(
                    servicio
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "Error actualizando servicio.";
        }
    }

    // =========================
    // 🔹 INACTIVAR
    // =========================

    public boolean inactivarServicio(
            int idServicio
    ) {

        try {

            return service.inactivarServicio(
                    idServicio
            );

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}