package repository;

import model.OrdenServicioTecnico;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenServicioTecnicoRepository {

    // =========================
    // 🔥 MEJORA 1: EXISTE ASIGNACIÓN
    // =========================
    public boolean existeAsignacion(
            int idOrdenServicio,
            int idTecnico
    ) {
        String sql =
                "SELECT COUNT(*) FROM orden_servicio_tecnicos "
                        + "WHERE id_orden_servicio = ? AND id_tecnico = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idOrdenServicio);
            ps.setInt(2, idTecnico);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println(
                    "❌ Error verificando existencia de asignación: "
                            + e.getMessage()
            );
        }
        return false;
    }

    // =========================
    // ASIGNAR TÉCNICO
    // =========================
    public boolean asignarTecnico(
            OrdenServicioTecnico asignacion
    ) {
        String sql =
                "INSERT INTO orden_servicio_tecnicos ("
                        + "id_orden_servicio,"
                        + "id_tecnico,"
                        + "id_especialidad,"
                        + "estado,"
                        + "observaciones"
                        + ") VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(
                    1,
                    asignacion.getIdOrdenServicio()
            );

            ps.setInt(
                    2,
                    asignacion.getIdTecnico()
            );

            ps.setInt(
                    3,
                    asignacion.getIdEspecialidad()
            );

            ps.setString(
                    4,
                    asignacion.getEstado()
            );

            ps.setString(
                    5,
                    asignacion.getObservaciones()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error asignando técnico: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // =========================
    // LISTAR POR ORDEN
    // =========================
    public List<OrdenServicioTecnico> listarPorOrden(
            int idOrdenServicio
    ) {
        List<OrdenServicioTecnico> lista =
                new ArrayList<>();

        String sql =
                "SELECT ost.*, "
                        + "CONCAT(t.nombres,' ',t.apellidos) AS tecnico, "
                        + "et.nombre AS especialidad "
                        + "FROM orden_servicio_tecnicos ost "
                        + "INNER JOIN tecnicos t "
                        + "ON ost.id_tecnico = t.id_tecnico "
                        + "INNER JOIN especialidades_tecnicas et "
                        + "ON ost.id_especialidad = et.id_especialidad "
                        + "WHERE ost.id_orden_servicio = ? "
                        + "ORDER BY ost.id_asignacion";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(
                    1,
                    idOrdenServicio
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {
                while (rs.next()) {
                    lista.add(
                            mapear(rs)
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error listando técnicos asignados: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public boolean existeAsignacionPorId(int idAsignacion) {
        return buscarPorId(idAsignacion) != null;
    }

    public OrdenServicioTecnico buscarPorId(
            int idAsignacion
    ) {
        String sql =
                "SELECT ost.*, "
                        + "CONCAT(t.nombres,' ',t.apellidos) AS tecnico, "
                        + "et.nombre AS especialidad "
                        + "FROM orden_servicio_tecnicos ost "
                        + "INNER JOIN tecnicos t "
                        + "ON ost.id_tecnico = t.id_tecnico "
                        + "INNER JOIN especialidades_tecnicas et "
                        + "ON ost.id_especialidad = et.id_especialidad "
                        + "WHERE ost.id_asignacion = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(
                    1,
                    idAsignacion
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error buscando asignación: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // ACTUALIZAR ESTADO
    // =========================
    public boolean actualizarEstado(
            int idAsignacion,
            String estado
    ) {
        String sql =
                "UPDATE orden_servicio_tecnicos "
                        + "SET estado = ? "
                        + "WHERE id_asignacion = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(
                    1,
                    estado
            );

            ps.setInt(
                    2,
                    idAsignacion
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error actualizando estado: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // =========================
    // REGISTRAR HORAS
    // =========================
    public boolean registrarHoras(
            int idAsignacion,
            double horas
    ) {
        String sql =
                "UPDATE orden_servicio_tecnicos "
                        + "SET horas_trabajadas = ? "
                        + "WHERE id_asignacion = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // 🔥 CORRECCIÓN 2: Utilizando BigDecimal para evitar problemas con decimal(5,2)
            ps.setBigDecimal(
                    1,
                    BigDecimal.valueOf(horas)
            );

            ps.setInt(
                    2,
                    idAsignacion
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error registrando horas: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // =========================
    // ACTUALIZAR OBSERVACIONES
    // =========================
    public boolean actualizarObservaciones(
            int idAsignacion,
            String observaciones
    ) {
        String sql =
                "UPDATE orden_servicio_tecnicos "
                        + "SET observaciones = ? "
                        + "WHERE id_asignacion = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(
                    1,
                    observaciones
            );

            // 🔥 CORRECCIÓN 1: ps.setInt en lugar de pasar un String concatenado
            ps.setInt(
                    2,
                    idAsignacion
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error actualizando observaciones: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // =========================
    // 🔥 MEJORA 2: FINALIZAR TÉCNICO
    // =========================
    public boolean finalizarTecnico(
            int idAsignacion,
            double horas,
            String observaciones
    ) {
        String sql =
                "UPDATE orden_servicio_tecnicos "
                        + "SET estado = 'Finalizado', "
                        + "horas_trabajadas = ?, "
                        + "observaciones = ? "
                        + "WHERE id_asignacion = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBigDecimal(1, BigDecimal.valueOf(horas));
            ps.setString(2, observaciones);
            ps.setInt(3, idAsignacion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error al finalizar técnico de la OS: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // =========================
    // ELIMINAR ASIGNACIÓN
    // =========================
    public boolean eliminarAsignacion(
            int idAsignacion
    ) {
        String sql =
                "DELETE FROM orden_servicio_tecnicos "
                        + "WHERE id_asignacion = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(
                    1,
                    idAsignacion
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error eliminando asignación: "
                            + e.getMessage()
            );
        }

        return false;
    }

    // =========================
    // MAPPER
    // =========================
    private OrdenServicioTecnico mapear(
            ResultSet rs
    ) throws SQLException {
        OrdenServicioTecnico o =
                new OrdenServicioTecnico();

        o.setIdAsignacion(
                rs.getInt("id_asignacion")
        );

        o.setIdOrdenServicio(
                rs.getInt("id_orden_servicio")
        );

        o.setIdTecnico(
                rs.getInt("id_tecnico")
        );

        o.setIdEspecialidad(
                rs.getInt("id_especialidad")
        );

        o.setFechaAsignacion(
                rs.getTimestamp("fecha_asignacion")
        );

        o.setEstado(
                rs.getString("estado")
        );

        // 🔥 CORRECCIÓN 3: Evitar el casteo por defecto a 0.0 si el campo viene NULL
        double horas = rs.getDouble("horas_trabajadas");
        if (!rs.wasNull()) {
            o.setHorasTrabajadas(horas);
        }

        o.setObservaciones(
                rs.getString("observaciones")
        );

        o.setNombreTecnico(
                rs.getString("tecnico")
        );

        o.setNombreEspecialidad(
                rs.getString("especialidad")
        );

        return o;
    }
}