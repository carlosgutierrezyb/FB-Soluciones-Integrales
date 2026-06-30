package repository;

import model.EntradaAlmacen;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Persistencia encargada de registrar los movimientos físicos de entrada y actualizar existencias.
 */
public class EntradaAlmacenRepository {

    private final InventarioRepository inventarioRepo;

    public EntradaAlmacenRepository() {
        this.inventarioRepo = new InventarioRepository();
    }

    /**
     * Inserta el registro de entrada física y actualiza el stock en una misma transacción.
     */
    public boolean guardar(Connection conn, EntradaAlmacen entrada) {
        String sql = "INSERT INTO entradas_almacen (id_orden, id_item, cantidad_recibida, "
                + "precio_compra_unitario, numero_remision, numero_factura) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entrada.getIdOrden());
            ps.setInt(2, entrada.getIdItem());
            ps.setInt(3, entrada.getCantidadRecibida());
            ps.setDouble(4, entrada.getPrecioCompraUnitario());
            ps.setString(5, entrada.getNumeroRemision());
            ps.setString(6, entrada.getNumeroFactura());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se insertaron registros en entradas_almacen.");
            }

            // Afectación directa del stock disponible en el inventario general
            boolean actualizado = inventarioRepo.actualizarStock(conn, entrada.getIdItem(), entrada.getCantidadRecibida());
            if (!actualizado) {
                throw new SQLException("Falló la actualización del stock físico en inventario.");
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error al procesar la inserción de la entrada: " + e.getMessage());
            return false;
        }
    }

    /**
     * Suma las cantidades recibidas históricamente de un item bajo una orden específica.
     */
    public int obtenerCantidadRecibida(Connection conn, int idItem, int idOrden) throws SQLException {
        String sql = "SELECT COALESCE(SUM(cantidad_recibida), 0) AS total FROM entradas_almacen WHERE id_item = ? AND id_orden = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItem);
            ps.setInt(2, idOrden);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    /**
     * Sobrecarga del método de consulta para conexiones directas fuera de transacciones complejas.
     */
    public int obtenerCantidadRecibida(int idItem, int idOrden) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return obtenerCantidadRecibida(conn, idItem, idOrden);
        } catch (SQLException e) {
            System.err.println("Error al calcular la cantidad histórica recibida: " + e.getMessage());
        }
        return 0;
    }

    public int obtenerIdItemPorEntrada(int idEntrada) {
        String sql = "SELECT id_item FROM entradas_almacen WHERE id_entrada = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_item");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener id_item: " + e.getMessage());
        }
        return 0;
    }

    public int obtenerCantidadPorEntrada(int idEntrada) {
        String sql = "SELECT cantidad_recibida FROM entradas_almacen WHERE id_entrada = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cantidad_recibida");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cantidad recibida: " + e.getMessage());
        }
        return 0;
    }

    public double obtenerPrecioCompraPorEntrada(int idEntrada) {
        String sql = "SELECT precio_compra_unitario FROM entradas_almacen WHERE id_entrada = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("precio_compra_unitario");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el precio de compra: " + e.getMessage());
        }
        return 0;
    }

    public String obtenerFacturaPorEntrada(int idEntrada) {
        String sql = "SELECT numero_factura FROM entradas_almacen WHERE id_entrada = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("numero_factura");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener número de factura: " + e.getMessage());
        }
        return null;
    }

    public String obtenerRemisionPorEntrada(int idEntrada) {
        String sql = "SELECT numero_remision FROM entradas_almacen WHERE id_entrada = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEntrada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("numero_remision");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener número de remisión: " + e.getMessage());
        }
        return null;
    }
}