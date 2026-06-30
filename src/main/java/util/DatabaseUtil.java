package util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase utilitaria para la gestión centralizada de conexiones y transacciones.
 */
public class DatabaseUtil {

    /**
     * Revoca de forma segura los cambios en una transacción activa.
     */
    public static void rollbackSilencioso(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Error crítico ejecutando rollback: " + e.getMessage());
        }
    }

    /**
     * Restablece el autoCommit a true y cierra la conexión de forma segura.
     */
    public static void cerrarConexionTransaccional(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión transaccional: " + e.getMessage());
        }
    }
}