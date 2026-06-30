package view;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard principal del sistema ERP.
 * Centraliza los accesos a los módulos operacionales, logísticos y comerciales.
 */
public class DashboardView extends JFrame {

    private JButton btnInventario;
    private JButton btnCompras;
    private JButton btnVentas;
    private JButton btnOrdenesServicio; // Nuevo módulo operativo
    private JButton btnAsignacionTecnica; // Nuevo módulo de despacho
    private JButton btnReportes;

    public DashboardView() {
        setTitle("F&B Soluciones Integrales - ERP");
        setSize(750, 520); // Incrementamos ligeramente la altura para la nueva fila de botones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Encabezado de la aplicación
        JLabel titulo = new JLabel("Sistema de Gestión Empresarial F&B", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Panel de control estructurado en 3 filas y 2 columnas
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 25, 25));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));

        // Inicialización de botones con paleta cromática corporativa
        btnInventario = crearBoton("Inventario", new Color(0, 102, 204));       // Azul
        btnCompras = crearBoton("Compras", new Color(0, 153, 76));             // Verde
        btnVentas = crearBoton("Ventas", new Color(204, 153, 0));               // Amarillo/Ocre

        // Nuevos accesos sincronizados con el Core de Servicios
        btnOrdenesServicio = crearBoton("Órdenes de Servicio", new Color(102, 51, 153)); // Púrpura
        btnAsignacionTecnica = crearBoton("Asignación Técnica", new Color(211, 84, 0));   // Naranja Operativo

        btnReportes = crearBoton("Reportes", new Color(102, 102, 102));         // Gris

        // Inserción secuencial en la grilla
        panelBotones.add(btnInventario);
        panelBotones.add(btnCompras);
        panelBotones.add(btnVentas);
        panelBotones.add(btnOrdenesServicio);
        panelBotones.add(btnAsignacionTecnica);
        panelBotones.add(panelBotones.add(btnReportes));

        add(panelBotones, BorderLayout.CENTER);
    }

    /**
     * Construye un botón estandarizado bajo las pautas visuales de la interfaz del ERP.
     */
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Getters para la captura de eventos en el controlador mediador

    public JButton getBtnInventario() {
        return btnInventario;
    }

    public JButton getBtnCompras() {
        return btnCompras;
    }

    public JButton getBtnVentas() {
        return btnVentas;
    }

    public JButton getBtnOrdenesServicio() {
        return btnOrdenesServicio;
    }

    public JButton getBtnAsignacionTecnica() {
        return btnAsignacionTecnica;
    }

    public JButton getBtnReportes() {
        return btnReportes;
    }
}