package controller;

import model.Categoria;
import model.Producto;
import repository.CategoriaRepository;
import repository.ProductoRepository;
import service.ProductoService;
import service.CompraService;
import view.InventarioView;

import java.util.List;

/**
 * Controlador encargado de coordinar la interacción entre la Vista y la lógica del sistema.
 */
public class InventarioController {

    // 🔹 Vista
    private InventarioView vista;

    // 🔹 Repositorios
    private ProductoRepository productoRepo;
    private CategoriaRepository categoriaRepo;

    // 🔹 Servicios
    private ProductoService productoService;
    private CompraService compraService;

    /**
     * Constructor original (lo dejamos para no romper nada)
     */
    public InventarioController() {
        inicializarDependencias();
    }

    /**
     * 🔥 Constructor PRO con inyección de vista (MVC real)
     */
    public InventarioController(InventarioView vista) {
        this.vista = vista;
        inicializarDependencias();
        inicializarEventos(); // 🔥 clave
    }

    /**
     * Inicializa repositorios y servicios
     */
    private void inicializarDependencias() {
        this.productoRepo = new ProductoRepository();
        this.categoriaRepo = new CategoriaRepository();
        this.productoService = new ProductoService();
        this.compraService = new CompraService();
    }

    /**
     * 🔥 Conecta la vista con la lógica
     */
    private void inicializarEventos() {

        if (vista == null) return;

        // 👉 Ejemplo (ajústalo a tus botones reales)
        /*
        vista.getBtnAgregar().addActionListener(e -> {
            String nombre = vista.getTxtNombre().getText();
            String stock = vista.getTxtStock().getText();
            String categoria = vista.getTxtCategoria().getText();
            String stockMin = vista.getTxtStockMin().getText();

            String resultado = agregarNuevoProducto(nombre, stock, categoria, stockMin);

            JOptionPane.showMessageDialog(vista, resultado);
        });
        */
    }

    // ================== MÉTODOS ACTUALES ==================

    public String agregarNuevoProducto(String nombre, String stockStr, String idCatStr, String stockMinStr) {
        return productoService.agregarProducto(nombre, stockStr, idCatStr, stockMinStr);
    }

    public boolean eliminarProducto(int id) {
        return productoRepo.eliminar(id);
    }

    public String editarProducto(int id, String nombre, String idCatStr, String stockMinStr) {
        return productoService.editarProducto(id, nombre, idCatStr, stockMinStr);
    }

    public List<Producto> obtenerInventario() {
        return productoRepo.listarTodo();
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepo.listarCategorias();
    }

    public String registrarCompra(int idItem, String cantStr, String precioStr, String factura) {
        return compraService.registrarCompra(idItem, cantStr, precioStr, factura);
    }
}