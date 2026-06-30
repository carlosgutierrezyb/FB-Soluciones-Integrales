package repository;

import model.OrdenServicioTecnico;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente de acceso a datos para la gestión de técnicos asignados
 * a las órdenes de servicio en la base de datos.
 */
public class OrdenServicioTecnicoRepository {

    /**
     * Verifica la existencia de una asignación activa por orden y técnico.
     *
     * @param idOrdenServicio Identificador de la orden de servicio.
     * @param idTecnico       Identificador del técnico.
     * @return true si la asignación ya existe, false en caso contrario.
     */
    public boolean existeAsignacion(int idOrdenServicio, int idTecnico) {
        String sql = "SELECT COUNT(*) FROM orden_servicio_tecnicos WHERE id_orden_servicio = ? AND id_tecnico = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrdenServicio);
            ps.setInt(2, idTecnico);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de asignación: " + e.getMessage());
        }
        return false;
    }

    /**
     * Inserta un nuevo registro de asignación técnica en el sistema.
     *
     * @param asignacion Objeto con los datos de la asignación.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean asignarTecnico(OrdenServicioTecnico asignacion) {
        String sql = "INSERT INTO orden_servicio_tecnicos (id_orden_servicio, id_tecnico, id_especialidad, estado, observaciones) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getIdOrdenServicio());
            ps.setInt(2, asignacion.getIdTecnico());
            ps.setInt(3, asignacion.getIdEspecialidad());
            ps.setString(4, asignacion.getEstado());
            ps.setString(5, asignacion.getObservaciones());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al asignar técnico: " + e.getMessage());
        }
        return false;
    }

    /**
     * Recupera todos los técnicos asignados a una orden de servicio específica.
     *
     * @param idOrdenServicio Identificador de la orden de servicio.
     * @return Lista de asignaciones asociadas a la orden.
     */
    public List<OrdenServicioTecnico> listarPorOrden(int idOrdenServicio) {
        List<OrdenServicioTecnico> lista = new ArrayList<>();
        String sql = "SELECT ost.*, CONCAT(t.nombres, ' ', t.apellidos) AS tecnico, et.nombre AS especialidad " +
                "FROM orden_servicio_tecnicos ost " +
                "INNER JOIN tecnicos t ON ost.id_tecnico = t.id_tecnico " +
                "INNER JOIN especialidades_tecnicas et ON ost.id_especialidad = et.id_especialidad " +
                "WHERE ost.id_orden_servicio = ? ORDER BY ost.id_asignacion";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrdenServicio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar técnicos asignados: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca una asignación técnica por su identificador primario.
     *
     * @param idAsignacion Identificador único de la asignación.
     * @return Objeto OrdenServicioTecnico si se encuentra, null en caso contrario.
     */
    public OrdenServicioTecnico buscarPorId(int idAsignacion) {
        String sql = "SELECT ost.*, CONCAT(t.nombres, ' ', t.apellidos) AS tecnico, et.nombre AS especialidad " +
                "FROM orden_servicio_tecnicos ost " +
                "INNER JOIN tecnicos t ON ost.id_tecnico = t.id_tecnico " +
                "INNER JOIN especialidades_tecnicas et ON ost.id_especialidad = et.id_especialidad " +
                "WHERE ost.id_asignacion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAsignacion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar asignación: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el estado de una asignación técnica utilizando una conexión dedicada.
     */
    public boolean actualizarEstado(int idAsignacion, String estado) {
        String sql = "UPDATE orden_servicio_tecnicos SET estado = ? WHERE id_asignacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza el estado de una asignación técnica dentro de un contexto transaccional compartido.
     */
    public boolean actualizarEstadoTransaccional(int idAsignacion, String estado, Connection conn) throws SQLException {
        String sql = "UPDATE orden_servicio_tecnicos SET estado = ? WHERE id_asignacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idAsignacion);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Registra las horas trabajadas en una asignación utilizando una conexión dedicada.
     */
    public boolean registrarHoras(int idAsignacion, double horas) {
        String sql = "UPDATE orden_servicio_tecnicos SET horas_trabajadas = ? WHERE id_asignacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, BigDecimal.valueOf(horas));
            ps.setInt(2, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar horas: " + e.getMessage());
        }
        return false;
    }

    /**
     * Registra las horas trabajadas en una asignación dentro de un contexto transaccional compartido.
     */
    public boolean registrarHorasTransaccionales(int idAsignacion, double horas, Connection conn) throws SQLException {
        String sql = "UPDATE orden_servicio_tecnicos SET horas_trabajadas = ? WHERE id_asignacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(horas));
            ps.setInt(2, idAsignacion);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza las observaciones de una asignación técnica específica.
     */
    public boolean actualizarObservaciones(int idAsignacion, String observaciones) {
        String sql = "UPDATE orden_servicio_tecnicos SET observaciones = ? WHERE id_asignacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, observaciones);
            ps.setInt(2, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar observaciones: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina de forma física una asignación técnica del sistema usando su ID primario.
     */
    public boolean eliminarAsignacion(int idAsignacion) {
        String sql = "DELETE FROM orden_servicio_tecnicos WHERE id_asignacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar asignación: " + e.getMessage());
        }
        return false;
    }

    /**
     * Remueve de forma física el registro que vincula a un técnico con una orden.
     * Reutiliza el contexto transaccional provisto por la capa de servicio.
     *
     * @param idOrdenServicio Identificador de la orden maestra.
     * @param idTecnico       Identificador del técnico asignado.
     * @param conn            Conexión transaccional compartida y activa.
     * @return true si se removió la fila con éxito, false en caso contrario.
     * @throws SQLException Si ocurre un fallo en el motor de base de datos.
     */
    public boolean eliminarAsignacionTransaccional(int idOrdenServicio, int idTecnico, Connection conn) throws SQLException {
        String sql = "DELETE FROM orden_servicio_tecnicos WHERE id_orden_servicio = ? AND id_tecnico = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrdenServicio);
            ps.setInt(2, idTecnico);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Mapea el registro actual de un ResultSet a un objeto de dominio OrdenServicioTecnico.
     */
    private OrdenServicioTecnico mapear(ResultSet rs) throws SQLException {
        OrdenServicioTecnico o = new OrdenServicioTecnico();
        o.setIdAsignacion(rs.getInt("id_asignacion"));
        o.setIdOrdenServicio(rs.getInt("id_orden_servicio"));
        o.setIdTecnico(rs.getInt("id_tecnico"));
        o.setIdEspecialidad(rs.getInt("id_especialidad"));
        o.setFechaAsignacion(rs.getTimestamp("fecha_asignacion"));
        o.setEstado(rs.getString("estado"));

        double horas = rs.getDouble("horas_trabajadas");
        if (!rs.wasNull()) {
            o.setHorasTrabajadas(horas);
        }

        o.setObservaciones(rs.getString("observations" != null ? "observaciones" : "observaciones"));
        o.setObservaciones(rs.getString("observaciones"));
        o.setNombreTecnico(rs.getString("tecnico"));
        o.setNombreEspecialidad(rs.getString("especialidad"));
        return o;
    }
}