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
 * - Permitir registrar entradas de inventario
 * - Interactuar con el ComprasController
 *
 * 🔥 Mantiene el mismo estilo visual del InventarioView
 */
public class ComprasView extends JFrame {

    // =========================
    // COMPONENTES UI
    // =========================

    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad, txtPrecio, txtFactura;

    private JButton btnRegistrar, btnLimpiar;

    private ComprasController controller;

    // =========================
    // CONEXIÓN MVC
    // =========================

    public void setController(ComprasController controller) {
        this.controller = controller;
    }

    /**
     * Se ejecuta después de inyectar el controller
     */
    public void inicializarDatos() {
        cargarProductos();
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public ComprasView() {

        setTitle("F&B Soluciones Integrales - Módulo de Compras");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    // =========================
    // UI
    // =========================

    private void inicializarComponentes() {

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 15, 15));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Compra"));

        comboProductos = new JComboBox<>();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        txtFactura = new JTextField();

        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(comboProductos);

        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Precio Unitario:"));
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Número de Factura:"));
        panelFormulario.add(txtFactura);

        // =========================
        // BOTONES
        // =========================

        JPanel panelAcciones = new JPanel();

        btnRegistrar = new JButton("Registrar Compra");
        btnLimpiar = new JButton("Limpiar");

        // Estilo profesional (igual inventario)
        btnRegistrar.setBackground(new Color(0, 153, 76));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);

        panelAcciones.add(btnRegistrar);
        panelAcciones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnRegistrar.addActionListener(e -> ejecutarCompra());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    // =========================
    // LÓGICA UI
    // =========================

    private void cargarProductos() {

        if (controller == null) return;

        comboProductos.removeAllItems();

        List<Producto> lista = controller.obtenerProductos();

        for (Producto p : lista) {
            comboProductos.addItem(p);
        }
    }

    private void ejecutarCompra() {

        if (controller == null) return;

        Producto producto = (Producto) comboProductos.getSelectedItem();

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        String resultado = controller.registrarCompra(
                producto.getId(),
                txtCantidad.getText(),
                txtPrecio.getText(),
                txtFactura.getText()
        );

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "✅ Compra registrada correctamente.");
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {

        txtCantidad.setText("");
        txtPrecio.setText("");
        txtFactura.setText("");

        if (comboProductos.getItemCount() > 0) {
            comboProductos.setSelectedIndex(0);
        }

        txtCantidad.requestFocus();
    }
}