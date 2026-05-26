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
 * - CRUD servicios
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

                lista.add(
                        mapearServicio(rs)
                );
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

                lista.add(
                        mapearServicio(rs)
                );
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

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

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
                        "codigo," +
                        "nombre," +
                        "descripcion," +
                        "categoria," +
                        "precio_base," +
                        "tiempo_estimado_horas," +
                        "estado" +
                        ") " +
                        "VALUES(?,?,?,?,?,?,?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    servicio.getCodigo()
            );

            ps.setString(
                    2,
                    servicio.getNombre()
            );

            ps.setString(
                    3,
                    servicio.getDescripcion()
            );

            ps.setString(
                    4,
                    servicio.getCategoria()
            );

            ps.setDouble(
                    5,
                    servicio.getPrecioBase()
            );

            ps.setDouble(
                    6,
                    servicio.getTiempoEstimadoHoras()
            );

            ps.setBoolean(
                    7,
                    servicio.isEstado()
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
            Servicio servicio
    ) {

        String sql =
                "UPDATE servicios SET " +
                        "codigo = ?, " +
                        "nombre = ?, " +
                        "descripcion = ?, " +
                        "categoria = ?, " +
                        "precio_base = ?, " +
                        "tiempo_estimado_horas = ?, " +
                        "estado = ? " +
                        "WHERE id_servicio = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    servicio.getCodigo()
            );

            ps.setString(
                    2,
                    servicio.getNombre()
            );

            ps.setString(
                    3,
                    servicio.getDescripcion()
            );

            ps.setString(
                    4,
                    servicio.getCategoria()
            );

            ps.setDouble(
                    5,
                    servicio.getPrecioBase()
            );

            ps.setDouble(
                    6,
                    servicio.getTiempoEstimadoHoras()
            );

            ps.setBoolean(
                    7,
                    servicio.isEstado()
            );

            ps.setInt(
                    8,
                    servicio.getIdServicio()
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 INACTIVAR
    // =========================
    public boolean inactivar(
            int idServicio
    ) {

        String sql =
                "UPDATE servicios " +
                        "SET estado = 0 " +
                        "WHERE id_servicio = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    idServicio
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 VALIDAR NOMBRE
    // =========================
    public boolean existeNombre(
            String nombre
    ) {

        String sql =
                "SELECT id_servicio " +
                        "FROM servicios " +
                        "WHERE LOWER(nombre) = LOWER(?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    nombre
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                return rs.next();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 VALIDAR CÓDIGO
    // =========================
    public boolean existeCodigo(
            String codigo
    ) {

        String sql =
                "SELECT id_servicio " +
                        "FROM servicios " +
                        "WHERE LOWER(codigo) = LOWER(?)";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    codigo
            );

            try (

                    ResultSet rs =
                            ps.executeQuery()

            ) {

                return rs.next();
            }

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

        s.setCodigo(
                rs.getString("codigo")
        );

        s.setNombre(
                rs.getString("nombre")
        );

        s.setDescripcion(
                rs.getString("descripcion")
        );

        s.setCategoria(
                rs.getString("categoria")
        );

        s.setPrecioBase(
                rs.getDouble("precio_base")
        );

        s.setTiempoEstimadoHoras(
                rs.getDouble("tiempo_estimado_horas")
        );

        s.setEstado(
                rs.getBoolean("estado")
        );

        return s;
    }
}