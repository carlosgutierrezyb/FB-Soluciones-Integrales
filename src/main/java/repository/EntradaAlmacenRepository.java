package repository;

import model.EntradaAlmacen;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository de entradas de almacén.
 *
 * 🔥 ERP REAL:
 * - Registra recepciones físicas
 * - Actualiza inventario
 * - Controla cantidades recibidas
 * - Maneja trazabilidad:
 *      • Remisión
 *      • Factura proveedor
 *
 * ❌ NO lógica de negocio
 */
public class EntradaAlmacenRepository {

    private InventarioRepository inventarioRepo;

    public EntradaAlmacenRepository() {

        this.inventarioRepo =
                new InventarioRepository();
    }

    // =========================
    // 🔹 GUARDAR ENTRADA
    // =========================
    public boolean guardar(
            Connection conn,
            EntradaAlmacen entrada
    ) {

        String sql =
                "INSERT INTO entradas_almacen "
                        + "(id_orden, id_item, cantidad_recibida, "
                        + "precio_compra_unitario, "
                        + "numero_remision, numero_factura) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            System.out.println(
                    "📦 Guardando entrada de almacén..."
            );

            // =========================
            // 🔹 INSERTAR ENTRADA
            // =========================

            ps.setInt(
                    1,
                    entrada.getIdOrden()
            );

            ps.setInt(
                    2,
                    entrada.getIdItem()
            );

            ps.setInt(
                    3,
                    entrada.getCantidadRecibida()
            );

            ps.setDouble(
                    4,
                    entrada.getPrecioCompraUnitario()
            );

            ps.setString(
                    5,
                    entrada.getNumeroRemision()
            );

            ps.setString(
                    6,
                    entrada.getNumeroFactura()
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo registrar la entrada."
                );
            }

            // =========================
            // 🔥 ACTUALIZAR INVENTARIO
            // =========================

            boolean actualizado =
                    inventarioRepo.actualizarStock(
                            conn,
                            entrada.getIdItem(),
                            entrada.getCantidadRecibida()
                    );

            if (!actualizado) {

                throw new SQLException(
                        "No se pudo actualizar inventario."
                );
            }

            System.out.println(
                    "✅ Entrada registrada correctamente."
            );

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error guardando entrada: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================
    // 🔹 TOTAL RECIBIDO
    // 🔥 USA MISMA TRANSACCIÓN
    // =========================
    public int obtenerCantidadRecibida(
            Connection conn,
            int idItem,
            int idOrden
    ) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(cantidad_recibida), 0) AS total "
                        + "FROM entradas_almacen "
                        + "WHERE id_item = ? "
                        + "AND id_orden = ?";

        try (
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idItem);

            ps.setInt(2, idOrden);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int total =
                            rs.getInt("total");

                    System.out.println(
                            "📦 Recibido OC "
                                    + idOrden
                                    + " ITEM "
                                    + idItem
                                    + ": "
                                    + total
                    );

                    return total;
                }
            }
        }

        return 0;
    }

    // =========================
    // 🔹 MÉTODO LEGACY
    // =========================
    public int obtenerCantidadRecibida(
            int idItem,
            int idOrden
    ) {

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            return obtenerCantidadRecibida(
                    conn,
                    idItem,
                    idOrden
            );

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error calculando recibido: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // =========================
    // 🔹 OBTENER ID ITEM
    // =========================
    public int obtenerIdItemPorEntrada(
            int idEntrada
    ) {

        String sql =
                "SELECT id_item "
                        + "FROM entradas_almacen "
                        + "WHERE id_entrada = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idEntrada);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt("id_item");
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo item: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // =========================
    // 🔹 OBTENER CANTIDAD
    // =========================
    public int obtenerCantidadPorEntrada(
            int idEntrada
    ) {

        String sql =
                "SELECT cantidad_recibida "
                        + "FROM entradas_almacen "
                        + "WHERE id_entrada = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idEntrada);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(
                            "cantidad_recibida"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo cantidad: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // =========================
    // 🔹 OBTENER PRECIO COMPRA
    // =========================
    public double obtenerPrecioCompraPorEntrada(
            int idEntrada
    ) {

        String sql =
                "SELECT precio_compra_unitario "
                        + "FROM entradas_almacen "
                        + "WHERE id_entrada = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idEntrada);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getDouble(
                            "precio_compra_unitario"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo precio compra: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // =========================
    // 🔹 OBTENER FACTURA
    // =========================
    public String obtenerFacturaPorEntrada(
            int idEntrada
    ) {

        String sql =
                "SELECT numero_factura "
                        + "FROM entradas_almacen "
                        + "WHERE id_entrada = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idEntrada);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getString(
                            "numero_factura"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo factura: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // 🔹 OBTENER REMISIÓN
    // =========================
    public String obtenerRemisionPorEntrada(
            int idEntrada
    ) {

        String sql =
                "SELECT numero_remision "
                        + "FROM entradas_almacen "
                        + "WHERE id_entrada = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idEntrada);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getString(
                            "numero_remision"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "❌ Error obteniendo remisión: "
                            + e.getMessage()
            );
        }

        return null;
    }
}