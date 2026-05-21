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
 *
 * ✅ Fuente real:
 * tabla producto
 */
public class InventarioRepository {

    // =========================
    // 🔹 OBTENER STOCK
    // =========================
    public int obtenerStock(int idProducto) {

        String sql =
                "SELECT stock_actual " +
                        "FROM producto " +
                        "WHERE id = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProducto);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt("stock_actual");
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo stock: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // =========================
    // 🔹 AUMENTAR STOCK
    // =========================
    public boolean actualizarStock(
            Connection conn,
            int idProducto,
            int cantidad
    ) throws SQLException {

        String sql =
                "UPDATE producto " +
                        "SET stock_actual = stock_actual + ? " +
                        "WHERE id = ?";

        try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, cantidad);

            ps.setInt(2, idProducto);

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo actualizar el stock."
                );
            }

            return true;
        }
    }

    // =========================
    // 🔹 DISMINUIR STOCK
    // =========================
    public boolean disminuirStock(
            Connection conn,
            int idProducto,
            int cantidad
    ) throws SQLException {

        String sql =
                "UPDATE producto " +
                        "SET stock_actual = stock_actual - ? " +
                        "WHERE id = ? " +
                        "AND stock_actual >= ?";

        try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, cantidad);

            ps.setInt(2, idProducto);

            ps.setInt(3, cantidad);

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "Stock insuficiente."
                );
            }

            return true;
        }
    }

    // =========================
    // 🔹 STOCK BAJO
    // =========================
    public Map<Integer, Integer> listarStockBajo() {

        Map<Integer, Integer> lista =
                new HashMap<>();

        String sql =
                "SELECT id, stock_actual " +
                        "FROM producto " +
                        "WHERE stock_actual < stock_minimo";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                Statement st =
                        conn.createStatement();

                ResultSet rs =
                        st.executeQuery(sql)

        ) {

            while (rs.next()) {

                lista.put(
                        rs.getInt("id"),
                        rs.getInt("stock_actual")
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error consultando stock bajo: "
                            + e.getMessage()
            );
        }

        return lista;
    }
}