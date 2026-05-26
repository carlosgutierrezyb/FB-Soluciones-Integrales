package view;

import javax.swing.*;
import java.awt.*;

/**
 * Módulo de Ventas ERP F&B
 *
 * 🔥 RESPONSABILIDADES:
 * - Gestión de clientes
 * - Gestión de servicios
 * - Órdenes de servicio
 * - Salida de almacén
 * - Facturación de ventas
 *
 * Arquitectura ERP:
 * Dashboard → VentasMenu → Procesos
 */
public class VentasMenuView extends JFrame {

    // =========================
    // BOTONES
    // =========================

    private JButton btnClientes;

    private JButton btnServicios;

    private JButton btnOrdenServicio;

    private JButton btnOrdenesServicio;

    private JButton btnSalidaAlmacen;

    private JButton btnFacturaVenta;

    // =========================
    // CONSTRUCTOR
    // =========================

    public VentasMenuView() {

        setTitle(
                "F&B Soluciones Integrales - Módulo Ventas"
        );

        setSize(850, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // 🔹 UI
    // =========================

    private void inicializarComponentes() {

        // =========================
        // TÍTULO
        // =========================

        JLabel titulo =
                new JLabel(
                        "Módulo de Ventas",
                        JLabel.CENTER
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        10,
                        10
                )
        );

        add(
                titulo,
                BorderLayout.NORTH
        );

        // =========================
        // PANEL BOTONES
        // =========================

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                20,
                                20
                        )
                );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        40,
                        20,
                        40
                )
        );

        // =========================
        // BOTONES
        // =========================

        btnClientes =
                crearBoton(
                        "Clientes",
                        new Color(0, 102, 204)
                );

        btnServicios =
                crearBoton(
                        "Servicios",
                        new Color(102, 51, 153)
                );

        btnOrdenServicio =
                crearBoton(
                        "Crear Orden Servicio",
                        new Color(0, 153, 76)
                );

        btnOrdenesServicio =
                crearBoton(
                        "Órdenes Servicio",
                        new Color(0, 153, 153)
                );

        btnSalidaAlmacen =
                crearBoton(
                        "Salida Almacén",
                        new Color(204, 153, 0)
                );

        btnFacturaVenta =
                crearBoton(
                        "Factura Venta",
                        new Color(153, 0, 153)
                );

        // =========================
        // AGREGAR BOTONES
        // =========================

        panelBotones.add(btnClientes);

        panelBotones.add(btnServicios);

        panelBotones.add(btnOrdenServicio);

        panelBotones.add(btnOrdenesServicio);

        panelBotones.add(btnSalidaAlmacen);

        panelBotones.add(btnFacturaVenta);

        add(
                panelBotones,
                BorderLayout.CENTER
        );
    }

    // =========================
    // 🔹 BOTÓN ESTILO ERP
    // =========================

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn =
                new JButton(texto);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        btn.setBackground(color);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setContentAreaFilled(true);

        btn.setOpaque(true);

        btn.setBorderPainted(false);

        return btn;
    }

    // =========================
    // GETTERS
    // =========================

    public JButton getBtnClientes() {

        return btnClientes;
    }

    public JButton getBtnServicios() {

        return btnServicios;
    }

    public JButton getBtnOrdenServicio() {

        return btnOrdenServicio;
    }

    public JButton getBtnOrdenesServicio() {

        return btnOrdenesServicio;
    }

    public JButton getBtnSalidaAlmacen() {

        return btnSalidaAlmacen;
    }

    public JButton getBtnFacturaVenta() {

        return btnFacturaVenta;
    }
}