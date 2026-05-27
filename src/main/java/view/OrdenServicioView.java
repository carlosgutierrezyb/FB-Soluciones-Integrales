package view;

import controller.ClienteController;
import controller.DireccionClienteController;
import controller.InventarioController;
import controller.OrdenServicioController;
import controller.ServicioController;
import model.Cliente;
import model.DetalleOrdenServicio;
import model.DireccionCliente;
import model.Producto;
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
 * - Agregar productos
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

    // 🔥 SERVICIOS
    private JComboBox<Servicio> comboServicios;

    private JTextField txtCantidadServicio;

    private JButton btnAgregarServicio;

    // 🔥 PRODUCTOS
    private JComboBox<Producto> comboProductos;

    private JTextField txtCantidadProducto;

    private JButton btnAgregarProducto;

    // 🔥 GENERALES
    private JComboBox<String> comboPrioridad;

    private JTextField txtContacto;

    private JTextField txtTelefono;

    private JTextArea txtObservaciones;

    private JDateChooser dateChooser;

    private JTable tabla;

    private DefaultTableModel modelo;

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
    // CONTROLLER
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

        setSize(1150, 760);

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
    // UI
    // =========================

    private void inicializarComponentes() {

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                12,
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

        // =========================
        // COMPONENTES
        // =========================

        comboClientes =
                new JComboBox<>();

        comboDirecciones =
                new JComboBox<>();

        comboServicios =
                new JComboBox<>();

        comboProductos =
                new JComboBox<>();

        txtCantidadServicio =
                new JTextField();

        txtCantidadProducto =
                new JTextField();

        btnAgregarServicio =
                new JButton(
                        "Agregar Servicio ➕"
                );

        btnAgregarProducto =
                new JButton(
                        "Agregar Producto ➕"
                );

        comboPrioridad =
                new JComboBox<>();

        comboPrioridad.addItem("Alta");
        comboPrioridad.addItem("Media");
        comboPrioridad.addItem("Baja");

        txtContacto =
                new JTextField();

        txtTelefono =
                new JTextField();

        txtObservaciones =
                new JTextArea(4, 20);

        txtObservaciones.setLineWrap(true);

        txtObservaciones.setWrapStyleWord(true);

        dateChooser =
                new JDateChooser();

        dateChooser.setDateFormatString(
                "yyyy-MM-dd"
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

        // =========================
        // SERVICIOS
        // =========================

        panelFormulario.add(
                new JLabel("Servicio:")
        );

        panelFormulario.add(comboServicios);

        panelFormulario.add(
                new JLabel("Cantidad Servicio:")
        );

        panelFormulario.add(txtCantidadServicio);

        panelFormulario.add(
                new JLabel("")
        );

        panelFormulario.add(btnAgregarServicio);

        // =========================
        // PRODUCTOS
        // =========================

        panelFormulario.add(
                new JLabel("Producto:")
        );

        panelFormulario.add(comboProductos);

        panelFormulario.add(
                new JLabel("Cantidad Producto:")
        );

        panelFormulario.add(txtCantidadProducto);

        panelFormulario.add(
                new JLabel("")
        );

        panelFormulario.add(btnAgregarProducto);

        // =========================
        // FECHA
        // =========================

        panelFormulario.add(
                new JLabel("Fecha Programada:")
        );

        panelFormulario.add(dateChooser);

        // =========================
        // PRIORIDAD
        // =========================

        panelFormulario.add(
                new JLabel("Prioridad:")
        );

        panelFormulario.add(comboPrioridad);

        // =========================
        // CONTACTO
        // =========================

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
                                "Tipo",
                                "Descripción",
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
                        "Items Agregados"
                )
        );

        panelCentro.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        JPanel panelAcciones =
                new JPanel();

        btnEliminar =
                new JButton(
                        "Eliminar ❌"
                );

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

        btnAgregarServicio.addActionListener(
                e -> agregarServicio()
        );

        btnAgregarProducto.addActionListener(
                e -> agregarProducto()
        );

        btnEliminar.addActionListener(
                e -> eliminarItem()
        );

        btnRegistrar.addActionListener(
                e -> registrarOrden()
        );

        btnLimpiar.addActionListener(
                e -> limpiar()
        );

        comboClientes.addActionListener(
                e -> cargarDireccionesCliente()
        );

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

        cargarProductos();
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
    // CONTACTO
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
    // PRODUCTOS
    // =========================

    private void cargarProductos() {

        comboProductos.removeAllItems();

        InventarioController controller =
                new InventarioController();

        List<Producto> lista =
                controller.obtenerInventario();

        for (Producto p : lista) {

            comboProductos.addItem(p);
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
                            txtCantidadServicio
                                    .getText()
                                    .trim()
                    );

            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cantidad inválida."
                );

                return;
            }

            DetalleOrdenServicio detalle =
                    new DetalleOrdenServicio();

            detalle.setTipoItem(
                    "SERVICIO"
            );

            detalle.setIdServicio(
                    servicio.getIdServicio()
            );

            detalle.setNombreReferencia(
                    servicio.getNombre()
            );

            detalle.setCantidad(
                    cantidad
            );

            listaDetalles.add(detalle);

            modelo.addRow(
                    new Object[]{
                            "SERVICIO",
                            servicio.getNombre(),
                            cantidad
                    }
            );

            txtCantidadServicio.setText("");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cantidad inválida."
            );
        }
    }

    // =========================
    // AGREGAR PRODUCTO
    // =========================

    private void agregarProducto() {

        Producto producto =
                (Producto)
                        comboProductos.getSelectedItem();

        if (producto == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );

            return;
        }

        try {

            int cantidad =
                    Integer.parseInt(
                            txtCantidadProducto
                                    .getText()
                                    .trim()
                    );

            if (cantidad <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cantidad inválida."
                );

                return;
            }

            DetalleOrdenServicio detalle =
                    new DetalleOrdenServicio();

            detalle.setTipoItem(
                    "PRODUCTO"
            );

            detalle.setIdProducto(
                    producto.getId()
            );

            detalle.setNombreReferencia(
                    producto.getNombre()
            );

            detalle.setCantidad(
                    cantidad
            );

            listaDetalles.add(detalle);

            modelo.addRow(
                    new Object[]{
                            "PRODUCTO",
                            producto.getNombre(),
                            cantidad
                    }
            );

            txtCantidadProducto.setText("");

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

    private void eliminarItem() {

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
                        "Debe agregar items."
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

        txtCantidadServicio.setText("");

        txtCantidadProducto.setText("");

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