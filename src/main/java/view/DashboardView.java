package view;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard principal del sistema F&B Soluciones Integrales.
 *
 * RESPONSABILIDADES:
 * ✔ Servir como menú central del sistema
 * ✔ Permitir acceso a los módulos:
 *   - Inventario
 *   - Compras
 *   - Ventas (futuro)
 *   - Reportes (futuro)
 *
 * 🔥 Diseño:
 * - Interfaz limpia
 * - Botones grandes tipo sistema empresarial
 */
public class DashboardView extends JFrame {

    private JButton btnInventario;
    private JButton btnCompras;
    private JButton btnVentas;
    private JButton btnReportes;

    public DashboardView() {

        setTitle("F&B Soluciones Integrales - Sistema Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // 🔹 Título superior
        JLabel titulo = new JLabel("Sistema de Gestión F&B", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        add(titulo, BorderLayout.NORTH);

        // 🔹 Panel de botones (grid)
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        btnInventario = crearBoton("Inventario", new Color(0, 102, 204));
        btnCompras = crearBoton("Compras", new Color(0, 153, 76));
        btnVentas = crearBoton("Ventas", new Color(204, 153, 0));
        btnReportes = crearBoton("Reportes", new Color(153, 0, 153));

        panelBotones.add(btnInventario);
        panelBotones.add(btnCompras);
        panelBotones.add(btnVentas);
        panelBotones.add(btnReportes);

        add(panelBotones, BorderLayout.CENTER);
    }

    /**
     * Crea botones con estilo uniforme.
     */
    private JButton crearBoton(String texto, Color color) {

        JButton btn = new JButton(texto);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        return btn;
    }

    // =========================
    // GETTERS (MVC)
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