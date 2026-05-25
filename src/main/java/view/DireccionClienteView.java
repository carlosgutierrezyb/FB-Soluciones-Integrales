package view;

import controller.DireccionClienteController;
import model.Cliente;
import model.DireccionCliente;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario dirección/sede cliente.
 *
 * 🔥 ERP F&B:
 * - Crear sedes
 * - Editar sedes
 * - Contactos por sede
 */
public class DireccionClienteView extends JDialog {

    // =========================
    // DATA
    // =========================

    private final Cliente cliente;

    private final DireccionClienteController controller;

    private DireccionCliente direccionEditando;

    // =========================
    // COMPONENTES
    // =========================

    private JTextField txtNombreSede;

    private JTextField txtDireccion;

    private JTextField txtCiudad;

    private JTextField txtContacto;

    private JTextField txtTelefono;

    private JButton btnGuardar;

    private JButton btnCancelar;

    // =========================
    // CONSTRUCTOR
    // =========================

    public DireccionClienteView(
            JFrame parent,
            Cliente cliente
    ) {

        super(
                parent,
                true
        );

        this.cliente = cliente;

        this.controller =
                new DireccionClienteController();

        setTitle(
                "Dirección / Sede Cliente"
        );

        setSize(500, 400);

        setLocationRelativeTo(parent);

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // UI
    // =========================

    private void inicializarComponentes() {

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                10,
                                10
                        )
                );

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Información Dirección"
                )
        );

        txtNombreSede =
                new JTextField();

        txtDireccion =
                new JTextField();

        txtCiudad =
                new JTextField();

        txtContacto =
                new JTextField();

        txtTelefono =
                new JTextField();

        panelFormulario.add(
                new JLabel("Nombre Sede:")
        );

        panelFormulario.add(txtNombreSede);

        panelFormulario.add(
                new JLabel("Dirección:")
        );

        panelFormulario.add(txtDireccion);

        panelFormulario.add(
                new JLabel("Ciudad:")
        );

        panelFormulario.add(txtCiudad);

        panelFormulario.add(
                new JLabel("Contacto:")
        );

        panelFormulario.add(txtContacto);

        panelFormulario.add(
                new JLabel("Teléfono:")
        );

        panelFormulario.add(txtTelefono);

        add(
                panelFormulario,
                BorderLayout.CENTER
        );

        // =========================
        // BOTONES
        // =========================

        JPanel panelBotones =
                new JPanel();

        btnGuardar =
                new JButton("Guardar");

        btnCancelar =
                new JButton("Cancelar");

        estilizarBoton(
                btnGuardar,
                new Color(0, 153, 76)
        );

        estilizarBoton(
                btnCancelar,
                new Color(153, 153, 153)
        );

        panelBotones.add(btnGuardar);

        panelBotones.add(btnCancelar);

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(
                e -> guardar()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // CARGAR DIRECCIÓN
    // =========================

    public void cargarDireccion(
            DireccionCliente direccion
    ) {

        this.direccionEditando =
                direccion;

        txtNombreSede.setText(
                direccion.getNombreSede()
        );

        txtDireccion.setText(
                direccion.getDireccion()
        );

        txtCiudad.setText(
                direccion.getCiudad()
        );

        txtContacto.setText(
                direccion.getContactoNombre()
        );

        txtTelefono.setText(
                direccion.getContactoTelefono()
        );
    }

    // =========================
    // GUARDAR
    // =========================

    private void guardar() {

        try {

            String nombreSede =
                    txtNombreSede
                            .getText()
                            .trim();

            String direccion =
                    txtDireccion
                            .getText()
                            .trim();

            String ciudad =
                    txtCiudad
                            .getText()
                            .trim();

            String contacto =
                    txtContacto
                            .getText()
                            .trim();

            String telefono =
                    txtTelefono
                            .getText()
                            .trim();

            if (direccion.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "La dirección es obligatoria."
                );

                return;
            }

            String respuesta;

            // =========================
            // NUEVO
            // =========================

            if (direccionEditando == null) {

                // Se envían los datos sueltos al controlador tal como él los espera
                respuesta = controller.guardar(
                        cliente.getIdCliente(),
                        nombreSede,
                        direccion,
                        ciudad,
                        contacto,
                        telefono
                );

            } else {

                // =========================
                // EDITAR
                // =========================

                direccionEditando.setNombreSede(
                        nombreSede
                );

                direccionEditando.setDireccion(
                        direccion
                );

                direccionEditando.setCiudad(
                        ciudad
                );

                direccionEditando.setContactoNombre(
                        contacto
                );

                direccionEditando.setContactoTelefono(
                        telefono
                );

                respuesta = controller.actualizar(
                        direccionEditando
                );
            }

            // Validamos basándonos en la respuesta de tipo String ("OK")
            if ("OK".equals(respuesta)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Dirección guardada correctamente."
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        respuesta, // Muestra el mensaje de error real devuelto por el negocio
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error guardando dirección."
            );
        }
    }

    // =========================
    // ESTILOS
    // =========================

    private void estilizarBoton(
            JButton btn,
            Color color
    ) {

        btn.setBackground(color);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setContentAreaFilled(true);

        btn.setOpaque(true);

        btn.setBorderPainted(false);
    }
}