package view;

import controller.ProveedorController;

import javax.swing.*;
import java.awt.*;

public class ProveedorView extends JDialog {

    private JTextField txtNombre, txtNumeroId, txtDireccion, txtCiudad;
    private JTextField txtTelefono, txtEmail;
    private JTextField txtContacto, txtCelular, txtEmailContacto;

    private JComboBox<String> comboTipoId, comboDv;

    private JButton btnGuardar, btnCancelar;

    private ProveedorController controller;

    public void setController(ProveedorController controller) {
        this.controller = controller;
    }

    public ProveedorView(Frame parent) {
        super(parent, "Nuevo Proveedor", true);
        setSize(500, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // 🔥 Grid CORREGIDO (2 columnas = limpio)
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
        comboDv.addItem(""); // vacío opcional
        for (int i = 0; i <= 9; i++) {
            comboDv.addItem(String.valueOf(i));
        }


        // =========================
        // CAMPOS (orden limpio)
        // =========================

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

        // =========================
        // BOTONES
        // =========================

        JPanel panelBotones = new JPanel();

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(e -> guardarProveedor());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarProveedor() {

        if (controller == null) return;

        // 🔥 VALIDACIÓN BÁSICA UI
        if (txtNombre.getText().trim().isEmpty() || txtNumeroId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Número de identificación son obligatorios.");
            return;
        }


        String resultado = controller.guardarProveedor(
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

        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Proveedor registrado correctamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}