package repository;

import model.FacturaCompra;

import java.sql.*;

/**
 * Repository de factura de compra.
 *
 * 🔥 ERP REAL:
 * - Maneja encabezado de factura
 * - Relaciona con orden de compra
 * - Permite validar entradas pendientes
 */
public class FacturaCompraRepository {

    // =========================
    // 🔹 CREAR FACTURA
    // =========================
    public int crear(Connection conn, FacturaCompra factura) throws SQLException {

        String sql = "INSERT INTO factura_compra (id_orden, numero_factura, fecha, estado) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, factura.getIdOrden()); // 🔥 CAMBIO CLAVE
            ps.setString(2, factura.getNumeroFactura());
            ps.setTimestamp(3, Timestamp.valueOf(factura.getFecha()));
            ps.setString(4, factura.getEstado());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("No se pudo crear la factura.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("✅ Factura creada ID: " + id);
                    return id;
                }
            }
        }

        throw new SQLException("No se obtuvo ID de factura.");
    }

    // =========================
    // 🔹 VALIDAR ENTRADAS SIN FACTURA
    // =========================
    public boolean existenEntradasSinFactura(Connection conn, int idOrden) throws SQLException {

        String sql = "SELECT COUNT(*) AS total " +
                "FROM entradas_almacen " +
                "WHERE id_orden = ? AND id_factura IS NULL";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    return total > 0;
                }
            }
        }

        return false;
    }

    // =========================
    // 🔹 ASOCIAR ENTRADAS A FACTURA
    // =========================
    public boolean asociarEntradas(Connection conn, int idFactura, int idOrden) throws SQLException {

        String sql = "UPDATE entradas_almacen " +
                "SET id_factura = ? " +
                "WHERE id_orden = ? AND id_factura IS NULL";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            ps.setInt(2, idOrden);

            int filas = ps.executeUpdate();

            System.out.println("📄 Entradas asociadas a factura: " + filas);

            return filas > 0;
        }
    }
}