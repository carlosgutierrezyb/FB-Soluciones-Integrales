package repository;

import model.DetalleOrdenCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository de detalle de órdenes de compra.
 *
 * 🔥 RESPONSABILIDAD:
 * - Insertar ítems de una orden
 * - Usar MISMA conexión del Service (transacción)
 *
 * ❌ No maneja conexión propia
 * ❌ No contiene lógica de negocio
 */
public class DetalleOrdenCompraRepository {

    /**
     * 🔥 Inserta múltiples detalles (modo ERP PRO)
     */
    public boolean insertarLista(List<DetalleOrdenCompra> lista, Connection conn) throws SQLException {

        if (lista == null || lista.isEmpty()) {
            throw new SQLException("La lista de detalles está vacía.");
        }

        String sql = "INSERT INTO detalle_orden (id_orden, id_item, cantidad_pedida) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            for (DetalleOrdenCompra d : lista) {

                ps.setInt(1, d.getIdOrden());
                ps.setInt(2, d.getIdItem());
                ps.setInt(3, d.getCantidadPedida());

                ps.addBatch(); // 🔥 eficiencia PRO
            }

            int[] resultados = ps.executeBatch();

            // 🔥 Validación opcional PRO
            for (int r : resultados) {
                if (r == 0) {
                    throw new SQLException("Error insertando uno de los detalles.");
                }
            }

            System.out.println("📦 Detalles insertados correctamente.");

            return true;
        }
    }
}