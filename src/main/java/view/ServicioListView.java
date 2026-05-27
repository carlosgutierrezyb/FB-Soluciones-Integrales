package view;

import controller.ServicioController;
import model.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Listado de servicios ERP.
 *
 * 🔥 RESPONSABILIDADES:
 * - Mostrar servicios registrados
 * - Crear servicios
 * - Editar servicios
 * - Inactivar servicios
 * - Mostrar SKU y categoría
 */
public class ServicioListView extends JFrame {

    // =========================
    // COMPONENTES
    // =========================

    private JTable tablaServicios;

    private DefaultTableModel modeloTabla;

    private JButton btnNuevo;

    private JButton btnEditar;

    private JButton btnInactivar;

    private JButton btnRefrescar;

    private ServicioController controller;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ServicioListView() {

        setTitle(
                "ERP F&B - Servicios"
        );

        setSize(1200, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

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
    }

    // =========================
    // UI
    // =========================

    private void inicializarComponentes() {

        // =========================
        // 🔹 TÍTULO
        // =========================

        JLabel titulo =
                new JLabel(
                        "Gestión de Servicios",
                        JLabel.CENTER
                );

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        add(
                titulo,
                BorderLayout.NORTH
        );

        // =========================
        // 🔹 TABLA
        // =========================

        String[] columnas = {

                "ID",
                "Código SKU",
                "Nombre",
                "Categoría",
                "Descripción",
                "Precio Base",
                "Tiempo Estimado",
                "Estado"
        };

        modeloTabla =
                new DefaultTableModel(
                        columnas,
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

        tablaServicios =
                new JTable(modeloTabla);

        tablaServicios.setRowHeight(25);

        tablaServicios
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(140);

        JScrollPane scroll =
                new JScrollPane(tablaServicios);

        add(
                scroll,
                BorderLayout.CENTER
        );

        // =========================
        // 🔹 BOTONES
        // =========================

        JPanel panelBotones =
                new JPanel();

        btnNuevo =
                new JButton("Nuevo");

        btnEditar =
                new JButton("Editar");

        btnInactivar =
                new JButton("Inactivar");

        btnRefrescar =
                new JButton("Refrescar");

        // =========================
        // 🔥 ESTILOS
        // =========================

        estilizarBoton(
                btnNuevo,
                new Color(0, 153, 76)
        );

        estilizarBoton(
                btnEditar,
                new Color(0, 102, 204)
        );

        estilizarBoton(
                btnInactivar,
                new Color(204, 0, 0)
        );

        estilizarBoton(
                btnRefrescar,
                new Color(102, 102, 102)
        );

        panelBotones.add(btnNuevo);

        panelBotones.add(btnEditar);

        panelBotones.add(btnInactivar);

        panelBotones.add(btnRefrescar);

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        // =========================
        // EVENTOS
        // =========================

        btnNuevo.addActionListener(
                e -> abrirNuevoServicio()
        );

        btnEditar.addActionListener(
                e -> editarServicio()
        );

        btnInactivar.addActionListener(
                e -> inactivarServicio()
        );

        btnRefrescar.addActionListener(
                e -> cargarServicios()
        );
    }

    // =========================
    // 🔹 CARGAR SERVICIOS
    // =========================

    public void cargarServicios() {

        if (controller == null) {

            return;
        }

        modeloTabla.setRowCount(0);

        List<Servicio> lista =
                controller.listarTodos();

        for (Servicio s : lista) {

            modeloTabla.addRow(
                    new Object[]{

                            s.getId(),

                            s.getCodigoReferencia(),

                            s.getNombre(),

                            s.getNombreCategoria(),

                            s.getDescripcion(),

                            s.getPrecioBase(),

                            s.getTiempoEstimadoHoras(),

                            s.getEstado()
                    }
            );
        }
    }

    // =========================
    // 🔹 NUEVO SERVICIO
    // =========================

    private void abrirNuevoServicio() {

        ServicioView view =
                new ServicioView(this);

        view.setController(controller);

        view.cargarCategorias();

        view.setVisible(true);

        cargarServicios();
    }

    // =========================
    // 🔹 EDITAR SERVICIO
    // =========================

    private void editarServicio() {

        int fila =
                tablaServicios.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un servicio."
            );

            return;
        }

        int idServicio =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        Servicio servicio =
                controller.buscarPorId(
                        idServicio
                );

        if (servicio == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cargar el servicio."
            );

            return;
        }

        ServicioView view =
                new ServicioView(this);

        view.setController(controller);

        view.cargarCategorias();

        // 🔥 CARGA AUTOMÁTICA DE DATOS
        view.cargarServicio(servicio);

        view.setVisible(true);

        cargarServicios();
    }

    // =========================
    // 🔹 INACTIVAR SERVICIO
    // =========================

    private void inactivarServicio() {

        int fila =
                tablaServicios.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un servicio."
            );

            return;
        }

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea inactivar el servicio seleccionado?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {

            return;
        }

        int idServicio =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        boolean eliminado =
                controller.inactivarServicio(
                        idServicio
                );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio inactivado correctamente."
            );

            cargarServicios();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo inactivar el servicio."
            );
        }
    }

    // =========================
    // 🔧 ESTILOS
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

    // =========================
    // GETTERS
    // =========================

    public JTable getTablaServicios() {

        return tablaServicios;
    }

    public JButton getBtnNuevo() {

        return btnNuevo;
    }

    public JButton getBtnEditar() {

        return btnEditar;
    }

    public JButton getBtnInactivar() {

        return btnInactivar;
    }

    public JButton getBtnRefrescar() {

        return btnRefrescar;
    }
}