package repository;

import model.Cliente;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de clientes.
 *
 * 🔥 ERP F&B:
 * - Maneja CRUD de clientes
 * - Base para ventas
 * - Base para órdenes de servicio
 */
public class ClienteRepository {

    // =========================
    // 🔹 LISTAR TODOS
    // =========================
    public List<Cliente> listarTodos() {

        List<Cliente> lista =
                new ArrayList<>();

        String sql =
                "SELECT * FROM clientes " +
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
                        mapear(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error listando clientes: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public boolean guardar(
            Cliente c
    ) {

        String sql =
                "INSERT INTO clientes (" +
                        "tipo_identificacion, " +
                        "nombre, " +
                        "identificacion, " +
                        "telefono, " +
                        "correo, " +
                        "direccion, " +
                        "ciudad, " +
                        "contacto_nombre, " +
                        "contacto_telefono, " +
                        "contacto_email, " +
                        "estado" +
                        ") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    c.getTipoIdentificacion()
            );

            ps.setString(
                    2,
                    c.getNombre()
            );

            ps.setString(
                    3,
                    c.getIdentificacion()
            );

            ps.setString(
                    4,
                    c.getTelefono()
            );

            ps.setString(
                    5,
                    c.getCorreo()
            );

            ps.setString(
                    6,
                    c.getDireccion()
            );

            ps.setString(
                    7,
                    c.getCiudad()
            );

            // 🔥 NUEVOS CAMPOS
            ps.setString(
                    8,
                    c.getContactoNombre()
            );

            ps.setString(
                    9,
                    c.getContactoTelefono()
            );

            ps.setString(
                    10,
                    c.getContactoEmail()
            );

            ps.setString(
                    11,
                    c.getEstado()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error guardando cliente: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public boolean actualizar(
            Cliente c
    ) {

        String sql =
                "UPDATE clientes SET " +
                        "tipo_identificacion = ?, " +
                        "nombre = ?, " +
                        "identificacion = ?, " +
                        "telefono = ?, " +
                        "correo = ?, " +
                        "direccion = ?, " +
                        "ciudad = ?, " +
                        "contacto_nombre = ?, " +
                        "contacto_telefono = ?, " +
                        "contacto_email = ?, " +
                        "estado = ? " +
                        "WHERE id_cliente = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    c.getTipoIdentificacion()
            );

            ps.setString(
                    2,
                    c.getNombre()
            );

            ps.setString(
                    3,
                    c.getIdentificacion()
            );

            ps.setString(
                    4,
                    c.getTelefono()
            );

            ps.setString(
                    5,
                    c.getCorreo()
            );

            ps.setString(
                    6,
                    c.getDireccion()
            );

            ps.setString(
                    7,
                    c.getCiudad()
            );

            // 🔥 NUEVOS CAMPOS
            ps.setString(
                    8,
                    c.getContactoNombre()
            );

            ps.setString(
                    9,
                    c.getContactoTelefono()
            );

            ps.setString(
                    10,
                    c.getContactoEmail()
            );

            ps.setString(
                    11,
                    c.getEstado()
            );

            ps.setInt(
                    12,
                    c.getIdCliente()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error actualizando cliente: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 ELIMINAR
    // =========================
    public boolean eliminar(
            int idCliente
    ) {

        String sql =
                "DELETE FROM clientes " +
                        "WHERE id_cliente = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idCliente
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error eliminando cliente: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Cliente buscarPorId(
            int idCliente
    ) {

        String sql =
                "SELECT * FROM clientes " +
                        "WHERE id_cliente = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idCliente
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
                    "❌ Error buscando cliente: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔧 MAPPER
    // =========================
    private Cliente mapear(
            ResultSet rs
    ) throws SQLException {

        Cliente c =
                new Cliente();

        c.setIdCliente(
                rs.getInt("id_cliente")
        );

        c.setNombre(
                rs.getString("nombre")
        );

        c.setTipoIdentificacion(
                rs.getString("tipo_identificacion")
        );

        c.setIdentificacion(
                rs.getString("identificacion")
        );

        c.setTelefono(
                rs.getString("telefono")
        );

        c.setCorreo(
                rs.getString("correo")
        );

        c.setDireccion(
                rs.getString("direccion")
        );

        c.setCiudad(
                rs.getString("ciudad")
        );

        // 🔥 NUEVOS CAMPOS
        c.setContactoNombre(
                rs.getString("contacto_nombre")
        );

        c.setContactoTelefono(
                rs.getString("contacto_telefono")
        );

        c.setContactoEmail(
                rs.getString("contacto_email")
        );

        c.setEstado(
                rs.getString("estado")
        );

        return c;
    }
}