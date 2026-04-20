package repository;

import model.EntradaAlmacen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository de entradas de almacén.
 *
 * 🔥 ERP REAL:
 * - Registra cada entrada (histórico)
 * - Actualiza inventario usando InventarioRepository
 * - Permite consultar cantidades recibidas
 */
public class EntradaAlmacenRepository {

    private InventarioRepository inventarioRepo;

    public EntradaAlmacenRepository() {
        this.inventarioRepo = new InventarioRepository();
    }

    /**
     * 🔥 Guarda entrada + actualiza inventario (PRO)
     */
    public boolean guardar(Connection conn, EntradaAlmacen entrada) {

        String sqlEntrada = "INSERT INTO entradas_almacen " +
                "(id_orden, id_item, cantidad_recibida, precio_compra_unitario, numero_factura) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement psEntrada = conn.prepareStatement(sqlEntrada)) {

            System.out.println("📦 Guardando entrada de almacén...");

            // =========================
            // 🔹 1. INSERT ENTRADA
            // =========================
            psEntrada.setInt(1, entrada.getIdOrden());
            psEntrada.setInt(2, entrada.getIdItem());
            psEntrada.setInt(3, entrada.getCantidadRecibida());
            psEntrada.setDouble(4, entrada.getPrecioCompraUnitario());
            psEntrada.setString(5, entrada.getNumeroFactura());

            int filasEntrada = psEntrada.executeUpdate();

            if (filasEntrada == 0) {
                throw new SQLException("No se pudo registrar la entrada.");
            }

            // =========================
            // 🔥 2. ACTUALIZAR INVENTARIO (BIEN HECHO)
            // =========================
            boolean actualizado = inventarioRepo.actualizarStock(
                    conn,
                    entrada.getIdItem(),
                    entrada.getCantidadRecibida()
            );

            if (!actualizado) {
                throw new SQLException("No se pudo actualizar el inventario.");
            }

            System.out.println("✅ Entrada registrada y stock actualizado correctamente.");

            return true;

        } catch (SQLException e) {

            System.err.println("❌ Error guardando entrada: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔹 TOTAL RECIBIDO POR ITEM Y ORDEN
    // =========================
    public int obtenerCantidadRecibida(int idItem, int idOrden) {

        String sql = "SELECT COALESCE(SUM(cantidad_recibida), 0) AS total " +
                "FROM entradas_almacen " +
                "WHERE id_item = ? AND id_orden = ?";

        try (Connection conn = util.DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idItem);
            ps.setInt(2, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error calculando cantidad recibida: " + e.getMessage());
        }

        return 0;
    }
}