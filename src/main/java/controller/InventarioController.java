package controller;

import model.Categoria;
import model.Producto;
import service.ProductoService;
import view.InventarioView;

import java.util.List;

/**
 * Controlador encargado del módulo de Inventario.
 *
 * Responsabilidades:
 * - Gestionar productos
 * - Gestionar categorías
 *
 * ❌ NO maneja compras
 * ❌ NO contiene lógica de negocio
 *
 * ✔ Solo orquesta entre Vista y Services
 */
public class InventarioController {

    // =========================
    // DEPENDENCIAS
    // =========================

    private InventarioView vista;
    private ProductoService productoService;

    /**
     * Constructor vacío (compatibilidad)
     */
    public InventarioController() {
        inicializarDependencias();
    }

    /**
     * Constructor con vista (MVC real)
     */
    public InventarioController(InventarioView vista) {
        this.vista = vista;
        inicializarDependencias();
        inicializarEventos();
    }

    /**
     * Inicializa servicios
     */
    private void inicializarDependencias() {
        this.productoService = new ProductoService();
    }

    /**
     * Conecta eventos de la vista (opcional)
     */
    private void inicializarEventos() {

        if (vista == null) return;

        // Aquí puedes conectar botones si lo necesitas
        // (Actualmente ya lo manejas desde la vista, lo cual está bien)
    }

    // =========================
    // PRODUCTOS
    // =========================

    public String agregarNuevoProducto(String nombre, String stockStr, String idCatStr, String stockMinStr) {
        return productoService.agregarProducto(nombre, stockStr, idCatStr, stockMinStr);
    }

    public String editarProducto(int id, String nombre, String idCatStr, String stockMinStr) {
        return productoService.editarProducto(id, nombre, idCatStr, stockMinStr);
    }

    public boolean eliminarProducto(int id) {
        return productoService.eliminarProducto(id);
    }

    public List<Producto> obtenerInventario() {
        return productoService.listarProductos();
    }

    // =========================
    // CATEGORÍAS
    // =========================

    public List<Categoria> obtenerCategorias() {
        return productoService.listarCategorias();
    }
}