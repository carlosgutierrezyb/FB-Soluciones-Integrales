package repository;

import model.DetalleOrdenServicio;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de detalle de órdenes de servicio.
 *
 * 🔥 ERP F&B:
 * Soporta:
 * - Servicios
 * - Productos
 * - Órdenes híbridas
 */
public class DetalleOrdenServicioRepository {

    // =========================
    // 🔹 INSERTAR LISTA
    // =========================
    public boolean insertarLista(
            List<DetalleOrdenServicio> lista,
            Connection conn
    ) throws SQLException {

        if (lista == null || lista.isEmpty()) {

            throw new SQLException(
                    "La lista de detalles está vacía."
            );
        }

        // 🔥 SQL CORREGIDO: Ajustado a las columnas reales de la tabla
        String sql =
                "INSERT INTO detalle_orden_servicio ("
                        + "id_orden_servicio, "
                        + "tipo_item, "
                        + "id_servicio, "
                        + "id_producto, "
                        + "codigo_referencia, "
                        + "cantidad, "
                        + "precio_unitario, "
                        + "observacion"
                        + ") "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            for (DetalleOrdenServicio d : lista) {

                if (d.getCantidad() <= 0) {

                    throw new SQLException(
                            "Cantidad inválida en detalle."
                    );
                }

                ps.setInt(
                        1,
                        d.getIdOrdenServicio()
                );

                ps.setString(
                        2,
                        d.getTipoItem()
                );

                // =========================
                // SERVICIO
                // =========================
                if ("SERVICIO".equals(d.getTipoItem())) {

                    ps.setInt(
                            3,
                            d.getIdServicio()
                    );

                } else {

                    ps.setNull(
                            3,
                            Types.INTEGER
                    );
                }

                // =========================
                // PRODUCTO
                // =========================
                if ("PRODUCTO".equals(d.getTipoItem())) {

                    ps.setInt(
                            4,
                            d.getIdProducto()
                    );

                } else {

                    ps.setNull(
                            4,
                            Types.INTEGER
                    );
                }

                // =========================
                // GENERALES
                // =========================
                ps.setString(
                        5,
                        d.getCodigoReferencia()
                );

                ps.setInt(
                        6,
                        d.getCantidad()
                );

                ps.setDouble(
                        7,
                        d.getPrecioUnitario()
                );

                ps.setString(
                        8,
                        d.getObservacion()
                );

                addBatchOpcional(ps);
            }

            ps.executeBatch();

            return true;
        }
    }

    // Auxiliar interno para encapsular la excepción del Batch
    private void addBatchOpcional(PreparedStatement ps) throws SQLException {
        ps.addBatch();
    }

    // =========================
    // 🔹 LISTAR POR ORDEN
    // =========================
    public List<DetalleOrdenServicio> listarPorOrden(
            int idOrdenServicio
    ) {

        List<DetalleOrdenServicio> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                        + "FROM detalle_orden_servicio "
                        + "WHERE id_orden_servicio = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idOrdenServicio
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                while (rs.next()) {

                    DetalleOrdenServicio d =
                            mapearDetalle(rs);

                    lista.add(d);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando detalles OS: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 OBTENER ITEM
    // =========================
    public DetalleOrdenServicio obtenerPorOrdenYReferencia(
            int idOrdenServicio,
            String tipoItem,
            int idReferencia
    ) {

        // 🔥 CORREGIDO: Filtra dinámicamente por la columna correspondiente al tipo
        String columnaId = "SERVICIO".equals(tipoItem) ? "id_servicio" : "id_producto";

        String sql =
                "SELECT * "
                        + "FROM detalle_orden_servicio "
                        + "WHERE id_orden_servicio = ? "
                        + "AND tipo_item = ? "
                        + "AND " + columnaId + " = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idOrdenServicio
            );

            ps.setString(
                    2,
                    tipoItem
            );

            ps.setInt(
                    3,
                    idReferencia
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return mapearDetalle(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error consultando detalle OS: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔥 ACTUALIZAR CANTIDAD
    // =========================
    public boolean actualizarCantidad(
            Connection conn,
            int idOrdenServicio,
            String tipoItem,
            int idReferencia,
            int nuevaCantidad
    ) throws SQLException {

        if (nuevaCantidad <= 0) {

            throw new SQLException(
                    "La cantidad debe ser mayor a 0."
            );
        }

        // 🔥 CORREGIDO: Apunta a la columna correcta del tipo de ítem
        String columnaId = "SERVICIO".equals(tipoItem) ? "id_servicio" : "id_producto";

        String sql =
                "UPDATE detalle_orden_servicio "
                        + "SET cantidad = ? "
                        + "WHERE id_orden_servicio = ? "
                        + "AND tipo_item = ? "
                        + "AND " + columnaId + " = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    nuevaCantidad
            );

            ps.setInt(
                    2,
                    idOrdenServicio
            );

            ps.setString(
                    3,
                    tipoItem
            );

            ps.setInt(
                    4,
                    idReferencia
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo actualizar el detalle."
                );
            }

            System.out.println(
                    "✏️ Cantidad actualizada."
            );

            return true;
        }
    }

    // =========================
    // 🔥 ELIMINAR ITEM
    // =========================
    public boolean eliminarItem(
            Connection conn,
            int idOrdenServicio,
            String tipoItem,
            int idReferencia
    ) throws SQLException {

        // 🔥 CORREGIDO: Apunta a la columna correcta del tipo de ítem
        String columnaId = "SERVICIO".equals(tipoItem) ? "id_servicio" : "id_producto";

        String sql =
                "DELETE FROM detalle_orden_servicio "
                        + "WHERE id_orden_servicio = ? "
                        + "AND tipo_item = ? "
                        + "AND " + columnaId + " = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idOrdenServicio
            );

            ps.setString(
                    2,
                    tipoItem
            );

            ps.setInt(
                    3,
                    idReferencia
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo eliminar el item."
                );
            }

            System.out.println(
                    "🗑️ Item eliminado correctamente."
            );

            return true;
        }
    }

    // =========================
    // 🔥 TOTAL ITEMS
    // =========================
    public int obtenerTotalItems(
            int idOrdenServicio
    ) {

        String sql =
                "SELECT "
                        + "COALESCE(SUM(cantidad), 0) AS total "
                        + "FROM detalle_orden_servicio "
                        + "WHERE id_orden_servicio = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idOrdenServicio
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error calculando total items: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // =========================
    // 🔥 MAPPER
    // =========================
    private DetalleOrdenServicio mapearDetalle(
            ResultSet rs
    ) throws SQLException {

        DetalleOrdenServicio d =
                new DetalleOrdenServicio();

        d.setIdDetalle(
                rs.getInt("id_detalle")
        );

        d.setIdOrdenServicio(
                rs.getInt("id_orden_servicio")
        );

        d.setTipoItem(
                rs.getString("tipo_item")
        );

        int idServicio =
                rs.getInt("id_servicio");

        if (!rs.wasNull()) {

            d.setIdServicio(idServicio);
        }

        int idProducto =
                rs.getInt("id_producto");

        if (!rs.wasNull()) {

            d.setIdProducto(idProducto);
        }

        d.setCodigoReferencia(
                rs.getString("codigo_referencia")
        );

        d.setCantidad(
                rs.getInt("cantidad")
        );

        d.setPrecioUnitario(
                rs.getDouble("precio_unitario")
        );

        d.setObservacion(
                rs.getString("observacion")
        );

        return d;
    }
}