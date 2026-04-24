package repository;

import model.FacturaCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de factura de compra.
 *
 * 🔥 ERP PRO REAL:
 * - Maneja encabezado de factura
 * - NO usa id_factura en entradas_almacen
 * - Usa detalle_factura_compra
 * - Permite múltiples facturas por OC
 * - Permite múltiples entradas por factura
 */
public class FacturaCompraRepository {

    // =========================
    // 🔹 CREAR FACTURA (ENCABEZADO)
    // =========================
    public int crear(Connection conn, FacturaCompra factura) throws SQLException {

        String sql = "INSERT INTO factura_compra " +
                "(id_proveedor, numero_factura, fecha, estado, observacion) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {

            ps.setInt(1, factura.getIdProveedor());
            ps.setString(2, factura.getNumeroFactura());
            ps.setTimestamp(3, Timestamp.valueOf(factura.getFecha()));
            ps.setString(4, factura.getEstado());
            ps.setString(5, factura.getObservacion());

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
    // 🔹 OBTENER ENTRADAS PENDIENTES DE FACTURAR
    // =========================
    public List<Integer> obtenerEntradasPendientes(Connection conn, int idOrden) throws SQLException {

        List<Integer> lista = new ArrayList<>();

        String sql = "SELECT ea.id_entrada " +
                "FROM entradas_almacen ea " +
                "LEFT JOIN detalle_factura_compra dfc " +
                "ON ea.id_entrada = dfc.id_entrada " +
                "WHERE ea.id_orden = ? " +
                "AND dfc.id_entrada IS NULL";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(rs.getInt("id_entrada"));
                }
            }
        }

        return lista;
    }

    // =========================
    // 🔹 VALIDAR SI EXISTEN ENTRADAS PENDIENTES
    // =========================
    public boolean existenEntradasPendientes(Connection conn, int idOrden) throws SQLException {

        List<Integer> pendientes = obtenerEntradasPendientes(conn, idOrden);
        return !pendientes.isEmpty();
    }

    // =========================
    // 🔹 CREAR DETALLE FACTURA
    // =========================
    public boolean crearDetalleFactura(
            Connection conn,
            int idFactura,
            int idEntrada,
            int idItem,
            int cantidadFacturada,
            double precioUnitario
    ) throws SQLException {

        String sql = "INSERT INTO detalle_factura_compra " +
                "(id_factura, id_entrada, id_item, cantidad_facturada, precio_unitario_factura, observacion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            ps.setInt(2, idEntrada);
            ps.setInt(3, idItem);
            ps.setInt(4, cantidadFacturada);
            ps.setDouble(5, precioUnitario);
            ps.setString(6, "Registro automático desde factura");

            int filas = ps.executeUpdate();

            return filas > 0;
        }
    }
}