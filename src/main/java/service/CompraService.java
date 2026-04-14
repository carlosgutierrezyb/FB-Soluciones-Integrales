package service;

import exception.BusinessException;
import model.EntradaAlmacen;
import repository.CompraRepository;
import repository.ProductoRepository;
import util.DatabaseConnection;
import util.ParseUtil;

import java.sql.Connection;

/**
 * Servicio de compras con manejo de transacciones reales.
 *
 * 🔥 RESPONSABILIDAD CRÍTICA:
 * - Garantizar consistencia de datos
 * - Manejar transacciones (commit / rollback)
 */
public class CompraService {

    private CompraRepository compraRepo;
    private ProductoRepository productoRepo;

    public CompraService() {
        this.compraRepo = new CompraRepository();
        this.productoRepo = new ProductoRepository();
    }

    /**
     * Registra una compra y actualiza stock en una sola transacción.
     */
    public String registrarCompra(int idProducto, String cantStr, String precioStr, String proveedor, String factura) {

        Connection conn = null;

        try {
            // =========================
            // 🔹 1. CONVERSIÓN
            // =========================
            int cantidad = ParseUtil.toPositiveInt(cantStr, "Cantidad");
            double precio = ParseUtil.toPositiveDouble(precioStr, "Precio");

            // =========================
            // 🔹 2. VALIDACIONES
            // =========================
            validarDatos(idProducto, factura, proveedor);

            // =========================
            // 🔹 3. CREAR MODELO
            // =========================
            EntradaAlmacen entrada = construirEntrada(idProducto, cantidad, precio, factura, proveedor);

            // =========================
            // 🔥 4. INICIAR TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // =========================
            // 🔹 5. INSERT COMPRA
            // =========================
            boolean compraOk = compraRepo.registrarEntrada(conn, entrada);

            if (!compraOk) {
                throw new RuntimeException("Error al registrar la compra");
            }

            // =========================
            // 🔹 6. ACTUALIZAR STOCK (MISMA CONEXIÓN 🔥)
            // =========================
            boolean stockOk = productoRepo.actualizarStock(conn, idProducto, cantidad);

            if (!stockOk) {
                throw new RuntimeException("Error al actualizar stock");
            }

            // =========================
            // 🔥 7. COMMIT
            // =========================
            conn.commit();

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);
            return e.getMessage();

        } catch (Exception e) {

            rollback(conn);
            e.printStackTrace();
            return "Error interno del sistema.";

        } finally {

            cerrarConexion(conn);
        }
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private void validarDatos(int idProducto, String factura, String proveedor) {

        if (idProducto <= 0) {
            throw new BusinessException("El producto es obligatorio.");
        }

        if (factura == null || factura.trim().isEmpty()) {
            throw new BusinessException("La factura es obligatoria.");
        }

        if (proveedor == null || proveedor.trim().isEmpty()) {
            throw new BusinessException("El proveedor es obligatorio.");
        }
    }

    private EntradaAlmacen construirEntrada(int idProducto, int cantidad, double precio, String factura, String proveedor) {

        EntradaAlmacen entrada = new EntradaAlmacen();
        entrada.setIdProducto(idProducto);
        entrada.setCantidad(cantidad);
        entrada.setPrecioCompra(precio);
        entrada.setNumeroFactura(factura);
        entrada.setProveedor(proveedor);

        return entrada;
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                System.out.println("↩️ Rollback ejecutado");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}