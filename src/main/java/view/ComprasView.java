package view;

import controller.ComprasController;
import model.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista del módulo de Compras.
 *
 * RESPONSABILIDADES:
 * ✔ Registrar entradas de inventario
 * ✔ Seleccionar producto
 * ✔ Ingresar cantidad, precio, proveedor y factura
 *
 * 🔥 Mantiene el mismo estilo profesional que InventarioView
 */
public class ComprasView extends JFrame {

    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad, txtPrecio, txtProveedor, txtFactura;

    private JButton btnRegistrar, btnLimpiar;

    private ComprasController controller;

    public void setController(ComprasController controller) {
        this.controller = controller;
    }

    public void inicializarDatos() {
        cargarProductos();
    }

    public ComprasView() {

        setTitle("F&B Soluciones Integrales - Módulo de Compras");
        setSize(600, 400);
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
        txtProveedor = new JTextField();
        txtFactura = new JTextField();

        panel.add(new JLabel("Producto:"));
        panel.add(comboProductos);

        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);

        panel.add(new JLabel("Precio Compra:"));
        panel.add(txtPrecio);

        panel.add(new JLabel("Proveedor:"));
        panel.add(txtProveedor);

        panel.add(new JLabel("Factura:"));
        panel.add(txtFactura);

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

        // EVENTOS
        btnRegistrar.addActionListener(e -> registrarCompra());
        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void cargarProductos() {

        if (controller == null) return;

        comboProductos.removeAllItems();

        List<Producto> lista = controller.obtenerProductos();

        for (Producto p : lista) {
            comboProductos.addItem(p);
        }
    }

    private void registrarCompra() {

        Producto producto = (Producto) comboProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        String resultado = controller.registrarCompra(
                producto.getId(),
                txtCantidad.getText(),
                txtPrecio.getText(),
                txtProveedor.getText(),
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
        txtProveedor.setText("");
        txtFactura.setText("");
        comboProductos.setSelectedIndex(0);
    }
}