package view;

import controller.ProductoController;
import model.Categoria;
import model.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista para registrar/editar productos.
 *
 * 🔥 ERP F&B:
 * - Crear productos
 * - Editar productos
 * - Selección automática categoría
 */
public class ProductoView extends JDialog {

    private JTextField txtNombre;
    private JTextField txtStock;
    private JTextField txtStockMin;

    private JComboBox<Categoria> comboCategoria;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private ProductoController controller;

    // =========================
    // 🔥 MODO EDICIÓN
    // =========================
    private Producto productoEditando;

    public void setController(
            ProductoController controller
    ) {

        this.controller = controller;
    }

    public ProductoView(Frame parent) {

        super(
                parent,
                "Nuevo Producto",
                true
        );

        setSize(450, 300);

        setLocationRelativeTo(parent);

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del Producto"
                )
        );

        txtNombre =
                new JTextField();

        txtStock =
                new JTextField();

        txtStockMin =
                new JTextField();

        comboCategoria =
                new JComboBox<>();

        panel.add(
                new JLabel("Nombre:")
        );

        panel.add(txtNombre);

        panel.add(
                new JLabel("Stock Inicial:")
        );

        panel.add(txtStock);

        panel.add(
                new JLabel("Stock Mínimo:")
        );

        panel.add(txtStockMin);

        panel.add(
                new JLabel("Categoría:")
        );

        panel.add(comboCategoria);

        add(panel, BorderLayout.CENTER);

        // =========================
        // BOTONES
        // =========================

        JPanel panelBotones =
                new JPanel();

        btnGuardar =
                new JButton("Guardar");

        btnCancelar =
                new JButton("Cancelar");

        btnGuardar.setBackground(
                new Color(0, 153, 76)
        );

        btnGuardar.setForeground(
                Color.WHITE
        );

        btnGuardar.setFocusPainted(false);

        panelBotones.add(btnGuardar);

        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(
                e -> guardarProducto()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // 🔹 CARGAR CATEGORÍAS
    // =========================
    public void cargarCategorias() {

        if (controller == null) return;

        comboCategoria.removeAllItems();

        List<Categoria> lista =
                controller.obtenerCategorias();

        for (Categoria c : lista) {

            comboCategoria.addItem(c);
        }
    }

    // =========================
    // 🔥 CARGAR PRODUCTO
    // =========================
    public void cargarProducto(
            Producto producto
    ) {

        this.productoEditando =
                producto;

        txtNombre.setText(
                producto.getNombre()
        );

        txtStock.setText(
                String.valueOf(
                        producto.getStockActual()
                )
        );

        txtStock.setEnabled(false);

        txtStockMin.setText(
                String.valueOf(
                        producto.getStockMinimo()
                )
        );

        // =========================
        // 🔥 SELECCIONAR CATEGORÍA
        // =========================
        for (
                int i = 0;
                i < comboCategoria.getItemCount();
                i++
        ) {

            Categoria categoria =
                    comboCategoria.getItemAt(i);

            if (
                    categoria.getId()
                            == producto.getIdCategoria()
            ) {

                comboCategoria.setSelectedIndex(i);

                break;
            }
        }

        setTitle(
                "Editar Producto"
        );
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    private void guardarProducto() {

        if (controller == null) return;

        if (
                txtNombre.getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre es obligatorio."
            );

            return;
        }

        Categoria categoria =
                (Categoria) comboCategoria.getSelectedItem();

        if (categoria == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría."
            );

            return;
        }

        String resultado;

        // =========================
        // 🔥 EDITAR
        // =========================
        if (productoEditando != null) {

            resultado =
                    controller.editarProducto(
                            productoEditando.getId(),
                            txtNombre.getText(),
                            String.valueOf(
                                    categoria.getId()
                            ),
                            txtStockMin.getText()
                    );

        } else {

            // =========================
            // 🔥 NUEVO
            // =========================
            resultado =
                    controller.agregarProducto(
                            txtNombre.getText(),
                            txtStock.getText(),
                            String.valueOf(
                                    categoria.getId()
                            ),
                            txtStockMin.getText()
                    );
        }

        if ("OK".equals(resultado)) {

            JOptionPane.showMessageDialog(
                    this,
                    productoEditando != null
                            ? "Producto actualizado correctamente."
                            : "Producto registrado correctamente."
            );

            limpiar();

            dispose();

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
    // 🔹 LIMPIAR
    // =========================
    private void limpiar() {

        txtNombre.setText("");

        txtStock.setText("");

        txtStockMin.setText("");

        txtStock.setEnabled(true);

        productoEditando = null;

        if (
                comboCategoria.getItemCount() > 0
        ) {

            comboCategoria.setSelectedIndex(0);
        }
    }
}