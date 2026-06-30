package repository;

import model.DetalleOrdenServicio;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente de persistencia encargado de gestionar el ciclo de vida de los ítems detallados
 * (tanto servicios técnicos como productos físicos) vinculados a las órdenes de servicio.
 */
public class DetalleOrdenServicioRepository {

    /**
     * Inserta una colección de detalles de forma atómica utilizando procesamiento por lotes (Batch).
     *
     * @param lista Colección de detalles a registrar.
     * @param conn  Conexión transaccional activa provista por la capa de servicio.
     * @return true si todo el lote se insertó correctamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de persistencia o se detecta una cantidad inválida.
     */
    public boolean insertarLista(List<DetalleOrdenServicio> lista, Connection conn) throws SQLException {
        if (lista == null || lista.isEmpty()) {
            throw new SQLException("La lista de detalles no puede estar vacía o nula.");
        }

        String sql = "INSERT INTO detalle_orden_servicio (id_orden_servicio, tipo_item, id_servicio, " +
                "id_producto, codigo_referencia, cantidad, precio_unitario, observacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DetalleOrdenServicio d : lista) {
                if (d.getCantidad() <= 0) {
                    throw new SQLException("Se detectó un detalle con una cantidad igual o inferior a cero.");
                }

                ps.setInt(1, d.getIdOrdenServicio());
                ps.setString(2, d.getTipoItem());

                if ("SERVICIO".equalsIgnoreCase(d.getTipoItem())) {
                    ps.setInt(3, d.getIdServicio());
                    ps.setNull(4, Types.INTEGER);
                } else if ("PRODUCTO".equalsIgnoreCase(d.getTipoItem())) {
                    ps.setNull(3, Types.INTEGER);
                    ps.setInt(4, d.getIdProducto());
                } else {
                    ps.setNull(3, Types.INTEGER);
                    ps.setNull(4, Types.INTEGER);
                }

                ps.setString(5, d.getCodigoReferencia());
                ps.setInt(6, d.getCantidad());
                ps.setDouble(7, d.getPrecioUnitario());
                ps.setString(8, d.getObservacion());

                ps.addBatch();
            }

            int[] resultados = ps.executeBatch();
            for (int resultado : resultados) {
                if (resultado == Statement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Recupera el detalle completo de ítems asociados a una orden de servicio.
     *
     * @param idOrdenServicio Identificador de la orden maestra.
     * @return Lista de detalles mapeados.
     */
    public List<DetalleOrdenServicio> listarPorOrden(int idOrdenServicio) {
        List<DetalleOrdenServicio> lista = new ArrayList<>();
        String sql = "SELECT id_detalle, id_orden_servicio, tipo_item, id_servicio, id_producto, " +
                "codigo_referencia, cantidad, precio_unitario, observacion " +
                "FROM detalle_orden_servicio WHERE id_orden_servicio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrdenServicio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDetalle(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar detalles de la orden de servicio: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un ítem específico dentro del detalle de una orden filtrando por su tipo e identificador de referencia.
     */
    public DetalleOrdenServicio obtenerPorOrdenYReferencia(int idOrdenServicio, String tipoItem, int idReferencia) {
        String columnaId = "SERVICIO".equalsIgnoreCase(tipoItem) ? "id_servicio" : "id_producto";
        String sql = "SELECT id_detalle, id_orden_servicio, tipo_item, id_servicio, id_producto, " +
                "codigo_referencia, cantidad, precio_unitario, observacion " +
                "FROM detalle_orden_servicio WHERE id_orden_servicio = ? AND tipo_item = ? AND " + columnaId + " = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrdenServicio);
            ps.setString(2, tipoItem);
            ps.setInt(3, idReferencia);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDetalle(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar ítem específico del detalle: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza la cantidad de un ítem del detalle compartiendo el contexto transaccional.
     */
    public boolean actualizarCantidad(Connection conn, int idOrdenServicio, String tipoItem, int idReferencia, int nuevaCantidad) throws SQLException {
        if (nuevaCantidad <= 0) {
            throw new SQLException("La cantidad a actualizar debe ser estrictamente mayor a cero.");
        }

        String columnaId = "SERVICIO".equalsIgnoreCase(tipoItem) ? "id_servicio" : "id_producto";
        String sql = "UPDATE detalle_orden_servicio SET cantidad = ? WHERE id_orden_servicio = ? AND tipo_item = ? AND " + columnaId + " = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idOrdenServicio);
            ps.setString(3, tipoItem);
            ps.setInt(4, idReferencia);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("No se modificó ninguna fila; la referencia especificada no existe.");
            }
            return true;
        }
    }

    /**
     * Remueve un ítem del detalle de la orden de servicio dentro de un bloque transaccional.
     */
    public boolean eliminarItem(Connection conn, int idOrdenServicio, String tipoItem, int idReferencia) throws SQLException {
        String columnaId = "SERVICIO".equalsIgnoreCase(tipoItem) ? "id_servicio" : "id_producto";
        String sql = "DELETE FROM detalle_orden_servicio WHERE id_orden_servicio = ? AND tipo_item = ? AND " + columnaId + " = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrdenServicio);
            ps.setString(2, tipoItem);
            ps.setInt(3, idReferencia);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("No se pudo eliminar el ítem; registro no localizado.");
            }
            return true;
        }
    }

    /**
     * Calcula de forma agregada la sumatoria total de unidades de ítems registradas en la orden.
     */
    public int obtenerTotalItems(int idOrdenServicio) {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) AS total FROM detalle_orden_servicio WHERE id_orden_servicio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrdenServicio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular el consolidado total de ítems: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Procesa la conversión de una fila del ResultSet hacia la entidad de control DetalleOrdenServicio.
     */
    private DetalleOrdenServicio mapearDetalle(ResultSet rs) throws SQLException {
        DetalleOrdenServicio d = new DetalleOrdenServicio();
        d.setIdDetalle(rs.getInt("id_detalle"));
        d.setIdOrdenServicio(rs.getInt("id_orden_servicio"));
        d.setTipoItem(rs.getString("tipo_item"));

        int idServicio = rs.getInt("id_servicio");
        if (!rs.wasNull()) {
            d.setIdServicio(idServicio);
        }

        int idProducto = rs.getInt("id_producto");
        if (!rs.wasNull()) {
            d.setIdProducto(idProducto);
        }

        d.setCodigoReferencia(rs.getString("codigo_referencia"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioUnitario(rs.getDouble("precio_unitario"));
        d.setObservacion(rs.getString("observacion"));
        return d;
    }
}