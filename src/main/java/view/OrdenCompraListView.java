package view;

import controller.OrdenCompraController;
import model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdenCompraListView extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnCargar;
    private JButton btnRecibir;
    private JButton btnFacturar;

    private JComboBox<String> comboEstado;

    private OrdenCompraController controller;

    public void setController(
            OrdenCompraController controller
    ) {

        this.controller = controller;
    }

    public OrdenCompraListView() {

        setTitle("Gestión de Órdenes de Compra");

        setSize(950, 550);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        // =========================
        // 🔹 PANEL SUPERIOR
        // =========================
        JPanel panelTop =
                new JPanel(
                        new FlowLayout(FlowLayout.LEFT)
                );

        panelTop.add(
                new JLabel("Estado:")
        );

        comboEstado =
                new JComboBox<>();

        comboEstado.addItem("TODAS");
        comboEstado.addItem("Pendiente");
        comboEstado.addItem("Parcial");
        comboEstado.addItem("Recibido");
        comboEstado.addItem("Cancelado");

        btnCargar =
                new JButton("Cargar Órdenes");

        panelTop.add(comboEstado);

        panelTop.add(btnCargar);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // 🔹 TABLA
        // =========================
        modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID Orden",
                                "Proveedor",
                                "Estado"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tabla =
                new JTable(modelo);

        tabla.setRowHeight(24);

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        // =========================
        // 🔹 PANEL INFERIOR
        // =========================
        JPanel panelBottom =
                new JPanel();

        btnRecibir =
                new JButton("Registrar Entrada");

        btnFacturar =
                new JButton("Registrar Factura");

        // 🔵 BOTÓN RECIBIR
        btnRecibir.setBackground(
                new Color(0, 102, 204)
        );

        btnRecibir.setForeground(Color.WHITE);

        btnRecibir.setFocusPainted(false);

        btnRecibir.setContentAreaFilled(true);

        btnRecibir.setOpaque(true);

        btnRecibir.setBorderPainted(false);

        // 🟢 BOTÓN FACTURAR
        btnFacturar.setBackground(
                new Color(0, 153, 0)
        );

        btnFacturar.setForeground(Color.WHITE);

        btnFacturar.setFocusPainted(false);

        btnFacturar.setContentAreaFilled(true);

        btnFacturar.setOpaque(true);

        btnFacturar.setBorderPainted(false);

        panelBottom.add(btnRecibir);

        panelBottom.add(btnFacturar);

        add(
                panelBottom,
                BorderLayout.SOUTH
        );

        // =========================
        // 🔥 EVENTOS
        // =========================
        btnCargar.addActionListener(
                e -> cargarOrdenes()
        );

        btnRecibir.addActionListener(
                e -> irAEntrada()
        );

        btnFacturar.addActionListener(
                e -> irAFactura()
        );
    }

    // =========================
    // 🔹 CARGAR ÓRDENES
    // =========================
    private void cargarOrdenes() {

        modelo.setRowCount(0);

        String estado =
                comboEstado.getSelectedItem()
                        .toString();

        List<OrdenCompra> lista;

        if ("TODAS".equalsIgnoreCase(estado)) {

            lista =
                    controller.listarTodas();

        } else {

            lista =
                    controller.listarPorEstado(
                            estado
                    );
        }

        for (OrdenCompra o : lista) {

            modelo.addRow(
                    new Object[]{
                            o.getId(),

                            // 🔥 AQUÍ VA EL NOMBRE
                            o.getNombreProveedor(),

                            o.getEstado()
                    }
            );
        }
    }

    // =========================
    // 🔹 OBTENER ORDEN
    // =========================
    private OrdenCompra obtenerOrdenSeleccionada() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una orden."
            );

            return null;
        }

        int idOrden =
                (int) modelo.getValueAt(
                        fila,
                        0
                );

        return controller.buscarPorId(idOrden);
    }

    // =========================
    // 🔹 IR A ENTRADA
    // =========================
    private void irAEntrada() {

        OrdenCompra orden =
                obtenerOrdenSeleccionada();

        if (orden == null) {

            return;
        }

        if (
                "Recibido".equalsIgnoreCase(
                        orden.getEstado()
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "La orden ya fue completamente recibida."
            );

            return;
        }

        // =========================
        // CREAR VISTA
        // =========================
        EntradaAlmacenView view =
                new EntradaAlmacenView();

        controller.EntradaAlmacenController controllerEntrada =
                new controller.EntradaAlmacenController();

        view.setController(controllerEntrada);

        // =========================
        // CARGAR ÓRDENES
        // =========================
        view.cargarOrdenes();

        // =========================
        // 🔥 PRESELECCIONAR ORDEN
        // =========================
        view.seleccionarOrden(orden);

        // =========================
        // MOSTRAR
        // =========================
        view.setVisible(true);

        dispose();
    }

    // =========================
    // 🔹 IR A FACTURA
    // =========================
    private void irAFactura() {

        OrdenCompra orden =
                obtenerOrdenSeleccionada();

        if (orden == null) {

            return;
        }

        if (
                "Pendiente".equalsIgnoreCase(
                        orden.getEstado()
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "La orden aún no tiene entradas de almacén registradas."
            );

            return;
        }

        if (
                "Cancelado".equalsIgnoreCase(
                        orden.getEstado()
                )
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "No puede facturar una orden cancelada."
            );

            return;
        }

        FacturaCompraView view =
                new FacturaCompraView();

        controller.FacturaCompraController controllerFactura =
                new controller.FacturaCompraController();

        view.setController(controllerFactura);

        view.setVisible(true);

        view.cargarOrdenes();

        dispose();
    }
}