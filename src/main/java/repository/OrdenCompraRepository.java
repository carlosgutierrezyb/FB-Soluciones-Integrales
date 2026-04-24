package repository;

import model.OrdenCompra;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de órdenes de compra.
 *
 * 🔥 ERP PRO:
 * - Crear órdenes de compra
 * - Consultar órdenes pendientes
 * - Consultar órdenes pendientes de facturación
 * - Actualizar estado
 */
public class OrdenCompraRepository {

    // =========================
    // 🔹 CREAR ORDEN DE COMPRA
    // =========================
    public int crear(OrdenCompra orden) {

        String sql = "INSERT INTO ordenes_compra (id_proveedor, estado) "
                + "VALUES (?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            System.out.println("🧾 Creando orden de compra...");

            ps.setInt(1, orden.getIdProveedor());
            ps.setString(2, orden.getEstado());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo crear la orden."
                );
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    int idGenerado = rs.getInt(1);

                    System.out.println(
                            "✅ Orden creada con ID: " + idGenerado
                    );

                    return idGenerado;
                }
            }

            throw new SQLException(
                    "No se obtuvo el ID de la orden."
            );

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error creando orden: " + e.getMessage()
            );

            return -1;
        }
    }

    // =========================
    // 🔹 LISTAR ÓRDENES PENDIENTES
    // =========================
    public List<OrdenCompra> listarPendientes() {

        List<OrdenCompra> lista = new ArrayList<>();

        String sql = "SELECT id_orden, id_proveedor, estado "
                + "FROM ordenes_compra "
                + "WHERE estado IN ('Pendiente', 'Parcial') "
                + "ORDER BY id_orden DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando órdenes pendientes: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔥 ÓRDENES PARA FACTURA DE COMPRA
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {

        List<OrdenCompra> lista = new ArrayList<>();

        String sql =
                "SELECT DISTINCT oc.id_orden, oc.id_proveedor, oc.estado "
                        + "FROM ordenes_compra oc "
                        + "INNER JOIN entradas_almacen ea "
                        + "   ON oc.id_orden = ea.id_orden "
                        + "WHERE ea.id_factura IS NULL "
                        + "ORDER BY oc.id_orden DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo órdenes para factura: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public OrdenCompra buscarPorId(int idOrden) {

        String sql = "SELECT id_orden, id_proveedor, estado "
                + "FROM ordenes_compra "
                + "WHERE id_orden = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error buscando orden: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔹 ACTUALIZAR ESTADO
    // =========================
    public boolean actualizarEstado(
            Connection conn,
            int idOrden,
            String estado
    ) throws SQLException {

        String sql = "UPDATE ordenes_compra "
                + "SET estado = ? "
                + "WHERE id_orden = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idOrden);

            int filas = ps.executeUpdate();

            System.out.println(
                    "🔄 Estado de orden actualizado a: "
                            + estado
            );

            return filas > 0;
        }
    }

    // =========================
    // 🔧 MAPPER CENTRAL
    // =========================
    private OrdenCompra mapearOrden(ResultSet rs)
            throws SQLException {

        OrdenCompra orden = new OrdenCompra();

        orden.setIdOrden(
                rs.getInt("id_orden")
        );

        orden.setIdProveedor(
                rs.getInt("id_proveedor")
        );

        orden.setEstado(
                rs.getString("estado")
        );

        return orden;
    }
}