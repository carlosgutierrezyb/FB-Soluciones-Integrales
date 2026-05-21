package repository;

import model.DetalleFacturaCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository detalle factura compra
 *
 * 🔥 ERP PRO:
 * - Maneja líneas facturadas
 * - Soporta múltiples facturas por OC
 * - Soporta facturación parcial
 * - Controla trazabilidad:
 *      factura ↔ entrada ↔ producto
 */
public class DetalleFacturaCompraRepository {

    // =========================
    // 🔹 GUARDAR DETALLE
    // =========================
    public boolean guardar(
            Connection conn,
            DetalleFacturaCompra detalle
    ) throws SQLException {

        String sql =
                "INSERT INTO detalle_factura_compra (" +
                        "id_factura, " +
                        "id_entrada, " +
                        "id_item, " +
                        "cantidad_facturada, " +
                        "precio_unitario_factura, " +
                        "observacion" +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    detalle.getIdFactura()
            );

            ps.setInt(
                    2,
                    detalle.getIdEntrada()
            );

            ps.setInt(
                    3,
                    detalle.getIdItem()
            );

            ps.setInt(
                    4,
                    detalle.getCantidadFacturada()
            );

            ps.setDouble(
                    5,
                    detalle.getPrecioUnitarioFactura()
            );

            ps.setString(
                    6,
                    detalle.getObservacion()
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo guardar detalle factura."
                );
            }

            System.out.println(
                    "✅ Detalle factura registrado."
            );

            return true;
        }
    }

    // =========================
    // 🔹 GUARDAR LOTE
    // =========================
    public boolean guardarLote(

            Connection conn,

            List<DetalleFacturaCompra> detalles

    ) throws SQLException {

        if (
                detalles == null
                        || detalles.isEmpty()
        ) {

            throw new SQLException(
                    "No existen detalles."
            );
        }

        for (

                DetalleFacturaCompra detalle
                : detalles

        ) {

            guardar(
                    conn,
                    detalle
            );
        }

        return true;
    }

    // =========================
    // 🔹 VALIDAR SI YA EXISTE
    // 🔥 misma factura + misma entrada
    // =========================
    public boolean existeDetalleFactura(

            Connection conn,

            int idFactura,

            int idEntrada,

            int idItem

    ) throws SQLException {

        String sql =
                "SELECT COUNT(*) total " +
                        "FROM detalle_factura_compra " +
                        "WHERE id_factura = ? " +
                        "AND id_entrada = ? " +
                        "AND id_item = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idFactura);

            ps.setInt(2, idEntrada);

            ps.setInt(3, idItem);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

    // =========================
    // 🔹 TOTAL FACTURADO POR ITEM
    // =========================
    public int obtenerCantidadFacturadaItem(

            Connection conn,

            int idOrden,

            int idItem

    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(dfc.cantidad_facturada),0) total " +
                        "FROM detalle_factura_compra dfc " +
                        "INNER JOIN factura_compra fc " +
                        "ON dfc.id_factura = fc.id " +
                        "WHERE fc.id_orden = ? " +
                        "AND dfc.id_item = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idOrden);

            ps.setInt(2, idItem);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }

    // =========================
    // 🔹 ELIMINAR DETALLES FACTURA
    // =========================
    public boolean eliminarPorFactura(

            Connection conn,

            int idFactura

    ) throws SQLException {

        String sql =
                "DELETE FROM detalle_factura_compra " +
                        "WHERE id_factura = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idFactura);

            int filas =
                    ps.executeUpdate();

            System.out.println(
                    "🗑 Detalles eliminados: " + filas
            );

            return true;
        }
    }

    // =========================
    // 🔹 TOTAL FACTURA
    // =========================
    public double obtenerTotalFactura(

            Connection conn,

            int idFactura

    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(subtotal),0) total " +
                        "FROM detalle_factura_compra " +
                        "WHERE id_factura = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idFactura);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return rs.getDouble("total");
                }
            }
        }

        return 0;
    }

    // =========================
    // 🔹 VALIDAR SI OC ESTÁ COMPLETA
    // =========================
    public boolean ordenCompletamenteFacturada(

            Connection conn,

            int idOrden

    ) throws SQLException {

        String sql =
                "SELECT " +
                        "COALESCE(SUM(ea.cantidad_recibida),0) recibido, " +
                        "COALESCE(SUM(dfc.cantidad_facturada),0) facturado " +
                        "FROM entradas_almacen ea " +
                        "LEFT JOIN factura_compra fc " +
                        "ON ea.id_orden = fc.id_orden " +
                        "LEFT JOIN detalle_factura_compra dfc " +
                        "ON fc.id = dfc.id_factura " +
                        "AND ea.id_item = dfc.id_item " +
                        "WHERE ea.id_orden = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idOrden);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    int recibido =
                            rs.getInt("recibido");

                    int facturado =
                            rs.getInt("facturado");

                    return recibido > 0
                            && recibido == facturado;
                }
            }
        }

        return false;
    }
}