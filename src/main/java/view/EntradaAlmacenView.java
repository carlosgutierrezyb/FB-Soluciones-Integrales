package view;

import controller.EntradaAlmacenController;
import model.OrdenCompra;
import model.DetalleOrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EntradaAlmacenView extends JFrame {

    private JComboBox<OrdenCompra> comboOrdenes;
    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtCantidadRecibida;

    private JButton btnCargarOrden;
    private JButton btnRegistrarEntrada;

    private EntradaAlmacenController controller;

    public void setController(EntradaAlmacenController controller) {
        this.controller = controller;
    }

    public EntradaAlmacenView() {

        setTitle("F&B Soluciones Integrales - Entrada de Almacén");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // =========================
        // PANEL SUPERIOR
        // =========================
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboOrdenes = new JComboBox<>();
        btnCargarOrden = new JButton("Cargar Orden");

        panelTop.add(new JLabel("Orden de Compra:"));
        panelTop.add(comboOrdenes);
        panelTop.add(btnCargarOrden);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================
        modelo = new DefaultTableModel(
                new Object[]{"Producto", "Cantidad Pedida", "Cantidad Recibida", "Pendiente"}, 0
        );

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // PANEL INFERIOR
        // =========================
        JPanel panelBottom = new JPanel();

        txtCantidadRecibida = new JTextField(10);
        btnRegistrarEntrada = new JButton("Registrar Entrada");

        panelBottom.add(new JLabel("Cantidad a ingresar:"));
        panelBottom.add(txtCantidadRecibida);
        panelBottom.add(btnRegistrarEntrada);

        add(panelBottom, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnCargarOrden.addActionListener(e -> cargarDetalleOrden());

        btnRegistrarEntrada.addActionListener(e -> registrarEntrada());
    }

    // =========================
    // 🔹 CARGAR ÓRDENES
    // =========================
    public void cargarOrdenes() {

        comboOrdenes.removeAllItems();

        List<OrdenCompra> lista = controller.obtenerOrdenesPendientes();

        for (OrdenCompra o : lista) {
            comboOrdenes.addItem(o);
        }
    }

    // =========================
    // 🔹 CARGAR DETALLE
    // =========================
    private void cargarDetalleOrden() {

        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return;
        }

        modelo.setRowCount(0);

        List<DetalleOrdenCompra> detalles = controller.obtenerDetalleOrden(orden.getId());

        for (DetalleOrdenCompra d : detalles) {

            int recibido = controller.obtenerCantidadRecibida(d.getIdItem(), orden.getId());
            int pendiente = d.getCantidadPedida() - recibido;

            modelo.addRow(new Object[]{
                    d.getIdItem(),
                    d.getCantidadPedida(),
                    recibido,
                    pendiente
            });
        }
    }

    // =========================
    // 🔹 REGISTRAR ENTRADA
    // =========================
    private void registrarEntrada() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();

        int idItem = (int) modelo.getValueAt(fila, 0);
        int pendiente = (int) modelo.getValueAt(fila, 3);

        try {
            int cantidad = Integer.parseInt(txtCantidadRecibida.getText());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                return;
            }

            if (cantidad > pendiente) {
                JOptionPane.showMessageDialog(this, "No puede ingresar más de lo pendiente.");
                return;
            }

            String resultado = controller.registrarEntrada(
                    orden.getId(),
                    idItem,
                    cantidad
            );

            if ("OK".equals(resultado)) {

                JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.");

                txtCantidadRecibida.setText("");

                cargarDetalleOrden(); // 🔥 refrescar

            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
        }
    }
}