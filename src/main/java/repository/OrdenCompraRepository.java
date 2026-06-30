package repository;

import model.OrdenCompra;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestión de persistencia y consultas para el ciclo de vida de las órdenes de compra.
 */
public class OrdenCompraRepository {

    /**
     * Inserta una nueva orden de compra y retorna la clave primaria autogenerada.
     */
    public int crear(OrdenCompra orden) {
        String sql = "INSERT INTO ordenes_compra (id_proveedor, estado) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orden.getIdProveedor());
            ps.setString(2, orden.getEstado());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo insertar el registro de la orden.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("No se generó el identificador único para la orden.");

        } catch (SQLException e) {
            System.err.println("Error al crear la orden de compra: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Recupera el histórico total de órdenes registradas en el sistema.
     */
    public List<OrdenCompra> listarTodas() {
        List<OrdenCompra> lista = new ArrayList<>();
        String sql = "SELECT oc.id_orden, oc.id_proveedor, p.nombre_razon_social AS nombre_proveedor, oc.estado "
                + "FROM ordenes_compra oc "
                + "INNER JOIN proveedores p ON oc.id_proveedor = p.id_proveedor "
                + "ORDER BY oc.id_orden DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar el histórico de órdenes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Filtra las órdenes basándose en un estado específico pasado por parámetro.
     */
    public List<OrdenCompra> listarPorEstado(String estado) {
        List<OrdenCompra> lista = new ArrayList<>();
        String sql = "SELECT oc.id_orden, oc.id_proveedor, p.nombre_razon_social AS nombre_proveedor, oc.estado "
                + "FROM ordenes_compra oc "
                + "INNER JOIN proveedores p ON oc.id_proveedor = p.id_proveedor "
                + "WHERE oc.estado = ? "
                + "ORDER BY oc.id_orden DESC";

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
     * Recupera las órdenes aptas para procesar ingresos físicos en el almacén.
     */
    public List<OrdenCompra> listarPendientes() {
        List<OrdenCompra> lista = new ArrayList<>();

        // CORRECCIÓN: Se añade 'Pendiente' (o el estado inicial que uses, ej. 'Aprobada') para incluir las órdenes nuevas
        String sql = "SELECT oc.id_orden, oc.id_proveedor, p.nombre_razon_social AS nombre_proveedor, oc.estado "
                + "FROM ordenes_compra oc "
                + "INNER JOIN proveedores p ON oc.id_proveedor = p.id_proveedor "
                + "WHERE oc.estado IN ('Pendiente', 'Parcial', 'Recibido', 'Aprobada') "
                + "ORDER BY oc.id_orden DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar órdenes pendientes de ingreso: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta las órdenes con recepciones parciales o totales destinadas a conciliación contable.
     */
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {
        List<OrdenCompra> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT oc.id_orden, oc.id_proveedor, p.nombre_razon_social AS nombre_proveedor, oc.estado "
                + "FROM ordenes_compra oc "
                + "INNER JOIN proveedores p ON oc.id_proveedor = p.id_proveedor "
                + "INNER JOIN entradas_almacen ea ON oc.id_orden = ea.id_orden "
                + "WHERE oc.estado IN ('Parcial', 'Recibido') "
                + "ORDER BY oc.id_orden DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener órdenes para facturación: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Localiza una orden de compra mediante su identificador primario.
     */
    public OrdenCompra buscarPorId(int idOrden) {
        String sql = "SELECT oc.id_orden, oc.id_proveedor, p.nombre_razon_social AS nombre_proveedor, oc.estado "
                + "FROM ordenes_compra oc "
                + "INNER JOIN proveedores p ON oc.id_proveedor = p.id_proveedor "
                + "WHERE oc.id_orden = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar orden por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza de forma transaccional el estado operativo de una orden de compra.
     */
    public boolean actualizarEstado(Connection conn, int idOrden, String estado) throws SQLException {
        String sql = "UPDATE ordenes_compra "
                + "SET estado = ? "
                + "WHERE id_orden = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idOrden);

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    /**
     * Transfiere las columnas de un renglón del ResultSet hacia la entidad del modelo de datos.
     */
    private OrdenCompra mapearOrden(ResultSet rs) throws SQLException {
        OrdenCompra orden = new OrdenCompra();
        orden.setIdOrden(rs.getInt("id_orden"));
        orden.setIdProveedor(rs.getInt("id_proveedor"));
        orden.setNombreProveedor(rs.getString("nombre_proveedor"));
        orden.setEstado(rs.getString("estado"));
        return orden;
    }
}