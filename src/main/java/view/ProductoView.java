package view;

import controller.ProductoController;
import model.Categoria;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista para registrar productos.
 *
 * 🔥 NIVEL PRO:
 * - Integrada con Controller
 * - Manejo de categorías
 * - Validaciones básicas UI
 */
public class ProductoView extends JDialog {

    private JTextField txtNombre, txtStock, txtStockMin;
    private JComboBox<Categoria> comboCategoria;

    private JButton btnGuardar, btnCancelar;

    private ProductoController controller;

    public void setController(ProductoController controller) {
        this.controller = controller;
    }

    public ProductoView(Frame parent) {
        super(parent, "Nuevo Producto", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));

        txtNombre = new JTextField();
        txtStock = new JTextField();
        txtStockMin = new JTextField();
        comboCategoria = new JComboBox<>();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Stock Inicial:"));
        panel.add(txtStock);

        panel.add(new JLabel("Stock Mínimo:"));
        panel.add(txtStockMin);

        panel.add(new JLabel("Categoría:"));
        panel.add(comboCategoria);

        add(panel, BorderLayout.CENTER);

        // =========================
        // BOTONES
        // =========================

        JPanel panelBotones = new JPanel();

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(e -> guardarProducto());
        btnCancelar.addActionListener(e -> dispose());
    }

    // =========================
    // CARGA DE DATOS
    // =========================

    public void cargarCategorias() {

        if (controller == null) return;

        comboCategoria.removeAllItems();

        List<Categoria> lista = controller.obtenerCategorias();

        for (Categoria c : lista) {
            comboCategoria.addItem(c);
        }
    }

    // =========================
    // ACCIONES
    // =========================

    private void guardarProducto() {

        if (controller == null) return;

        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

        if (categoria == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.");
            return;
        }

        String resultado = controller.agregarProducto(
                txtNombre.getText(),
                txtStock.getText(),
                String.valueOf(categoria.getId()),
                txtStockMin.getText()
        );

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
            limpiar();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtStock.setText("");
        txtStockMin.setText("");

        if (comboCategoria.getItemCount() > 0)
            comboCategoria.setSelectedIndex(0);
    }
}