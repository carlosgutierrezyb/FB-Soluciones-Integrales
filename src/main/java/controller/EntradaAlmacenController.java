package controller;

import model.DetalleOrdenCompra;
import model.OrdenCompra;
import service.EntradaAlmacenService;

import java.util.List;

/**
 * Controlador de entradas de almacén.
 *
 * 🔥 RESPONSABILIDAD:
 * - Recibir solicitudes desde la UI
 * - Delegar al Service
 */
public class EntradaAlmacenController {

    private EntradaAlmacenService service;

    public EntradaAlmacenController() {
        this.service = new EntradaAlmacenService();
    }

    // =========================
    // 🔹 ÓRDENES PENDIENTES
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {
        return service.obtenerOrdenesPendientes();
    }

    // =========================
    // 🔹 DETALLE DE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(int idOrden) {
        return service.obtenerDetalleOrden(idOrden);
    }

    // =========================
    // 🔹 CANTIDAD RECIBIDA
    // =========================
    public int obtenerCantidadRecibida(int idItem, int idOrden) {
        return service.obtenerCantidadRecibida(idItem, idOrden);
    }

    // =========================
    // 🔹 REGISTRAR ENTRADA
    // =========================
    public String registrarEntrada(int idOrden, int idItem, int cantidad) {
        return service.registrarEntrada(idOrden, idItem, cantidad);
    }
}