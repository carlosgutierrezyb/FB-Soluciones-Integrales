package view;

import controller.DireccionClienteController;
import model.Cliente;
import model.DireccionCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestión de direcciones/sedes del cliente.
 *
 * 🔥 ERP F&B:
 * - Múltiples sedes
 * - Contactos por sede
 * - Direcciones para órdenes servicio
 */
public class DireccionClienteListView extends JFrame {

    // =========================
    // DATA
    // =========================

    private final Cliente cliente;

    private final DireccionClienteController controller;

    // =========================
    // COMPONENTES
    // =========================

    private JTable tablaDirecciones;

    private DefaultTableModel modeloTabla;

    private JButton btnNuevo;

    private JButton btnEditar;

    private JButton btnEliminar;

    private JButton btnRefrescar;

    // =========================
    // CONSTRUCTOR
    // =========================

    public DireccionClienteListView(
            Cliente cliente
    ) {

        this.cliente = cliente;

        this.controller =
                new DireccionClienteController();

        setTitle(
                "Direcciones / Sedes - "
                        + cliente.getNombre()
        );

        setSize(1000, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();

        cargarDirecciones();
    }

    // =========================
    // UI
    // =========================

    private void inicializarComponentes() {

        // =========================
        // TÍTULO
        // =========================

        JLabel titulo =
                new JLabel(
                        "Direcciones / Sedes del Cliente",
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
        // TABLA
        // =========================

        String[] columnas = {

                "ID",
                "Sede",
                "Dirección",
                "Ciudad",
                "Contacto",
                "Teléfono"
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

        tablaDirecciones =
                new JTable(modeloTabla);

        tablaDirecciones.setRowHeight(25);

        JScrollPane scroll =
                new JScrollPane(
                        tablaDirecciones
                );

        add(
                scroll,
                BorderLayout.CENTER
        );

        // =========================
        // BOTONES
        // =========================

        JPanel panelBotones =
                new JPanel();

        btnNuevo =
                new JButton("Nueva");

        btnEditar =
                new JButton("Editar");

        btnEliminar =
                new JButton("Eliminar");

        btnRefrescar =
                new JButton("Refrescar");

        estilizarBoton(
                btnNuevo,
                new Color(0, 153, 76)
        );

        estilizarBoton(
                btnEditar,
                new Color(0, 102, 204)
        );

        estilizarBoton(
                btnEliminar,
                new Color(204, 0, 0)
        );

        estilizarBoton(
                btnRefrescar,
                new Color(102, 102, 102)
        );

        panelBotones.add(btnNuevo);

        panelBotones.add(btnEditar);

        panelBotones.add(btnEliminar);

        panelBotones.add(btnRefrescar);

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        // =========================
        // EVENTOS
        // =========================

        btnNuevo.addActionListener(
                e -> nuevaDireccion()
        );

        btnEditar.addActionListener(
                e -> editarDireccion()
        );

        btnEliminar.addActionListener(
                e -> eliminarDireccion()
        );

        btnRefrescar.addActionListener(
                e -> cargarDirecciones()
        );
    }

    // =========================
    // CARGAR
    // =========================

    private void cargarDirecciones() {

        modeloTabla.setRowCount(0);

        List<DireccionCliente> lista =
                controller.listarPorCliente(
                        cliente.getIdCliente()
                );

        for (DireccionCliente d : lista) {

            modeloTabla.addRow(
                    new Object[]{

                            d.getIdDireccion(),

                            d.getNombreSede(),

                            d.getDireccion(),

                            d.getCiudad(),

                            d.getContactoNombre(),

                            d.getContactoTelefono()
                    }
            );
        }
    }

    // =========================
    // NUEVA
    // =========================

    private void nuevaDireccion() {

        DireccionClienteView view =
                new DireccionClienteView(
                        this,
                        cliente
                );

        view.setVisible(true);

        cargarDirecciones();
    }

    // =========================
    // EDITAR
    // =========================

    private void editarDireccion() {

        int fila =
                tablaDirecciones.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una dirección."
            );

            return;
        }

        int idDireccion =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        DireccionCliente direccion =
                controller.buscarPorId(
                        idDireccion
                );

        if (direccion == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cargar dirección."
            );

            return;
        }

        DireccionClienteView view =
                new DireccionClienteView(
                        this,
                        cliente
                );

        view.cargarDireccion(
                direccion
        );

        view.setVisible(true);

        cargarDirecciones();
    }

    // =========================
    // ELIMINAR
    // =========================

    private void eliminarDireccion() {

        int fila =
                tablaDirecciones.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una dirección."
            );

            return;
        }

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar la dirección?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {

            return;
        }

        int idDireccion =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        boolean eliminado =
                controller.eliminar(
                        idDireccion
                );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Dirección eliminada."
            );

            cargarDirecciones();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar."
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