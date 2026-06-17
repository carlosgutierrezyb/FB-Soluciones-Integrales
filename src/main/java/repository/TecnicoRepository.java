package repository;

import model.Tecnico;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TecnicoRepository {

    // =========================
    // LISTAR TODOS
    // =========================

    public List<Tecnico> listarTodos() {

        List<Tecnico> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM tecnicos " +
                        "ORDER BY nombres, apellidos";

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
                        mapearTecnico(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando técnicos: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // LISTAR ACTIVOS
    // =========================

    public List<Tecnico> listarActivos() {

        List<Tecnico> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM tecnicos " +
                        "WHERE estado = 'Activo' " +
                        "ORDER BY nombres, apellidos";

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
                        mapearTecnico(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando técnicos activos: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // LISTAR POR ESPECIALIDAD
    // =========================

    public List<Tecnico> listarPorEspecialidad(
            int idEspecialidad
    ) {

        List<Tecnico> lista =
                new ArrayList<>();

        String sql =
                "SELECT DISTINCT t.* " +
                        "FROM tecnicos t " +
                        "INNER JOIN tecnico_especialidad te " +
                        "ON t.id_tecnico = te.id_tecnico " +
                        "WHERE te.id_especialidad = ? " +
                        "AND t.estado = 'Activo' " +
                        "ORDER BY t.nombres, t.apellidos";

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

                while (rs.next()) {

                    lista.add(
                            mapearTecnico(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando técnicos por especialidad: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // BUSCAR POR ID
    // =========================

    public Tecnico buscarPorId(
            int idTecnico
    ) {

        String sql =
                "SELECT * " +
                        "FROM tecnicos " +
                        "WHERE id_tecnico = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idTecnico
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return mapearTecnico(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error buscando técnico: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // MAPPER
    // =========================

    private Tecnico mapearTecnico(
            ResultSet rs
    ) throws SQLException {

        Tecnico tecnico =
                new Tecnico();

        tecnico.setIdTecnico(
                rs.getInt("id_tecnico")
        );

        tecnico.setDocumento(
                rs.getString("documento")
        );

        tecnico.setNombres(
                rs.getString("nombres")
        );

        tecnico.setApellidos(
                rs.getString("apellidos")
        );

        tecnico.setTelefono(
                rs.getString("telefono")
        );

        tecnico.setEmail(
                rs.getString("email")
        );

        tecnico.setDireccion(
                rs.getString("direccion")
        );

        tecnico.setEstado(
                rs.getString("estado")
        );

        tecnico.setFechaIngreso(
                rs.getDate("fecha_ingreso")
        );

        tecnico.setObservaciones(
                rs.getString("observaciones")
        );

        tecnico.setFechaCreacion(
                rs.getTimestamp("fecha_creacion")
        );

        return tecnico;
    }
}