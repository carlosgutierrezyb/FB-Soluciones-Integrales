package view;

import controller.EspecialidadTecnicaController;
import controller.OrdenServicioController;
import controller.OrdenServicioTecnicoController;
import controller.TecnicoController;
import model.OrdenServicio;
import model.OrdenServicioTecnico;
import model.Tecnico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Gestión Operativa Multi-Técnico de la Orden de Servicio.
 * * 🔥 ERP PRO:
 * - Control de estados por Técnico asignado.
 * - Libre de dependencias financieras/facturación.
 * - Orquestación mediante OrdenServicioTecnicoController.
 */
public class OrdenServicioGestionView extends JFrame {

    private final OrdenServicio orden;

    private final OrdenServicioController ordenController;
    private final OrdenServicioTecnicoController asignacionController;
    private final TecnicoController tecnicoController;
    private final EspecialidadTecnicaController especialidadController;

    private JLabel lblOrden;
    private JLabel lblCliente;
    private JLabel lblEstado;

    private JTable tablaTecnicos;
    private DefaultTableModel modeloTecnicos;

    private JButton btnAsignarTecnico;
    private JButton btnIniciarServicio;
    private JButton btnFinalizarServicio;
    private JButton btnActualizar;

    public OrdenServicioGestionView(OrdenServicio orden) {
        this.orden = orden;

        this.ordenController = new OrdenServicioController();
        this.asignacionController = new OrdenServicioTecnicoController();
        this.tecnicoController = new TecnicoController();
        this.especialidadController = new EspecialidadTecnicaController();

        setTitle("Gestión Orden Servicio #" + orden.getIdOrdenServicio());
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        cargarAsignaciones();
        refrescarCabeceraOrden();
    }

