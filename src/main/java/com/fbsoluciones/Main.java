package com.fbsoluciones;

import controller.DashboardController;
import controller.InventarioController;
import view.DashboardView;
import view.InventarioView;
import util.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;

/**
 * Punto de entrada de la aplicación F&B Soluciones Integrales.
 *
 * RESPONSABILIDADES:
 * ✔ Validar conexión a base de datos
 * ✔ Configurar entorno visual (Look & Feel)
 * ✔ Inicializar módulos (MVC)
 * ✔ Lanzar interfaz gráfica
 *
 * 🔥 Preparado para:
 * - Módulo de Compras
 * - Módulo de Ventas
 * - Dashboard principal
 */
public class Main {

    public static void main(String[] args) {

        // 🔹 1. Validar conexión
        if (!probarConexion()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar a la base de datos.\nEl sistema se cerrará.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 🔹 2. Look & Feel
        configurarLookAndFeel();

        // 🔹 3. Lanzar UI (hilo seguro de Swing)
        SwingUtilities.invokeLater(Main::iniciarAplicacion);
    }

    /**
     * Verifica conexión a base de datos.
     */
    private static boolean probarConexion() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa a la base de datos.");
                return true;
            }

        } catch (Exception e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }

        return false;
    }

    /**
     * Aplica estilo visual del sistema operativo.
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
     * Inicializa la aplicación con arquitectura MVC.
     */
    private static void iniciarAplicacion() {

        // 🔹 Crear Dashboard
        DashboardView dashboard = new DashboardView();

        // 🔹 Conectar Controller
        new DashboardController(dashboard);

        // 🔹 Mostrar
        dashboard.setVisible(true);

        System.out.println("🟢 Sistema F&B iniciado con Dashboard.");
    }
}