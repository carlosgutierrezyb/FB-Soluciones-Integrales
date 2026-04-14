package com.fbsoluciones;

import controller.InventarioController;
import view.InventarioView;
import util.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;

/**
 * Punto de entrada de la aplicación F&B Soluciones Integrales.
 */
public class Main {

    public static void main(String[] args) {

        // 🔹 1. Validar conexión a base de datos
        probarConexion();

        // 🔹 2. Configurar estilo visual
        configurarLookAndFeel();

        // 🔹 3. Lanzar UI en hilo de Swing
        SwingUtilities.invokeLater(Main::iniciarAplicacion);
    }

    /**
     * Verifica la conexión a la base de datos.
     */
    private static void probarConexion() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa a la base de datos.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }

    /**
     * Configura el Look & Feel del sistema.
     */
    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("🎨 Look & Feel aplicado.");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo aplicar el Look & Feel.");
        }
    }

    /**
     * Inicializa la aplicación con patrón MVC.
     */
    private static void iniciarAplicacion() {

        // 🔹 1. Crear vista
        InventarioView vista = new InventarioView();

        // 🔹 2. Crear controller (inyecta vista)
        InventarioController controller = new InventarioController(vista);

        // 🔹 3. Inyectar controller en la vista
        vista.setController(controller);

        // 🔹 4. Cargar datos
        vista.inicializarDatos();

        // 🔹 5. Mostrar UI
        vista.setVisible(true);

        System.out.println("🟢 Sistema F&B Soluciones iniciado correctamente.");
    }
}