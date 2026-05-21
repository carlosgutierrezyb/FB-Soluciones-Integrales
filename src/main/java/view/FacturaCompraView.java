package view;

import controller.FacturaCompraController;
import model.OrdenCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.time.LocalDate;
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
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();

        txtFecha.setText(LocalDate.now().toString());
    }

    private void inicializarComponentes() {
        // =========================
        // PANEL SUPERIOR
        // =========================
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        comboOrdenes = new JComboBox<>();
        comboOrdenes.setPreferredSize(new Dimension(250, 30));

        btnCargar = new JButton("Cargar Orden");

        panelTop.add(new JLabel("Orden de Compra:"));
        panelTop.add(comboOrdenes);

        panelTop.add(btnCargar);

        panelTop.add(new JLabel("Número Factura:"));

        txtNumeroFactura = new JTextField();
        txtNumeroFactura.setPreferredSize(
                new Dimension(180, 30)
        );

        panelTop.add(txtNumeroFactura);



        add(panelTop, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================
        modelo = new DefaultTableModel(
                new Object[]{
                        "ID Item",
                        "Producto",
                        "Pedida",
                        "Recibida",
                        "Facturada",
                        "Pendiente",
                        "Cant. Factura",
                        "Valor Unitario"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0, 2, 3, 4, 5, 6 -> Integer.class;
                    case 7 -> Double.class;
                    default -> String.class;
                };
            }
        };

        tabla = new JTable(modelo);
        tabla.putClientProperty("terminateEditOnFocusLost", true);
        tabla.setRowHeight(25);

        // 🔥 ASIGNACIÓN DE EDITORES NUMÉRICOS CON ACTIVACIÓN AL PRIMER CLIC (VERSIÓN PRO DEFINITIVA)
        TableCellEditor editorNumerico = new CeldaNumericaEditor();
        tabla.getColumnModel().getColumn(6).setCellEditor(editorNumerico);
        tabla.getColumnModel().getColumn(7).setCellEditor(editorNumerico);

        // =========================
        // 🔥 UX: RENDERIZADOR RESPETANDO TUS REGLAS DE COLOR
        // =========================
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                boolean esEditable = (column == 6 || column == 7);

                Component c = super.getTableCellRendererComponent(
                        table, value,
                        esEditable ? false : isSelected,
                        esEditable ? false : hasFocus,
                        row, column
                );

                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(true);
                }

                // ============================================================
                // 🎨 ASIGNACIÓN DE COLORES
                // ============================================================
                if (esEditable) {
                    // Cambia a blanco si tiene el foco o si es la celda seleccionada actual
                    if (hasFocus || (table.getSelectedRow() == row && table.getSelectedColumn() == column)) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(255, 255, 180)); // Tu amarillo de reposo
                    }
                    c.setForeground(Color.BLACK);
                } else if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(new Color(240, 240, 240));
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        tabla.setDefaultRenderer(Integer.class, tabla.getDefaultRenderer(Object.class));
        tabla.setDefaultRenderer(Double.class, tabla.getDefaultRenderer(Object.class));

        tabla.setSelectionBackground(new Color(0, 120, 215));
        tabla.setSelectionForeground(Color.WHITE);

        // ========================================================
