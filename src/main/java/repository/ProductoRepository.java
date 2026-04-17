package repository;

import model.Producto;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de la persistencia de productos (tabla inventario).
 *
 * Responsabilidades:
 * ✔ CRUD de productos
 * ✔ Consultas de inventario
 * ✔ Operaciones de stock (sin lógica de negocio)
 *
 * ⚠️ IMPORTANTE:
 * - No maneja transacciones
 * - Puede recibir Connection desde Service para operaciones críticas
 */
public class ProductoRepository {

    // =========================
    // CRUD BÁSICO
    // =========================

    public boolean guardar(Producto p) {

        String sql = "INSERT INTO inventario (codigo_referencia, nombre_item, cantidad, id_categoria, stock_minimo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoReferencia());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getStockActual());
            ps.setInt(4, p.getIdCategoria());
            ps.setInt(5, p.getStockMinimo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto p) {

        String sql = "UPDATE inventario SET nombre_item = ?, id_categoria = ?, stock_minimo = ? WHERE id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdCategoria());
            ps.setInt(3, p.getStockMinimo());
            ps.setInt(4, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {

        String sql = "DELETE FROM inventario WHERE id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // CONSULTAS
    // =========================

    public List<Producto> listarTodos() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT id_item, codigo_referencia, nombre_item, cantidad, id_categoria, stock_minimo FROM inventario";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    public Producto buscarPorId(int id) {

        String sql = "SELECT * FROM inventario WHERE id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearProducto(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }

        return null;
    }

    // =========================
    // STOCK (BÁSICO)
    // =========================

    public boolean aumentarStock(int idProducto, int cantidad) {

        String sql = "UPDATE inventario SET cantidad = cantidad + ? WHERE id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al aumentar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean disminuirStock(int idProducto, int cantidad) {

        String sql = "UPDATE inventario SET cantidad = cantidad - ? WHERE id_item = ? AND cantidad >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al disminuir stock: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // 🔥 STOCK PARA TRANSACCIONES (PRO)
    // =========================

    /**
     * Actualiza el stock dentro de una transacción controlada por el Service.
     *
     * @param conn conexión activa
     * @param idProducto producto a actualizar
     * @param cantidad cantidad a sumar
     */
    public boolean actualizarStock(Connection conn, int idProducto, int cantidad) {

        String sql = "UPDATE inventario SET cantidad = cantidad + ? WHERE id_item = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en actualización de stock (transacción): " + e.getMessage());
            return false;
        }
    }

    // =========================
    // OTROS
    // =========================

    public String obtenerUltimoCodigoPorCategoria(int idCategoria) {

        String sql = "SELECT codigo_referencia FROM inventario WHERE id_categoria = ? ORDER BY codigo_referencia DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("codigo_referencia");
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo código: " + e.getMessage());
        }

        return null;
    }

    public List<Producto> listarStockBajo() {

        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM inventario WHERE cantidad < stock_minimo";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error consultando stock bajo: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Mapper centralizado
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {

        Producto p = new Producto();

        p.setId(rs.getInt("id_item"));
        p.setCodigoReferencia(rs.getString("codigo_referencia"));
        p.setNombre(rs.getString("nombre_item"));
        p.setStockActual(rs.getInt("cantidad"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setStockMinimo(rs.getInt("stock_minimo"));

        return p;
    }
}