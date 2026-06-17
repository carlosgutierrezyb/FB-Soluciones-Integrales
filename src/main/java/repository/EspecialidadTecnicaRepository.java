package repository;

import model.EspecialidadTecnica;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadTecnicaRepository {

    // =========================
    // LISTAR TODAS
    // =========================

    public List<EspecialidadTecnica> listarTodas() {

        List<EspecialidadTecnica> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM especialidades_tecnicas " +
                        "ORDER BY nombre";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {

            while (rs.next()) {

                lista.add(
                        mapearEspecialidad(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando especialidades: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // LISTAR ACTIVAS
    // =========================

    public List<EspecialidadTecnica> listarActivas() {

        List<EspecialidadTecnica> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM especialidades_tecnicas " +
                        "WHERE estado = 'Activa' " +
                        "ORDER BY nombre";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {

            while (rs.next()) {

                lista.add(
                        mapearEspecialidad(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando especialidades activas: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // BUSCAR POR ID
    // =========================

    public EspecialidadTecnica buscarPorId(
            int idEspecialidad
    ) {

        String sql =
                "SELECT * " +
                        "FROM especialidades_tecnicas " +
                        "WHERE id_especialidad = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idEspecialidad
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return mapearEspecialidad(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error buscando especialidad: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // MAPPER
    // =========================

    private EspecialidadTecnica mapearEspecialidad(
            ResultSet rs
    ) throws SQLException {

        EspecialidadTecnica especialidad =
                new EspecialidadTecnica();

        especialidad.setIdEspecialidad(
                rs.getInt("id_especialidad")
        );

        especialidad.setNombre(
                rs.getString("nombre")
        );

        especialidad.setDescripcion(
                rs.getString("descripcion")
        );

        especialidad.setEstado(
                rs.getString("estado")
        );

        return especialidad;
    }
}