package repository;

import model.OrdenCompra;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de órdenes de compra.
 *
 * 🔥 RESPONSABILIDAD:
 * - Crear órdenes de compra
 * - Consultar órdenes
 * - Actualizar estado
 */
public class OrdenCompraRepository {

    /**
     * 🔹 Crear orden de compra
     */
    public int crear(OrdenCompra orden) {

        String sql = "INSERT INTO ordenes_compra (id_proveedor, estado) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            System.out.println("🧾 Creando orden de compra...");

            ps.setInt(1, orden.getIdProveedor());
            ps.setString(2, orden.getEstado());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("No se pudo crear la orden.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    System.out.println("✅ Orden creada con ID: " + idGenerado);
                    return idGenerado;
                } else {
                    throw new SQLException("No se obtuvo el ID de la orden.");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error creando orden: " + e.getMessage());
            return -1;
        }
    }

    // =========================
    // 🔹 LISTAR PENDIENTES
    // =========================
    public List<OrdenCompra> listarPendientes() {

        List<OrdenCompra> lista = new ArrayList<>();

        String sql = "SELECT id, id_proveedor, estado " +
                "FROM ordenes_compra " +
                "WHERE estado IN ('Pendiente', 'Parcial')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                OrdenCompra o = new OrdenCompra();

                // 🔥 CORRECCIÓN AQUÍ
                o.setIdOrden(rs.getInt("id"));

                o.setIdProveedor(rs.getInt("id_proveedor"));
                o.setEstado(rs.getString("estado"));

                lista.add(o);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error listando órdenes pendientes: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // 🔹 ACTUALIZAR ESTADO (TRANSACCIÓN)
    // =========================
    public boolean actualizarEstado(Connection conn, int idOrden, String estado) throws SQLException {

        String sql = "UPDATE ordenes_compra SET estado = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idOrden);

            int filas = ps.executeUpdate();

            System.out.println("🔄 Estado de orden actualizado a: " + estado);

            return filas > 0;
        }
    }
}