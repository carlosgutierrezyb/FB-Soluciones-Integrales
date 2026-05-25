package repository;

import model.Servicio;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository Servicios.
 *
 * 🔥 ERP F&B:
 * - Catálogo de servicios
 * - Servicios activos
 * - Base órdenes servicio
 */
public class ServicioRepository {

    // =========================
    // 🔹 LISTAR ACTIVOS
    // =========================
    public List<Servicio> listarActivos() {

        List<Servicio> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM servicios " +
                        "WHERE estado = 1 " +
                        "ORDER BY nombre";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {

            while (rs.next()) {

                Servicio s =
                        mapearServicio(rs);

                lista.add(s);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // 🔹 LISTAR TODOS
    // =========================
    public List<Servicio> listarTodos() {

        List<Servicio> lista =
                new ArrayList<>();

        String sql =
                "SELECT * " +
                        "FROM servicios " +
                        "ORDER BY nombre";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {

            while (rs.next()) {

                Servicio s =
                        mapearServicio(rs);

                lista.add(s);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Servicio buscarPorId(
            int idServicio
    ) {

        String sql =
                "SELECT * " +
                        "FROM servicios " +
                        "WHERE id_servicio = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(1, idServicio);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearServicio(rs);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public boolean guardar(
            Servicio servicio
    ) {

        String sql =
                "INSERT INTO servicios(" +
                        "nombre," +
                        "descripcion," +
                        "estado" +
                        ") " +
                        "VALUES(?,?,?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    servicio.getNombre()
            );

            ps.setString(
                    2,
                    servicio.getDescripcion()
            );

            ps.setBoolean(
                    3,
                    servicio.isEstado()
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 MAPEAR
    // =========================
    private Servicio mapearServicio(
            ResultSet rs
    ) throws Exception {

        Servicio s =
                new Servicio();

        s.setIdServicio(
                rs.getInt("id_servicio")
        );

        s.setNombre(
                rs.getString("nombre")
        );

        s.setDescripcion(
                rs.getString("descripcion")
        );

        s.setEstado(
                rs.getBoolean("estado")
        );

        return s;
    }
}