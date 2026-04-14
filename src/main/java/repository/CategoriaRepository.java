package repository;

import model.Categoria;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el acceso a datos para la tabla 'categorias'.
 * Esto permite que el sistema sea escalable: si mañana agregas
 * más tipos de productos en Workbench, aparecerán solos en la app.
 */
public class CategoriaRepository {

    /**
     * Obtiene la lista completa de categorías desde MySQL.
     * @return Lista de objetos Categoria.
     */
    public List<Categoria> listarCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_cat, nombre FROM categorias ORDER BY nombre ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("id_cat"));
                cat.setNombre(rs.getString("nombre"));
                lista.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }
}