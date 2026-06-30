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
 * Vista de creación y configuración de Órdenes de Servicio Híbridas.
 * Optimizada para evitar la apertura múltiple de conexiones en cascada.
 */
public class OrdenServicioView extends JFrame {

    private JComboBox<Cliente> comboClientes;
    private JComboBox<DireccionCliente> comboDirecciones;
    private JComboBox<Servicio> comboServicios;
    private JTextField txtCantidadServicio;
    private JButton btnAgregarServicio;
    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidadProducto;
    private JButton btnAgregarProducto;
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

    private final List<DetalleOrdenServicio> listaDetalles = new ArrayList<>();
    private OrdenServicioController controller;

    // 🔥 SOLUCIÓN OPTIMIZADA: Instancias únicas de controladores compartidas por la vista
    private ClienteController controllerCliente;
    private DireccionClienteController dirController;
    private ServicioController servController;
    private InventarioController invController;

    public void setController(OrdenServicioController controller) {
        this.controller = controller;
    }

    public OrdenServicioView() {
        setTitle("F&B Soluciones Integrales - Nueva Orden de Servicio");
        setSize(1150, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Inicializamos los controladores una sola vez aquí
        inicializarControladores();
        inicializarComponentes();
        inicializarDatos();
    }

    /**
     * Prepara los controladores requeridos para los catálogos evitando múltiples news en métodos hijos.
     */
    private void inicializarControladores() {
        this.controllerCliente = new ClienteController();
        this.dirController = new DireccionClienteController();
        this.servController = new ServicioController();
        this.invController = new InventarioController();
    }

    private void inicializarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(12, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Información de la Orden de Servicio"));

        comboClientes = new JComboBox<>();
        comboDirecciones = new JComboBox<>();
        comboServicios = new JComboBox<>();
        comboProductos = new JComboBox<>();

        txtCantidadServicio = new JTextField();
        txtCantidadProducto = new JTextField();

        btnAgregarServicio = new JButton("Agregar Servicio ➕");
        btnAgregarProducto = new JButton("Agregar Producto ➕");

        comboPrioridad = new JComboBox<>();
        comboPrioridad.addItem("Alta");
        comboPrioridad.addItem("Media");
        comboPrioridad.addItem("Baja");

        txtContacto = new JTextField();
        txtTelefono = new JTextField();
        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        panelFormulario.add(new JLabel("Cliente:"));
        panelFormulario.add(comboClientes);
        panelFormulario.add(new JLabel("Dirección Servicio:"));
        panelFormulario.add(comboDirecciones);
        panelFormulario.add(new JLabel("Servicio Técnico:"));
        panelFormulario.add(comboServicios);
        panelFormulario.add(new JLabel("Cantidad Servicio:"));
        panelFormulario.add(txtCantidadServicio);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnAgregarServicio);
        panelFormulario.add(new JLabel("Componente / Producto:"));
        panelFormulario.add(comboProductos);
        panelFormulario.add(new JLabel("Cantidad Producto:"));
        panelFormulario.add(txtCantidadProducto);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnAgregarProducto);
        panelFormulario.add(new JLabel("Fecha Programada:"));
        panelFormulario.add(dateChooser);
        panelFormulario.add(new JLabel("Prioridad Operativa:"));
        panelFormulario.add(comboPrioridad);
        panelFormulario.add(new JLabel("Nombre Contacto:"));
        panelFormulario.add(txtContacto);
        panelFormulario.add(new JLabel("Teléfono Contacto:"));
        panelFormulario.add(txtTelefono);

        add(panelFormulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"Tipo", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(24);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createTitledBorder("Ítems Consolidados en el Detalle"));
        panelCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel();
        btnEliminar = new JButton("Remover Ítem ❌");
        panelAcciones.add(btnEliminar);
        panelCentro.add(panelAcciones, BorderLayout.SOUTH);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        panelBottom.setBorder(BorderFactory.createTitledBorder("Observaciones y Requerimientos de Trabajo"));
        panelBottom.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Crear Orden Servicio");
        btnLimpiar = new JButton("Limpiar Formulario");

        btnRegistrar.setBackground(new Color(0, 153, 76));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBottom.add(panelBotones, BorderLayout.SOUTH);

        add(panelBottom, BorderLayout.SOUTH);

