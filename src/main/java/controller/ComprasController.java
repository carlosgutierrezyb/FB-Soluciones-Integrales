package controller;

import model.Producto;
import repository.ProductoRepository;
import service.CompraService;

import java.util.List;

/**
 * Controlador del módulo de compras.
 *
 * Responsabilidades:
 * - Conectar la vista con el servicio
 * - Proveer datos (productos)
 * - Ejecutar compras
 */
public class ComprasController {

    private CompraService compraService;
    private ProductoRepository productoRepo;

    public ComprasController() {
        this.compraService = new CompraService();
        this.productoRepo = new ProductoRepository();
    }

    /**
     * Retorna lista de productos para el combo.
     */
    public List<Producto> obtenerProductos() {
        return productoRepo.listarTodo();
    }

    /**
     * Ejecuta una compra.
     */
    public String registrarCompra(int idProducto, String cantidad, String precio, String proveedor, String factura) {
        return compraService.registrarCompra(idProducto, cantidad, precio, proveedor, factura);
    }
}