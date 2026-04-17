package view;

import controller.ComprasController;
import controller.ProveedorController;
import model.DetalleOrdenCompra;
import model.Producto;
import model.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ComprasView extends JFrame {

    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad;
    private JComboBox<Proveedor> comboProveedores;

    private JButton btnNuevoProveedor, btnAgregar, btnEliminar;
    private JButton btnRegistrar, btnLimpiar;

    private JTable tabla;
    private DefaultTableModel modelo;

    // 🔥 LISTA REAL DE DETALLES (lo que se envía al controller)
    private List<DetalleOrdenCompra> listaDetalles = new ArrayList<>();

    private ComprasController controller;

    public void setController(ComprasController controller) {
        this.controller = controller;
    }

    public void inicializarDatos() {
        cargarProductos();
        cargarProveedores();
    }

    public ComprasView() {

        setTitle("F&B Soluciones Integrales - Orden de Compra");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Crear Orden de Compra"));

        comboProductos = new JComboBox<>();
        txtCantidad = new JTextField();
        comboProveedores = new JComboBox<>();

        btnNuevoProveedor = new JButton("+");
        btnAgregar = new JButton("Agregar ➕");
        btnEliminar = new JButton("Eliminar ❌");

        // Producto
        panel.add(new JLabel("Producto:"));
        panel.add(comboProductos);

        // Cantidad
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);

        // Proveedor
        panel.add(new JLabel("Proveedor:"));
        JPanel panelProveedor = new JPanel(new BorderLayout(5, 0));
        panelProveedor.add(comboProveedores, BorderLayout.CENTER);
        panelProveedor.add(btnNuevoProveedor, BorderLayout.EAST);
        panel.add(panelProveedor);

        // Botones carrito
        panel.add(btnAgregar);
        panel.add(btnEliminar);

        add(panel, BorderLayout.NORTH);

        // =========================
        // TABLA (CARRITO)
        // =========================
        modelo = new DefaultTableModel(new Object[]{"Producto", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 🔥 evita edición manual
            }
        };

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // =========================
        // BOTONES INFERIORES
        // =========================
        JPanel panelBotones = new JPanel();

        btnRegistrar = new JButton("Crear Orden de Compra");
        btnLimpiar = new JButton("Limpiar");

        btnRegistrar.setBackground(new Color(0, 102, 204));
        btnRegistrar.setForeground(Color.WHITE);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnAgregar.addActionListener(e -> agregarItem());
        btnEliminar.addActionListener(e -> eliminarItem());
        btnRegistrar.addActionListener(e -> registrarOrden());
        btnLimpiar.addActionListener(e -> limpiar());

        btnNuevoProveedor.addActionListener(e -> {
            ProveedorView form = new ProveedorView(this);
            form.setController(new ProveedorController());
            form.setVisible(true);

            cargarProveedores();
        });
    }

    // =========================
    // AGREGAR ITEM
    // =========================
    private void agregarItem() {

        Producto producto = (Producto) comboProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        try {

            int cantidad = Integer.parseInt(txtCantidad.getText());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Cantidad debe ser mayor a cero.");
                return;
            }

            // 🔥 Tabla visual
            modelo.addRow(new Object[]{
                    producto.getNombre(),
                    cantidad
            });

            // 🔥 Lista real
            DetalleOrdenCompra detalle = new DetalleOrdenCompra();
            detalle.setIdItem(producto.getId());
            detalle.setCantidadPedida(cantidad);

            listaDetalles.add(detalle);

            txtCantidad.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
        }
    }

    // =========================
    // ELIMINAR ITEM
    // =========================
    private void eliminarItem() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
            return;
        }

        modelo.removeRow(fila);
        listaDetalles.remove(fila);
    }

    // =========================
    // REGISTRAR ORDEN
    // =========================
    private void registrarOrden() {

        Proveedor proveedor = (Proveedor) comboProveedores.getSelectedItem();

        if (proveedor == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor.");
            return;
        }

        if (listaDetalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto.");
            return;
        }

        String resultado = controller.crearOrdenCompra(
                proveedor.getId(),
                listaDetalles
        );

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Orden creada correctamente.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // LIMPIAR
    // =========================
    private void limpiar() {

        txtCantidad.setText("");
        modelo.setRowCount(0);
        listaDetalles.clear();
    }

    // =========================
    // CARGAS
    // =========================
    private void cargarProductos() {

        if (controller == null) return;

        comboProductos.removeAllItems();

        List<Producto> lista = controller.obtenerProductos();

        for (Producto p : lista) {
            comboProductos.addItem(p);
        }
    }

    private void cargarProveedores() {

        if (controller == null) return;

        comboProveedores.removeAllItems();

        List<Proveedor> lista = controller.obtenerProveedores();

        for (Proveedor p : lista) {
            comboProveedores.addItem(p);
        }
    }
}