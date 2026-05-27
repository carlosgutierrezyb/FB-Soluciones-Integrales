package view;

import controller.ServicioController;
import model.Categoria;
import model.Servicio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista formulario servicios.
 *
 * 🔥 ERP F&B:
 * - Crear servicio
 * - Editar servicio
 * - Catálogo comercial
 */
public class ServicioView extends JDialog {

    // =========================
    // CONTROLES
    // =========================

    private JTextField txtNombre;

    private JTextArea txtDescripcion;

    // 🔥 ACTUALIZADO: ComboBox tipado correctamente
    private JComboBox<Categoria> comboCategorias;

    private JTextField txtPrecioBase;

    private JTextField txtTiempoEstimado;

    private JCheckBox chkActivo;

    private JButton btnGuardar;

    private JButton btnCancelar;

    // =========================
    // DATA
    // =========================

    private ServicioController controller;

    private Servicio servicioEditando;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ServicioView(
            JFrame parent
    ) {

        super(
                parent,
                true
        );

        setTitle(
                "Gestión Servicio"
        );

        setSize(650, 600);

        setLocationRelativeTo(parent);

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // CONTROLLER
    // =========================

    public void setController(
            ServicioController controller
    ) {

        this.controller = controller;

        cargarCategorias();
    }

    // =========================
    // CARGAR CATEGORÍAS
    // =========================

    public void cargarCategorias() {

        if (controller == null) {

            return;
        }

        comboCategorias.removeAllItems();

        List<Categoria> lista =
                controller.obtenerCategorias();

        for (Categoria c : lista) {

            comboCategorias.addItem(c);
        }
    }

    // =========================
    // UI
    // =========================

    private void inicializarComponentes() {

        JPanel panelFormulario =
                new JPanel(
                        new GridBagLayout()
                );

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Información Servicio"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        // =========================
        // CONTROLES
        // =========================

        txtNombre =
                new JTextField();

        txtDescripcion =
                new JTextArea(5, 20);

        txtDescripcion.setLineWrap(true);

        txtDescripcion.setWrapStyleWord(true);

        // 🔥 ASIGNACIÓN CORRECTA
        comboCategorias =
                new JComboBox<>();

        txtPrecioBase =
                new JTextField();

        txtTiempoEstimado =
                new JTextField();

        chkActivo =
                new JCheckBox("Servicio Activo");

        chkActivo.setSelected(true);

        // =========================
        // FILA 1
        // =========================

        gbc.gridx = 0;
        gbc.gridy = 0;

        panelFormulario.add(
                new JLabel("Nombre:"),
                gbc
        );

        gbc.gridx = 1;

        panelFormulario.add(
                txtNombre,
                gbc
        );

        // =========================
        // FILA 2
        // =========================

        gbc.gridx = 0;
        gbc.gridy++;

        panelFormulario.add(
                new JLabel("Descripción:"),
                gbc
        );

        gbc.gridx = 1;

        panelFormulario.add(
                new JScrollPane(txtDescripcion),
                gbc
        );

        // =========================
        // FILA 3
        // =========================

        gbc.gridx = 0;
        gbc.gridy++;

        panelFormulario.add(
                new JLabel("Categoría:"),
                gbc
        );

        gbc.gridx = 1;

        panelFormulario.add(
                comboCategorias,
                gbc
        );

        // =========================
        // FILA 4
        // =========================

        gbc.gridx = 0;
        gbc.gridy++;

        panelFormulario.add(
                new JLabel("Precio Base:"),
                gbc
        );

        gbc.gridx = 1;

        panelFormulario.add(
                txtPrecioBase,
                gbc
        );

        // =========================
        // FILA 5
        // =========================

        gbc.gridx = 0;
        gbc.gridy++;

        panelFormulario.add(
                new JLabel("Tiempo Estimado (Horas):"),
                gbc
        );

        gbc.gridx = 1;

        panelFormulario.add(
                txtTiempoEstimado,
                gbc
        );

        // =========================
        // FILA 6
        // =========================

        gbc.gridx = 1;
        gbc.gridy++;

        panelFormulario.add(
                chkActivo,
                gbc
        );

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

        estilizarBotonGuardar(
                btnGuardar
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
                e -> guardarServicio()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // CARGAR SERVICIO
    // =========================

    public void cargarServicio(
            Servicio servicio
    ) {

        this.servicioEditando =
                servicio;

        txtNombre.setText(
                servicio.getNombre()
        );

        txtDescripcion.setText(
                servicio.getDescripcion()
        );

        txtPrecioBase.setText(
                String.valueOf(
                        servicio.getPrecioBase()
                )
        );

        txtTiempoEstimado.setText(
                String.valueOf(
                        servicio.getTiempoEstimadoHoras()
                )
        );

        // 🔥 CORREGIDO: Comparación con String estado
        chkActivo.setSelected(
                "ACTIVO".equals(servicio.getEstado())
        );

        // 🔥 SELECCIONAR CATEGORÍA
        for (
                int i = 0;
                i < comboCategorias.getItemCount();
                i++
        ) {

            Categoria categoria =
                    comboCategorias.getItemAt(i);

            if (
                    categoria.getId()
                            == servicio.getIdCategoria()
            ) {

                comboCategorias.setSelectedIndex(i);

                break;
            }
        }
    }

    // =========================
    // GUARDAR
    // =========================

    private void guardarServicio() {

        try {

            Categoria categoriaSeleccionada =
                    (Categoria) comboCategorias.getSelectedItem();

            if (categoriaSeleccionada == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione una categoría."
                );

                return;
            }

            Servicio servicio;

            if (servicioEditando == null) {

                servicio =
                        new Servicio();

            } else {

                servicio =
                        servicioEditando;
            }

            servicio.setNombre(
                    txtNombre.getText().trim()
            );

            servicio.setDescripcion(
                    txtDescripcion.getText().trim()
            );

            servicio.setIdCategoria(
                    categoriaSeleccionada.getId()
            );

            servicio.setPrecioBase(
                    Double.parseDouble(
                            txtPrecioBase.getText().trim()
                    )
            );

            servicio.setTiempoEstimadoHoras(
                    Double.parseDouble(
                            txtTiempoEstimado.getText().trim()
                    )
            );

            // 🔥 CORREGIDO: Se le asigna el valor al modelo basándose en la UI
            servicio.setEstado(
                    chkActivo.isSelected() ? "ACTIVO" : "INACTIVO"
            );

            String resultado =
                    controller.guardarServicio(
                            servicio
                    );

            if ("OK".equals(resultado)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Servicio guardado correctamente."
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        resultado
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Precio o tiempo inválido."
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error guardando servicio."
            );
        }
    }

    // =========================
    // ESTILOS
    // =========================

    private void estilizarBotonGuardar(
            JButton boton
    ) {

        boton.setBackground(
                new Color(0, 153, 76)
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFocusPainted(false);

        boton.setContentAreaFilled(true);

        boton.setOpaque(true);

        boton.setBorderPainted(false);
    }
}