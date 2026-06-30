package view;

import controller.ProveedorController;
import model.Proveedor;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para el registro y actualización de proveedores en el ERP.
 */
public class ProveedorView extends JDialog {

    private JTextField txtNombre;
    private JTextField txtNumeroId;
    private JTextField txtDireccion;
    private JTextField txtCiudad;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtContacto;
    private JTextField txtCelular;
    private JTextField txtEmailContacto;

    private JComboBox<String> comboTipoId;
    private JComboBox<String> comboDv;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private ProveedorController controller;
    private Proveedor proveedorEdicion; // Mantiene la referencia si se abre para editar

    public void setController(ProveedorController controller) {
        this.controller = controller;
    }

    public ProveedorView(Frame parent) {
        super(parent, "Proveedor", true);
        setSize(500, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Proveedor"));

        txtNombre = new JTextField();
        txtNumeroId = new JTextField();
        txtDireccion = new JTextField();
        txtCiudad = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtContacto = new JTextField();
        txtCelular = new JTextField();
        txtEmailContacto = new JTextField();

        comboTipoId = new JComboBox<>(new String[]{"NIT", "CC", "CE", "PASAPORTE"});

        comboDv = new JComboBox<>();
        comboDv.addItem("");
        for (int i = 0; i <= 9; i++) {
            comboDv.addItem(String.valueOf(i));
        }

        // Construcción del formulario
        panel.add(new JLabel("Tipo ID:"));
        panel.add(comboTipoId);
        panel.add(new JLabel("Número ID:"));
        panel.add(txtNumeroId);
        panel.add(new JLabel("DV:"));
        panel.add(comboDv);
        panel.add(new JLabel("Nombre / Razón Social:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Ciudad:"));
        panel.add(txtCiudad);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contacto:"));
        panel.add(txtContacto);
        panel.add(new JLabel("Celular Contacto:"));
        panel.add(txtCelular);
        panel.add(new JLabel("Email Contacto:"));
        panel.add(txtEmailContacto);

        add(panel, BorderLayout.CENTER);

        // Configuración de botones e interfaz inferior
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        // Solución al fondo blanco/letra blanca en Look and Feel nativos
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

        // Control de acciones
        btnGuardar.addActionListener(e -> guardarProveedor());
        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Mapea los datos del proveedor seleccionado en los campos para pasar a modo edición.
     */
    public void cargarProveedor(Proveedor p) {
        this.proveedorEdicion = p;
        setTitle("Editar Proveedor");

        comboTipoId.setSelectedItem(p.getTipoIdentificacion());
        txtNumeroId.setText(p.getNumeroIdentificacion());
        comboDv.setSelectedItem(p.getDv());
        txtNombre.setText(p.getNombreRazonSocial());
        txtDireccion.setText(p.getDireccion());
        txtCiudad.setText(p.getCiudad());
        txtTelefono.setText(p.getTelefono());
        txtEmail.setText(p.getEmail());
        txtContacto.setText(p.getContactoNombre());
        txtCelular.setText(p.getContactoCelular());
        txtEmailContacto.setText(p.getContactoEmail());
    }

    /**
     * Valida las restricciones mínimas obligatorias y despacha el registro al controlador.
     */
    private void guardarProveedor() {
        if (controller == null) return;

        if (txtNombre.getText().trim().isEmpty() || txtNumeroId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Número ID son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resultado;

        if (proveedorEdicion == null) {
            // Envío para inserción de nuevo registro
            resultado = controller.guardarProveedor(
                    txtNombre.getText(),
                    comboTipoId.getSelectedItem().toString(),
                    txtNumeroId.getText(),
                    comboDv.getSelectedItem().toString(),
                    txtDireccion.getText(),
                    txtCiudad.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    txtContacto.getText(),
                    txtCelular.getText(),
                    txtEmailContacto.getText()
            );
        } else {
            // Envío para actualizar registro existente utilizando su ID primario
            resultado = controller.actualizarProveedor(
                    proveedorEdicion.getId(),
                    txtNombre.getText(),
                    comboTipoId.getSelectedItem().toString(),
                    txtNumeroId.getText(),
                    comboDv.getSelectedItem().toString(),
                    txtDireccion.getText(),
                    txtCiudad.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    txtContacto.getText(),
                    txtCelular.getText(),
                    txtEmailContacto.getText()
            );
        }

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Proveedor guardado correctamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}