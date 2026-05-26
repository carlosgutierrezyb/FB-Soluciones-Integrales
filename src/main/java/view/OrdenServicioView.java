package view;

import controller.ClienteController;
import controller.DireccionClienteController;
import controller.OrdenServicioController;
import controller.ServicioController;
import model.Cliente;
import model.DetalleOrdenServicio;
import model.DireccionCliente;
import model.Servicio;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista creación órdenes servicio.
 *
 * 🔥 ERP F&B:
 * - Crear OS
 * - Agregar servicios
 * - Programación
 * - Direcciones múltiples
 */
public class OrdenServicioView extends JFrame {

    // =========================
    // CONTROLES
    // =========================

    private JComboBox<Cliente> comboClientes;

    private JComboBox<DireccionCliente>
            comboDirecciones;

    private JComboBox<Servicio> comboServicios;

    private JComboBox<String> comboPrioridad;

    private JTextField txtCantidad;

    private JTextField txtContacto;

    private JTextField txtTelefono;

    private JTextArea txtObservaciones;

    private JDateChooser dateChooser;

    private JTable tabla;

    private DefaultTableModel modelo;

    private JButton btnAgregar;

    private JButton btnEliminar;

    private JButton btnRegistrar;

    private JButton btnLimpiar;

    // =========================
    // DATA
    // =========================

    private final List<DetalleOrdenServicio>
            listaDetalles =
            new ArrayList<>();

    private OrdenServicioController controller;

    // =========================
    // SET CONTROLLER
    // =========================

    public void setController(
            OrdenServicioController controller
    ) {

        this.controller = controller;
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public OrdenServicioView() {

        setTitle(
                "F&B Soluciones Integrales - Orden Servicio"
        );

        setSize(1050, 720);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new BorderLayout(10, 10)
        );

        inicializarComponentes();

        inicializarDatos();
    }

    // =========================
    // COMPONENTES
    // =========================

