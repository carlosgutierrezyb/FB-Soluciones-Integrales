package repository;

import model.Producto;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {

    public boolean guardar(Producto p) {
        // SQL actualizado incluyendo codigo_referencia
        String sql = "INSERT INTO inventario (codigo_referencia, nombre_item, cantidad, id_categoria, stock_minimo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getCodigoReferencia());
            pstmt.setString(2, p.getNombre());
            pstmt.setInt(3, p.getStockActual());
            pstmt.setInt(4, p.getIdCategoria());
            pstmt.setInt(5, p.getStockMinimo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para ELIMINAR
    public boolean eliminar(int id) {
        String sql = "DELETE FROM inventario WHERE id_item = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // Método para EDITAR
    public boolean actualizar(Producto p) {
        String sql = "UPDATE inventario SET nombre_item = ?, id_categoria = ?, stock_minimo = ? WHERE id_item = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNombre());
            pstmt.setInt(2, p.getIdCategoria());
            pstmt.setInt(3, p.getStockMinimo());
            pstmt.setInt(4, p.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }



    public List<Producto> listarTodo() {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT id_item, codigo_referencia, nombre_item, cantidad, stock_minimo FROM inventario";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id_item"));
                p.setCodigoReferencia(rs.getString("codigo_referencia"));
                p.setNombre(rs.getString("nombre_item"));
                p.setStockActual(rs.getInt("cantidad"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public String obtenerUltimoCodigoPorCategoria(int idCategoria) {
        String sql = "SELECT codigo_referencia FROM inventario WHERE id_categoria = ? ORDER BY codigo_referencia DESC LIMIT 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("codigo_referencia");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Si no hay productos aún en esa categoría
    }
}