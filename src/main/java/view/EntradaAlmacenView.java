package view;

import controller.EntradaAlmacenController;
import model.DetalleOrdenCompra;
import model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Entrada de Almacén
 *
 * 🔥 ERP PRO:
 * - Recepción parcial o total
 * - Trazabilidad logística
 * - Control de remisiones
 * - Validación contra factura proveedor
 *
 * ⚠ IMPORTANTE:
 * Entrada Almacén = logística.
 * Factura Compra = contabilidad.
 */
public class EntradaAlmacenView extends JFrame {

    // =========================
    // COMPONENTES
    // =========================

    private JComboBox<OrdenCompra> comboOrdenes;

    private JTable tabla;

    private DefaultTableModel modelo;

    private JTextField txtCantidadRecibida;

    private JTextField txtNumeroFactura;

    private JTextField txtNumeroRemision;

    private JButton btnCargarOrden;

    private JButton btnRegistrarEntrada;

    private EntradaAlmacenController controller;

    // =========================
    // SET CONTROLLER
    // =========================
    public void setController(
            EntradaAlmacenController controller
    ) {

        this.controller = controller;
    }

    // =========================
    // CONSTRUCTOR
    // =========================
    public EntradaAlmacenView() {

        setTitle(
                "F&B Soluciones Integrales - Entrada de Almacén"
        );

        setSize(950, 600);

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // COMPONENTES UI
    // =========================
    private void inicializarComponentes() {

        // =========================
        // PANEL SUPERIOR
        // =========================

        JPanel panelTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        comboOrdenes =
                new JComboBox<>();

        btnCargarOrden =
                new JButton(
                        "Cargar Orden"
                );

        panelTop.add(
                new JLabel(
                        "Orden de Compra:"
                )
        );

        panelTop.add(comboOrdenes);

        panelTop.add(btnCargarOrden);

        add(
                panelTop,
                BorderLayout.NORTH
        );

        // =========================
        // TABLA
        // =========================

        modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID ITEM",
                                "Producto",
                                "Cantidad Pedida",
                                "Cantidad Recibida",
                                "Pendiente"
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

        // 🔥 OCULTAR COLUMNA ID ITEM
        tabla.getColumnModel()
                .getColumn(0)
                .setMinWidth(0);

        tabla.getColumnModel()
                .getColumn(0)
                .setMaxWidth(0);

        tabla.getColumnModel()
                .getColumn(0)
                .setWidth(0);

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        // =========================
        // PANEL INFERIOR
        // =========================

        JPanel panelBottom =
                new JPanel(
                        new FlowLayout()
                );

        txtCantidadRecibida =
                new JTextField(8);

        txtNumeroFactura =
                new JTextField(12);

        txtNumeroRemision =
                new JTextField(12);

        btnRegistrarEntrada =
                new JButton(
                        "Registrar Entrada"
                );

        // =========================
        // ESTILO BOTÓN
        // =========================

        btnRegistrarEntrada.setBackground(
                new Color(0, 102, 204)
        );

        btnRegistrarEntrada.setForeground(
                Color.WHITE
        );

        btnRegistrarEntrada.setFocusPainted(false);

        btnRegistrarEntrada.setContentAreaFilled(true);

        btnRegistrarEntrada.setOpaque(true);

        btnRegistrarEntrada.setBorderPainted(false);

        // =========================
        // COMPONENTES PANEL
        // =========================

        panelBottom.add(
                new JLabel("Cantidad:")
        );

        panelBottom.add(
                txtCantidadRecibida
        );

        panelBottom.add(
                new JLabel("Factura:")
        );

        panelBottom.add(
                txtNumeroFactura
        );

        panelBottom.add(
                new JLabel("Remisión:")
        );

        panelBottom.add(
                txtNumeroRemision
        );

        panelBottom.add(
                btnRegistrarEntrada
        );

        add(
                panelBottom,
                BorderLayout.SOUTH
        );

        // =========================
        // EVENTOS
        // =========================

        btnCargarOrden.addActionListener(
                e -> cargarDetalleOrden()
        );

        btnRegistrarEntrada.addActionListener(
                e -> registrarEntrada()
        );
    }

    // =========================
    // CARGAR ÓRDENES
    // =========================
    public void cargarOrdenes() {

        comboOrdenes.removeAllItems();

        List<OrdenCompra> lista =
                controller.obtenerOrdenesPendientes();

        for (OrdenCompra o : lista) {

            comboOrdenes.addItem(o);
        }
    }

    // =========================
    // PRESELECCIONAR ORDEN
    // =========================
    public void seleccionarOrden(
            OrdenCompra orden
    ) {

        comboOrdenes.setSelectedItem(orden);

        cargarDetalleOrden();
    }

    // =========================
    // CARGAR DETALLE
    // =========================
    private void cargarDetalleOrden() {

        OrdenCompra orden =
                (OrdenCompra)
                        comboOrdenes.getSelectedItem();

        if (orden == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una orden."
            );

            return;
        }

        modelo.setRowCount(0);

        List<DetalleOrdenCompra> detalles =
                controller.obtenerDetalleOrden(
                        orden.getId()
                );

        for (DetalleOrdenCompra d : detalles) {

            int recibido =
                    controller.obtenerCantidadRecibida(
                            d.getIdItem(),
                            orden.getId()
                    );

            int pendiente =
                    d.getCantidadPedida()
                            - recibido;

            modelo.addRow(
                    new Object[]{

                            // 🔥 ID OCULTO
                            d.getIdItem(),

                            // 🔥 NOMBRE PRODUCTO
                            d.getNombreItem(),

                            d.getCantidadPedida(),

                            recibido,

                            pendiente
                    }
            );
        }
    }

    // =========================
    // REGISTRAR ENTRADA
    // =========================
    private void registrarEntrada() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );

            return;
        }

        OrdenCompra orden =
                (OrdenCompra)
                        comboOrdenes.getSelectedItem();

        if (orden == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una orden."
            );

            return;
        }

        // 🔥 EL ID SIGUE EXISTIENDO
        // SOLO ESTÁ OCULTO
        int idItem =
                (int) modelo.getValueAt(
                        fila,
                        0
                );

        int pendiente =
                (int) modelo.getValueAt(
                        fila,
                        4
                );

        try {

            int cantidad =
                    Integer.parseInt(
                            txtCantidadRecibida
                                    .getText()
                                    .trim()
                    );

            String numeroFactura =
                    txtNumeroFactura
                            .getText()
                            .trim();

            String numeroRemision =
                    txtNumeroRemision
                            .getText()
                            .trim();

            // =========================
            // VALIDACIONES
            // =========================

            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cantidad inválida."
                );

                return;
            }

            if (cantidad > pendiente) {

                JOptionPane.showMessageDialog(
                        this,
                        "No puede exceder lo pendiente."
                );

                return;
            }

            if (
                    numeroFactura.isEmpty()
                            && numeroRemision.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar factura, remisión o ambas."
                );

                return;
            }

            // =========================
            // REGISTRAR
            // =========================

            String resultado =
                    controller.registrarEntrada(
                            orden.getId(),
                            idItem,
                            cantidad,
                            numeroFactura,
                            numeroRemision
                    );

            if ("OK".equals(resultado)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrada registrada correctamente."
                );

                limpiarCampos();

                cargarDetalleOrden();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        resultado,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cantidad inválida."
            );
        }
    }

    // =========================
    // LIMPIAR
    // =========================
    private void limpiarCampos() {

        txtCantidadRecibida.setText("");

        txtNumeroFactura.setText("");

        txtNumeroRemision.setText("");
    }
}