package view;

import controller.InventarioController;
import model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal de la gestión de inventario.
 * Esta clase hereda de JFrame para crear la ventana.
 */

public class InventarioView extends JFrame {

    // Componentes de la interfaz
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNombre, txtStock, txtPrecio;
    private JButton btnGuardar, btnRefrescar;

    // Referencia al controlador (MVC)
    private InventarioController controller;

    public InventarioView() {
        // Inicializamos el controlador
        this.controller = new InventarioController();

        // Configuraciones de la ventana
        setTitle("F&B Soluciones Integrales - Inventario");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new BorderLayout(10, 10)); // Espaciado entre paneles

        // 1. Construir la interfaz
        inicializarComponentes();

        // 2. Cargar datos iniciales
        refrescarTabla();
    }

    private void inicializarComponentes() {
        // --- PANEL SUPERIOR: Formulario ---
        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registro de Productos"));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtStock = new JTextField();
        txtPrecio = new JTextField();
        btnGuardar = new JButton("Guardar Producto");

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(txtId);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(txtStock);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);

        // --- PANEL CENTRAL: Tabla ---
        String[] columnas = {"ID", "Descripción", "Stock", "Precio Unitario"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        // --- PANEL INFERIOR: Acciones ---
        JPanel panelAcciones = new JPanel();
        btnRefrescar = new JButton("Actualizar Lista");
        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnRefrescar);

        // --- Agregar todo al JFrame ---
        add(panelFormulario, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        // --- EVENTOS (Lógica del botón) ---
        btnGuardar.addActionListener(e -> ejecutarGuardado());
        btnRefrescar.addActionListener(e -> refrescarTabla());
    }

    private void ejecutarGuardado() {
        // Llamamos al controlador con los datos de los campos de texto
        String resultado = controller.agregarNuevoProducto(
                txtId.getText(),
                txtNombre.getText(),
                txtStock.getText(),
                txtPrecio.getText()
        );

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Guardado con éxito");
            limpiarCampos();
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarTabla() {
        // Limpiamos la tabla
        modeloTabla.setRowCount(0);

        // Pedimos los datos al controlador
        List<Producto> lista = controller.obtenerInventario();

        // Llenamos la tabla fila por fila
        for (Producto p : lista) {
            Object[] fila = { p.getId(), p.getNombre(), p.getStockActual(), p.getPrecioUnitario() };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtStock.setText("");
        txtPrecio.setText("");
    }
}
