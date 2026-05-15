package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import java.awt.*;

/**
 * Vista para registrar y editar clientes.
 *
 * 🔥 ERP F&B
 * - CRUD Clientes
 * - Soporte empresas y contactos
 */
public class ClienteView extends JDialog {

    private JTextField txtNombre;
    private JTextField txtNumeroId;
    private JTextField txtDireccion;
    private JTextField txtCiudad;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    // 🔥 CONTACTO
    private JTextField txtContacto;
    private JTextField txtTelefonoContacto;
    private JTextField txtCorreoContacto;

    private JComboBox<String> comboTipoId;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private ClienteController controller;

    // 🔥 MODO EDICIÓN
    private Cliente clienteEdicion;

    // =========================
    // SET CONTROLLER
    // =========================
    public void setController(
            ClienteController controller
    ) {

        this.controller = controller;
    }

    // =========================
    // CONSTRUCTOR
    // =========================
    public ClienteView(Frame parent) {

        super(
                parent,
                "Nuevo Cliente",
                true
        );

        setSize(550, 550);

        setLocationRelativeTo(parent);

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // UI
    // =========================
    private void inicializarComponentes() {

        JPanel panel =
                new JPanel(
                        new GridLayout(10, 2, 10, 10)
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del Cliente"
                )
        );

        txtNombre = new JTextField();

        txtNumeroId = new JTextField();

        txtDireccion = new JTextField();

        txtCiudad = new JTextField();

        txtTelefono = new JTextField();

        txtEmail = new JTextField();

        // 🔥 CONTACTO
        txtContacto = new JTextField();

        txtTelefonoContacto = new JTextField();

        txtCorreoContacto = new JTextField();

        comboTipoId =
                new JComboBox<>(
                        new String[]{
                                "CC",
                                "NIT",
                                "CE",
                                "PASAPORTE"
                        }
                );

        // =========================
        // CAMPOS
        // =========================

        panel.add(new JLabel("Tipo Identificación:"));
        panel.add(comboTipoId);

        panel.add(new JLabel("Número Identificación:"));
        panel.add(txtNumeroId);

        panel.add(new JLabel("Nombre / Razón Social:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);

        panel.add(new JLabel("Ciudad:"));
        panel.add(txtCiudad);

        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);

        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(txtEmail);

        // 🔥 CONTACTO
        panel.add(new JLabel("Nombre Contacto:"));
        panel.add(txtContacto);

        panel.add(new JLabel("Teléfono Contacto:"));
        panel.add(txtTelefonoContacto);

        panel.add(new JLabel("Correo Contacto:"));
        panel.add(txtCorreoContacto);

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

        btnGuardar.setContentAreaFilled(true);

        btnGuardar.setOpaque(true);

        btnGuardar.setBorderPainted(false);

        panelBotones.add(btnGuardar);

        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(
                e -> guardarCliente()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // 🔹 CARGAR CLIENTE
    // =========================
    public void cargarCliente(
            Cliente c
    ) {

        this.clienteEdicion = c;

        setTitle("Editar Cliente");

        comboTipoId.setSelectedItem(
                c.getTipoIdentificacion()
        );

        txtNumeroId.setText(
                c.getIdentificacion()
        );

        txtNombre.setText(
                c.getNombre()
        );

        txtDireccion.setText(
                c.getDireccion()
        );

        txtCiudad.setText(
                c.getCiudad()
        );

        txtTelefono.setText(
                c.getTelefono()
        );

        txtEmail.setText(
                c.getCorreo()
        );

        // 🔥 CONTACTO
        txtContacto.setText(
                c.getContactoNombre()
        );

        txtTelefonoContacto.setText(
                c.getContactoTelefono()
        );

        txtCorreoContacto.setText(
                c.getContactoEmail()
        );
    }

    // =========================
    // GUARDAR / ACTUALIZAR
    // =========================
    private void guardarCliente() {

        if (controller == null) return;

        if (
                txtNombre.getText()
                        .trim()
                        .isEmpty()

                        ||

                        txtNumeroId.getText()
                                .trim()
                                .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nombre y número de identificación son obligatorios."
            );

            return;
        }

        String resultado;

        // =========================
        // 🔹 NUEVO
        // =========================
        if (clienteEdicion == null) {

            resultado =
                    controller.guardarCliente(

                            txtNombre.getText(),

                            comboTipoId
                                    .getSelectedItem()
                                    .toString(),

                            txtNumeroId.getText(),

                            txtDireccion.getText(),

                            txtCiudad.getText(),

                            txtTelefono.getText(),

                            txtEmail.getText(),

                            txtContacto.getText(),

                            txtTelefonoContacto.getText(),

                            txtCorreoContacto.getText()
                    );
        }

        // =========================
        // 🔹 EDITAR
        // =========================
        else {

            resultado =
                    controller.actualizarCliente(

                            clienteEdicion.getIdCliente(),

                            txtNombre.getText(),

                            comboTipoId
                                    .getSelectedItem()
                                    .toString(),

                            txtNumeroId.getText(),

                            txtDireccion.getText(),

                            txtCiudad.getText(),

                            txtTelefono.getText(),

                            txtEmail.getText(),

                            txtContacto.getText(),

                            txtTelefonoContacto.getText(),

                            txtCorreoContacto.getText(),

                            clienteEdicion.getEstado()
                    );
        }

        if ("OK".equals(resultado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente guardado correctamente."
            );

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
}