package controller;

import model.DetalleOrdenCompra;
import service.CompraService;
import model.Producto;
import model.Proveedor;
import repository.ProductoRepository;
import repository.ProveedorRepository;

import java.util.List;

/**
 * Controlador del módulo de compras.
 *
 * 🔥 RESPONSABILIDAD:
 * - Recibir datos de la vista (carrito)
 * - Delegar al service
 *
 * ✔ Maneja múltiples productos
 * ✔ Código limpio (sin Object[])
 */
public class ComprasController {

    private CompraService compraService;
    private ProductoRepository productoRepo;
    private ProveedorRepository proveedorRepo;

    public ComprasController() {
        this.compraService = new CompraService();
        this.productoRepo = new ProductoRepository();
        this.proveedorRepo = new ProveedorRepository();
    }

    // =========================
    // 🔹 CREAR ORDEN DE COMPRA
    // =========================
    public String crearOrdenCompra(
            int idProveedor,
            List<DetalleOrdenCompra> detalles
    ) {

        try {

            System.out.println("🧾 Generando ORDEN DE COMPRA...");

            // =========================
            // 🔹 VALIDACIONES
            // =========================
            if (idProveedor <= 0) {
                return "Proveedor inválido.";
            }

            if (detalles == null || detalles.isEmpty()) {
                return "Debe agregar al menos un producto.";
            }

            // Validar cantidades
            for (DetalleOrdenCompra d : detalles) {
                if (d.getCantidadPedida() <= 0) {
                    return "Cantidad inválida en uno de los productos.";
                }
            }

            // =========================
            // 🔹 LLAMAR SERVICE
            // =========================
            return compraService.crearOrdenCompra(idProveedor, detalles);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error procesando la orden.";
        }
    }

    // =========================
    // 🔹 LISTAR PRODUCTOS
    // =========================
    public List<Producto> obtenerProductos() {
        return productoRepo.listarTodos();
    }

    // =========================
    // 🔹 LISTAR PROVEEDORES
    // =========================
    public List<Proveedor> obtenerProveedores() {
        return proveedorRepo.listarTodos();
    }
}