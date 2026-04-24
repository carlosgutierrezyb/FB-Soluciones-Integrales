package view;

import controller.FacturaCompraController;
import model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * View de Factura de Compra
 *
 * 🔥 ERP PRO REAL:
 * - factura contra entradas recibidas
 * - múltiples facturas por OC
 * - validación real contra remisiones
 */
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
        setSize(850, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();

        // fecha automática de hoy
        txtFecha.setText(LocalDate.now().toString());
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        // =========================
        // 🔹 PANEL SUPERIOR
        // =========================
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        comboOrdenes = new JComboBox<>();
        comboOrdenes.setPreferredSize(new Dimension(250, 30));

        btnCargar = new JButton("Cargar Orden");

        panelTop.add(new JLabel("Orden de Compra:"));
        panelTop.add(comboOrdenes);
        panelTop.add(btnCargar);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // 🔹 TABLA CENTRAL
        // =========================
        modelo = new DefaultTableModel(
                new Object[]{
                        "Producto",
                        "Cantidad Pedida",
                        "Cantidad Recibida"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo lectura
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // 🔹 PANEL INFERIOR
        // =========================
        JPanel panelBottom = new JPanel(new GridLayout(3, 2, 10, 10));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNumeroFactura = new JTextField();
        txtFecha = new JTextField();

        btnRegistrar = new JButton("Registrar Factura");
        btnRegistrar.setBackground(new Color(0, 153, 0));
        btnRegistrar.setForeground(Color.WHITE);

        panelBottom.add(new JLabel("Número de Factura:"));
        panelBottom.add(txtNumeroFactura);

        panelBottom.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panelBottom.add(txtFecha);

        panelBottom.add(new JLabel(""));
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

        OrdenCompra orden =
                (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una orden de compra."
            );
            return;
        }

        modelo.setRowCount(0);

        List<Object[]> datos =
                controller.obtenerResumenOrden(
                        orden.getIdOrden()
                );

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La orden no tiene detalle para mostrar."
            );
            return;
        }

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }

    // =========================
    // 🔹 REGISTRAR FACTURA
    // =========================
    private void registrarFactura() {

        OrdenCompra orden =
                (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una orden."
            );
            return;
        }

        String numero =
                txtNumeroFactura.getText().trim();

        String fecha =
                txtFecha.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el número de factura."
            );
            return;
        }

        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la fecha."
            );
            return;
        }

        /*
         * idProveedor viene desde la OC
         */
        int idProveedor = orden.getIdProveedor();

        String resultado =
                controller.registrarFactura(
                        idProveedor,
                        orden.getIdOrden(),
                        numero,
                        fecha
                );

        if ("OK".equals(resultado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "✅ Factura registrada correctamente."
            );

            limpiar();
            cargarOrdenes();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    resultado,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // 🔹 LIMPIAR
    // =========================
    private void limpiar() {

        txtNumeroFactura.setText("");
        txtFecha.setText(LocalDate.now().toString());
        modelo.setRowCount(0);
    }

    // =========================
    // 🔹 CARGA INICIAL
    // =========================
    public void cargarOrdenes() {

        if (controller == null) {
            return;
        }

        comboOrdenes.removeAllItems();

        List<OrdenCompra> lista =
                controller.obtenerOrdenesParaFacturaCompra();

        for (OrdenCompra orden : lista) {
            comboOrdenes.addItem(orden);
        }
    }
}