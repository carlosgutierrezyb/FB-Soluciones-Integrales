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
                "SELECT s.*, c.nombre AS nombre_categoria " +
                        "FROM servicios s " +
                        "LEFT JOIN categorias c ON s.id_categoria = c.id_cat " +
                        "WHERE s.estado = 'ACTIVO' " +
                        "ORDER BY s.nombre";

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
                "SELECT s.*, c.nombre AS nombre_categoria " +
                        "FROM servicios s " +
                        "LEFT JOIN categorias c ON s.id_categoria = c.id_cat " +
                        "ORDER BY s.nombre";

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
            int id
    ) {

        String sql =
                "SELECT s.*, c.nombre AS nombre_categoria " +
                        "FROM servicios s " +
                        "LEFT JOIN categorias c ON s.id_categoria = c.id_cat " +
                        "WHERE s.id = ?";

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
                        "codigo_referencia," +
                        "nombre," +
                        "id_categoria," +
                        "descripcion," +
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
                    servicio.getCodigoReferencia()
            );

            ps.setString(
                    2,
                    servicio.getNombre()
            );

            ps.setInt(
                    3,
                    servicio.getIdCategoria()
            );

            ps.setString(
                    4,
                    servicio.getDescripcion()
            );

            ps.setDouble(
                    5,
                    servicio.getPrecioBase()
            );

            ps.setDouble(
                    6,
                    servicio.getTiempoEstimadoHoras()
            );

            ps.setString(
                    7,
                    servicio.getEstado()
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
                        "codigo_referencia = ?, " +
                        "nombre = ?, " +
                        "id_categoria = ?, " +
                        "descripcion = ?, " +
                        "precio_base = ?, " +
                        "tiempo_estimado_horas = ?, " +
                        "estado = ? " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setString(
                    1,
                    servicio.getCodigoReferencia()
            );

            ps.setString(
                    2,
                    servicio.getNombre()
            );

            ps.setInt(
                    3,
                    servicio.getIdCategoria()
            );

            ps.setString(
                    4,
                    servicio.getDescripcion()
            );

            ps.setDouble(
                    5,
                    servicio.getPrecioBase()
            );

            ps.setDouble(
                    6,
                    servicio.getTiempoEstimadoHoras()
            );

            ps.setString(
                    7,
                    servicio.getEstado()
            );

            ps.setInt(
                    8,
                    servicio.getId()
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
            int id
    ) {

        String sql =
                "UPDATE servicios " +
                        "SET estado = 'INACTIVO' " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    id
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 🔹 REACTIVAR
    // =========================
    public boolean reactivar(
            int id
    ) {

        String sql =
                "UPDATE servicios " +
                        "SET estado = 'ACTIVO' " +
                        "WHERE id = ?";

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    id
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
                "SELECT id " +
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
                "SELECT id " +
                        "FROM servicios " +
                        "WHERE LOWER(codigo_referencia) = LOWER(?)";

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
    // 🔥 OBTENER ÚLTIMO CÓDIGO
    // =========================
    public String obtenerUltimoCodigoPorCategoria(
            int idCategoria
    ) {

        String sql =
                "SELECT s.codigo_referencia " +
                        "FROM servicios s " +
                        "INNER JOIN categorias c " +
                        "ON s.id_categoria = c.id_cat " +
                        "WHERE s.id_categoria = ? " +
                        "AND s.codigo_referencia LIKE CONCAT('SERV-', c.prefijo, '-%') " +
                        "ORDER BY s.id DESC " +
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

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 🔹 MAPEAR
    // =========================
    private Servicio mapearServicio(
            ResultSet rs
    ) throws Exception {

        Servicio s =
                new Servicio();

        s.setId(
                rs.getInt("id")
        );

        s.setCodigoReferencia(
                rs.getString("codigo_referencia")
        );

        s.setNombre(
                rs.getString("nombre")
        );

        s.setIdCategoria(
                rs.getInt("id_categoria")
        );

        s.setNombreCategoria(
                rs.getString("nombre_categoria")
        );

        s.setDescripcion(
                rs.getString("descripcion")
        );

        s.setPrecioBase(
                rs.getDouble("precio_base")
        );

        s.setTiempoEstimadoHoras(
                rs.getDouble("tiempo_estimado_horas")
        );

        s.setEstado(
                rs.getString("estado")
        );

        return s;
    }
}