    private void inicializarComponentes() {
        // =========================
        // PANEL SUPERIOR (CABECERA)
        // =========================
        JPanel panelSuperior = new JPanel(new GridLayout(3, 1, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblOrden = new JLabel("Orden: " + orden.getIdOrdenServicio());
        lblCliente = new JLabel("Cliente: " + orden.getNombreCliente());
        lblEstado = new JLabel("Estado: " + orden.getEstado());

        lblOrden.setFont(new Font("SansSerif", Font.BOLD, 14));

        panelSuperior.add(lblOrden);
        panelSuperior.add(lblCliente);
        panelSuperior.add(lblEstado);

        add(panelSuperior, BorderLayout.NORTH);

        // =========================
        // TABLA DE TÉCNICOS ASIGNADOS
        // =========================
        modeloTecnicos = new DefaultTableModel(
                new Object[]{"ID Asignación", "Técnico", "Especialidad", "Estado Técnico", "Horas"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTecnicos = new JTable(modeloTecnicos);
        tablaTecnicos.setRowHeight(24);
        tablaTecnicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tablaTecnicos), BorderLayout.CENTER);

        // =========================
        // PANEL INFERIOR (BOTONES DE ACCIÓN)
        // =========================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAsignarTecnico = new JButton("Asignar Técnico");
        btnIniciarServicio = new JButton("Iniciar Labor (Técnico)");
        btnFinalizarServicio = new JButton("Finalizar Labor (Técnico)");
        btnActualizar = new JButton("Actualizar Vista");

        // Estilos rápidos de acción operativa
        btnAsignarTecnico.setBackground(new Color(0, 102, 204));
        btnAsignarTecnico.setForeground(Color.WHITE);
        btnIniciarServicio.setBackground(new Color(230, 126, 34));
        btnIniciarServicio.setForeground(Color.WHITE);
        btnFinalizarServicio.setBackground(new Color(46, 204, 113));
        btnFinalizarServicio.setForeground(Color.WHITE);

        panelBotones.add(btnAsignarTecnico);
        panelBotones.add(btnIniciarServicio);
        panelBotones.add(btnFinalizarServicio);
        panelBotones.add(btnActualizar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================
        btnAsignarTecnico.addActionListener(e -> asignarTecnico());
        btnIniciarServicio.addActionListener(e -> iniciarServicioTecnico());
        btnFinalizarServicio.addActionListener(e -> finalizarServicioTecnico());
        btnActualizar.addActionListener(e -> {
            cargarAsignaciones();
            refrescarCabeceraOrden();
        });
    }

    private void cargarAsignaciones() {
        modeloTecnicos.setRowCount(0);
        List<OrdenServicioTecnico> lista = asignacionController.listarPorOrden(orden.getIdOrdenServicio());

        for (OrdenServicioTecnico a : lista) {
            modeloTecnicos.addRow(
                    new Object[]{
                            a.getIdAsignacion(),
                            a.getNombreTecnico(),
                            a.getNombreEspecialidad(),
                            a.getEstado(),
                            a.getHorasTrabajadas()
                    }
            );
        }
    }

    private void refrescarCabeceraOrden() {
        // Consultamos el estado en tiempo real de la cabecera de la orden
        OrdenServicio ordenActualizada = ordenController.buscarPorId(orden.getIdOrdenServicio());
        if (ordenActualizada != null) {
            orden.setEstado(ordenActualizada.getEstado());
            lblEstado.setText("Estado Orden: " + orden.getEstado());
        }
    }

    private int obtenerIdAsignacionSeleccionada() {
        int fila = tablaTecnicos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un técnico de la lista para proceder.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) modeloTecnicos.getValueAt(fila, 0);
    }

    private void asignarTecnico() {
        List<Tecnico> tecnicos = tecnicoController.listarActivos();

        if (tecnicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No existen técnicos activos en el sistema.");
            return;
        }

        Tecnico tecnico = (Tecnico) JOptionPane.showInputDialog(
                this,
                "Seleccione el técnico a incorporar:",
                "Asignar Técnico de Servicio",
                JOptionPane.PLAIN_MESSAGE,
                null,
                tecnicos.toArray(),
                tecnicos.get(0)
        );

        if (tecnico == null) return;

        String especialidad = JOptionPane.showInputDialog(this, "Ingrese el ID de la Especialidad requerida:");
        if (especialidad == null || especialidad.isBlank()) return;

        try {
            OrdenServicioTecnico asignacion = new OrdenServicioTecnico();
            asignacion.setIdOrdenServicio(orden.getIdOrdenServicio());
            asignacion.setIdTecnico(tecnico.getIdTecnico());
            asignacion.setIdEspecialidad(Integer.parseInt(especialidad.trim()));
            asignacion.setEstado("Asignado");

            String resultado = asignacionController.asignarTecnico(asignacion);

            JOptionPane.showMessageDialog(this, "OK".equals(resultado) ? "Técnico asignado con éxito." : resultado);

            cargarAsignaciones();
            refrescarCabeceraOrden();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "El ID de la especialidad debe ser un número entero válido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // 🔥 CORRECCIÓN 1: Iniciar labor usando la asignación específica del técnico seleccionado
    private void iniciarServicioTecnico() {
        int idAsignacion = obtenerIdAsignacionSeleccionada();
        if (idAsignacion == -1) return;

        // El controller transaccional inicia al técnico y recalcula la Orden Maestra a 'En ejecución'
        String resultado = asignacionController.iniciarServicio(idAsignacion);
        JOptionPane.showMessageDialog(this, resultado);

        cargarAsignaciones();
        refrescarCabeceraOrden();
    }

    // 🔥 CORRECCIÓN 2: Finalizar labor del técnico seleccionado registrando sus tiempos
    private void finalizarServicioTecnico() {
        int idAsignacion = obtenerIdAsignacionSeleccionada();
        if (idAsignacion == -1) return;

        String horasInput = JOptionPane.showInputDialog(this, "Ingrese la cantidad de horas invertidas en la labor:");
        if (horasInput == null || horasInput.isBlank()) return;

        try {
            double horas = Double.parseDouble(horasInput.trim());
            if (horas < 0) {
                JOptionPane.showMessageDialog(this, "Las horas trabajadas no pueden ser negativas.");
                return;
            }

            // El controller guarda tiempos y valida si todos los técnicos terminaron para cerrar la OS a 'Finalizada'
            String resultado = asignacionController.finalizarServicio(idAsignacion, horas);
            JOptionPane.showMessageDialog(this, resultado);

            cargarAsignaciones();
            refrescarCabeceraOrden();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un valor numérico válido para las horas.");
        }
    }
}