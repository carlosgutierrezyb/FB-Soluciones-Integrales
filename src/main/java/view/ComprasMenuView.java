package view;

import javax.swing.*;
import java.awt.*;

/**
 * Submódulo de Compras ERP
 *
 * 🔥 RESPONSABILIDADES:
 * - Centralizar procesos de compras
 * - Navegar entre:
 *   ✔ Proveedores
 *   ✔ Crear Orden
 *   ✔ Órdenes de Compra
 *   ✔ Entrada de Almacén
 *   ✔ Factura de Compra
 */
public class ComprasMenuView extends JFrame {

    private JButton btnProveedores;

    private JButton btnCrearOrden;
    private JButton btnOrdenesCompra;
    private JButton btnEntradaAlmacen;
    private JButton btnFacturaCompra;

    public ComprasMenuView() {

        setTitle("ERP F&B - Módulo de Compras");

        setSize(750, 450);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        // =========================
        // TÍTULO
        // =========================
        JLabel titulo = new JLabel(
                "Módulo de Compras",
                JLabel.CENTER
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

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
        // PANEL BOTONES
        // =========================
        JPanel panelBotones = new JPanel(
                new GridLayout(3, 2, 20, 20)
        );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        40,
                        40,
                        40
                )
        );

        // =========================
        // BOTONES
        // =========================

        btnProveedores =
                crearBoton(
                        "Proveedores",
                        new Color(0, 102, 204)
                );

        btnCrearOrden =
                crearBoton(
                        "Crear Orden",
                        new Color(0, 153, 76)
                );

        btnOrdenesCompra =
                crearBoton(
                        "Órdenes de Compra",
                        new Color(0, 153, 153)
                );

        btnEntradaAlmacen =
                crearBoton(
                        "Entrada Almacén",
                        new Color(204, 153, 0)
                );

        btnFacturaCompra =
                crearBoton(
                        "Factura Compra",
                        new Color(153, 0, 153)
                );

        // =========================
        // AGREGAR BOTONES
        // =========================

        panelBotones.add(btnProveedores);

        panelBotones.add(btnCrearOrden);

        panelBotones.add(btnOrdenesCompra);

        panelBotones.add(btnEntradaAlmacen);

        panelBotones.add(btnFacturaCompra);

        add(panelBotones, BorderLayout.CENTER);
    }

    // =========================
    // 🔹 CREAR BOTÓN ESTILO ERP
    // =========================
    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn = new JButton(texto);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
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
    // 🔹 GETTERS
    // =========================

    public JButton getBtnProveedores() {
        return btnProveedores;
    }

    public JButton getBtnCrearOrden() {
        return btnCrearOrden;
    }

    public JButton getBtnOrdenesCompra() {
        return btnOrdenesCompra;
    }

    public JButton getBtnEntradaAlmacen() {
        return btnEntradaAlmacen;
    }

    public JButton getBtnFacturaCompra() {
        return btnFacturaCompra;
    }
}