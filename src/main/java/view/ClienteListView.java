package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Listado de clientes ERP.
 *
 * 🔥 RESPONSABILIDADES:
 * - Mostrar clientes registrados
 * - Permitir crear clientes
 * - Permitir editar clientes
 * - Permitir inactivar clientes
 * - Base del CRM ERP
 */
public class ClienteListView extends JFrame {

    // =========================
    // COMPONENTES
    // =========================
    private JTable tablaClientes;

    private DefaultTableModel modeloTabla;

    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnInactivar;
    private JButton btnRefrescar;

    private ClienteController controller;

    // =========================
    // CONSTRUCTOR
    // =========================
    public ClienteListView() {

        setTitle("ERP F&B - Clientes");

        setSize(1300, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    // =========================
    // CONTROLLER
    // =========================
    public void setController(
            ClienteController controller
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
                        "Gestión de Clientes",
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

        add(titulo, BorderLayout.NORTH);

        // =========================
        // 🔹 TABLA
        // =========================
        String[] columnas = {

                "ID",
                "Tipo ID",
                "Identificación",
                "Nombre",
                "Teléfono",
                "Correo",
                "Ciudad",

                // 🔥 CONTACTO
                "Contacto",
                "Teléfono Contacto",
                "Correo Contacto",

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

        tablaClientes =
                new JTable(modeloTabla);

        tablaClientes.setRowHeight(25);

        JScrollPane scroll =
                new JScrollPane(tablaClientes);

        add(scroll, BorderLayout.CENTER);

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

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnNuevo.addActionListener(
                e -> abrirNuevoCliente()
        );

        btnEditar.addActionListener(
                e -> editarCliente()
        );

        btnInactivar.addActionListener(
                e -> inactivarCliente()
        );

        btnRefrescar.addActionListener(
                e -> cargarClientes()
        );
    }

    // =========================
    // 🔹 CARGAR CLIENTES
    // =========================
    public void cargarClientes() {

        if (controller == null) {
            return;
        }

        modeloTabla.setRowCount(0);

        List<Cliente> lista =
                controller.listarClientes();

        for (Cliente c : lista) {

            modeloTabla.addRow(
                    new Object[]{

                            c.getIdCliente(),

                            c.getTipoIdentificacion(),

                            c.getIdentificacion(),

                            c.getNombre(),

                            c.getTelefono(),

                            c.getCorreo(),

                            c.getCiudad(),

                            // 🔥 CONTACTO
                            c.getContactoNombre(),

                            c.getContactoTelefono(),

                            c.getContactoEmail(),

                            c.getEstado()
                    }
            );
        }
    }

    // =========================
    // 🔹 NUEVO CLIENTE
    // =========================
    private void abrirNuevoCliente() {

        ClienteView view =
                new ClienteView(this);

        view.setController(controller);

        view.setVisible(true);

        cargarClientes();
    }

    // =========================
    // 🔹 EDITAR CLIENTE
    // =========================
    private void editarCliente() {

        int fila =
                tablaClientes.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente."
            );

            return;
        }

        int idCliente =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        Cliente cliente =
                controller.buscarClientePorId(
                        idCliente
                );

        if (cliente == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cargar el cliente."
            );

            return;
        }

        ClienteView view =
                new ClienteView(this);

        view.setController(controller);

        // 🔥 CARGAR DATOS EXISTENTES
        view.cargarCliente(cliente);

        view.setVisible(true);

        cargarClientes();
    }

    // =========================
    // 🔹 INACTIVAR CLIENTE
    // =========================
    private void inactivarCliente() {

        int fila =
                tablaClientes.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente."
            );

            return;
        }

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea inactivar el cliente seleccionado?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {

            return;
        }

        int idCliente =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        boolean eliminado =
                controller.eliminarCliente(
                        idCliente
                );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente inactivado correctamente."
            );

            cargarClientes();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo inactivar el cliente."
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
    public JTable getTablaClientes() {
        return tablaClientes;
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