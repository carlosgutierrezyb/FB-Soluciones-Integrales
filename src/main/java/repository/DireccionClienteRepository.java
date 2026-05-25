package repository;

import model.DireccionCliente;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository Direcciones Cliente.
 *
 * 🔥 ERP F&B:
 * - CRUD direcciones/sedes
 * - Consulta por cliente
 * - Base órdenes de servicio
 */
public class DireccionClienteRepository {

    // =========================
    // 🔹 LISTAR POR CLIENTE
    // =========================
    public List<DireccionCliente> listarPorCliente(
            int idCliente
    ) {

        List<DireccionCliente> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM direcciones_cliente " +
                        "WHERE id_cliente = ? " +
                        "AND estado = 1 " +
                        "ORDER BY nombre_sede ASC";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idCliente);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                DireccionCliente d =
                        mapear(rs);

                lista.add(d);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public boolean guardar(
            DireccionCliente d
    ) {

        String sql =
                "INSERT INTO direcciones_cliente (" +
                        "id_cliente," +
                        "nombre_sede," +
                        "direccion," +
                        "ciudad," +
                        "contacto_nombre," +
                        "contacto_telefono," +
                        "estado" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    d.getIdCliente()
            );

            ps.setString(
                    2,
                    d.getNombreSede()
            );

            ps.setString(
                    3,
                    d.getDireccion()
            );

            ps.setString(
                    4,
                    d.getCiudad()
            );

            ps.setString(
                    5,
                    d.getContactoNombre()
            );

            ps.setString(
                    6,
                    d.getContactoTelefono()
            );

            ps.setBoolean(
                    7,
                    d.isEstado()
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public boolean actualizar(
            DireccionCliente d
    ) {

        String sql =
                "UPDATE direcciones_cliente SET " +
                        "id_cliente = ?, " +
                        "nombre_sede = ?, " +
                        "direccion = ?, " +
                        "ciudad = ?, " +
                        "contacto_nombre = ?, " +
                        "contacto_telefono = ?, " +
                        "estado = ? " +
                        "WHERE id_direccion = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, d.getIdCliente());
            ps.setString(2, d.getNombreSede());
            ps.setString(3, d.getDireccion());
            ps.setString(4, d.getCiudad());
            ps.setString(5, d.getContactoNombre());
            ps.setString(6, d.getContactoTelefono());
            ps.setBoolean(7, d.isEstado());
            ps.setInt(8, d.getIdDireccion());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public DireccionCliente buscarPorId(
            int idDireccion
    ) {

        String sql =
                "SELECT * " +
                        "FROM direcciones_cliente " +
                        "WHERE id_direccion = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idDireccion);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return mapear(rs);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 🔹 ELIMINAR (BORRADO LÓGICO)
    // =========================
    public boolean eliminar(
            int idDireccion
    ) {

        String sql =
                "UPDATE direcciones_cliente SET " +
                        "estado = 0 " +
                        "WHERE id_direccion = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idDireccion);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 MAPEAR RESULTSET
    // =========================
    private DireccionCliente mapear(
            ResultSet rs
    ) throws Exception {

        DireccionCliente d =
                new DireccionCliente();

        d.setIdDireccion(
                rs.getInt("id_direccion")
        );

        d.setIdCliente(
                rs.getInt("id_cliente")
        );

        d.setNombreSede(
                rs.getString("nombre_sede")
        );

        d.setDireccion(
                rs.getString("direccion")
        );

        d.setCiudad(
                rs.getString("ciudad")
        );

        d.setContactoNombre(
                rs.getString("contacto_nombre")
        );

        d.setContactoTelefono(
                rs.getString("contacto_telefono")
        );

        d.setEstado(
                rs.getBoolean("estado")
        );

        return d;
    }
}