package repository;

import model.OrdenCompra;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de órdenes de compra.
 * <p>
 * 🔥 ERP PRO:
 * - Crear órdenes
 * - Listar órdenes
 * - Filtrar por estado
 * - Obtener órdenes para facturación
 * - Actualizar estados
 */
public class OrdenCompraRepository {

    // =========================
    // 🔹 CREAR ORDEN
    // =========================
    public int crear(OrdenCompra orden) {

        String sql =
                "INSERT INTO ordenes_compra " +
                        "(id_proveedor, estado) " +
                        "VALUES (?, ?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )

        ) {

            System.out.println(
                    "🧾 Creando orden..."
            );

            ps.setInt(
                    1,
                    orden.getIdProveedor()
            );

            ps.setString(
                    2,
                    orden.getEstado()
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo crear la orden."
                );
            }

            try (

                    ResultSet rs =
                            ps.getGeneratedKeys()

            ) {

                if (rs.next()) {

                    int idGenerado =
                            rs.getInt(1);

                    System.out.println(
                            "✅ Orden creada ID: "
                                    + idGenerado
                    );

                    return idGenerado;
                }
            }

            throw new SQLException(
                    "No se obtuvo ID."
            );

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error creando orden: "
                            + e.getMessage()
            );

            return -1;
        }
    }

    // =========================
    // 🔹 LISTAR TODAS
    // =========================
    public List<OrdenCompra> listarTodas() {

        List<OrdenCompra> lista =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "oc.id_orden, " +
                        "oc.id_proveedor, " +
                        "p.nombre_razon_social AS nombre_proveedor, " +
                        "oc.estado " +
                        "FROM ordenes_compra oc " +
                        "INNER JOIN proveedores p " +
                        "ON oc.id_proveedor = p.id_proveedor " +
                        "ORDER BY oc.id_orden DESC";

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
                        mapearOrden(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando órdenes: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 LISTAR POR ESTADO
    // =========================
    public List<OrdenCompra> listarPorEstado(
            String estado
    ) {

        List<OrdenCompra> lista =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "oc.id_orden, " +
                        "oc.id_proveedor, " +
                        "p.nombre_razon_social AS nombre_proveedor, " +
                        "oc.estado " +
                        "FROM ordenes_compra oc " +
                        "INNER JOIN proveedores p " +
                        "ON oc.id_proveedor = p.id_proveedor " +
                        "WHERE oc.estado = ? " +
                        "ORDER BY oc.id_orden DESC";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(1, estado);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                while (rs.next()) {

                    lista.add(
                            mapearOrden(rs)
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error filtrando órdenes: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 LISTAR PENDIENTES
    // =========================
    public List<OrdenCompra> listarPendientes() {

        List<OrdenCompra> lista =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "oc.id_orden, " +
                        "oc.id_proveedor, " +
                        "p.nombre_razon_social AS nombre_proveedor, " +
                        "oc.estado " +
                        "FROM ordenes_compra oc " +
                        "INNER JOIN proveedores p " +
                        "ON oc.id_proveedor = p.id_proveedor " +
                        "WHERE oc.estado IN ('Parcial', 'Recibido') " +
                        "ORDER BY oc.id_orden DESC";

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
                        mapearOrden(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando pendientes: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 ÓRDENES PARA FACTURA (CORREGIDO)
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {

        List<OrdenCompra> lista = new ArrayList<>();

        String sql =
                "SELECT DISTINCT " +
                        "oc.id_orden, " +
                        "oc.id_proveedor, " +
                        "p.nombre_razon_social AS nombre_proveedor, " +
                        "oc.estado " +
                        "FROM ordenes_compra oc " +
                        "INNER JOIN proveedores p " +
                        "ON oc.id_proveedor = p.id_proveedor " +
                        "INNER JOIN entradas_almacen ea " +
                        "ON oc.id_orden = ea.id_orden " +
                        "WHERE oc.estado IN ('Parcial', 'Recibido') " +
                        "ORDER BY oc.id_orden DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }

        } catch (SQLException e) {
            System.err.println(
                    "❌ Error obteniendo órdenes factura: " + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public OrdenCompra buscarPorId(
            int idOrden
    ) {

        String sql =
                "SELECT " +
                        "oc.id_orden, " +
                        "oc.id_proveedor, " +
                        "p.nombre_razon_social AS nombre_proveedor, " +
                        "oc.estado " +
                        "FROM ordenes_compra oc " +
                        "INNER JOIN proveedores p " +
                        "ON oc.id_proveedor = p.id_proveedor " +
                        "WHERE oc.id_orden = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idOrden);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return mapearOrden(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error buscando orden: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔹 ACTUALIZAR ESTADO
    // =========================
    public boolean actualizarEstado(
            Connection conn,
            int idOrden,
            String estado
    ) throws SQLException {

        String sql =
                "UPDATE ordenes_compra " +
                        "SET estado = ? " +
                        "WHERE id_orden = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(1, estado);

            ps.setInt(2, idOrden);

            int filas =
                    ps.executeUpdate();

            System.out.println(
                    "🔄 Estado actualizado: "
                            + estado
            );

            return filas > 0;
        }
    }

    // =========================
    // 🔧 MAPPER
    // =========================
    private OrdenCompra mapearOrden(
            ResultSet rs
    ) throws SQLException {

        OrdenCompra orden =
                new OrdenCompra();

        orden.setIdOrden(
                rs.getInt("id_orden")
        );

        orden.setIdProveedor(
                rs.getInt("id_proveedor")
        );

        orden.setNombreProveedor(
                rs.getString("nombre_proveedor")
        );

        orden.setEstado(
                rs.getString("estado")
        );

        return orden;
    }
}