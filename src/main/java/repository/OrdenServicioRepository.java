package repository;

import model.OrdenServicio;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de órdenes de servicio.
 *
 * 🔥 ERP PRO:
 * - Crear órdenes
 * - Listar órdenes
 * - Filtrar por estado
 * - Filtrar por cliente
 * - Actualizar estados
 */
public class OrdenServicioRepository {

    // =========================
    // 🔹 CREAR ORDEN (NORMAL)
    // =========================
    public int crear(OrdenServicio orden) {

        try (

                Connection conn =
                        DatabaseConnection.getConnection()

        ) {

            return crear(
                    orden,
                    conn
            );

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error creando orden servicio: "
                            + e.getMessage()
            );

            return -1;
        }
    }

    // =========================
    // 🔹 CREAR ORDEN (TRANSACCIONAL)
    // =========================
    public int crear(
            OrdenServicio orden,
            Connection conn
    ) throws SQLException {

        String sql =
                "INSERT INTO ordenes_servicio "
                        + "("
                        + "id_cliente, "
                        + "fecha_programada, "
                        + "prioridad, "
                        + "estado, "
                        + "direccion_servicio, "
                        + "contacto_nombre, "
                        + "contacto_telefono, "
                        + "observaciones, "
                        + "creado_por"
                        + ") "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )

        ) {

            System.out.println(
                    "🛠️ Creando orden de servicio..."
            );

            ps.setInt(
                    1,
                    orden.getIdCliente()
            );

            ps.setDate(
                    2,
                    orden.getFechaProgramada()
            );

            ps.setString(
                    3,
                    orden.getPrioridad()
            );

            ps.setString(
                    4,
                    orden.getEstado()
            );

            ps.setString(
                    5,
                    orden.getDireccionServicio()
            );

            ps.setString(
                    6,
                    orden.getContactoNombre()
            );

            ps.setString(
                    7,
                    orden.getContactoTelefono()
            );

            ps.setString(
                    8,
                    orden.getObservaciones()
            );

            if (orden.getCreadoPor() != null) {

                ps.setInt(
                        9,
                        orden.getCreadoPor()
                );

            } else {

                ps.setNull(
                        9,
                        Types.INTEGER
                );
            }

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
                            "✅ Orden servicio creada ID: "
                                    + idGenerado
                    );

                    return idGenerado;
                }
            }

            throw new SQLException(
                    "No se obtuvo ID."
            );
        }
    }

    // =========================
    // 🔹 LISTAR TODAS
    // =========================
    public List<OrdenServicio> listarTodas() {

        List<OrdenServicio> lista =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "os.*, "
                        + "c.nombre AS nombre_cliente "
                        + "FROM ordenes_servicio os "
                        + "INNER JOIN clientes c "
                        + "ON os.id_cliente = c.id_cliente "
                        + "ORDER BY os.id_orden_servicio DESC";

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
                    "❌ Error listando órdenes servicio: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 LISTAR POR ESTADO
    // =========================
    public List<OrdenServicio> listarPorEstado(
            String estado
    ) {

        List<OrdenServicio> lista =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "os.*, "
                        + "c.nombre AS nombre_cliente "
                        + "FROM ordenes_servicio os "
                        + "INNER JOIN clientes c "
                        + "ON os.id_cliente = c.id_cliente "
                        + "WHERE os.estado = ? "
                        + "ORDER BY os.id_orden_servicio DESC";

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
                    "❌ Error filtrando órdenes servicio: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 LISTAR POR CLIENTE
    // =========================
    public List<OrdenServicio> listarPorCliente(
            int idCliente
    ) {

        List<OrdenServicio> lista =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "os.*, "
                        + "c.nombre AS nombre_cliente "
                        + "FROM ordenes_servicio os "
                        + "INNER JOIN clientes c "
                        + "ON os.id_cliente = c.id_cliente "
                        + "WHERE os.id_cliente = ? "
                        + "ORDER BY os.id_orden_servicio DESC";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idCliente);

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
                    "❌ Error filtrando por cliente: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public OrdenServicio buscarPorId(
            int idOrdenServicio
    ) {

        String sql =
                "SELECT "
                        + "os.*, "
                        + "c.nombre AS nombre_cliente "
                        + "FROM ordenes_servicio os "
                        + "INNER JOIN clientes c "
                        + "ON os.id_cliente = c.id_cliente "
                        + "WHERE os.id_orden_servicio = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idOrdenServicio
            );

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
                    "❌ Error buscando orden servicio: "
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
            int idOrdenServicio,
            String estado
    ) throws SQLException {

        String sql =
                "UPDATE ordenes_servicio "
                        + "SET estado = ? "
                        + "WHERE id_orden_servicio = ?";

        try (

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    estado
            );

            ps.setInt(
                    2,
                    idOrdenServicio
            );

            int filas =
                    ps.executeUpdate();

            System.out.println(
                    "🔄 Estado OS actualizado: "
                            + estado
            );

            return filas > 0;
        }
    }

    // =========================
    // 🔧 MAPPER
    // =========================
    private OrdenServicio mapearOrden(
            ResultSet rs
    ) throws SQLException {

        OrdenServicio orden =
                new OrdenServicio();

        orden.setIdOrdenServicio(
                rs.getInt("id_orden_servicio")
        );

        orden.setIdCliente(
                rs.getInt("id_cliente")
        );

        orden.setNombreCliente(
                rs.getString("nombre_cliente")
        );

        orden.setEstado(
                rs.getString("estado")
        );

        orden.setFechaCreacion(
                rs.getTimestamp("fecha_creacion")
        );

        orden.setFechaProgramada(
                rs.getDate("fecha_programada")
        );

        orden.setPrioridad(
                rs.getString("prioridad")
        );

        orden.setDireccionServicio(
                rs.getString("direccion_servicio")
        );

        orden.setContactoNombre(
                rs.getString("contacto_nombre")
        );

        orden.setContactoTelefono(
                rs.getString("contacto_telefono")
        );

        orden.setObservaciones(
                rs.getString("observaciones")
        );

        int creadoPor =
                rs.getInt("creado_por");

        if (!rs.wasNull()) {

            orden.setCreadoPor(
                    creadoPor
            );
        }

        return orden;
    }
}