package repository;

import model.EntradaAlmacen;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Repositorio encargado únicamente de persistir las entradas de almacén.
 *
 * ❌ NO maneja lógica de negocio
 * ❌ NO actualiza stock
 * ✔ Solo inserta registros
 */
public class CompraRepository {

    /**
     * Registra una entrada de inventario (compra).
     */
    public boolean registrarEntrada(EntradaAlmacen entrada) {

        String sql = "INSERT INTO entradas_almacen (id_item, cantidad_recibida, precio_compra_unitario, numero_factura) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entrada.getIdProducto()); // 🔥 corregido
            ps.setInt(2, entrada.getCantidad());
            ps.setDouble(3, entrada.getPrecioCompra());
            ps.setString(4, entrada.getNumeroFactura());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar entrada: " + e.getMessage());
            return false;
        }
    }
}