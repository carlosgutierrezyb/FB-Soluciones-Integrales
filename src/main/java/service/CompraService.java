package service;

import exception.BusinessException;
import model.DetalleOrdenCompra;
import model.OrdenCompra;
import repository.DetalleOrdenCompraRepository;
import repository.OrdenCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de compras (nivel ERP).
 *
 * 🔥 RESPONSABILIDAD:
 * - Crear orden de compra (pedido al proveedor)
 * - Registrar múltiples productos (detalle)
 * - Manejar transacciones
 *
 * ❌ NO maneja entradas de almacén (eso va en otro flujo)
 */
public class CompraService {

    private OrdenCompraRepository ordenRepo;
    private DetalleOrdenCompraRepository detalleRepo;

    public CompraService() {
        this.ordenRepo = new OrdenCompraRepository();
        this.detalleRepo = new DetalleOrdenCompraRepository();
    }

    /**
     * 🔥 CREAR ORDEN DE COMPRA (PRO ERP)
     */
    public String crearOrdenCompra(
            int idProveedor,
            List<DetalleOrdenCompra> detalles
    ) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 1. VALIDACIONES
            // =========================
            if (idProveedor <= 0) {
                throw new BusinessException("Proveedor inválido.");
            }

            if (detalles == null || detalles.isEmpty()) {
                throw new BusinessException("Debe agregar al menos un producto.");
            }

            // =========================
            // 🔹 2. INICIAR TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("🚀 Iniciando creación de orden de compra...");

            // =========================
            // 🔹 3. CREAR ORDEN
            // =========================
            OrdenCompra orden = new OrdenCompra();
            orden.setIdProveedor(idProveedor);
            orden.setEstado("Pendiente");

            int idOrden = ordenRepo.crear(orden);

            if (idOrden <= 0) {
                throw new SQLException("No se pudo crear la orden.");
            }

            // =========================
            // 🔹 4. ASIGNAR ORDEN A DETALLES
            // =========================
            for (DetalleOrdenCompra d : detalles) {
                d.setIdOrden(idOrden);
            }

            // =========================
            // 🔹 5. INSERTAR DETALLE (BATCH)
            // =========================
            boolean detallesOK = detalleRepo.insertarLista(detalles, conn);

            if (!detallesOK) {
                throw new SQLException("Error insertando detalle de orden.");
            }

            // =========================
            // 🔹 6. COMMIT
            // =========================
            conn.commit();

            System.out.println("✅ Orden de compra creada correctamente.");

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);
            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();
            rollback(conn);
            return "Error interno creando la orden.";

        } finally {

            cerrarConexion(conn);
        }
    }

    // =========================
    // 🔧 MÉTODOS AUXILIARES
    // =========================

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                System.out.println("⚠️ Rollback ejecutado.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}