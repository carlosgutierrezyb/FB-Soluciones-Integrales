package view;

import controller.OrdenServicioController;
import model.OrdenServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Consulta Maestra y Monitoreo de Órdenes de Servicio.
 * Permite filtrar el estado operativo general y disparar flujos de gestión multi-técnico.
 */
public class OrdenServicioListView extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboEstado;

    private JButton btnCargar;
    private JButton btnVerDetalle;
    private JButton btnGestionar;

    private OrdenServicioController controller;

    public void setController(OrdenServicioController controller) {
        this.controller = controller;
    }

    public OrdenServicioListView() {
        setTitle("F&B Soluciones Integrales - Monitor de Órdenes de Servicio");
        setSize(1250, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Inicialización preventiva del controlador para la capa de datos
        this.controller = new OrdenServicioController();

        inicializarComponentes();

        // 🔥 OPTIMIZACIÓN UX: Carga automática al abrir el módulo para evitar tablas vacías
        cargarOrdenes();
    }

    private void inicializarComponentes() {
        // =========================
        // PANEL SUPERIOR (FILTROS)
        // =========================
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel lblFiltro = new JLabel("Filtrar por Estado:");
        lblFiltro.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelTop.add(lblFiltro);

        comboEstado = new JComboBox<>();
        comboEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboEstado.addItem("TODAS");
        comboEstado.addItem("Pendiente");
        comboEstado.addItem("Asignada");
        comboEstado.addItem("En ejecución");
        comboEstado.addItem("Finalizada");
        comboEstado.addItem("Cancelada");
        panelTop.add(comboEstado);

        btnCargar = new JButton("Cargar Órdenes 🔄");
        btnCargar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCargar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTop.add(btnCargar);

        add(panelTop, BorderLayout.NORTH);

        // =========================
        // TABLA MAESTRA
        // =========================
        modelo = new DefaultTableModel(
                new Object[]{
                        "ID Orden",
                        "Cliente / Contacto",
                        "Estado Actual",
                        "Prioridad",
                        "Fecha Programada",
                        "Dirección de Servicio"
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
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // =========================
        // PANEL INFERIOR (ACCIONES)
        // =========================
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        btnVerDetalle = crearBotonUI("Ver Detalle Completo 📄", new Color(0, 102, 204));
        btnGestionar = crearBotonUI("Gestionar Operación ⚙️", new Color(0, 153, 0));

        panelBottom.add(btnVerDetalle);
        panelBottom.add(btnGestionar);

        add(panelBottom, BorderLayout.SOUTH);

        // =========================
        // ASIGNACIÓN DE EVENTOS
        // =========================
        btnCargar.addActionListener(e -> cargarOrdenes());
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnGestionar.addActionListener(e -> gestionarOrden());
    }

    /**
     * Helper para estandarizar el look de los botones inferiores en Swing.
     */
    private JButton crearBotonUI(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

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
            // 🔥 CORREGIDO: Ajustado para usar getContactoNombre() de manera segura
            String clienteRepresentacion = os.getContactoNombre() != null ? os.getContactoNombre() : "N/A";

            modelo.addRow(
                    new Object[]{
                            os.getIdOrdenServicio(),
                            clienteRepresentacion,
                            os.getEstado(),
                            os.getPrioridad(),
                            os.getFechaProgramada(),
                            os.getDireccionServicio()
                    }
            );
        }
    }

    private OrdenServicio obtenerOrdenSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una orden de servicio de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int idOrden = (int) modelo.getValueAt(fila, 0);
        return controller.buscarPorId(idOrden);
    }

    private void verDetalle() {
        OrdenServicio orden = obtenerOrdenSeleccionada();
        if (orden == null) return;

        String cliente = orden.getContactoNombre() != null ? orden.getContactoNombre() : "N/A";
        String observaciones = (orden.getObservaciones() != null && !orden.getObservaciones().isBlank()) ? orden.getObservaciones() : "Sin observaciones registradas.";

        String mensaje = String.format(
                "========================================\n" +
                        "               DETALLE DE ORDEN DE SERVICIO #%d\n" +
                        "========================================\n\n" +
                        "• Cliente: %s\n" +
                        "• Estado General: %s\n" +
                        "• Prioridad: %s\n" +
                        "• Fecha Programada: %s\n\n" +
                        "• Dirección de Destino:\n  %s\n\n" +
                        "• Personal de Contacto:\n  %s (%s)\n\n" +
                        "• Notas de Campo:\n  %s",
                orden.getIdOrdenServicio(),
                cliente,
                orden.getEstado(),
                orden.getPrioridad(),
                orden.getFechaProgramada() != null ? orden.getFechaProgramada().toString() : "No definida",
                orden.getDireccionServicio(),
                orden.getContactoNombre(),
                orden.getContactoTelefono(),
                observaciones
        );

        JOptionPane.showMessageDialog(this, mensaje, "F&B - Ficha de la Orden", JOptionPane.INFORMATION_MESSAGE);
    }

    private void gestionarOrden() {
        OrdenServicio orden = obtenerOrdenSeleccionada();
        if (orden == null) return;

        // 🔥 COMPILACIÓN VERDE: Abre la vista operativa pasando la entidad maestra de forma exitosa
        OrdenServicioGestionView view = new OrdenServicioGestionView(orden);
        view.setVisible(true);
    }
}