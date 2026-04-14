package controller;

import model.Producto;
import model.Compra;
import dao.ProductoDAO;
import dao.CompraDAO;
import view.ComprasView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de Compras
 *
 * Responsabilidades:
 * - Gestionar lógica de compras
 * - Validar datos de entrada
 * - Actualizar inventario automáticamente
 * - Servir de puente entre Vista y DAO
 */
public class ComprasController {

    private ComprasView view;
    private ProductoDAO productoDAO;
    private CompraDAO compraDAO;

    /**
     * Constructor con inyección de dependencias
     */
    public ComprasController(ComprasView view) {
        this.view = view;
        this.productoDAO = new ProductoDAO();
        this.compraDAO = new CompraDAO();
    }

    /**
     * Obtiene lista de productos para el combo
     */
    public List<Producto> obtenerProductos() {
        return productoDAO.obtenerTodos();
    }

    /**
     * Registra una compra y actualiza inventario
     */
    public String registrarCompra(int idProducto, String cantidadStr, String precioStr, String proveedor) {

        // =========================
        // VALIDACIONES
        // =========================
        if (cantidadStr == null || cantidadStr.isEmpty() ||
                precioStr == null || precioStr.isEmpty() ||
                proveedor == null || proveedor.isEmpty()) {

            return "Todos los campos son obligatorios.";
        }

        int cantidad;
        double precio;

        try {
            cantidad = Integer.parseInt(cantidadStr);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            return "Cantidad o precio inválidos.";
        }

        if (cantidad <= 0 || precio <= 0) {
            return "Valores deben ser mayores a cero.";
        }

        Producto producto = productoDAO.obtenerPorId(idProducto);

        if (producto == null) {
            return "Producto no encontrado.";
        }

        // =========================
        // CREAR COMPRA
        // =========================
        Compra compra = new Compra();
        compra.setIdProducto(idProducto);
        compra.setCantidad(cantidad);
        compra.setPrecioUnitario(precio);
        compra.setProveedor(proveedor);
        compra.setFecha(java.time.LocalDate.now());

        boolean guardado = compraDAO.registrarCompra(compra);

        if (!guardado) {
            return "Error al registrar compra.";
        }

        // =========================
        // ACTUALIZAR STOCK
        // =========================
        int nuevoStock = producto.getStockActual() + cantidad;

        productoDAO.actualizarStock(idProducto, nuevoStock);

        return "OK";
    }

    /**
     * Devuelve historial de compras (para tabla)
     */
    public List<String[]> obtenerHistorialCompras() {

        List<Compra> lista = compraDAO.obtenerCompras();
        List<String[]> datos = new ArrayList<>();

        for (Compra c : lista) {

            Producto p = productoDAO.obtenerPorId(c.getIdProducto());

            String nombreProducto = (p != null) ? p.getNombre() : "Desconocido";

            datos.add(new String[]{
                    nombreProducto,
                    String.valueOf(c.getCantidad())
            });
        }

        return datos;
    }
}