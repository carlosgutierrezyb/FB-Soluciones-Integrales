package repository;

import model.OrdenServicio;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente de persistencia encargado de gestionar las operaciones CRUD de la base de datos
 * para la cabecera de las órdenes de servicio de la compañía.
 */
public class OrdenServicioRepository {

    /**
     * Registra una orden de servicio en la base de datos utilizando una conexión dedicada.
     *
     * @param orden Entidad que contiene la información de la cabecera.
     * @return El identificador autogenerado por el motor relacional, o -1 en caso de fallo.
     */
    public int crear(OrdenServicio orden) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(orden, conn);
        } catch (SQLException e) {
            System.err.println("Error al crear orden de servicio: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Inserta el registro de la orden dentro de un contexto transaccional controlado externamente.
     *
     * @param orden Entidad con la información de la orden.
     * @param conn  Conexión activa compartida por la transacción de la capa de servicio.
     * @return El identificador autogenerado de la orden de servicio instalada.
     * @throws SQLException Si ocurre algún error de sintaxis o de restricciones en la base de datos.
     */
    public int crear(OrdenServicio orden, Connection conn) throws SQLException {
        String sql = "INSERT INTO ordenes_servicio (id_cliente, fecha_programada, prioridad, estado, " +
                "direccion_servicio, contacto_nombre, contacto_telefono, observaciones, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orden.getIdCliente());
            ps.setDate(2, orden.getFechaProgramada());
            ps.setString(3, orden.getPrioridad());
            ps.setString(4, orden.getEstado());
            ps.setString(5, orden.getDireccionServicio());
            ps.setString(6, orden.getContactoNombre());
            ps.setString(7, orden.getContactoTelefono());
            ps.setString(8, orden.getObservaciones());

            if (orden.getCreadoPor() != null) {
                ps.setInt(9, orden.getCreadoPor());
            } else {
                ps.setNull(9, Types.INTEGER);
            }

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("La inserción de la orden de servicio no afectó ninguna fila.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("Error al recuperar la clave primaria generada para la orden.");
        }
    }

    /**
     * Obtiene el universo total de órdenes de servicio vinculando el nombre del cliente asociado.
     *
     * @return Lista completa de instancias de OrdenServicio.
     */
    public List<OrdenServicio> listarTodas() {
        List<OrdenServicio> lista = new ArrayList<>();
        String sql = "SELECT os.*, c.nombre AS nombre_cliente FROM ordenes_servicio os " +
                "INNER JOIN clientes c ON os.id_cliente = c.id_cliente ORDER BY os.id_orden_servicio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todas las órdenes de servicio: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Filtra y lista las órdenes de servicio que se ubiquen en un estado operativo particular.
     *
     * @param estado Estado de interés (Ej: Pendiente, En ejecución, Finalizada).
     * @return Lista de órdenes de servicio bajo el estado provisto.
     */
    public List<OrdenServicio> listarPorEstado(String estado) {
        List<OrdenServicio> lista = new ArrayList<>();
        String sql = "SELECT os.*, c.nombre AS nombre_cliente FROM ordenes_servicio os " +
                "INNER JOIN clientes c ON os.id_cliente = c.id_cliente " +
                "WHERE os.estado = ? ORDER BY os.id_orden_servicio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar órdenes por estado: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Recupera el historial de órdenes de servicio ligadas a un cliente.
     *
     * @param idCliente Identificador único del cliente.
     * @return Lista de órdenes de servicio registradas a nombre del cliente.
     */
    public List<OrdenServicio> listarPorCliente(int idCliente) {
        List<OrdenServicio> lista = new ArrayList<>();
        String sql = "SELECT os.*, c.nombre AS nombre_cliente FROM ordenes_servicio os " +
                "INNER JOIN clientes c ON os.id_cliente = c.id_cliente " +
                "WHERE os.id_cliente = ? ORDER BY os.id_orden_servicio DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar órdenes por cliente: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca la cabecera de una orden de servicio basándose en su clave primaria relacional.
     *
     * @param idOrdenServicio Clave primaria de la orden.
     * @return Entidad completa mapeada, o null si el registro no es localizado.
     */
    public OrdenServicio buscarPorId(int idOrdenServicio) {
        String sql = "SELECT os.*, c.nombre AS nombre_cliente FROM ordenes_servicio os " +
                "INNER JOIN clientes c ON os.id_cliente = c.id_cliente WHERE os.id_orden_servicio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrdenServicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar orden de servicio por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el estado macro de una orden compartiendo una transacción activa de la capa intermedia.
     *
     * @param idOrdenServicio Identificador de la orden.
     * @param estado          Nuevo estado operativo global.
     * @param conn            Conexión relacional transaccional.
     * @return true si se actualizó el registro con éxito, false en caso contrario.
     * @throws SQLException Si ocurre un error interno de ejecución SQL en el lote de la transacción.
     */
    public boolean actualizarEstadoTransaccional(int idOrdenServicio, String estado, Connection conn) throws SQLException {
        String sql = "UPDATE ordenes_servicio SET estado = ? WHERE id_orden_servicio = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idOrdenServicio);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Método heredado para actualizar el estado del registro relacional de forma aislada.
     */
    public boolean actualizarEstado(Connection conn, int idOrdenServicio, String estado) throws SQLException {
        return actualizarEstadoTransaccional(idOrdenServicio, estado, conn);
    }

    /**
     * Actualiza el estado general de una orden de servicio de forma directa e independiente.
     *
     * @param idOrdenServicio Identificador de la orden.
     * @param estado          Nuevo estado operacional de destino.
     * @return true si la fila fue modificada, de lo contrario false.
     */
    public boolean actualizarEstado(int idOrdenServicio, String estado) {
        String sql = "UPDATE ordenes_servicio SET estado = ? WHERE id_orden_servicio = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idOrdenServicio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado directo de la OS: " + e.getMessage());
            return false;
        }
    }

    /**
     * Traduce el registro actual de un ResultSet a una estructura de datos orientada a objetos de tipo OrdenServicio.
     */
    private OrdenServicio mapearOrden(ResultSet rs) throws SQLException {
        OrdenServicio orden = new OrdenServicio();
        orden.setIdOrdenServicio(rs.getInt("id_orden_servicio"));
        orden.setIdCliente(rs.getInt("id_cliente"));
        orden.setNombreCliente(rs.getString("nombre_cliente"));
        orden.setEstado(rs.getString("estado"));
        orden.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        orden.setFechaProgramada(rs.getDate("fecha_programada"));
        orden.setPrioridad(rs.getString("prioridad"));
        orden.setDireccionServicio(rs.getString("direccion_servicio"));
        orden.setContactoNombre(rs.getString("contacto_nombre"));
        orden.setContactoTelefono(rs.getString("contacto_telefono"));
        orden.setObservaciones(rs.getString("observaciones"));

        int creadoPor = rs.getInt("creado_por");
        if (!rs.wasNull()) {
            orden.setCreadoPor(creadoPor);
        }
        return orden;
    }
}