package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión a MySQL.
 */
public class DatabaseConnection {

    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/fb_seguridad_admin?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Sin contraseña

    /**
     * Establece y retorna una conexión con el servidor de base de datos.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Registrar el driver (opcional en Java moderno, pero seguro)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a MySQL");
            return conn;

        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ No se encontró el driver MySQL. Revisa el pom.xml", e);
        }
    }
}
