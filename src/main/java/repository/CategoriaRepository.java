package repository;

import model.Categoria;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el acceso a datos para la tabla 'categorias'.
 *
 * 🔥 ERP F&B:
 * - Categorías de productos
 * - Categorías de servicios
 * - Prefijos ERP
 * - Generación de códigos inteligentes
 */
public class CategoriaRepository {

    // =========================
    // 🔹 LISTAR CATEGORÍAS
    // =========================
    public List<Categoria> listarCategorias() {

        List<Categoria> lista =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "id_cat, " +
                        "nombre, " +
                        "prefijo " +
                        "FROM categorias " +
                        "ORDER BY nombre ASC";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(sql)

        ) {

            while (rs.next()) {

                Categoria cat =
                        new Categoria();

                // =========================
                // MAPEAR
                // =========================

                cat.setId(
                        rs.getInt("id_cat")
                );

                cat.setNombre(
                        rs.getString("nombre")
                );

                cat.setPrefijo(
                        rs.getString("prefijo")
                );

                lista.add(cat);
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error al listar categorías: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Categoria buscarPorId(
            int idCategoria
    ) {

        String sql =
                "SELECT " +
                        "id_cat, " +
                        "nombre, " +
                        "prefijo " +
                        "FROM categorias " +
                        "WHERE id_cat = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idCategoria
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    Categoria categoria =
                            new Categoria();

                    categoria.setId(
                            rs.getInt("id_cat")
                    );

                    categoria.setNombre(
                            rs.getString("nombre")
                    );

                    categoria.setPrefijo(
                            rs.getString("prefijo")
                    );

                    return categoria;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error buscando categoría: "
                            + e.getMessage()
            );
        }

        return null;
    }
}