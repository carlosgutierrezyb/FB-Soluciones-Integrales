package dao;

import model.Compra;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    /**
     * Registrar compra en BD
     */
    public boolean registrarCompra(Compra compra) {

        String sql = "INSERT INTO compras (id_producto, cantidad, precio_unitario, proveedor, fecha, total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, compra.getIdProducto());
            stmt.setInt(2, compra.getCantidad());
            stmt.setDouble(3, compra.getPrecioUnitario());
            stmt.setString(4, compra.getProveedor());
            stmt.setDate(5, Date.valueOf(compra.getFecha()));
            stmt.setDouble(6, compra.getTotal());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtener historial de compras
     */
    public List<Compra> obtenerCompras() {

        List<Compra> lista = new ArrayList<>();

        String sql = "SELECT * FROM compras ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Compra c = new Compra();

                c.setId(rs.getInt("id"));
                c.setIdProducto(rs.getInt("id_producto"));
                c.setCantidad(rs.getInt("cantidad"));
                c.setPrecioUnitario(rs.getDouble("precio_unitario"));
                c.setProveedor(rs.getString("proveedor"));
                c.setFecha(rs.getDate("fecha").toLocalDate());
                c.setTotal(rs.getDouble("total"));

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}