package repository;

import model.DetalleOrdenServicio;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de detalle de órdenes de servicio.
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

        String sql =
                "INSERT INTO detalle_orden_servicio "
                        + "("
                        + "id_orden_servicio, "
                        + "id_servicio, "
                        + "cantidad, "
                        + "observacion"
                        + ") "
                        + "VALUES (?, ?, ?, ?)";

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

                ps.setInt(
                        2,
                        d.getIdServicio()
                );

                ps.setInt(
                        3,
                        d.getCantidad()
                );

                ps.setString(
                        4,
                        d.getObservacion()
                );

                ps.addBatch();
            }

            ps.executeBatch();

            return true;
        }
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
                "SELECT "
                        + "d.id_detalle, "
                        + "d.id_orden_servicio, "
                        + "d.id_servicio, "
                        + "s.nombre AS nombre_servicio, "
                        + "d.cantidad, "
                        + "d.observacion "
                        + "FROM detalle_orden_servicio d "
                        + "INNER JOIN servicios s "
                        + "ON d.id_servicio = s.id_servicio "
                        + "WHERE d.id_orden_servicio = ?";

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
                            new DetalleOrdenServicio();

                    d.setIdDetalle(
                            rs.getInt("id_detalle")
                    );

                    d.setIdOrdenServicio(
                            rs.getInt("id_orden_servicio")
                    );

                    d.setIdServicio(
                            rs.getInt("id_servicio")
                    );

                    d.setNombreServicio(
                            rs.getString("nombre_servicio")
                    );

                    d.setCantidad(
                            rs.getInt("cantidad")
                    );

                    d.setObservacion(
                            rs.getString("observacion")
                    );

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
    // 🔹 OBTENER SERVICIO
    // =========================
    public DetalleOrdenServicio obtenerPorOrdenYServicio(
            int idOrdenServicio,
            int idServicio
    ) {

        String sql =
                "SELECT "
                        + "d.id_detalle, "
                        + "d.id_orden_servicio, "
                        + "d.id_servicio, "
                        + "s.nombre AS nombre_servicio, "
                        + "d.cantidad, "
                        + "d.observacion "
                        + "FROM detalle_orden_servicio d "
                        + "INNER JOIN servicios s "
                        + "ON d.id_servicio = s.id_servicio "
                        + "WHERE d.id_orden_servicio = ? "
                        + "AND d.id_servicio = ?";

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

            ps.setInt(
                    2,
                    idServicio
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    DetalleOrdenServicio d =
                            new DetalleOrdenServicio();

                    d.setIdDetalle(
                            rs.getInt("id_detalle")
                    );

                    d.setIdOrdenServicio(
                            rs.getInt("id_orden_servicio")
                    );

                    d.setIdServicio(
                            rs.getInt("id_servicio")
                    );

                    d.setNombreServicio(
                            rs.getString("nombre_servicio")
                    );

                    d.setCantidad(
                            rs.getInt("cantidad")
                    );

                    d.setObservacion(
                            rs.getString("observacion")
                    );

                    return d;
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
            int idServicio,
            int nuevaCantidad
    ) throws SQLException {

        if (nuevaCantidad <= 0) {

            throw new SQLException(
                    "La cantidad debe ser mayor a 0."
            );
        }

        String sql =
                "UPDATE detalle_orden_servicio "
                        + "SET cantidad = ? "
                        + "WHERE id_orden_servicio = ? "
                        + "AND id_servicio = ?";

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

            ps.setInt(
                    3,
                    idServicio
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo actualizar el detalle."
                );
            }

            System.out.println(
                    "✏️ Cantidad servicio actualizada."
            );

            return true;
        }
    }

    // =========================
    // 🔥 ELIMINAR SERVICIO
    // =========================
    public boolean eliminarServicio(
            Connection conn,
            int idOrdenServicio,
            int idServicio
    ) throws SQLException {

        String sql =
                "DELETE FROM detalle_orden_servicio "
                        + "WHERE id_orden_servicio = ? "
                        + "AND id_servicio = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idOrdenServicio
            );

            ps.setInt(
                    2,
                    idServicio
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo eliminar el servicio."
                );
            }

            System.out.println(
                    "🗑️ Servicio eliminado correctamente."
            );

            return true;
        }
    }

    // =========================
    // 🔥 TOTAL SERVICIOS
    // =========================
    public int obtenerTotalServicios(
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
                    "❌ Error calculando total servicios: "
                            + e.getMessage()
            );
        }

        return 0;
    }
}