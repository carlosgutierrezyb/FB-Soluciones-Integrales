package repository;

import model.OrdenCompra;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository de órdenes de compra.
 *
 * 🔥 RESPONSABILIDAD:
 * - Crear órdenes de compra
 * - Retornar el ID generado
 */
public class OrdenCompraRepository {

    /**
     * Inserta una nueva orden de compra y retorna el ID generado.
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

            // 🔥 Obtener ID generado
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
}