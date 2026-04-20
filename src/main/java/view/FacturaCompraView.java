package view;

import controller.FacturaCompraController;
import model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacturaCompraView extends JFrame {

    private JComboBox<OrdenCompra> comboOrdenes;
    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNumeroFactura;
    private JTextField txtFecha;

    private JButton btnCargar;
    private JButton btnRegistrar;

    private FacturaCompraController controller;

    public void setController(FacturaCompraController controller) {
        this.controller = controller;
    }

    public FacturaCompraView() {

        setTitle("Registro de Factura de Compra");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // =========================
        // 🔹 PANEL SUPERIOR
        // =========================
        JPanel panelTop = new JPanel(new FlowLayout());

        comboOrdenes = new JComboBox<>();
        btnCargar = new JButton("Cargar Orden");

        panelTop.add(new JLabel("Orden:"));
        panelTop.add(comboOrdenes);
        panelTop.add(btnCargar);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // 🔹 TABLA
        // =========================
        modelo = new DefaultTableModel(
                new Object[]{"Producto", "Pedido", "Recibido"}, 0
        );

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // 🔹 PANEL INFERIOR
        // =========================
        JPanel panelBottom = new JPanel(new GridLayout(3, 2, 10, 10));

        txtNumeroFactura = new JTextField();
        txtFecha = new JTextField();

        btnRegistrar = new JButton("Registrar Factura");
        btnRegistrar.setBackground(new Color(0, 153, 0));
        btnRegistrar.setForeground(Color.WHITE);

        panelBottom.add(new JLabel("Número Factura:"));
        panelBottom.add(txtNumeroFactura);

        panelBottom.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelBottom.add(txtFecha);

        panelBottom.add(new JLabel());
        panelBottom.add(btnRegistrar);

        add(panelBottom, BorderLayout.SOUTH);

        // =========================
        // 🔥 EVENTOS
        // =========================
        btnCargar.addActionListener(e -> cargarDetalle());
        btnRegistrar.addActionListener(e -> registrarFactura());
    }

    // =========================
    // 🔹 CARGAR DETALLE ORDEN
    // =========================
    private void cargarDetalle() {

        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return;
        }

        modelo.setRowCount(0);

        List<Object[]> datos = controller.obtenerResumenOrden(orden.getIdOrden());

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }

    // =========================
    // 🔹 REGISTRAR FACTURA
    // =========================
    private void registrarFactura() {

        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return;
        }

        String numero = txtNumeroFactura.getText().trim();
        String fecha = txtFecha.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese número de factura.");
            return;
        }

        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la fecha.");
            return;
        }

        String resultado = controller.registrarFactura(
                orden.getIdOrden(),
                numero,
                fecha
        );

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Factura registrada correctamente.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // 🔹 LIMPIAR
    // =========================
    private void limpiar() {
        txtNumeroFactura.setText("");
        txtFecha.setText("");
        modelo.setRowCount(0);
    }

    // =========================
    // 🔹 CARGA INICIAL
    // =========================
    public void cargarOrdenes() {

        if (controller == null) return;

        comboOrdenes.removeAllItems();

        // 🔥 CAMBIO CLAVE AQUÍ
        List<OrdenCompra> lista = controller.obtenerOrdenesParaFacturaCompra();

        for (OrdenCompra o : lista) {
            comboOrdenes.addItem(o);
        }
    }
}