        btnAgregarServicio.addActionListener(e -> agregarServicio());
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarItem());
        btnRegistrar.addActionListener(e -> registrarOrden());
        btnLimpiar.addActionListener(e -> limpiar());
        comboClientes.addActionListener(e -> cargarDireccionesCliente());
        comboDirecciones.addActionListener(e -> cargarContactoDireccion());
    }

    private void inicializarDatos() {
        cargarClientes();
        cargarServicios();
        cargarProductos();
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();
        // 🔥 CORREGIDO: Usa la instancia global compartida
        List<Cliente> lista = controllerCliente.listarClientes();
        for (Cliente c : lista) {
            comboClientes.addItem(c);
        }
        cargarDireccionesCliente();
    }

    private void cargarDireccionesCliente() {
        comboDirecciones.removeAllItems();
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        if (cliente == null) return;

        // 🔥 CORREGIDO: Usa la instancia global compartida
        List<DireccionCliente> lista = dirController.listarPorCliente(cliente.getIdCliente());
        for (DireccionCliente d : lista) {
            comboDirecciones.addItem(d);
        }
        cargarContactoDireccion();
    }

    private void cargarContactoDireccion() {
        DireccionCliente direccion = (DireccionCliente) comboDirecciones.getSelectedItem();
        if (direccion == null) {
            txtContacto.setText("");
            txtTelefono.setText("");
            return;
        }
        txtContacto.setText(direccion.getContactoNombre());
        txtTelefono.setText(direccion.getContactoTelefono());
    }

    private void cargarServicios() {
        comboServicios.removeAllItems();
        // 🔥 CORREGIDO: Usa la instancia global compartida
        List<Servicio> lista = servController.listarActivos();
        for (Servicio s : lista) {
            comboServicios.addItem(s);
        }
    }

    private void cargarProductos() {
        comboProductos.removeAllItems();
        // 🔥 CORREGIDO: Usa la instancia global compartida
        List<Producto> lista = invController.obtenerInventario();
        for (Producto p : lista) {
            comboProductos.addItem(p);
        }
    }

    private void agregarServicio() {
        Servicio servicio = (Servicio) comboServicios.getSelectedItem();
        if (servicio == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un servicio válido.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidadServicio.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            DetalleOrdenServicio detalle = new DetalleOrdenServicio();
            detalle.setTipoItem("SERVICIO");
            detalle.setIdServicio(servicio.getIdServicio());
            detalle.setCodigoReferencia("SERV-" + servicio.getIdServicio());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(servicio.getPrecioBase());

            listaDetalles.add(detalle);

            double subtotal = cantidad * servicio.getPrecioBase();
            modelo.addRow(new Object[]{
                    "SERVICIO",
                    servicio.getNombre(),
                    cantidad,
                    servicio.getPrecioBase(),
                    subtotal
            });

            txtCantidadServicio.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un formato numérico entero y válido para la cantidad.");
        }
    }

    private void agregarProducto() {
        Producto producto = (Producto) comboProductos.getSelectedItem();
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto del catálogo.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidadProducto.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            double precioUnidad = obtenerPrecioProductoDinamico(producto);

            DetalleOrdenServicio detalle = new DetalleOrdenServicio();
            detalle.setTipoItem("PRODUCTO");
            detalle.setIdProducto(producto.getId());

            String codigoRef = producto.getCodigoReferencia();
            if (codigoRef == null || codigoRef.trim().isEmpty()) {
                codigoRef = "PROD-" + producto.getId();
            }
            detalle.setCodigoReferencia(codigoRef);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnidad);

            listaDetalles.add(detalle);

            double subtotal = cantidad * precioUnidad;
            modelo.addRow(new Object[]{
                    "PRODUCTO",
                    producto.getNombre(),
                    cantidad,
                    precioUnidad,
                    subtotal
            });

            txtCantidadProducto.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un formato numérico entero y válido para la cantidad.");
        }
    }

    private double obtenerPrecioProductoDinamico(Producto producto) {
        try {
            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void eliminarItem() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para descartar.");
            return;
        }
        modelo.removeRow(fila);
        listaDetalles.remove(fila);
    }

    private void registrarOrden() {
        try {
            Cliente cliente = (Cliente) comboClientes.getSelectedItem();
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Es mandatorio seleccionar un cliente.");
                return;
            }
            if (listaDetalles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No puede generar una orden sin ítems en el detalle.");
                return;
            }
            if (dateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Establezca la fecha programada de atención técnica.");
                return;
            }

            DireccionCliente direccion = (DireccionCliente) comboDirecciones.getSelectedItem();
            String direccionServicio = (direccion != null) ? direccion.getDireccion() : "";

            Date fechaProgramada = new Date(dateChooser.getDate().getTime());

            if (controller == null) {
                JOptionPane.showMessageDialog(this, "Error de vinculación: Capa de control no asignada en la vista.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String resultado = controller.crearOrdenServicio(
                    cliente.getIdCliente(),
                    fechaProgramada,
                    comboPrioridad.getSelectedItem().toString(),
                    direccionServicio,
                    txtContacto.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtObservaciones.getText().trim(),
                    listaDetalles
            );

            if ("OK".equals(resultado)) {
                JOptionPane.showMessageDialog(this, "Orden de Servicio registrada con éxito en el sistema.");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Aviso del Sistema", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("Fallo crítico en la interfaz de usuario: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Surgió un inconveniente imprevisto al intentar procesar la orden.");
        }
    }

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