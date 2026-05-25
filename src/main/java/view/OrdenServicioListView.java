package view;

import controller.OrdenServicioController;
import model.OrdenServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de gestión de órdenes de servicio.
 *
 * 🔥 ERP:
 * - Consultar órdenes
 * - Filtrar por estado
 * - Gestionar ejecución
 */
public class OrdenServicioListView extends JFrame {

    private JTable tabla;

    private DefaultTableModel modelo;

    private JButton btnCargar;

    private JButton btnVerDetalle;

    private JButton btnEjecutar;

    private JComboBox<String> comboEstado;

    private OrdenServicioController controller;

    public void setController(
            OrdenServicioController controller
    ) {

        this.controller = controller;
    }

    public OrdenServicioListView() {

        setTitle(
                "Gestión de Órdenes de Servicio"
        );

        setSize(1100, 600);

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        // =========================
        // 🔹 PANEL SUPERIOR
        // =========================
        JPanel panelTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panelTop.add(
                new JLabel("Estado:")
        );

        comboEstado =
                new JComboBox<>();

        comboEstado.addItem("TODAS");
        comboEstado.addItem("Pendiente");
        comboEstado.addItem("Agendada");
        comboEstado.addItem("En ejecución");
        comboEstado.addItem("Ejecutada");
        comboEstado.addItem("Cancelada");

        btnCargar =
                new JButton(
                        "Cargar Órdenes"
                );

        panelTop.add(comboEstado);

        panelTop.add(btnCargar);

        add(
                panelTop,
                BorderLayout.NORTH
        );

        // =========================
        // 🔹 TABLA
        // =========================
        modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Cliente",
                                "Estado",
                                "Prioridad",
                                "Fecha Programada",
                                "Dirección"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tabla =
                new JTable(modelo);

        tabla.setRowHeight(24);

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        // =========================
        // 🔹 PANEL INFERIOR
        // =========================
        JPanel panelBottom =
                new JPanel();

        btnVerDetalle =
                new JButton(
                        "Ver Detalle"
                );

        btnEjecutar =
                new JButton(
                        "Gestionar Orden"
                );

        // 🔵 BOTÓN DETALLE
        btnVerDetalle.setBackground(
                new Color(0, 102, 204)
        );

        btnVerDetalle.setForeground(
                Color.WHITE
        );

        btnVerDetalle.setFocusPainted(false);

        btnVerDetalle.setContentAreaFilled(true);

        btnVerDetalle.setOpaque(true);

        btnVerDetalle.setBorderPainted(false);

        // 🟢 BOTÓN EJECUCIÓN
        btnEjecutar.setBackground(
                new Color(0, 153, 0)
        );

        btnEjecutar.setForeground(
                Color.WHITE
        );

        btnEjecutar.setFocusPainted(false);

        btnEjecutar.setContentAreaFilled(true);

        btnEjecutar.setOpaque(true);

        btnEjecutar.setBorderPainted(false);

        panelBottom.add(btnVerDetalle);

        panelBottom.add(btnEjecutar);

        add(
                panelBottom,
                BorderLayout.SOUTH
        );

        // =========================
        // 🔥 EVENTOS
        // =========================
        btnCargar.addActionListener(
                e -> cargarOrdenes()
        );

        btnVerDetalle.addActionListener(
                e -> verDetalle()
        );

        btnEjecutar.addActionListener(
                e -> gestionarOrden()
        );
    }

    // =========================
    // 🔹 CARGAR ÓRDENES
    // =========================
    private void cargarOrdenes() {

        modelo.setRowCount(0);

        String estado =
                comboEstado
                        .getSelectedItem()
                        .toString();

        List<OrdenServicio> lista;

        if (
                "TODAS".equalsIgnoreCase(
                        estado
                )
        ) {

            lista =
                    controller.listarTodas();

        } else {

            lista =
                    controller.listarPorEstado(
                            estado
                    );
        }

        for (OrdenServicio os : lista) {

            modelo.addRow(
                    new Object[]{

                            os.getId(),

                            os.getNombreCliente(),

                            os.getEstado(),

                            os.getPrioridad(),

                            os.getFechaProgramada(),

                            os.getDireccionServicio()
                    }
            );
        }
    }

    // =========================
    // 🔹 OBTENER ORDEN
    // =========================
    private OrdenServicio obtenerOrdenSeleccionada() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una orden."
            );

            return null;
        }

        int idOrden =
                (int) modelo.getValueAt(
                        fila,
                        0
                );

        return controller.buscarPorId(
                idOrden
        );
    }

    // =========================
    // 🔹 VER DETALLE
    // =========================
    private void verDetalle() {

        OrdenServicio orden =
                obtenerOrdenSeleccionada();

        if (orden == null) {

            return;
        }

        String mensaje =
                "Orden: "
                        + orden.getId()
                        + "\n\n"

                        + "Cliente: "
                        + orden.getNombreCliente()
                        + "\n"

                        + "Estado: "
                        + orden.getEstado()
                        + "\n"

                        + "Prioridad: "
                        + orden.getPrioridad()
                        + "\n"

                        + "Fecha Programada: "
                        + orden.getFechaProgramada()
                        + "\n"

                        + "Dirección: "
                        + orden.getDireccionServicio()
                        + "\n\n"

                        + "Contacto: "
                        + orden.getContactoNombre()
                        + "\n"

                        + "Teléfono: "
                        + orden.getContactoTelefono()
                        + "\n\n"

                        + "Observaciones:\n"
                        + orden.getObservaciones();

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Detalle Orden Servicio",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================
    // 🔹 GESTIONAR ORDEN
    // =========================
    private void gestionarOrden() {

        OrdenServicio orden =
                obtenerOrdenSeleccionada();

        if (orden == null) {

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Aquí posteriormente abrirás "
                        + "la vista operativa de ejecución "
                        + "de servicios.\n\n"
                        + "Orden seleccionada: "
                        + orden.getId()
        );
    }
}