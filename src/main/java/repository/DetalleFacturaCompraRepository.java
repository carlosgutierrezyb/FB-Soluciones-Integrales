package repository;

import model.DetalleFacturaCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Repository de detalle de factura de compra.
 *
 * 🔥 ERP PRO:
 * - Guarda cada línea facturada
 * - Relaciona factura ↔ entrada ↔ producto
 * - Permite trazabilidad completa
 * - NO maneja lógica de negocio
 *
 * Tabla:
 * detalle_factura_compra
 */
public class DetalleFacturaCompraRepository {

    // =========================
    // 🔹 GUARDAR DETALLE FACTURA
    // =========================
    public boolean guardar(Connection conn, DetalleFacturaCompra detalle) throws SQLException {

        String sql = "INSERT INTO detalle_factura_compra ("
                + "id_factura, "
                + "id_entrada, "
                + "id_item, "
                + "cantidad_facturada, "
                + "precio_unitario_factura, "
                + "observacion"
                + ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdFactura());
            ps.setInt(2, detalle.getIdEntrada());
            ps.setInt(3, detalle.getIdItem());
            ps.setInt(4, detalle.getCantidadFacturada());
            ps.setDouble(5, detalle.getPrecioUnitarioFactura());
            ps.setString(6, detalle.getObservacion());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("No se pudo guardar el detalle de factura.");
            }

            System.out.println("✅ Detalle de factura registrado correctamente.");

            return true;
        }
    }

    // =========================
    // 🔹 GUARDAR MÚLTIPLES DETALLES
    // =========================
    public boolean guardarLote(
            Connection conn,
            java.util.List<DetalleFacturaCompra> detalles
    ) throws SQLException {

        if (detalles == null || detalles.isEmpty()) {
            throw new SQLException("No existen detalles para registrar.");
        }

        for (DetalleFacturaCompra detalle : detalles) {

            boolean ok = guardar(conn, detalle);

            if (!ok) {
                throw new SQLException(
                        "Falló el registro de un detalle de factura."
                );
            }
        }

        return true;
    }

    // =========================
    // 🔹 VALIDAR DUPLICADO
    // =========================
    public boolean existeDetalleFactura(
            Connection conn,
            int idFactura,
            int idEntrada
    ) throws SQLException {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM detalle_factura_compra "
                + "WHERE id_factura = ? "
                + "AND id_entrada = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            ps.setInt(2, idEntrada);

            try (var rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

    // =========================
    // 🔹 ELIMINAR DETALLES POR FACTURA
    // =========================
    public boolean eliminarPorFactura(
            Connection conn,
            int idFactura
    ) throws SQLException {

        String sql = "DELETE FROM detalle_factura_compra "
                + "WHERE id_factura = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);

            int filas = ps.executeUpdate();

            System.out.println(
                    "🗑 Detalles eliminados de factura: " + filas
            );

            return true;
        }
    }

    // =========================
    // 🔹 TOTAL FACTURADO POR FACTURA
    // =========================
    public double obtenerTotalFactura(
            Connection conn,
            int idFactura
    ) throws SQLException {

        String sql = "SELECT COALESCE(SUM(subtotal), 0) AS total "
                + "FROM detalle_factura_compra "
                + "WHERE id_factura = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);

            try (var rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }

        return 0;
    }
}