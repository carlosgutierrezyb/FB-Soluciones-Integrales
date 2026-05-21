package repository;

import model.Producto;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de productos.
 *
 * 🔥 RESPONSABILIDAD:
 * - Manejar catálogo de productos
 * - Manejar estado ACTIVO / INACTIVO
 * - Manejar stock básico
 *
 * ❌ NO contiene lógica de negocio
 */
public class ProductoRepository {

    // =========================
    // 🔹 GUARDAR
    // =========================
    public boolean guardar(Producto p) {

        String sql =
                "INSERT INTO producto (" +
                        "codigo_referencia, " +
                        "nombre, " +
                        "id_categoria, " +
                        "stock_actual, " +
                        "stock_minimo, " +
                        "estado" +
                        ") VALUES (?, ?, ?, ?, ?, ?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    p.getCodigoReferencia()
            );

            ps.setString(
                    2,
                    p.getNombre()
            );

            ps.setInt(
                    3,
                    p.getIdCategoria()
            );

            ps.setInt(
                    4,
                    p.getStockActual()
            );

            ps.setInt(
                    5,
                    p.getStockMinimo()
            );

            ps.setString(
                    6,
                    p.getEstado() == null
                            ? "ACTIVO"
                            : p.getEstado()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error al guardar producto: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public boolean actualizar(Producto p) {

        String sql =
                "UPDATE producto " +
                        "SET nombre = ?, " +
                        "id_categoria = ?, " +
                        "stock_minimo = ? " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    p.getNombre()
            );

            ps.setInt(
                    2,
                    p.getIdCategoria()
            );

            ps.setInt(
                    3,
                    p.getStockMinimo()
            );

            ps.setInt(
                    4,
                    p.getId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error al actualizar producto: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 INACTIVAR PRODUCTO
    // =========================
    public boolean eliminar(int id) {

        String sql =
                "UPDATE producto " +
                        "SET estado = 'INACTIVO' " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error inactivando producto: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 REACTIVAR PRODUCTO
    // =========================
    public boolean reactivarProducto(int id) {

        String sql =
                "UPDATE producto " +
                        "SET estado = 'ACTIVO' " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error reactivando producto: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 LISTAR ACTIVOS
    // =========================
    public List<Producto> listarTodos() {

        List<Producto> lista =
                new ArrayList<>();

        String sql =
                "SELECT * FROM producto " +
                        "WHERE estado = 'ACTIVO' " +
                        "ORDER BY nombre";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                Statement st =
                        conn.createStatement();

                ResultSet rs =
                        st.executeQuery(sql)

        ) {

            while (rs.next()) {

                lista.add(
                        mapearProducto(rs)
                );
            }

            System.out.println(
                    "Productos activos encontrados: "
                            + lista.size()
            );

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error al listar productos: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 LISTAR INACTIVOS
    // =========================
    public List<Producto> listarInactivos() {

        List<Producto> lista =
                new ArrayList<>();

        String sql =
                "SELECT * FROM producto " +
                        "WHERE estado = 'INACTIVO' " +
                        "ORDER BY nombre";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                Statement st =
                        conn.createStatement();

                ResultSet rs =
                        st.executeQuery(sql)

        ) {

            while (rs.next()) {

                lista.add(
                        mapearProducto(rs)
                );
            }

            System.out.println(
                    "Productos inactivos encontrados: "
                            + lista.size()
            );

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando productos inactivos: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Producto buscarPorId(int id) {

        String sql =
                "SELECT * FROM producto " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, id);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error al buscar producto: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔹 ALIAS
    // =========================
    public Producto obtenerPorId(int id) {
        return buscarPorId(id);
    }

    // =========================
    // 🔥 INVENTARIO SIMPLE
    // =========================
    public boolean aumentarStock(
            int idProducto,
            int cantidad
    ) {

        String sql =
                "UPDATE producto " +
                        "SET stock_actual = stock_actual + ? " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, cantidad);

            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error aumentando stock: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 ÚLTIMO CÓDIGO
    // =========================
    public String obtenerUltimoCodigoPorCategoria(
            int idCategoria
    ) {

        String sql =
                "SELECT codigo_referencia " +
                        "FROM producto " +
                        "WHERE id_categoria = ? " +
                        "ORDER BY id DESC " +
                        "LIMIT 1";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idCategoria);

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                if (rs.next()) {

                    return rs.getString(
                            "codigo_referencia"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo código: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔧 MAPPER
    // =========================
    private Producto mapearProducto(
            ResultSet rs
    ) throws SQLException {

        Producto p =
                new Producto();

        p.setId(
                rs.getInt("id")
        );

        p.setCodigoReferencia(
                rs.getString("codigo_referencia")
        );

        p.setNombre(
                rs.getString("nombre")
        );

        p.setIdCategoria(
                rs.getInt("id_categoria")
        );

        p.setEstado(
                rs.getString("estado")
        );

        p.setStockActual(
                rs.getInt("stock_actual")
        );

        p.setStockMinimo(
                rs.getInt("stock_minimo")
        );

        return p;
    }
}