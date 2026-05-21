package controller;

import model.OrdenCompra;
import service.OrdenCompraService;

import java.util.List;

/**
 * Controller de Órdenes de Compra.
 *
 * 🔥 RESPONSABILIDADES:
 * - Comunicación Vista ↔ Service
 * - Consultas de órdenes
 * - Filtros por estado
 */
public class OrdenCompraController {

    private OrdenCompraService service;

    public OrdenCompraController() {

        this.service =
                new OrdenCompraService();
    }

    // =========================
    // 🔹 LISTAR TODAS
    // =========================
    public List<OrdenCompra> listarTodas() {

        return service.listarTodas();
    }

    // =========================
    // 🔹 LISTAR POR ESTADO
    // =========================
    public List<OrdenCompra> listarPorEstado(
            String estado
    ) {

        return service.listarPorEstado(
                estado
        );
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public OrdenCompra buscarPorId(int id) {

        return service.buscarPorId(id);
    }
}