package view;

import controller.InventarioController;
import model.Categoria;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

/**
 * Vista de Inventario (Catálogo de Productos)
 *
 * 🔥 RESPONSABILIDADES:
 * - Mostrar productos
 * - Crear productos
 * - Editar productos
 * - Inactivar productos
 * - Reactivar productos
 * - Mostrar alertas de stock bajo
 */
public class InventarioView extends JFrame {

    // =========================
    // COMPONENTES UI
    // =========================

    private JTable tablaProductos;

    private DefaultTableModel modeloTabla;

    private JTextField txtNombre;

    private JTextField txtStock;

    private JTextField txtStockMinimo;

    private JComboBox<Categoria> comboCategorias;

    // 🔥 NUEVO
    private JComboBox<String> comboFiltroEstado;

    private JButton btnGuardar;

    private JButton btnRefrescar;

    private JButton btnEliminar;

    // 🔥 NUEVO
    private JButton btnReactivar;

    private JButton btnLimpiar;

    private int idSeleccionado = -1;

    private InventarioController controller;

    // =========================
    // 🔹 SET CONTROLLER
    // =========================
    public void setController(
            InventarioController controller
    ) {

        this.controller = controller;
    }

    // =========================
    // 🔹 INICIALIZAR DATOS
    // =========================
    public void inicializarDatos() {

        cargarCategorias();

        refrescarTabla();
    }

