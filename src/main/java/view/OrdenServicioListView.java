package view;

import controller.OrdenServicioController;
import model.OrdenServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestión Operativa de Órdenes de Servicio.
 *
 * 🔥 ERP F&B:
 * - Consultar órdenes de forma maestra.
 * - Filtrar por los estados reales del nuevo flujo del ERP.
 * - Acceso directo a la gestión multi-técnico y operativa.
 */
public class OrdenServicioListView extends JFrame {

    // =========================
    // COMPONENTES
    // =========================

    private JTable tabla;

    private DefaultTableModel modelo;

    private JButton btnCargar;

    private JButton btnVerDetalle;

    private JButton btnGestionar;

    private JComboBox<String> comboEstado;

    private OrdenServicioController controller;

    // =========================
    // CONTROLLER
    // =========================

    public void setController(OrdenServicioController controller) {
        this.controller = controller;
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public OrdenServicioListView() {

        setTitle("Gestión de Órdenes de Servicio");

        setSize(1250, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        // Inicializamos el controlador por defecto por seguridad arquitectónica
        this.controller = new OrdenServicioController();

        inicializarComponentes();
    }

    // =========================
    // COMPONENTES
    // =========================

    private void inicializarComponentes() {

        // =========================
        // PANEL SUPERIOR
        // =========================

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelTop.add(new JLabel("Estado:"));

        comboEstado = new JComboBox<>();

        // 🔥 CORRECCIÓN 4: Estados alineados con el flujo operativo real sin depender de facturación
        comboEstado.addItem("TODAS");
        comboEstado.addItem("Pendiente");
        comboEstado.addItem("Asignada");
        comboEstado.addItem("En ejecución");
        comboEstado.addItem("Finalizada");
        comboEstado.addItem("Cancelada");

        btnCargar = new JButton("Cargar Órdenes");

        panelTop.add(comboEstado);
        panelTop.add(btnCargar);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================

        // 🔥 CORRECCIÓN: Columnas simplificadas para desacoplar técnicos y facturación directa
        modelo = new DefaultTableModel(
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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        tabla.setRowHeight(26);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // =========================
        // PANEL INFERIOR
        // =========================

        JPanel panelBottom = new JPanel();

        btnVerDetalle = new JButton("Ver Detalle");

        btnGestionar = new JButton("Gestionar Orden");

        // 🔵 DETALLE
        btnVerDetalle.setBackground(new Color(0, 102, 204));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.setOpaque(true);
        btnVerDetalle.setBorderPainted(false);

        // 🟢 GESTIÓN
        btnGestionar.setBackground(new Color(0, 153, 0));
        btnGestionar.setForeground(Color.WHITE);
        btnGestionar.setFocusPainted(false);
        btnGestionar.setOpaque(true);
        btnGestionar.setBorderPainted(false);

        panelBottom.add(btnVerDetalle);
        panelBottom.add(btnGestionar);

        add(panelBottom, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnCargar.addActionListener(e -> cargarOrdenes());

        btnVerDetalle.addActionListener(e -> verDetalle());

        btnGestionar.addActionListener(e -> gestionarOrden());
    }

    // =========================
    // CARGAR ÓRDENES
    // =========================

    private void cargarOrdenes() {

        modelo.setRowCount(0);

        String estado = comboEstado.getSelectedItem().toString();

        List<OrdenServicio> lista;

        if ("TODAS".equalsIgnoreCase(estado)) {
            lista = controller.listarTodas();
        } else {
            lista = controller.listarPorEstado(estado);
        }

        for (OrdenServicio os : lista) {
            // 🔥 CORRECCIÓN: Uso de getters corregidos libres de errores de compilación
            modelo.addRow(
                    new Object[]{
                            os.getIdOrdenServicio(),
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
    // OBTENER ORDEN
    // =========================

    private OrdenServicio obtenerOrdenSeleccionada() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden.");
            return null;
        }

        // 🔥 CORRECCIÓN: Extracción limpia basada en el ID real mapeado en la columna 0
        int idOrden = (int) modelo.getValueAt(fila, 0);

        return controller.buscarPorId(idOrden);
    }

    // =========================
    // VER DETALLE
    // =========================

    private void verDetalle() {

        OrdenServicio orden = obtenerOrdenSeleccionada();

        if (orden == null) {
            return;
        }

        String mensaje =
                "ORDEN #" + orden.getIdOrdenServicio()
                        + "\n\nCliente: " + orden.getNombreCliente()
                        + "\nEstado: " + orden.getEstado()
                        + "\nPrioridad: " + orden.getPrioridad()
                        + "\nFecha Programada: " + orden.getFechaProgramada()
                        + "\n\nDirección:\n" + orden.getDireccionServicio()
                        + "\n\nContacto:\n" + orden.getContactoNombre()
                        + "\n" + orden.getContactoTelefono()
                        + "\n\nObservaciones:\n" + orden.getObservaciones();

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Detalle Orden Servicio",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================
    // GESTIONAR ORDEN
    // =========================

    private void gestionarOrden() {

        OrdenServicio orden = obtenerOrdenSeleccionada();

        if (orden == null) {
            return;
        }

        // 🔥 CORRECCIÓN: Instanciación limpia abriendo la ventana de gestión unificada real
        OrdenServicioGestionView view = new OrdenServicioGestionView(orden);
        view.setVisible(true);
    }
}