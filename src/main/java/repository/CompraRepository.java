package repository;

import model.EntradaAlmacen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Repositorio de compras (entradas de inventario).
 *
 * RESPONSABILIDAD:
 * ✔ Persistir datos en la base de datos
 *
 * NO HACE:
 * ❌ Validaciones
 * ❌ Lógica de negocio
 * ❌ Manejo de transacciones
 */
public class CompraRepository {

    /**
     * Registra una entrada de inventario.
     *
     * @param conn conexión activa (controlada por el Service)
     * @param entrada datos de la compra
     */
    public boolean registrarEntrada(Connection conn, EntradaAlmacen entrada) {

        String sql = "INSERT INTO entradas_almacen " +
                "(id_item, cantidad_recibida, precio_compra_unitario, numero_factura, proveedor, fecha_entrada) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 🔹 Mapeo de datos
            ps.setInt(1, entrada.getIdProducto());
            ps.setInt(2, entrada.getCantidad());
            ps.setDouble(3, entrada.getPrecioCompra());
            ps.setString(4, entrada.getNumeroFactura());
            ps.setString(5, entrada.getProveedor());

            // 🔥 Conversión LocalDateTime → Timestamp (PRO)
            ps.setTimestamp(6, Timestamp.valueOf(entrada.getFechaEntrada()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("❌ Error al registrar entrada: " + e.getMessage());
            return false;
        }
    }
}