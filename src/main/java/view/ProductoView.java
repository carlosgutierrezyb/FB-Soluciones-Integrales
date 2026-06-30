package view;

import controller.ProductoController;
import model.Categoria;
import model.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Formulario para la creación y edición de productos en el inventario.
 */
public class ProductoView extends JDialog {

    private JTextField txtNombre;
    private JTextField txtStock;
    private JTextField txtStockMin;
    private JComboBox<Categoria> comboCategoria;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private ProductoController controller;
    private Producto productoEditando; // Almacena el producto si el formulario se abre en modo edición

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
        // Formulario central
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

        // Barra inferior de botones
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        // Configuración de estilo del botón Guardar (Corrige fondo oculto por el sistema)
        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setContentAreaFilled(true);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Controladores de eventos
        btnGuardar.addActionListener(e -> guardarProducto());
        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Llena el combo box de categorías desde la base de datos a través del controlador.
     */
    public void cargarCategorias() {
        if (controller == null) return;

        comboCategoria.removeAllItems();
        List<Categoria> lista = controller.obtenerCategorias();
        for (Categoria c : lista) {
            comboCategoria.addItem(c);
        }
    }

    /**
     * Pasa el formulario a modo edición, bloqueando el stock inicial y preseleccionando la categoría.
     */
    public void cargarProducto(Producto producto) {
        this.productoEditando = producto;

        txtNombre.setText(producto.getNombre());
        txtStock.setText(String.valueOf(producto.getStockActual()));
        txtStock.setEnabled(false); // No se permite alterar el stock inicial desde edición
        txtStockMin.setText(String.valueOf(producto.getStockMinimo()));

        // Selecciona la categoría que le corresponde al producto en el combo
        for (int i = 0; i < comboCategoria.getItemCount(); i++) {
            Categoria categoria = comboCategoria.getItemAt(i);
            if (categoria.getId() == producto.getIdCategoria()) {
                comboCategoria.setSelectedIndex(i);
                break;
            }
        }

        setTitle("Editar Producto");
    }

    /**
     * Procesa la validación y el registro de los datos, discriminando si es una inserción o actualización.
     */
    private void guardarProducto() {
        if (controller == null) return;

        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Categoria categoria = (Categoria) comboCategoria.getSelectedItem();
        if (categoria == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resultado;

        if (productoEditando != null) {
            // Flujo de actualización
            resultado = controller.editarProducto(
                    productoEditando.getId(),
                    txtNombre.getText(),
                    String.valueOf(categoria.getId()),
                    txtStockMin.getText()
            );
        } else {
            // Flujo de inserción nueva
            resultado = controller.agregarProducto(
                    txtNombre.getText(),
                    txtStock.getText(),
                    String.valueOf(categoria.getId()),
                    txtStockMin.getText()
            );
        }

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this,
                    productoEditando != null ? "Producto actualizado correctamente." : "Producto registrado correctamente.");
            limpiar();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Restablece los valores del formulario a su estado original por defecto.
     */
    private void limpiar() {
        txtNombre.setText("");
        txtStock.setText("");
        txtStockMin.setText("");
        txtStock.setEnabled(true);
        productoEditando = null;

        if (comboCategoria.getItemCount() > 0) {
            comboCategoria.setSelectedIndex(0);
        }
    }
}