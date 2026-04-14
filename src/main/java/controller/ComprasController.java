package controller;

import model.Producto;
import service.CompraService;
import service.ProductoService;

import java.util.List;

/**
 * Controlador del módulo de Compras.
 *
 * Responsabilidades:
 * - Conectar la vista de compras con la lógica de negocio
 * - Delegar operaciones al CompraService
 *
 * 🔥 NO contiene lógica de negocio
 */
public class ComprasController {

    private CompraService compraService;
    private ProductoService productoService;

    /**
     * Constructor
     */
    public ComprasController() {
        this.compraService = new CompraService();
        this.productoService = new ProductoService();
    }

    /**
     * Obtiene lista de productos para el combo.
     */
    public List<Producto> obtenerProductos() {
        return productoService.listarProductos();
    }

    /**
     * Registra una compra.
     */
    public String registrarCompra(int idProducto, String cantidad, String precio, String factura) {
        return compraService.registrarCompra(idProducto, cantidad, precio, factura);
    }
}