// 🔥 TAB PERSONALIZADO ENTRE CELDAS EDITABLES
// ========================================================
        tabla.setSurrendersFocusOnKeystroke(true);

        InputMap im = tabla.getInputMap(
                JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        ActionMap am = tabla.getActionMap();

// ========================================================
// TAB
// ========================================================
        im.put(KeyStroke.getKeyStroke("TAB"), "moverTab");

        am.put("moverTab", new AbstractAction() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                int fila = tabla.getSelectedRow();
                int columna = tabla.getSelectedColumn();

                // ====================================================
                // SI ESTÁ EN CANT FACTURA
                // → PASA A VALOR UNITARIO
                // ====================================================
                if (columna == 6) {

                    tabla.changeSelection(
                            fila,
                            7,
                            false,
                            false
                    );

                    tabla.editCellAt(fila, 7);

                    Component editor =
                            tabla.getEditorComponent();

                    if (editor != null) {
                        editor.requestFocus();
                    }

                    return;
                }

                // ====================================================
                // SI ESTÁ EN VALOR UNITARIO
                // ====================================================
                if (columna == 7) {

                    // ================================================
                    // SI NO ES LA ÚLTIMA FILA
                    // → BAJA A CANT FACTURA
                    // ================================================
                    if (fila < tabla.getRowCount() - 1) {

                        tabla.changeSelection(
                                fila + 1,
                                6,
                                false,
                                false
                        );

                        tabla.editCellAt(fila + 1, 6);

                        Component editor =
                                tabla.getEditorComponent();

                        if (editor != null) {
                            editor.requestFocus();
                        }

                    } else {

                        // ============================================
                        // ÚLTIMA FILA
                        // → IR A NÚMERO FACTURA O BOTÓN
                        // ============================================

                        // ============================================
// CONFIRMAR EDICIÓN ACTUAL
// ============================================
                        if (tabla.isEditing()) {
                            tabla.getCellEditor().stopCellEditing();
                        }

// ============================================
// QUITAR SELECCIÓN DE LA TABLA
// ============================================
                        tabla.clearSelection();

// ============================================
// MANDAR FOCO AL BOTÓN
// ============================================
                        SwingUtilities.invokeLater(() -> {
                            btnRegistrar.requestFocusInWindow();
                        });
                    }
                }
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // PANEL INFERIOR
        // =========================
        JPanel panelBottom = new JPanel(new GridLayout(3, 2, 10, 10));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setBackground(Color.LIGHT_GRAY);

        btnRegistrar = new JButton("Registrar Factura");
        btnRegistrar.setBackground(new Color(0, 153, 0));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorderPainted(false);


        panelBottom.add(new JLabel("Fecha Registro:"));
        panelBottom.add(txtFecha);
        panelBottom.add(new JLabel(""));
        panelBottom.add(btnRegistrar);

        add(panelBottom, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================
        btnCargar.addActionListener(e -> cargarDetalle());
        btnRegistrar.addActionListener(e -> registrarFactura());
    }

    private void cargarDetalle() {
        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return;
        }

        modelo.setRowCount(0);

        List<Object[]> datos = controller.obtenerResumenOrden(orden.getIdOrden());

        if (datos == null || datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La orden no tiene detalle.");
            return;
        }

        for (Object[] fila : datos) {
            modelo.addRow(new Object[]{
                    fila[0],
                    fila[1],
                    fila[2],
                    fila[3],
                    fila[4],
                    fila[5],
                    0,
                    0.0
            });
        }
    }

    private void registrarFactura() {
        OrdenCompra orden = (OrdenCompra) comboOrdenes.getSelectedItem();

        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return;
        }

        String numeroFactura = txtNumeroFactura.getText().trim();

        if (numeroFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese número de factura.");
            return;
        }

        boolean hayItems = false;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            int cantidad = parseIntSafe(modelo.getValueAt(i, 6));
            double valor = parseDoubleFlexible(modelo.getValueAt(i, 7));
            int pendiente = parseIntSafe(modelo.getValueAt(i, 5));

            if (cantidad < 0) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                return;
            }

            if (cantidad > pendiente) {
                JOptionPane.showMessageDialog(this, "No puede facturar más de lo pendiente.");
                return;
            }

            if (cantidad > 0) {
                if (valor <= 0) {
                    JOptionPane.showMessageDialog(this, "Valor unitario inválido.");
                    return;
                }
                hayItems = true;
            }
        }

        if (!hayItems) {
            JOptionPane.showMessageDialog(this, "Debe facturar al menos un ítem.");
            return;
        }

        String resultado = controller.registrarFactura(orden, numeroFactura, modelo);

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Factura registrada correctamente.");
            limpiar();
            cargarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseIntSafe(Object obj) {
        try {
            return Integer.parseInt(obj.toString().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleFlexible(Object obj) {
        try {
            String valor = obj.toString().trim();

            if (valor.isEmpty()) return 0.0;

            valor = valor.replace(",", ".");

            return Double.parseDouble(valor);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void limpiar() {
        txtNumeroFactura.setText("");
        txtFecha.setText(LocalDate.now().toString());
        modelo.setRowCount(0);
    }

    public void cargarOrdenes() {
        if (controller == null) return;

        comboOrdenes.removeAllItems();

        List<OrdenCompra> lista = controller.obtenerOrdenesParaFacturaCompra();

        for (OrdenCompra orden : lista) {
            comboOrdenes.addItem(orden);
        }
    }

    public void seleccionarOrden(OrdenCompra orden) {
        comboOrdenes.setSelectedItem(orden);
        cargarDetalle();
    }

    // ============================================================
    // 🔥 CLASE INTERNA: EDITOR NUMÉRICO PRO DEFINITIVO
    // ============================================================
    private static class CeldaNumericaEditor extends DefaultCellEditor {

        private final JTextField textField;
        private int columnaActual;

        public CeldaNumericaEditor() {
            super(new JTextField());
            this.textField = (JTextField) getComponent();
            this.textField.setHorizontalAlignment(JTextField.RIGHT);

            // 🔥 editar desde el primer clic
            setClickCountToStart(1);
        }

        // ========================================================
        // 🔥 ACTIVAR EDICIÓN LIMPIA
        // ========================================================
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);

            this.columnaActual = column;
            textField.setBackground(Color.WHITE);

            String texto = textField.getText().trim();

            // 🔥 limpiar inmediatamente ANTES de escribir
            if (texto.equals("0")
                    || texto.equals("0.0")
                    || texto.equals("0,0")
                    || texto.equals("0.00")
                    || texto.equals("0,00")) {

                textField.setText("");
            } else {
                textField.selectAll();
            }

            return c;
        }

        // ========================================================
        // 🔥 DEVOLVER VALOR CORRECTO
        // ========================================================
        @Override
        public Object getCellEditorValue() {

            String texto = textField.getText()
                    .trim()
                    .replace(",", ".");

            if (texto.isEmpty()) {

                // columna cantidad
                if (columnaActual == 6) {
                    return 0;
                }

                // columna valor unitario
                return 0.0;
            }

            try {

                // ====================================================
                // CANTIDAD FACTURA → INTEGER
                // ====================================================
                if (columnaActual == 6) {

                    return Integer.parseInt(texto);
                }

                // ====================================================
                // VALOR UNITARIO → DOUBLE
                // ====================================================
                return Double.parseDouble(texto);

            } catch (Exception e) {

                if (columnaActual == 6) {
                    return 0;
                }

                return 0.0;
            }
        }
    }
}