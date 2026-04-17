package controller;

import model.Producto;
import model.Categoria;
import service.ProductoService;

import java.util.List;

/**
 * Controlador del módulo de productos.
 *
 * 🔥 RESPONSABILIDAD:
 * - Recibir datos de la vista
 * - Delegar al servicio
 * - NO contiene lógica de negocio
 */
public class ProductoController {

    private ProductoService productoService;

    public ProductoController() {
        this.productoService = new ProductoService();
    }

    // =========================
    // CREAR PRODUCTO
    // =========================

    public String agregarProducto(
            String nombre,
            String stock,
            String idCategoria,
            String stockMinimo
    ) {

        return productoService.agregarProducto(
                nombre,
                stock,
                idCategoria,
                stockMinimo
        );
    }

    // =========================
    // EDITAR PRODUCTO
    // =========================

    public String editarProducto(
            int id,
            String nombre,
            String idCategoria,
            String stockMinimo
    ) {

        return productoService.editarProducto(
                id,
                nombre,
                idCategoria,
                stockMinimo
        );
    }

    // =========================
    // ELIMINAR PRODUCTO
    // =========================

    public boolean eliminarProducto(int id) {
        return productoService.eliminarProducto(id);
    }

    // =========================
    // LISTAR PRODUCTOS
    // =========================

    public List<Producto> obtenerProductos() {
        return productoService.listarProductos();
    }

    // =========================
    // LISTAR CATEGORÍAS
    // =========================

    public List<Categoria> obtenerCategorias() {
        return productoService.listarCategorias();
    }
}