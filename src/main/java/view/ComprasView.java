package view;

import controller.ComprasController;
import model.Producto;
import model.Proveedor;
import view.ProveedorView;
import controller.ProveedorController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ComprasView extends JFrame {

    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad, txtPrecio, txtFactura;
    private JComboBox<Proveedor> comboProveedores;
    private JButton btnNuevoProveedor;
    private JButton btnRegistrar, btnLimpiar;

    private ComprasController controller;

    public void setController(ComprasController controller) {
        this.controller = controller;
    }

    public void inicializarDatos() {
        cargarProductos();
        cargarProveedores();
    }

    public ComprasView() {

        setTitle("F&B Soluciones Integrales - Módulo de Compras");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Compra"));

        comboProductos = new JComboBox<>();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        comboProveedores = new JComboBox<>();
        btnNuevoProveedor = new JButton("+");
        txtFactura = new JTextField();

        // 🔹 Producto
        panel.add(new JLabel("Producto:"));
        panel.add(comboProductos);

        // 🔹 Cantidad
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);

        // 🔹 Precio
        panel.add(new JLabel("Precio Compra:"));
        panel.add(txtPrecio);

        // 🔹 Proveedor
        panel.add(new JLabel("Proveedor:"));

        JPanel panelProveedor = new JPanel(new BorderLayout(5, 0));
        panelProveedor.add(comboProveedores, BorderLayout.CENTER);
        panelProveedor.add(btnNuevoProveedor, BorderLayout.EAST);

        panel.add(panelProveedor); // ✅ AQUÍ estaba el error

        // 🔹 Factura
        panel.add(new JLabel("Factura:"));
        panel.add(txtFactura);

        // 🔹 Botones
        JPanel panelBotones = new JPanel();

        btnRegistrar = new JButton("Registrar Compra");
        btnLimpiar = new JButton("Limpiar");

        btnRegistrar.setBackground(new Color(0, 153, 76));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnRegistrar.addActionListener(e -> registrarCompra());
        btnLimpiar.addActionListener(e -> limpiar());

        btnNuevoProveedor.addActionListener(e -> {

            ProveedorView form = new ProveedorView(this);
            form.setController(new ProveedorController());

            form.setVisible(true);

            // 🔥 (en el siguiente paso recargamos proveedores automáticamente)
        });
    }

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

    private void registrarCompra() {

        Producto producto = (Producto) comboProductos.getSelectedItem();
        Proveedor proveedor = (Proveedor) comboProveedores.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        if (proveedor == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor.");
            return;
        }

        String resultado = controller.registrarCompra(
                producto.getId(),
                txtCantidad.getText(),
                txtPrecio.getText(),
                proveedor.getNombreRazonSocial(),
                txtFactura.getText()
        );

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Compra registrada correctamente.");
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtFactura.setText("");

        if (comboProductos.getItemCount() > 0)
            comboProductos.setSelectedIndex(0);

        if (comboProveedores.getItemCount() > 0)
            comboProveedores.setSelectedIndex(0);
    }
}