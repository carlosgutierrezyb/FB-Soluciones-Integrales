package repository;

import util.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository de inventario.
 *
 * 🔥 RESPONSABILIDAD:
 * - Manejar stock
 * - Consultar niveles de inventario
 * - Ejecutar movimientos (entradas/salidas)
 */
public class InventarioRepository {

    // =========================
    // 🔹 OBTENER STOCK
    // =========================
    public int obtenerStock(int idProducto) {

        String sql = "SELECT stock_actual FROM inventario WHERE id_producto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock_actual");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error obteniendo stock: " + e.getMessage());
        }

        return 0;
    }

    // =========================
    // 🔹 ACTUALIZAR STOCK (TRANSACCIÓN)
    // =========================
    public boolean actualizarStock(Connection conn, int idProducto, int cantidad) throws SQLException {

        String sql = "UPDATE inventario SET stock_actual = stock_actual + ? WHERE id_producto = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("No se pudo actualizar el stock.");
            }

            return true;
        }
    }

    // =========================
    // 🔹 DISMINUIR STOCK (VALIDADO)
    // =========================
    public boolean disminuirStock(Connection conn, int idProducto, int cantidad) throws SQLException {

        String sql = "UPDATE inventario " +
                "SET stock_actual = stock_actual - ? " +
                "WHERE id_producto = ? AND stock_actual >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException("Stock insuficiente.");
            }

            return true;
        }
    }

    // =========================
    // 🔹 CREAR REGISTRO INVENTARIO
    // =========================
    public boolean crearRegistro(int idProducto, int stockInicial, int stockMinimo) {

        String sql = "INSERT INTO inventario (id_producto, stock_actual, stock_minimo) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.setInt(2, stockInicial);
            ps.setInt(3, stockMinimo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error creando inventario: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔹 STOCK BAJO
    // =========================
    public Map<Integer, Integer> listarStockBajo() {

        Map<Integer, Integer> lista = new HashMap<>();

        String sql = "SELECT id_producto, stock_actual " +
                "FROM inventario " +
                "WHERE stock_actual < stock_minimo";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.put(
                        rs.getInt("id_producto"),
                        rs.getInt("stock_actual")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Error consultando stock bajo: " + e.getMessage());
        }

        return lista;
    }
}