package view;

import controller.EntradaAlmacenController;
import model.DetalleOrdenCompra;
import model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interfaz para la recepción y control de mercancías contra órdenes de compra.
 */
public class EntradaAlmacenView extends JFrame {

    private JComboBox<OrdenCompra> comboOrdenes;
    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtCantidadRecibida;
    private JTextField txtNumeroFactura;
    private JTextField txtNumeroRemision;

    private JButton btnCargarOrden;
    private JButton btnRegistrarEntrada;

    private EntradaAlmacenController controller;

    public void setController(EntradaAlmacenController controller) {
        this.controller = controller;
        // Al asignar el controlador, forzamos la carga fresca de órdenes pendientes
        cargarOrdenes();
    }

    public EntradaAlmacenView() {
        setTitle("F&B Soluciones Integrales - Entrada de Almacén");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel de selección superior
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboOrdenes = new JComboBox<>();
        comboOrdenes.setPreferredSize(new Dimension(300, 26));

        btnCargarOrden = new JButton("Cargar Orden");

        panelTop.add(new JLabel("Orden de Compra:"));
        panelTop.add(comboOrdenes);
        panelTop.add(btnCargarOrden);
        add(panelTop, BorderLayout.NORTH);

        // Configuración de la tabla de items
        modelo = new DefaultTableModel(
                new Object[]{"ID ITEM", "Producto", "Cantidad Pedida", "Cantidad Recibida", "Pendiente"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        // Ocultar columna ID del item para mantener limpia la UI
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel de operaciones inferior
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        txtCantidadRecibida = new JTextField(8);
        txtNumeroFactura = new JTextField(12);
        txtNumeroRemision = new JTextField(12);
        btnRegistrarEntrada = new JButton("Registrar Entrada");

        // Configuración del botón azul de guardado logístico
        btnRegistrarEntrada.setBackground(new Color(0, 102, 204));
        btnRegistrarEntrada.setForeground(Color.WHITE);
        btnRegistrarEntrada.setContentAreaFilled(true);
        btnRegistrarEntrada.setOpaque(true);
        btnRegistrarEntrada.setBorderPainted(false);
        btnRegistrarEntrada.setFocusPainted(false);
        btnRegistrarEntrada.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBottom.add(new JLabel("Cantidad:"));
        panelBottom.add(txtCantidadRecibida);
        panelBottom.add(new JLabel("Factura:"));
        panelBottom.add(txtNumeroFactura);
        panelBottom.add(new JLabel("Remisión:"));
        panelBottom.add(txtNumeroRemision);
        panelBottom.add(btnRegistrarEntrada);
        add(panelBottom, BorderLayout.SOUTH);

        // Enlace de acciones
        btnCargarOrden.addActionListener(e -> cargarDetalleOrden());
        btnRegistrarEntrada.addActionListener(e -> registrarEntrada());
    }

    /**
     * Consulta las órdenes activas en la base de datos y actualiza el combobox.
     */
    public void cargarOrdenes() {
        if (controller == null) return;

        comboOrdenes.removeAllItems();
        List<OrdenCompra> lista = controller.obtenerOrdenesPendientes();
        for (OrdenCompra o : lista) {
            comboOrdenes.addItem(o);
        }
    }

    /**
     * Permite enfocar de manera externa una orden específica y cargar sus renglones automáticamente.
     */
    public void seleccionarOrden(OrdenCompra orden) {
        comboOrdenes.setSelectedItem(orden);
        cargarDetalleOrden();
    }

    /**
     * Mapea los productos de la orden seleccionada calculando las cantidades pendientes de ingreso.
     */
    private void cargarDetalleOrden() {
        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();
        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.setRowCount(0);
        List<DetalleOrdenCompra> detalles = controller.obtenerDetalleOrden(orden.getId());

        for (DetalleOrdenCompra d : detalles) {
            int recibido = controller.obtenerCantidadRecibida(d.getIdItem(), orden.getId());
            int pendiente = d.getCantidadPedida() - recibido;

            modelo.addRow(new Object[]{
                    d.getIdItem(),
                    d.getNombreItem(),
                    d.getCantidadPedida(),
                    recibido,
                    pendiente
            });
        }
    }

    /**
     * Valida el remanente físico del item seleccionado y registra la novedad en el almacén.
     */
    private void registrarEntrada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();
        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden válida.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idItem = (int) modelo.getValueAt(fila, 0);
        int pendiente = (int) modelo.getValueAt(fila, 4);

        try {
            int cantidad = Integer.parseInt(txtCantidadRecibida.getText().trim());
            String numeroFactura = txtNumeroFactura.getText().trim();
            String numeroRemision = txtNumeroRemision.getText().trim();

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cantidad > pendiente) {
                JOptionPane.showMessageDialog(this, "La cantidad ingresada supera las unidades pendientes.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (numeroFactura.isEmpty() && numeroRemision.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe registrar al menos un documento de soporte (Factura o Remisión).", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String resultado = controller.registrarEntrada(orden.getId(), idItem, cantidad, numeroFactura, numeroRemision);

            if ("OK".equals(resultado)) {
                JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.");
                limpiarCampos();
                cargarDetalleOrden();
                cargarOrdenes(); // Recarga la lista por si la orden completó todos sus items y ya no está pendiente
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entero válido en el campo de cantidad.", "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCantidadRecibida.setText("");
        txtNumeroFactura.setText("");
        txtNumeroRemision.setText("");
    }
}