package view;

import controller.ProveedorController;
import model.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Listado de proveedores ERP F&B
 *
 * 🔥 RESPONSABILIDADES:
 * - Mostrar proveedores
 * - Crear nuevos proveedores
 * - Editar proveedores
 * - Refrescar información
 */
public class ProveedorListView extends JFrame {

    private JTable tabla;

    private DefaultTableModel modelo;

    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnRefrescar;

    private ProveedorController controller;

    public void setController(
            ProveedorController controller
    ) {

        this.controller = controller;
    }

    public ProveedorListView() {

        setTitle(
                "ERP F&B - Proveedores"
        );

        setSize(950, 500);

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(10, 10)
        );

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        inicializarComponentes();
    }

    // =========================
    // 🔹 COMPONENTES
    // =========================
    private void inicializarComponentes() {

        // =========================
        // TÍTULO
        // =========================
        JLabel titulo =
                new JLabel(
                        "Gestión de Proveedores",
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
                        15,
                        10,
                        10,
                        10
                )
        );

        add(titulo, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================
        modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Tipo ID",
                                "Número",
                                "DV",
                                "Proveedor",
                                "Ciudad",
                                "Teléfono",
                                "Email",
                                "Contacto",
                                "Celular Contacto",
                                "Email Contacto"
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

        tabla.setRowHeight(25);

        JScrollPane scroll =
                new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // =========================
        // BOTONES
        // =========================
        JPanel panelBotones =
                new JPanel();

        btnNuevo =
                crearBoton(
                        "Nuevo",
                        new Color(0, 153, 76)
                );

        btnEditar =
                crearBoton(
                        "Editar",
                        new Color(0, 102, 204)
                );

        btnRefrescar =
                crearBoton(
                        "Refrescar",
                        new Color(102, 102, 102)
                );

        panelBotones.add(btnNuevo);

        panelBotones.add(btnEditar);

        panelBotones.add(btnRefrescar);

        add(panelBotones, BorderLayout.SOUTH);

        // =========================
        // EVENTOS
        // =========================

        btnNuevo.addActionListener(
                e -> abrirNuevoProveedor()
        );

        btnEditar.addActionListener(
                e -> editarProveedor()
        );

        btnRefrescar.addActionListener(
                e -> cargarProveedores()
        );
    }

    // =========================
    // 🔹 CARGAR PROVEEDORES
    // =========================
    public void cargarProveedores() {

        if (controller == null) return;

        modelo.setRowCount(0);

        List<Proveedor> lista =
                controller.listarProveedores();

        for (Proveedor p : lista) {

            modelo.addRow(
                    new Object[]{
                            p.getId(),
                            p.getTipoIdentificacion(),
                            p.getNumeroIdentificacion(),
                            p.getDv(),
                            p.getNombreRazonSocial(),
                            p.getCiudad(),
                            p.getTelefono(),
                            p.getEmail(),
                            p.getContactoNombre(),
                            p.getContactoCelular(),
                            p.getContactoEmail()
                    }
            );
        }
    }

    // =========================
    // 🔹 NUEVO PROVEEDOR
    // =========================
    private void abrirNuevoProveedor() {

        ProveedorView view =
                new ProveedorView(this);

        view.setController(controller);

        view.setVisible(true);

        cargarProveedores();
    }

    // =========================
    // 🔹 EDITAR PROVEEDOR
    // =========================
    private void editarProveedor() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un proveedor."
            );

            return;
        }

        // 🔥 Obtener ID
        int idProveedor =
                (int) modelo.getValueAt(fila, 0);

        // 🔥 Buscar proveedor completo
        Proveedor proveedor =
                controller.buscarProveedorPorId(
                        idProveedor
                );

        if (proveedor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cargar el proveedor."
            );

            return;
        }

        // 🔥 Abrir formulario
        ProveedorView view =
                new ProveedorView(this);

        view.setController(controller);

        // 🔥 Cargar datos existentes
        view.cargarProveedor(proveedor);

        view.setVisible(true);

        // 🔥 Refrescar tabla
        cargarProveedores();
    }

    // =========================
    // 🔹 CREAR BOTÓN ERP
    // =========================
    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn =
                new JButton(texto);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setBackground(color);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setContentAreaFilled(true);

        btn.setOpaque(true);

        btn.setBorderPainted(false);

        return btn;
    }
}