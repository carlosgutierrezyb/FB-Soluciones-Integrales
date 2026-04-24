package controller;

import model.DetalleOrdenCompra;
import model.OrdenCompra;
import service.EntradaAlmacenService;
import service.OrdenCompraService;

import java.util.List;

/**
 * Controlador de entradas de almacén.
 *
 * 🔥 ERP PRO:
 * - UI delega aquí
 * - órdenes → OrdenCompraService
 * - entradas → EntradaAlmacenService
 */
public class EntradaAlmacenController {

    private EntradaAlmacenService entradaService;
    private OrdenCompraService ordenService;

    public EntradaAlmacenController() {
        this.entradaService = new EntradaAlmacenService();
        this.ordenService = new OrdenCompraService();
    }

    // =========================
    // 🔹 ÓRDENES PENDIENTES
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {
        return ordenService.obtenerOrdenesPendientes();
    }

    // =========================
    // 🔹 DETALLE DE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(int idOrden) {
        return ordenService.obtenerDetalleOrden(idOrden);
    }

    // =========================
    // 🔹 CANTIDAD RECIBIDA
    // =========================
    public int obtenerCantidadRecibida(int idItem, int idOrden) {
        return entradaService.obtenerCantidadRecibida(idItem, idOrden);
    }

    // =========================
    // 🔥 REGISTRAR ENTRADA (CORREGIDO PRO)
    // =========================
    public String registrarEntrada(
            int idOrden,
            int idItem,
            int cantidad,
            double precioCompra,
            String numeroFactura
    ) {
        return entradaService.registrarEntrada(
                idOrden,
                idItem,
                cantidad,
                precioCompra,
                numeroFactura
        );
    }
}