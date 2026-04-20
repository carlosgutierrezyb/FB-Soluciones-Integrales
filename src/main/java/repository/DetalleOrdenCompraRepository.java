package repository;

import model.DetalleOrdenCompra;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de detalle de órdenes de compra.
 *
 * 🔥 RESPONSABILIDAD:
 * - Insertar ítems de una orden
 * - Consultar detalles
 * - Soportar entradas parciales
 * - Permitir modificación controlada de órdenes
 */
public class DetalleOrdenCompraRepository {

    // =========================
    // 🔹 INSERTAR LISTA
    // =========================
    public boolean insertarLista(List<DetalleOrdenCompra> lista, Connection conn) throws SQLException {

        if (lista == null || lista.isEmpty()) {
            throw new SQLException("La lista de detalles está vacía.");
        }

        String sql = "INSERT INTO detalle_orden (id_orden, id_item, cantidad_pedida) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            for (DetalleOrdenCompra d : lista) {

                if (d.getCantidadPedida() <= 0) {
                    throw new SQLException("Cantidad inválida en detalle.");
                }

                ps.setInt(1, d.getIdOrden());
                ps.setInt(2, d.getIdItem());
                ps.setInt(3, d.getCantidadPedida());

                ps.addBatch();
            }

            ps.executeBatch();

            return true;
        }
    }

    // =========================
    // 🔹 LISTAR POR ORDEN
    // =========================
    public List<DetalleOrdenCompra> listarPorOrden(int idOrden) {

        List<DetalleOrdenCompra> lista = new ArrayList<>();

        String sql = "SELECT id_orden, id_item, cantidad_pedida " +
                "FROM detalle_orden WHERE id_orden = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    DetalleOrdenCompra d = new DetalleOrdenCompra();
                    d.setIdOrden(rs.getInt("id_orden"));
                    d.setIdItem(rs.getInt("id_item"));
                    d.setCantidadPedida(rs.getInt("cantidad_pedida"));

                    lista.add(d);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error listando detalles: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // 🔹 OBTENER ITEM ESPECÍFICO
    // =========================
    public DetalleOrdenCompra obtenerPorOrdenYItem(int idOrden, int idItem) {

        String sql = "SELECT id_orden, id_item, cantidad_pedida " +
                "FROM detalle_orden " +
                "WHERE id_orden = ? AND id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            ps.setInt(2, idItem);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    DetalleOrdenCompra d = new DetalleOrdenCompra();
                    d.setIdOrden(rs.getInt("id_orden"));
                    d.setIdItem(rs.getInt("id_item"));
                    d.setCantidadPedida(rs.getInt("cantidad_pedida"));

                    return d;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error consultando detalle: " + e.getMessage());
        }

        return null;
    }

    // =========================
    // 🔥 ACTUALIZAR CANTIDAD (PRO)
    // =========================
    public boolean actualizarCantidad(Connection conn, int idOrden, int idItem, int nuevaCantidad) throws SQLException {

        if (nuevaCantidad <= 0) {
            throw new SQLException("La cantidad debe ser mayor a 0.");
        }

        String sql = "UPDATE detalle_orden SET cantidad_pedida = ? " +
                "WHERE id_orden = ? AND id_item = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idOrden);
            ps.setInt(3, idItem);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("No se pudo actualizar el detalle.");
            }

            System.out.println("✏️ Cantidad actualizada correctamente.");

            return true;
        }
    }

    // =========================
    // 🔥 ELIMINAR ITEM (PRO)
    // =========================
    public boolean eliminarItem(Connection conn, int idOrden, int idItem) throws SQLException {

        String sql = "DELETE FROM detalle_orden WHERE id_orden = ? AND id_item = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            ps.setInt(2, idItem);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("No se pudo eliminar el ítem.");
            }

            System.out.println("🗑️ Ítem eliminado correctamente.");

            return true;
        }
    }

    // =========================
    // 🔥 TOTAL PEDIDO
    // =========================
    public int obtenerTotalPedido(int idOrden) {

        String sql = "SELECT COALESCE(SUM(cantidad_pedida), 0) AS total " +
                "FROM detalle_orden WHERE id_orden = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error calculando total pedido: " + e.getMessage());
        }

        return 0;
    }
}