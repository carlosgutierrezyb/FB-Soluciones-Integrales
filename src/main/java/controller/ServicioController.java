package controller;

import model.Servicio;
import service.ServicioService;

import java.util.List;

/**
 * Controller Servicios.
 *
 * 🔥 ERP F&B:
 * - Comunicación View ↔ Service
 * - Catálogo servicios
 * - Base órdenes servicio
 */
public class ServicioController {

    private ServicioService service;

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
    // 🔹 GUARDAR
    // =========================
    public String guardarServicio(

            String nombre,
            String descripcion,
            boolean estado

    ) {

        try {

            Servicio servicio =
                    new Servicio();

            servicio.setNombre(
                    nombre
            );

            servicio.setDescripcion(
                    descripcion
            );

            servicio.setEstado(
                    estado
            );

            return service.guardarServicio(
                    servicio
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "Error en controller servicio.";
        }
    }
}