package repository;

import model.Producto;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de productos.
 *
 * 🔥 RESPONSABILIDAD:
 * - Manejar datos maestros (catálogo)
 * - NO maneja stock (eso es InventarioRepository)
 */
public class ProductoRepository {

    // =========================
    // 🔹 GUARDAR
    // =========================
    public boolean guardar(Producto p) {

        String sql = "INSERT INTO producto (codigo_referencia, nombre, id_categoria) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoReferencia());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public boolean actualizar(Producto p) {

        String sql = "UPDATE producto SET nombre = ?, id_categoria = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdCategoria());
            ps.setInt(3, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔹 ELIMINAR
    // =========================
    public boolean eliminar(int id) {

        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔹 LISTAR
    // =========================
    public List<Producto> listarTodos() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM producto";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Producto buscarPorId(int id) {

        String sql = "SELECT * FROM producto WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar producto: " + e.getMessage());
        }

        return null;
    }

    // =========================
    // 🔥 ALIAS PRO
    // =========================
    public Producto obtenerPorId(int id) {
        return buscarPorId(id);
    }

    // =========================
    // 🔧 MAPPER
    // =========================
    private Producto mapearProducto(ResultSet rs) throws SQLException {

        Producto p = new Producto();

        p.setId(rs.getInt("id"));
        p.setCodigoReferencia(rs.getString("codigo_referencia"));
        p.setNombre(rs.getString("nombre"));
        p.setIdCategoria(rs.getInt("id_categoria"));

        return p;
    }
}