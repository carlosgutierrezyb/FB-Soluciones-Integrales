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

    private OrdenCompraController controller;

    public void setController(OrdenCompraController controller) {
        this.controller = controller;
    }

    public OrdenCompraListView() {

        setTitle("Gestión de Órdenes de Compra");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // =========================
        // 🔹 PANEL SUPERIOR
        // =========================
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnCargar = new JButton("Cargar Órdenes");

        panelTop.add(btnCargar);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // 🔹 TABLA
        // =========================
        modelo = new DefaultTableModel(
                new Object[]{"ID Orden", "Proveedor", "Estado"}, 0
        );

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // 🔹 PANEL INFERIOR
        // =========================
        JPanel panelBottom = new JPanel();

        btnRecibir = new JButton("Registrar Entrada");
        btnFacturar = new JButton("Registrar Factura");

// 🔵 BOTÓN RECIBIR
        btnRecibir.setBackground(new Color(0, 102, 204));
        btnRecibir.setForeground(Color.WHITE);
        btnRecibir.setFocusPainted(false);
        btnRecibir.setContentAreaFilled(true);
        btnRecibir.setOpaque(true);
        btnRecibir.setBorderPainted(false);

// 🟢 BOTÓN FACTURAR
        btnFacturar.setBackground(new Color(0, 153, 0));
        btnFacturar.setForeground(Color.WHITE);
        btnFacturar.setFocusPainted(false);
        btnFacturar.setContentAreaFilled(true);
        btnFacturar.setOpaque(true);
        btnFacturar.setBorderPainted(false);

        panelBottom.add(btnRecibir);
        panelBottom.add(btnFacturar);

        add(panelBottom, BorderLayout.SOUTH);

        // =========================
        // 🔥 EVENTOS
        // =========================
        btnCargar.addActionListener(e -> cargarOrdenes());
        btnRecibir.addActionListener(e -> irAEntrada());
        btnFacturar.addActionListener(e -> irAFactura());
    }

    // =========================
    // 🔹 CARGAR ÓRDENES
    // =========================
    private void cargarOrdenes() {

        modelo.setRowCount(0);

        List<OrdenCompra> lista = controller.listarTodas();

        for (OrdenCompra o : lista) {
            modelo.addRow(new Object[]{
                    o.getId(),
                    o.getIdProveedor(),
                    o.getEstado()
            });
        }
    }

    // =========================
    // 🔹 OBTENER ORDEN SELECCIONADA
    // =========================
    private OrdenCompra obtenerOrdenSeleccionada() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return null;
        }

        int idOrden = (int) modelo.getValueAt(fila, 0);

        return controller.buscarPorId(idOrden);
    }

    // =========================
    // 🔹 IR A ENTRADA DE ALMACÉN
    // =========================
    private void irAEntrada() {

        OrdenCompra orden = obtenerOrdenSeleccionada();

        if (orden == null) return;

        if ("Recibido".equalsIgnoreCase(orden.getEstado())) {
            JOptionPane.showMessageDialog(this,
                    "La orden ya fue completamente recibida.");
            return;
        }

        EntradaAlmacenView view = new EntradaAlmacenView();
        view.setController(new controller.EntradaAlmacenController());

        view.setVisible(true);
        view.cargarOrdenes();

        dispose(); // cerrar esta ventana
    }

    // =========================
    // 🔹 IR A FACTURA
    // =========================
    private void irAFactura() {

        OrdenCompra orden = obtenerOrdenSeleccionada();

        if (orden == null) return;

        if ("Pendiente".equalsIgnoreCase(orden.getEstado())) {
            JOptionPane.showMessageDialog(this,
                    "Primero debe registrar la entrada.");
            return;
        }

        FacturaCompraView view = new FacturaCompraView();
        view.setController(new controller.FacturaCompraController());

        view.setVisible(true);
        view.cargarOrdenes();

        dispose();
    }
}