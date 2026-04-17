package repository;

import model.EntradaAlmacen;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Repository de entradas de almacén.
 *
 * 🔥 ERP REAL:
 * - Registra compras ligadas a orden de compra
 * - Afecta inventario
 * - Permite trazabilidad completa
 */
public class EntradaAlmacenRepository {

    /**
     * Guarda una entrada de almacén (compra).
     */
    public boolean guardar(Connection conn, EntradaAlmacen entrada) {

        String sql = "INSERT INTO entradas_almacen " +
                "(id_orden, id_item, cantidad_recibida, precio_compra_unitario, numero_factura) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("📦 Guardando entrada de almacén...");

            ps.setInt(1, entrada.getIdOrden());
            ps.setInt(2, entrada.getIdItem());
            ps.setInt(3, entrada.getCantidadRecibida());
            ps.setDouble(4, entrada.getPrecioCompraUnitario());
            ps.setString(5, entrada.getNumeroFactura());

            int filas = ps.executeUpdate();

            System.out.println("✅ Entrada guardada correctamente.");

            return filas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error guardando entrada: " + e.getMessage());
            return false;
        }
    }
}