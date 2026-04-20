package view;

import controller.EditarOrdenCompraController;
import model.DetalleOrdenCompra;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EditarOrdenCompraView extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnGuardar, btnEliminar;

    private EditarOrdenCompraController controller;

    private int idOrden;

    public void setController(EditarOrdenCompraController controller) {
        this.controller = controller;
    }

    public EditarOrdenCompraView(int idOrden) {
        this.idOrden = idOrden;

        setTitle("Editar Orden de Compra #" + idOrden);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {

        modelo = new DefaultTableModel(
                new Object[]{"Producto", "Pedido", "Recibido", "Pendiente"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // SOLO "Pedido" editable
            }
        };

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        btnGuardar = new JButton("Guardar Cambios");
        btnEliminar = new JButton("Eliminar Ítem");

        btnGuardar.setBackground(new Color(0, 102, 204));
        btnGuardar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(204, 0, 0));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS
        btnGuardar.addActionListener(e -> guardarCambios());
        btnEliminar.addActionListener(e -> eliminarItem());
    }

    // =========================
    // 🔹 CARGAR DATOS
    // =========================
    private void cargarDatos() {

        modelo.setRowCount(0);

        List<Object[]> datos = controller.obtenerDatosOrden(idOrden);

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }

    // =========================
    // 🔹 GUARDAR CAMBIOS
    // =========================
    private void guardarCambios() {

        try {

            for (int i = 0; i < modelo.getRowCount(); i++) {

                Producto producto = (Producto) modelo.getValueAt(i, 0);

                int nuevaCantidad = Integer.parseInt(
                        modelo.getValueAt(i, 1).toString()
                );

                int recibido = Integer.parseInt(
                        modelo.getValueAt(i, 2).toString()
                );

                if (nuevaCantidad < recibido) {
                    JOptionPane.showMessageDialog(this,
                            "No puede ser menor a lo recibido para: " + producto.getNombre());
                    return;
                }

                controller.actualizarCantidad(
                        idOrden,
                        producto.getId(),
                        nuevaCantidad
                );
            }

            JOptionPane.showMessageDialog(this, "Orden actualizada correctamente.");
            cargarDatos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en datos.");
        }
    }

    // =========================
    // 🔹 ELIMINAR ITEM
    // =========================
    private void eliminarItem() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem.");
            return;
        }

        Producto producto = (Producto) modelo.getValueAt(fila, 0);
        int recibido = Integer.parseInt(modelo.getValueAt(fila, 2).toString());

        if (recibido > 0) {
            JOptionPane.showMessageDialog(this,
                    "No puede eliminar un producto que ya tiene entradas.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este ítem?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            controller.eliminarItem(idOrden, producto.getId());

            cargarDatos();
        }
    }
}