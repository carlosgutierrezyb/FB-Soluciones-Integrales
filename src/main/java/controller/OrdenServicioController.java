package controller;

import model.DetalleOrdenServicio;
import model.OrdenServicio;
import service.OrdenServicioService;

import java.sql.Date;
import java.util.List;

/**
 * Controller Órdenes Servicio.
 *
 * 🔥 ERP F&B:
 * - Comunicación View ↔ Service
 * - Crear órdenes
 * - Consultas
 * - Filtros
 */
public class OrdenServicioController {

    private OrdenServicioService service;

    public OrdenServicioController() {

        this.service =
                new OrdenServicioService();
    }

    // =========================
    // 🔹 CREAR ORDEN SERVICIO
    // =========================

    public String crearOrdenServicio(

            int idCliente,

            Date fechaProgramada,

            String prioridad,

            String direccionServicio,

            String contactoNombre,

            String contactoTelefono,

            String observaciones,

            List<DetalleOrdenServicio> detalles
    ) {

        return service.crearOrdenServicio(

                idCliente,

                fechaProgramada,

                prioridad,

                direccionServicio,

                contactoNombre,

                contactoTelefono,

                observaciones,

                detalles
        );
    }

    // =========================
    // 🔹 LISTAR TODAS
    // =========================

    public List<OrdenServicio> listarTodas() {

        return service.listarTodas();
    }

    // =========================
    // 🔹 LISTAR POR ESTADO
    // =========================

    public List<OrdenServicio> listarPorEstado(
            String estado
    ) {

        return service.listarPorEstado(
                estado
        );
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================

    public OrdenServicio buscarPorId(
            int idOrdenServicio
    ) {

        return service.buscarPorId(
                idOrdenServicio
        );
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================

    public List<DetalleOrdenServicio>
    obtenerDetalleOrden(

            int idOrdenServicio
    ) {

        return service.obtenerDetalleOrden(
                idOrdenServicio
        );
    }
}