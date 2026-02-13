package repository;

import model.Producto;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona el CRUD de la tabla 'inventario' en MySQL.
 */
public class ProductoRepository {

    /**
     * CREATE: Inserta un nuevo producto en la base de datos.
     */
    public boolean guardar(Producto p) {
        // Usamos los nombres de columnas exactos de tu tabla MySQL
        String sql = "INSERT INTO inventario (nombre_item, cantidad, categoria, unidad) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNombre());
            pstmt.setInt(2, p.getStockActual());
            pstmt.setString(3, "General"); // Valor por defecto para categoría
            pstmt.setString(4, "Unidad");  // Valor por defecto para unidad

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ: Recupera todos los productos de la tabla 'inventario'.
     */
    public List<Producto> listarTodo() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_item, nombre_item, cantidad FROM inventario";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                // Mapeamos el nombre de la columna de la BD al objeto Java
                p.setId(rs.getInt("id_item"));
                p.setNombre(rs.getString("nombre_item"));
                p.setStockActual(rs.getInt("cantidad"));
                p.setPrecioUnitario(0.0); // Tu tabla no tiene precio aún
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // NOTA: Acá agregará Update y Delete.
}