    // =========================
    // 🔹 CONSTRUCTOR
    // =========================
    public InventarioView() {

        setTitle(
                "F&B Soluciones Integrales - Catálogo de Referencias"
        );

        setSize(1050, 650);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        // =========================
        // FORMULARIO
        // =========================

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(3, 4, 15, 15)
                );

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Gestión de Referencias"
                )
        );

        txtNombre =
                new JTextField();

        txtStock =
                new JTextField();

        txtStockMinimo =
                new JTextField();

        comboCategorias =
                new JComboBox<>();

        // 🔥 NUEVO FILTRO
        comboFiltroEstado =
                new JComboBox<>();

        comboFiltroEstado.addItem("ACTIVOS");
        comboFiltroEstado.addItem("INACTIVOS");

        panelFormulario.add(
                new JLabel("Nombre del Producto:")
        );

        panelFormulario.add(txtNombre);

        panelFormulario.add(
                new JLabel("Categoría:")
        );

        panelFormulario.add(comboCategorias);

        panelFormulario.add(
                new JLabel("Stock Inicial:")
        );

        panelFormulario.add(txtStock);

        panelFormulario.add(
                new JLabel("Stock Mínimo:")
        );

        panelFormulario.add(txtStockMinimo);

        // 🔥 FILTRO
        panelFormulario.add(
                new JLabel("Mostrar:")
        );

        panelFormulario.add(comboFiltroEstado);

        panelFormulario.add(new JLabel(""));
        panelFormulario.add(new JLabel(""));

        // =========================
        // TABLA
        // =========================

        String[] columnas = {
                "ID",
                "Código SKU",
                "Nombre",
                "Stock",
                "Stock Mínimo",
                "Estado"
        };

        modeloTabla =
                new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tablaProductos =
                new JTable(modeloTabla);

        tablaProductos
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(120);

        tablaProductos.setDefaultRenderer(
                Object.class,
                new StockBajoRenderer()
        );

        JScrollPane scrollTabla =
                new JScrollPane(tablaProductos);

        // =========================
        // BOTONES
        // =========================

        JPanel panelAcciones =
                new JPanel();

        btnGuardar =
                new JButton(
                        "Registrar en Catálogo"
                );

        btnRefrescar =
                new JButton(
                        "Actualizar Vista"
                );

        btnEliminar =
                new JButton(
                        "Inactivar Producto"
                );

        btnReactivar =
                new JButton(
                        "Reactivar Producto"
                );

        btnLimpiar =
                new JButton(
                        "Limpiar Formulario"
                );

        // =========================
        // ESTILO BOTÓN GUARDAR
        // =========================

        btnGuardar.setBackground(
                new Color(0, 102, 204)
        );

        btnGuardar.setForeground(
                Color.WHITE
        );

        btnGuardar.setFocusPainted(false);

        btnGuardar.setOpaque(true);

        btnGuardar.setBorderPainted(false);

        // =========================
        // ESTILO BOTÓN INACTIVAR
        // =========================

        btnEliminar.setBackground(
                new Color(204, 0, 0)
        );

        btnEliminar.setForeground(
                Color.WHITE
        );

        btnEliminar.setFocusPainted(false);

        btnEliminar.setOpaque(true);

        btnEliminar.setBorderPainted(false);

        // =========================
        // ESTILO BOTÓN REACTIVAR
        // =========================

        btnReactivar.setBackground(
                new Color(0, 153, 76)
        );

        btnReactivar.setForeground(
                Color.WHITE
        );

        btnReactivar.setFocusPainted(false);

        btnReactivar.setOpaque(true);

        btnReactivar.setBorderPainted(false);

        panelAcciones.add(btnGuardar);

        panelAcciones.add(btnRefrescar);

        panelAcciones.add(btnEliminar);

        panelAcciones.add(btnReactivar);

        panelAcciones.add(btnLimpiar);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        add(
                scrollTabla,
                BorderLayout.CENTER
        );

        add(
                panelAcciones,
                BorderLayout.SOUTH
        );

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(
                e -> ejecutarGuardado()
        );

        btnRefrescar.addActionListener(
                e -> refrescarTabla()
        );

        btnEliminar.addActionListener(
                e -> ejecutarEliminacion()
        );

        btnReactivar.addActionListener(
                e -> ejecutarReactivacion()
        );

        btnLimpiar.addActionListener(
                e -> limpiarCampos()
        );

        // 🔥 CAMBIO DE FILTRO
        comboFiltroEstado.addActionListener(
                e -> refrescarTabla()
        );

        tablaProductos
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tablaProductos.getSelectedRow();

                        if (fila != -1) {

                            idSeleccionado =
                                    (int) modeloTabla.getValueAt(
                                            fila,
                                            0
                                    );

                            txtNombre.setText(
                                    (String) modeloTabla.getValueAt(
                                            fila,
                                            2
                                    )
                            );

                            txtStock.setText(
                                    String.valueOf(
                                            modeloTabla.getValueAt(
                                                    fila,
                                                    3
                                            )
                                    )
                            );

                            txtStockMinimo.setText(
                                    String.valueOf(
                                            modeloTabla.getValueAt(
                                                    fila,
                                                    4
                                            )
                                    )
                            );

                            // =========================
// 🔥 SELECCIONAR CATEGORÍA
// =========================
                            Producto producto =
                                    controller.buscarProductoPorId(
                                            idSeleccionado
                                    );

                            if (producto != null) {

                                for (
                                        int i = 0;
                                        i < comboCategorias.getItemCount();
                                        i++
                                ) {

                                    Categoria categoria =
                                            comboCategorias.getItemAt(i);

                                    if (
                                            categoria.getId()
                                                    == producto.getIdCategoria()
                                    ) {

                                        comboCategorias.setSelectedIndex(i);

                                        break;
                                    }
                                }
                            }



                            btnGuardar.setText(
                                    "Actualizar Cambios"
                            );

                            txtStock.setEnabled(false);
                        }
                    }
                });
    }

    // =========================
    // 🔹 CARGAR CATEGORÍAS
    // =========================
    private void cargarCategorias() {

        if (controller == null) {

            return;
        }

        comboCategorias.removeAllItems();

        List<Categoria> lista =
                controller.obtenerCategorias();

        for (Categoria cat : lista) {

            comboCategorias.addItem(cat);
        }
    }

    // =========================
    // 🔹 GUARDAR / EDITAR
    // =========================
    private void ejecutarGuardado() {

        Categoria catSeleccionada =
                (Categoria) comboCategorias.getSelectedItem();

        if (catSeleccionada == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría."
            );

            return;
        }

        String resultado;

        if (idSeleccionado == -1) {

            resultado =
                    controller.agregarNuevoProducto(
                            txtNombre.getText(),
                            txtStock.getText(),
                            String.valueOf(
                                    catSeleccionada.getId()
                            ),
                            txtStockMinimo.getText()
                    );

        } else {

            resultado =
                    controller.editarProducto(
                            idSeleccionado,
                            txtNombre.getText(),
                            String.valueOf(
                                    catSeleccionada.getId()
                            ),
                            txtStockMinimo.getText()
                    );
        }

        if (resultado.equals("OK")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Operación exitosa."
            );

            limpiarCampos();

            refrescarTabla();

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
    // 🔹 INACTIVAR
    // =========================
    private void ejecutarEliminacion() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );

            return;
        }

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea inactivar esta referencia?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion == JOptionPane.YES_OPTION) {

            boolean eliminado =
                    controller.eliminarProducto(
                            idSeleccionado
                    );

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Producto inactivado."
                );

                limpiarCampos();

                refrescarTabla();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo inactivar."
                );
            }
        }
    }

    // =========================
    // 🔹 REACTIVAR
    // =========================
    private void ejecutarReactivacion() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );

            return;
        }

        boolean reactivado =
                controller.reactivarProducto(
                        idSeleccionado
                );

        if (reactivado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Producto reactivado."
            );

            limpiarCampos();

            refrescarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo reactivar."
            );
        }
    }

    // =========================
    // 🔹 REFRESCAR TABLA
    // =========================
    private void refrescarTabla() {

        if (controller == null) {

            return;
        }

        modeloTabla.setRowCount(0);

        String filtro =
                comboFiltroEstado.getSelectedItem().toString();

        List<Producto> lista;

        if (filtro.equals("INACTIVOS")) {

            lista =
                    controller.obtenerProductosInactivos();

        } else {

            lista =
                    controller.obtenerInventario();
        }

        boolean hayAlertas = false;

        for (Producto p : lista) {

            modeloTabla.addRow(
                    new Object[]{
                            p.getId(),
                            p.getCodigoReferencia(),
                            p.getNombre(),
                            p.getStockActual(),
                            p.getStockMinimo(),
                            p.getEstado()
                    }
            );

            if (
                    p.getStockActual()
                            < p.getStockMinimo()
            ) {

                hayAlertas = true;
            }
        }

        if (
                hayAlertas
                        && modeloTabla.getRowCount() > 0
                        && filtro.equals("ACTIVOS")
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "⚠ Hay productos con stock bajo.",
                    "Alerta Inventario",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================
    // 🔹 LIMPIAR FORMULARIO
    // =========================
    private void limpiarCampos() {

        idSeleccionado = -1;

        txtNombre.setText("");

        txtStock.setText("");

        txtStockMinimo.setText("");

        txtStock.setEnabled(true);

        btnGuardar.setText(
                "Registrar en Catálogo"
        );

        if (comboCategorias.getItemCount() > 0) {

            comboCategorias.setSelectedIndex(0);
        }

        tablaProductos.clearSelection();

        txtNombre.requestFocus();
    }

    // =========================
    // 🔹 RENDERER STOCK BAJO
    // =========================
    class StockBajoRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            Component c =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            int stock =
                    (int) table.getValueAt(
                            row,
                            3
                    );

            int minimo =
                    (int) table.getValueAt(
                            row,
                            4
                    );

            if (stock < minimo) {

                c.setBackground(
                        new Color(255, 199, 206)
                );

                c.setForeground(Color.BLACK);

            } else {

                c.setBackground(
                        isSelected
                                ? table.getSelectionBackground()
                                : Color.WHITE
                );

                c.setForeground(
                        isSelected
                                ? table.getSelectionForeground()
                                : Color.BLACK
                );
            }

            return c;
        }
    }
}