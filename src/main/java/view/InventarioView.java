package view;

import controller.InventarioController;
import model.Categoria;
import model.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Ventana de Catálogo de Inventario.
 * El Código de Referencia se genera automáticamente y se muestra en la tabla.
 */
public class InventarioView extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtStock, txtStockMinimo;
    private JComboBox<Categoria> comboCategorias;
    private JButton btnGuardar, btnRefrescar;
    private JButton btnEliminar, btnLimpiar;
    private int idSeleccionado = -1;

    private InventarioController controller;

    public InventarioView() {
        this.controller = new InventarioController();

        setTitle("F&B Soluciones Integrales - Catálogo de Referencias");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        refrescarTabla();
    }

    private void inicializarComponentes() {

        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 15, 15));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Crear Nueva Referencia"));

        txtNombre = new JTextField();
        txtStock = new JTextField();
        txtStockMinimo = new JTextField();
        comboCategorias = new JComboBox<>();
        cargarCategorias();

        panelFormulario.add(new JLabel("Nombre del Producto:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(comboCategorias);

        panelFormulario.add(new JLabel("Stock Inicial:"));
        panelFormulario.add(txtStock);
        panelFormulario.add(new JLabel("Stock Mínimo (Alerta):"));
        panelFormulario.add(txtStockMinimo);

        String[] columnas = {"ID BD", "Código SKU", "Nombre Referencia", "Stock", "Mínimo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaProductos.setDefaultRenderer(Object.class, new StockBajoRenderer());

        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        JPanel panelAcciones = new JPanel();

        btnGuardar = new JButton("Registrar en Catálogo");
        btnRefrescar = new JButton("Actualizar Vista");
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnLimpiar = new JButton("Limpiar Formulario");

        btnGuardar.setBackground(new Color(0, 102, 204));
        btnGuardar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(204, 0, 0));
        btnEliminar.setForeground(Color.WHITE);

        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnRefrescar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        // EVENTOS
        btnGuardar.addActionListener(e -> ejecutarGuardado());
        btnRefrescar.addActionListener(e -> refrescarTabla());
        btnEliminar.addActionListener(e -> ejecutarEliminacion());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaProductos.getSelectedRow();
                if (fila != -1) {

                    idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                    String nombre = (String) modeloTabla.getValueAt(fila, 2);
                    int stock = (int) modeloTabla.getValueAt(fila, 3);
                    int stockMin = (int) modeloTabla.getValueAt(fila, 4);

                    txtNombre.setText(nombre);
                    txtStock.setText(String.valueOf(stock));
                    txtStockMinimo.setText(String.valueOf(stockMin));

                    btnGuardar.setText("Actualizar Cambios");
                    txtStock.setEnabled(false);
                }
            }
        });
    }

    private void cargarCategorias() {
        comboCategorias.removeAllItems();
        List<Categoria> lista = controller.obtenerCategorias();
        for (Categoria cat : lista) {
            comboCategorias.addItem(cat);
        }
    }

    private void ejecutarGuardado() {

        Categoria catSeleccionada = (Categoria) comboCategorias.getSelectedItem();

        if (catSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría primero.");
            return;
        }

        String resultado;

        if (idSeleccionado == -1) {

            resultado = controller.agregarNuevoProducto(
                    txtNombre.getText(),
                    txtStock.getText(),
                    String.valueOf(catSeleccionada.getIdCat()),
                    txtStockMinimo.getText()
            );

        } else {

            resultado = controller.editarProducto(
                    idSeleccionado,
                    txtNombre.getText(),
                    String.valueOf(catSeleccionada.getIdCat()),
                    txtStockMinimo.getText()
            );
        }

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Operación exitosa.");
            limpiarCampos();
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarEliminacion() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar esta referencia?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {

            if (controller.eliminarProducto(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Referencia eliminada.");
                limpiarCampos();
                refrescarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
            }
        }
    }

    private void refrescarTabla() {

        modeloTabla.setRowCount(0);
        List<Producto> lista = controller.obtenerInventario();

        boolean hayAlertas = false;

        for (Producto p : lista) {

            Object[] fila = {
                    p.getId(),
                    p.getCodigoReferencia(),
                    p.getNombre(),
                    p.getStockActual(),
                    p.getStockMinimo()
            };

            modeloTabla.addRow(fila);

            // 🔴 Verificación de alerta
            if (p.getStockActual() < p.getStockMinimo()) {
                hayAlertas = true;
            }
        }

        // Mostrar alerta si hay productos bajos
        if (hayAlertas) {
            JOptionPane.showMessageDialog(
                    this,
                    "⚠ Hay productos con stock por debajo del mínimo.",
                    "Alerta de Inventario",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void limpiarCampos() {

        idSeleccionado = -1;
        txtNombre.setText("");
        txtStock.setText("");
        txtStockMinimo.setText("");
        txtStock.setEnabled(true);
        btnGuardar.setText("Registrar en Catálogo");

        if (comboCategorias.getItemCount() > 0)
            comboCategorias.setSelectedIndex(0);

        tablaProductos.clearSelection();
        txtNombre.requestFocus();
    }

    class StockBajoRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            int stock = (int) table.getValueAt(row, 3);
            int minimo = (int) table.getValueAt(row, 4);

            if (stock < minimo) {
                c.setBackground(new Color(255, 199, 206)); // Rojo claro
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

}

