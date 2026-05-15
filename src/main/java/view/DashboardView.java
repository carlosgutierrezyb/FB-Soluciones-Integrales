package view;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard principal ERP F&B

 * - Módulos principales
 * - Navegación escalable
 * - Base ERP real
 */
public class DashboardView extends JFrame {

    private JButton btnInventario;
    private JButton btnCompras;
    private JButton btnVentas;
    private JButton btnReportes;

    public DashboardView() {

        setTitle("F&B Soluciones Integrales - ERP");
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // =========================
        // 🔹 TÍTULO
        // =========================
        JLabel titulo = new JLabel(
                "Sistema de Gestión Empresarial F&B",
                JLabel.CENTER
        );

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        10,
                        10,
                        10
                )
        );

        add(titulo, BorderLayout.NORTH);

        // =========================
        // 🔹 PANEL BOTONES
        // =========================
        JPanel panelBotones = new JPanel(
                new GridLayout(2, 2, 25, 25)
        );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        50,
                        30,
                        50
                )
        );

        btnInventario = crearBoton(
                "Inventario",
                new Color(0, 102, 204)
        );

        btnCompras = crearBoton(
                "Compras",
                new Color(0, 153, 76)
        );

        btnVentas = crearBoton(
                "Ventas",
                new Color(204, 153, 0)
        );

        btnReportes = crearBoton(
                "Reportes",
                new Color(102, 102, 102)
        );

        panelBotones.add(btnInventario);
        panelBotones.add(btnCompras);
        panelBotones.add(btnVentas);
        panelBotones.add(btnReportes);

        add(panelBotones, BorderLayout.CENTER);
    }

    // =========================
    // 🔹 CREAR BOTÓN
    // =========================
    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn = new JButton(texto);

        btn.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return btn;
    }

    // =========================
    // 🔹 GETTERS
    // =========================

    public JButton getBtnInventario() {
        return btnInventario;
    }

    public JButton getBtnCompras() {
        return btnCompras;
    }

    public JButton getBtnVentas() {
        return btnVentas;
    }

    public JButton getBtnReportes() {
        return btnReportes;
    }
}