    private void inicializarComponentes() {

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                9,
                                2,
                                10,
                                10
                        )
                );

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Información Orden Servicio"
                )
        );

        comboClientes =
                new JComboBox<>();

        comboDirecciones =
                new JComboBox<>();

        comboServicios =
                new JComboBox<>();

        comboPrioridad =
                new JComboBox<>();

        comboPrioridad.addItem("Alta");
        comboPrioridad.addItem("Media");
        comboPrioridad.addItem("Baja");

        txtCantidad =
                new JTextField();

        txtContacto =
                new JTextField();

        txtTelefono =
                new JTextField();

        txtObservaciones =
                new JTextArea(4, 20);

        txtObservaciones.setLineWrap(true);

        txtObservaciones.setWrapStyleWord(true);

        // 🔥 CALENDARIO
        dateChooser =
                new JDateChooser();

        dateChooser.setDateFormatString(
                "yyyy-MM-dd"
        );

        btnAgregar =
                new JButton(
                        "Agregar Servicio ➕"
                );

        btnEliminar =
                new JButton(
                        "Eliminar ❌"
                );

        // =========================
        // FORMULARIO
        // =========================

        panelFormulario.add(
                new JLabel("Cliente:")
        );

        panelFormulario.add(comboClientes);

        panelFormulario.add(
                new JLabel("Dirección Servicio:")
        );

        panelFormulario.add(comboDirecciones);

        panelFormulario.add(
                new JLabel("Servicio:")
        );

        panelFormulario.add(comboServicios);

        panelFormulario.add(
                new JLabel("Cantidad:")
        );

        panelFormulario.add(txtCantidad);

        panelFormulario.add(
                new JLabel("Fecha Programada:")
        );

        panelFormulario.add(dateChooser);

        panelFormulario.add(
                new JLabel("Prioridad:")
        );

        panelFormulario.add(comboPrioridad);

        panelFormulario.add(
                new JLabel("Nombre Contacto:")
        );

        panelFormulario.add(txtContacto);

        panelFormulario.add(
                new JLabel("Teléfono Contacto:")
        );

        panelFormulario.add(txtTelefono);

        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        // =========================
        // TABLA
        // =========================

        modelo =
                new DefaultTableModel(
                        new Object[]{
                                "Servicio",
                                "Cantidad"
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

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelCentro.setBorder(
                BorderFactory.createTitledBorder(
                        "Servicios Agregados"
                )
        );

        panelCentro.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        JPanel panelAcciones =
                new JPanel();

        panelAcciones.add(btnAgregar);

        panelAcciones.add(btnEliminar);

        panelCentro.add(
                panelAcciones,
                BorderLayout.SOUTH
        );

        add(
                panelCentro,
                BorderLayout.CENTER
        );

        // =========================
        // PANEL INFERIOR
        // =========================

        JPanel panelBottom =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelBottom.setBorder(
                BorderFactory.createTitledBorder(
                        "Observaciones"
                )
        );

        panelBottom.add(
                new JScrollPane(txtObservaciones),
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel();

        btnRegistrar =
                new JButton(
                        "Crear Orden Servicio"
                );

        btnLimpiar =
                new JButton(
                        "Limpiar"
                );

        btnRegistrar.setBackground(
                new Color(0, 153, 76)
        );

        btnRegistrar.setForeground(
                Color.WHITE
        );

        btnRegistrar.setFocusPainted(false);

        btnRegistrar.setOpaque(true);

        btnRegistrar.setBorderPainted(false);

        panelBotones.add(btnRegistrar);

        panelBotones.add(btnLimpiar);

        panelBottom.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(
                panelBottom,
                BorderLayout.SOUTH
        );

        // =========================
        // EVENTOS
        // =========================

        btnAgregar.addActionListener(
                e -> agregarServicio()
        );

        btnEliminar.addActionListener(
                e -> eliminarServicio()
        );

        btnRegistrar.addActionListener(
                e -> registrarOrden()
        );

        btnLimpiar.addActionListener(
                e -> limpiar()
        );

        // 🔥 AUTO CARGA DIRECCIONES
        comboClientes.addActionListener(
                e -> cargarDireccionesCliente()
        );

        // 🔥 AUTO COMPLETAR CONTACTO
        comboDirecciones.addActionListener(
                e -> cargarContactoDireccion()
        );
    }

    // =========================
    // DATOS
    // =========================

    private void inicializarDatos() {

        cargarClientes();

        cargarServicios();
    }

    // =========================
    // CLIENTES
    // =========================

    private void cargarClientes() {

        comboClientes.removeAllItems();

        ClienteController controllerCliente =
                new ClienteController();

        List<Cliente> lista =
                controllerCliente.listarClientes();

        for (Cliente c : lista) {

            comboClientes.addItem(c);
        }

        cargarDireccionesCliente();
    }

    // =========================
    // DIRECCIONES
    // =========================

    private void cargarDireccionesCliente() {

        comboDirecciones.removeAllItems();

        Cliente cliente =
                (Cliente)
                        comboClientes.getSelectedItem();

        if (cliente == null) {

            return;
        }

        DireccionClienteController controller =
                new DireccionClienteController();

        List<DireccionCliente> lista =
                controller.listarPorCliente(
                        cliente.getIdCliente()
                );

        for (DireccionCliente d : lista) {

            comboDirecciones.addItem(d);
        }

        cargarContactoDireccion();
    }

    // =========================
    // CONTACTOS
    // =========================

    private void cargarContactoDireccion() {

        DireccionCliente direccion =
                (DireccionCliente)
                        comboDirecciones.getSelectedItem();

        if (direccion == null) {

            txtContacto.setText("");

            txtTelefono.setText("");

            return;
        }

        txtContacto.setText(
                direccion.getContactoNombre()
        );

        txtTelefono.setText(
                direccion.getContactoTelefono()
        );
    }

    // =========================
    // SERVICIOS
    // =========================

    private void cargarServicios() {

        comboServicios.removeAllItems();

        ServicioController controller =
                new ServicioController();

        List<Servicio> lista =
                controller.listarActivos();

        for (Servicio s : lista) {

            comboServicios.addItem(s);
        }
    }

    // =========================
    // AGREGAR SERVICIO
    // =========================

    private void agregarServicio() {

        Servicio servicio =
                (Servicio)
                        comboServicios.getSelectedItem();

        if (servicio == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un servicio."
            );

            return;
        }

        try {

            int cantidad =
                    Integer.parseInt(
                            txtCantidad.getText().trim()
                    );

            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cantidad inválida."
                );

                return;
            }

            modelo.addRow(
                    new Object[]{
                            servicio.getNombre(),
                            cantidad
                    }
            );

            DetalleOrdenServicio detalle =
                    new DetalleOrdenServicio();

            detalle.setIdServicio(
                    servicio.getIdServicio()
            );

            detalle.setNombreServicio(
                    servicio.getNombre()
            );

            detalle.setCantidad(cantidad);

            listaDetalles.add(detalle);

            txtCantidad.setText("");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cantidad inválida."
            );
        }
    }

    // =========================
    // ELIMINAR
    // =========================

    private void eliminarServicio() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una fila."
            );

            return;
        }

        modelo.removeRow(fila);

        listaDetalles.remove(fila);
    }

    // =========================
    // REGISTRAR
    // =========================

    private void registrarOrden() {

        try {

            Cliente cliente =
                    (Cliente)
                            comboClientes.getSelectedItem();

            if (cliente == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione cliente."
                );

                return;
            }

            if (listaDetalles.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe agregar servicios."
                );

                return;
            }

            if (dateChooser.getDate() == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione fecha."
                );

                return;
            }

            DireccionCliente direccion =
                    (DireccionCliente)
                            comboDirecciones.getSelectedItem();

            String direccionServicio = "";

            if (direccion != null) {

                direccionServicio =
                        direccion.getDireccion();
            }

            Date fechaProgramada =
                    new Date(
                            dateChooser
                                    .getDate()
                                    .getTime()
                    );

            String resultado =
                    controller.crearOrdenServicio(
                            cliente.getIdCliente(),
                            fechaProgramada,
                            comboPrioridad
                                    .getSelectedItem()
                                    .toString(),
                            direccionServicio,
                            txtContacto
                                    .getText()
                                    .trim(),
                            txtTelefono
                                    .getText()
                                    .trim(),
                            txtObservaciones
                                    .getText()
                                    .trim(),
                            listaDetalles
                    );

            if ("OK".equals(resultado)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Orden creada correctamente."
                );

                limpiar();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        resultado
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error registrando orden."
            );
        }
    }

    // =========================
    // LIMPIAR
    // =========================

    private void limpiar() {

        txtCantidad.setText("");

        txtContacto.setText("");

        txtTelefono.setText("");

        txtObservaciones.setText("");

        dateChooser.setDate(null);

        modelo.setRowCount(0);

        listaDetalles.clear();

        if (comboPrioridad.getItemCount() > 0) {

            comboPrioridad.setSelectedIndex(0);
        }
    }
}