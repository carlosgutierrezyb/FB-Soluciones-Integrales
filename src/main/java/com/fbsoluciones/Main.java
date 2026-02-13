package com.fbsoluciones;

import controller.InventarioController;
import view.InventarioView;
import javax.swing.SwingUtilities;
import util.DatabaseConnection;

/**
 * Punto de entrada de la aplicación de F&B Soluciones Integrales.
 */
public class Main {

    public static void main(String[] args) {

        // ===== PRUEBA DE CONEXIÓN A MYSQL =====
        try {
            if (DatabaseConnection.getConnection() != null) {
                System.out.println("✅ ¡Conexión exitosa a la base de datos F&B!");
            }
        } catch (Exception e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }

        // ===== INICIO DE INTERFAZ SWING =====
        SwingUtilities.invokeLater(() -> {

            // Crear ventana
            InventarioView vista = new InventarioView();

            // Mostrar ventana
            vista.setVisible(true);

            // Confirmación en consola
            System.out.println("🟢 Sistema F&B Soluciones: Interfaz cargada correctamente.");
        });
    }
}


