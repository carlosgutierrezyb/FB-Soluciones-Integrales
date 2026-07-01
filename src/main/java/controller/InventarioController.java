package controller;

import model.Categoria;
import model.Producto;
import service.ProductoService;
import view.InventarioView;

import java.util.List;

/**
 * Controlador encargado del módulo de Inventario.
 *
 * 🔥 RESPONSABILIDADES:
 * - Gestionar productos
 * - Gestionar categorías
 * - Orquestar Vista ↔ Service
 *
 * ❌ NO contiene SQL
 * ❌ NO lógica de negocio
 */
public class InventarioController {

    // =========================
    // DEPENDENCIAS
    // =========================

    private InventarioView vista;

    private ProductoService productoService;

    // =========================
    // 🔹 CONSTRUCTOR VACÍO
    // =========================
    public InventarioController() {

        inicializarDependencias();
    }

    // =========================
    // 🔹 CONSTRUCTOR MVC
    // =========================
    public InventarioController(
            InventarioView vista
    ) {

        this.vista = vista;

        inicializarDependencias();

        inicializarEventos();
    }

    // =========================
    // 🔹 INICIALIZAR SERVICES
    // =========================
    private void inicializarDependencias() {

        this.productoService =
                new ProductoService();
    }

    // =========================
    // 🔹 EVENTOS
    // =========================
    private void inicializarEventos() {

        if (vista == null) {

            return;
        }

        // Eventos futuros
    }

    // =========================
    // 🔹 CREAR PRODUCTO
    // =========================
    public String agregarNuevoProducto(
            String nombre,
            String stockStr,
            String idCatStr,
            String stockMinStr
    ) {

        return productoService.agregarProducto(
                nombre,
                stockStr,
                idCatStr,
                stockMinStr
        );
    }

    // =========================
    // 🔹 EDITAR PRODUCTO
    // =========================
    public String editarProducto(
            int id,
            String nombre,
            String idCatStr,
            String stockMinStr
    ) {

        return productoService.editarProducto(
                id,
                nombre,
                idCatStr,
                stockMinStr
        );
    }

    // =========================
    // 🔹 INACTIVAR PRODUCTO
    // =========================
    public boolean eliminarProducto(int id) {

        return productoService.eliminarProducto(id);
    }

    // =========================
    // 🔹 REACTIVAR PRODUCTO
    // =========================
    public boolean reactivarProducto(int id) {

        return productoService.reactivarProducto(id);
    }

    // =========================
    // 🔹 LISTAR PRODUCTOS ACTIVOS
    // =========================
    public List<Producto> obtenerInventario() {

        return productoService.listarProductos();
    }

    // =========================
    // 🔹 LISTAR PRODUCTOS INACTIVOS
    // =========================
    public List<Producto> obtenerProductosInactivos() {

        return productoService.listarProductosInactivos();
    }

    // =========================
    // 🔹 LISTAR CATEGORÍAS
    // =========================
    public List<Categoria> obtenerCategorias() {

        return productoService.listarCategorias();
    }

    // =========================
    // 🔹 BUSCAR PRODUCTO POR ID
    // =========================
    public Producto buscarProductoPorId(
            int id
    ) {

        return productoService.buscarProductoPorId(id);
    }

    // =========================
    // 🔹 DESCONTAR STOCK TRANSACCIONAL
    // =========================
    public String descontarStock(int idProducto, int cantidad) {
        if (idProducto <= 0) return "ID de producto inválido.";
        if (cantidad <= 0) return "La cantidad a descontar debe ser mayor a cero.";

        try {
            boolean exito = productoService.descontarStock(idProducto, cantidad);
            return exito ? "OK" : "No se pudo actualizar el stock en el inventario.";
        } catch (Exception e) {
            return "Error en el módulo de inventario: " + e.getMessage();
        }
    }

}