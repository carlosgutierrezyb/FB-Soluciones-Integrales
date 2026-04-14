package view;

import controller.ComprasController;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Compras
 *
 * Responsabilidades:
 * - Registrar compras de productos
 * - Mostrar historial de compras (básico)
 * - Actualizar inventario automáticamente
 */
public class ComprasView extends JFrame {

    // =========================
    // COMPONENTES UI
    // =========================
    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JTextField txtProveedor;

    private JTable tablaCompras;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar, btnRefrescar, btnLimpiar;

    // Controller
    private ComprasController controller;

    /**
     * Inyección del controlador
     */
    public void setController(ComprasController controller) {
        this.controller = controller;
    }

    /**
     * Inicialización de datos
     */
    public void inicializarDatos() {
        cargarProductos();
        refrescarTabla();
    }

    /**
     * Constructor → solo UI
     */
    public ComprasView() {

        setTitle("F&B Soluciones Integrales - Registro de Compras");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    /**
     * Construcción de la interfaz
     */
    private void inicializarComponentes() {

        // Panel formulario
        JPanel panelFormulario = new JPanel(new GridLayout(1, 4, 15, 15));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Compra"));

        comboProductos = new JComboBox<>();
        txtCantidad = new JTextField();

        panelFormulario.setLayout(new GridLayout(2, 4, 15, 15));

        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(comboProductos);

        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Precio Unitario:"));
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Proveedor:"));
        panelFormulario.add(txtProveedor);

        // Tabla
        String[] columnas = {"Producto", "Cantidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCompras = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaCompras);

        // Panel botones
        JPanel panelAcciones = new JPanel();

        btnRegistrar = new JButton("Registrar Compra");
        btnRefrescar = new JButton("Actualizar Vista");
        btnLimpiar = new JButton("Limpiar");

        // Estilo consistente con InventarioView
        btnRegistrar.setBackground(new Color(0, 153, 76));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);

        panelAcciones.add(btnRegistrar);
        panelAcciones.add(btnRefrescar);
        panelAcciones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================
        btnRegistrar.addActionListener(e -> ejecutarCompra());
        btnRefrescar.addActionListener(e -> refrescarTabla());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    /**
     * Carga productos en el combo
     */
    private void cargarProductos() {

        if (controller == null) return;

        comboProductos.removeAllItems();

        List<Producto> lista = controller.obtenerProductos();

        for (Producto p : lista) {
            comboProductos.addItem(p);
        }
    }

    /**
     * Ejecuta la compra
     */
    private void ejecutarCompra() {

        Producto producto = (Producto) comboProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        String cantidad = txtCantidad.getText();

        String resultado = controller.registrarCompra(
                producto.getId(),
                txtCantidad.getText(),
                txtPrecio.getText(),
                txtProveedor.getText()
        );

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Compra registrada correctamente.");

            limpiarCampos();
            refrescarTabla();

        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refresca la tabla de compras
     */
    private void refrescarTabla() {

        if (controller == null) return;

        modeloTabla.setRowCount(0);

        List<String[]> lista = controller.obtenerHistorialCompras();

        for (String[] fila : lista) {
            modeloTabla.addRow(fila);
        }
    }

    /**
     * Limpia formulario
     */
    private void limpiarCampos() {

        txtCantidad.setText("");

        if (comboProductos.getItemCount() > 0)
            comboProductos.setSelectedIndex(0);

        txtCantidad.requestFocus();
    }
}