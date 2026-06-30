package controller;

import model.DetalleOrdenCompra;
import model.OrdenCompra;
import service.EntradaAlmacenService;
import service.OrdenCompraService;

import java.util.List;

/**
 * Controlador intermedio para gestionar el flujo de ingresos de mercancía al almacén.
 */
public class EntradaAlmacenController {

    private final EntradaAlmacenService entradaService;
    private final OrdenCompraService ordenService;

    public EntradaAlmacenController() {
        this.entradaService = new EntradaAlmacenService();
        this.ordenService = new OrdenCompraService();
    }

    /**
     * Recupera la lista de órdenes de compra aptas para recibir mercancía.
     */
    public List<OrdenCompra> obtenerOrdenesPendientes() {
        return ordenService.obtenerOrdenesPendientes();
    }

    /**
     * Devuelve los items y las cantidades estipuladas para una orden específica.
     */
    public List<DetalleOrdenCompra> obtenerDetalleOrden(int idOrden) {
        return ordenService.obtenerDetalleOrden(idOrden);
    }

    /**
     * Consulta la cantidad total acumulada que ya ha ingresado de un item específico.
     */
    public int obtenerCantidadRecibida(int idItem, int idOrden) {
        return entradaService.obtenerCantidadRecibida(idItem, idOrden);
    }

    /**
     * Despacha el registro de una nueva recepción parcial o total hacia la capa de negocio.
     */
    public String registrarEntrada(int idOrden, int idItem, int cantidad, String numeroFactura, String numeroRemision) {
        return entradaService.registrarEntrada(idOrden, idItem, cantidad, numeroFactura, numeroRemision);
    }
}