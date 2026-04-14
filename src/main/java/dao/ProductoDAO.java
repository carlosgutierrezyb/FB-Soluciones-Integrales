package dao;

import model.Producto;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    /**
     * Obtener todos los productos
     */
    public List<Producto> obtenerTodos() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setStockActual(rs.getInt("stock_actual"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtener producto por ID
     */
    public Producto obtenerPorId(int id) {

        String sql = "SELECT * FROM productos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setStockActual(rs.getInt("stock_actual"));

                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualizar stock
     */
    public boolean actualizarStock(int idProducto, int nuevoStock) {

        String sql = "UPDATE productos SET stock_actual = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, idProducto);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}