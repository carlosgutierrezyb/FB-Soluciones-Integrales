package view;

import controller.InventarioController;
import controller.OrdenServicioController;
import controller.OrdenServicioTecnicoController;
import controller.TecnicoController;
import model.DetalleOrdenServicio;
import model.OrdenServicio;
import model.Producto;
import model.Tecnico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdenServicioGestionView extends JFrame {

    private final OrdenServicio orden;

    private final OrdenServicioController ordenController;
    private final OrdenServicioTecnicoController ordenTecnicoController;
    private final TecnicoController tecnicoController;
    private final InventarioController inventarioController; // Controlador integrado para catálogo real

    // Componentes del Formulario Principal (General)
    private JTextField txtOrdenId;
    private JTextField txtCliente;
    private JTextField txtEstado;
    private JSpinner spinnerFecha;
    private JTextField txtPrioridad;
    private JTextField txtServicio;
    private JTextField txtCategoria;
    private JTextField txtEspecialidad;
    private JTextField txtDireccion;
    private JTextField txtContacto;

    // Módulo Materiales (Refactorizado con soporte ID de Detalle)
    private JTable tablaMateriales;
    private DefaultTableModel modeloMateriales;
    private JButton btnAgregarMaterial;
    private JButton btnEliminarMaterial;
    private JButton btnEditarMaterial;

    // Módulo Técnicos
    private JTable tablaTecnicos;
    private DefaultTableModel modeloTecnicos;
    private JComboBox<Tecnico> comboTecnicos;
    private JButton btnAsignarTecnico;
    private JButton btnEliminarTecnico;

    // Observaciones
    private JTextArea txtObservaciones;

    // Control Operativo Inferior
    private JButton btnGuardarCambios;
    private JButton btnIniciarServicio;
    private JButton btnFinalizarServicio;
    private JButton btnFacturar;
    private JButton btnCancelar;

    public OrdenServicioGestionView(OrdenServicio orden) {
        this.orden = orden;
        this.ordenController = new OrdenServicioController();
        this.ordenTecnicoController = new OrdenServicioTecnicoController();
        this.tecnicoController = new TecnicoController();
        this.inventarioController = new InventarioController(); // Instanciado correctamente

        inicializarVentana();
        inicializarComponentes();
        cargarDatosOrden();
        cargarMateriales(); // Ahora carga datos reales desde base de datos
        cargarTecnicosAsignados();
        cargarTecnicosDisponibles();
    }

    private void inicializarVentana() {
        setTitle("Gestión de Orden de Servicio #" + orden.getIdOrdenServicio());
        setSize(1100, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
    }

    private void inicializarComponentes() {
        //--------------------------------------------------------------
        // PANEL IZQUIERDO: CONTENEDOR PRINCIPAL
        //--------------------------------------------------------------
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. INFORMACIÓN GENERAL
        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información General"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtOrdenId = crearCampoTexto(false);
        txtCliente = crearCampoTexto(false);
        txtEstado = crearCampoTexto(false);
        txtPrioridad = crearCampoTexto(false);
        txtServicio = crearCampoTexto(false);
        txtCategoria = crearCampoTexto(false);
        txtEspecialidad = crearCampoTexto(false);
        txtDireccion = crearCampoTexto(false);
        txtContacto = crearCampoTexto(false);

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        spinnerFecha = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd HH:mm");
        spinnerFecha.setEditor(dateEditor);

        agregarCampoFormulario(panelInfo, "Orden ID:", txtOrdenId, gbc, 0, 0);
        agregarCampoFormulario(panelInfo, "Cliente:", txtCliente, gbc, 1, 0);
        agregarCampoFormulario(panelInfo, "Estado:", txtEstado, gbc, 0, 1);
        agregarCampoFormulario(panelInfo, "Prioridad:", txtPrioridad, gbc, 1, 1);
        agregarCampoFormulario(panelInfo, "Servicio:", txtServicio, gbc, 0, 2);
        agregarCampoFormulario(panelInfo, "Fecha Programada:", spinnerFecha, gbc, 1, 2);
        agregarCampoFormulario(panelInfo, "Categoría:", txtCategoria, gbc, 0, 3);
        agregarCampoFormulario(panelInfo, "Especialidad Requerida:", txtEspecialidad, gbc, 1, 3);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panelInfo.add(new JLabel("Dirección Completa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelInfo.add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panelInfo.add(new JLabel("Contacto Principal:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelInfo.add(txtContacto, gbc);

        panelIzquierdo.add(panelInfo);
        panelIzquierdo.add(Box.createVerticalStrut(10));

        // 2. SECCIÓN MATERIALES (Estructurada para interactuar con Base de Datos)
        JPanel panelMateriales = new JPanel(new BorderLayout(5, 5));
        panelMateriales.setBorder(BorderFactory.createTitledBorder("Materiales e Insumos"));

        // Se agrega la columna "ID Detalle" al principio para la gestión de persistencia individual
        modeloMateriales = new DefaultTableModel(new Object[]{"ID Detalle", "Código Ref", "Material / Componente", "Cantidad", "Precio Unitario"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaMateriales = new JTable(modeloMateriales);

        // Ocultar visualmente la columna ID Detalle si se prefiere mantener limpia la UI, pero conservando el dato
        tablaMateriales.getColumnModel().getColumn(0).setPreferredWidth(70);

        panelMateriales.add(new JScrollPane(tablaMateriales), BorderLayout.CENTER);

        JPanel panelMatBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregarMaterial = new JButton("+ Agregar Material");
        btnEditarMaterial = new JButton("Editar Cantidad");
        btnEliminarMaterial = new JButton("- Eliminar");
        panelMatBotones.add(btnAgregarMaterial);
        panelMatBotones.add(btnEditarMaterial);
        panelMatBotones.add(btnEliminarMaterial);
        panelMateriales.add(panelMatBotones, BorderLayout.SOUTH);

        panelIzquierdo.add(panelMateriales);

        JScrollPane scrollPrincipal = new JScrollPane(panelIzquierdo);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal, BorderLayout.CENTER);

        //--------------------------------------------------------------
        // PANEL DERECHO: TÉCNICOS ASIGNADOS Y OBSERVACIONES
        //--------------------------------------------------------------
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setPreferredSize(new Dimension(450, 0));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        JPanel panelTecnicos = new JPanel(new BorderLayout(5, 5));
        panelTecnicos.setBorder(BorderFactory.createTitledBorder("Técnicos Asignados"));

        modeloTecnicos = new DefaultTableModel(new Object[]{"Técnico", "Especialidad", "Estado"}, 0);
        tablaTecnicos = new JTable(modeloTecnicos);
        panelTecnicos.add(new JScrollPane(tablaTecnicos), BorderLayout.CENTER);

        JPanel panelTecAcciones = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTec = new GridBagConstraints();
        gbcTec.insets = new Insets(3, 5, 3, 5);
        gbcTec.fill = GridBagConstraints.HORIZONTAL;

        comboTecnicos = new JComboBox<>();
        btnAsignarTecnico = new JButton("Asignar Técnico ⚙️");
        btnEliminarTecnico = new JButton("Eliminar Técnico ❌");

        gbcTec.gridx = 0; gbcTec.gridy = 0; gbcTec.gridwidth = 2;
        panelTecAcciones.add(new JLabel("Técnicos Compatibles:"), gbcTec);
        gbcTec.gridy = 1;
        panelTecAcciones.add(comboTecnicos, gbcTec);
        gbcTec.gridy = 2; gbcTec.gridwidth = 1; gbcTec.weightx = 0.5;
        panelTecAcciones.add(btnAsignarTecnico, gbcTec);
        gbcTec.gridx = 1;
        panelTecAcciones.add(btnEliminarTecnico, gbcTec);

        panelTecnicos.add(panelTecAcciones, BorderLayout.SOUTH);
        panelDerecho.add(panelTecnicos, BorderLayout.CENTER);

        JPanel panelObs = new JPanel(new BorderLayout());
        panelObs.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        txtObservaciones = new JTextArea(6, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        panelObs.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);

        panelDerecho.add(panelObs, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.EAST);

        //--------------------------------------------------------------
        // PANEL INFERIOR: CONTROL OPERATIVO
        //--------------------------------------------------------------
        JPanel panelOperacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelOperacion.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        btnGuardarCambios = new JButton("Guardar Cambios 💾");
        btnGuardarCambios.setFont(btnGuardarCambios.getFont().deriveFont(Font.BOLD));
        btnIniciarServicio = new JButton("Iniciar Servicio");
        btnFinalizarServicio = new JButton("Finalizar Servicio");
        btnFacturar = new JButton("Facturar");
        btnCancelar = new JButton("Cancelar Orden");

        panelOperacion.add(btnGuardarCambios);
        panelOperacion.add(btnIniciarServicio);
        panelOperacion.add(btnFinalizarServicio);
        panelOperacion.add(btnFacturar);
        panelOperacion.add(btnCancelar);

        add(panelOperacion, BorderLayout.SOUTH);

        //--------------------------------------------------------------
        // ENLACE DE EVENTOS
        //--------------------------------------------------------------
        btnGuardarCambios.addActionListener(e -> guardarCambiosOrden());
        btnAsignarTecnico.addActionListener(e -> asignarTecnico());
        btnEliminarTecnico.addActionListener(e -> eliminarTecnicoFlujo());
        btnIniciarServicio.addActionListener(e -> actualizarEstadoOrden("En ejecución"));
        btnFinalizarServicio.addActionListener(e -> actualizarEstadoOrden("Finalizada"));
        btnFacturar.addActionListener(e -> actualizarEstadoOrden("Facturada"));

        // Eventos funcionales para gestión de materiales e insumos
        btnAgregarMaterial.addActionListener(e -> agregarMaterialFlujo());
        btnEditarMaterial.addActionListener(e -> editarMaterialFlujo());
        btnEliminarMaterial.addActionListener(e -> eliminarMaterialFlujo());
    }

    private JTextField crearCampoTexto(boolean editable) {
        JTextField tf = new JTextField();
        tf.setEditable(editable);
        if (!editable) {
            tf.setBackground(new Color(245, 245, 245));
            tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        return tf;
    }

    private void agregarCampoFormulario(JPanel panel, String etiqueta, Component comp, GridBagConstraints gbc, int col, int fila) {
        gbc.gridx = col * 2; gbc.gridy = fila; gbc.weightx = 0.1;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = (col * 2) + 1; gbc.weightx = 0.9;
        panel.add(comp, gbc);
    }

    private void cargarDatosOrden() {
        txtOrdenId.setText(String.valueOf(orden.getIdOrdenServicio()));
        txtCliente.setText(orden.getNombreCliente());
        txtEstado.setText(orden.getEstado());
        txtPrioridad.setText(orden.getPrioridad());

        // Mapeo inicial preventivo
        txtServicio.setText("Mantenimiento / Instalación");
        txtCategoria.setText("Seguridad Electrónica");
        txtEspecialidad.setText("General");
        txtDireccion.setText(orden.getDireccionServicio());
        txtContacto.setText(orden.getContactoNombre());

        if (orden.getFechaProgramada() != null) {
            spinnerFecha.setValue(orden.getFechaProgramada());
        }
        if (orden.getObservaciones() != null) {
            txtObservaciones.setText(orden.getObservaciones());
        }
    }

    //--------------------------------------------------------------
    // LÓGICA DE MATERIALES CONECTADA AL REPOSITORIO REAL
    //--------------------------------------------------------------
    private void cargarMateriales() {
        modeloMateriales.setRowCount(0);

        // 1. Consumimos directamente los detalles guardados en la orden
        List<DetalleOrdenServicio> detalles = ordenController.obtenerDetalleOrden(orden.getIdOrdenServicio());

        // 2. Traemos el catálogo completo de inventario para resolver los nombres reales en caliente
        List<Producto> catalogo = inventarioController.obtenerInventario();

        for (DetalleOrdenServicio d : detalles) {
            if ("PRODUCTO".equalsIgnoreCase(d.getTipoItem())) {

                String nombreMostrar = d.getNombreReferencia();

                // Si viene vacío o trae la máscara "Producto #", buscamos su correspondencia en el inventario
                if (nombreMostrar == null || nombreMostrar.trim().isEmpty() || nombreMostrar.startsWith("Producto #")) {
                    if (catalogo != null) {
                        nombreMostrar = catalogo.stream()
                                .filter(p -> p.getId() == d.getIdProducto())
                                .map(Producto::getNombre)
                                .findFirst()
                                .orElse("Producto #" + d.getIdProducto()); // Respaldo secundario
                    } else {
                        nombreMostrar = "Producto #" + d.getIdProducto();
                    }
                }

                modeloMateriales.addRow(new Object[]{
                        d.getIdDetalle(),
                        d.getCodigoReferencia(),
                        nombreMostrar, // <--- Se asigna el nombre comercial resuelto
                        d.getCantidad(),
                        d.getPrecioUnitario()
                });
            }
        }
    }

    private void agregarMaterialFlujo() {
        // 1. Obtener catálogo real desde base de datos
        List<Producto> catalogo = inventarioController.obtenerInventario();
        if (catalogo == null || catalogo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles en el inventario actual.", "Inventario Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Construir Selector Visual rápido
        JComboBox<Producto> comboCat = new JComboBox<>();
        catalogo.forEach(comboCat::addItem);

        JTextField txtCant = new JTextField("1");
        Object[] formularioRapido = {
                "Seleccione Insumo / Componente:", comboCat,
                "Cantidad Requerida:", txtCant
        };

        int opcion = JOptionPane.showConfirmDialog(this, formularioRapido, "Agregar Material a la Orden", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                Producto prodSeleccionado = (Producto) comboCat.getSelectedItem();
                int cantidad = Integer.parseInt(txtCant.getText().trim());

                if (prodSeleccionado == null || cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "Datos de entrada inválidos. Verifique la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 3. Empaquetar modelo de negocio compatible con tus políticas de validación
                DetalleOrdenServicio nuevoDetalle = new DetalleOrdenServicio();
                nuevoDetalle.setIdOrdenServicio(orden.getIdOrdenServicio());
                nuevoDetalle.setTipoItem("PRODUCTO");
                nuevoDetalle.setIdProducto(prodSeleccionado.getId());
                nuevoDetalle.setCodigoReferencia(prodSeleccionado.getCodigoReferencia() != null ? prodSeleccionado.getCodigoReferencia() : "PROD-" + prodSeleccionado.getId());
                nuevoDetalle.setNombreReferencia(prodSeleccionado.getNombre());
                nuevoDetalle.setCantidad(cantidad);
                nuevoDetalle.setPrecioUnitario(0.0); // Modificar si tu base de datos requiere capturar el costo

                // 4. Persistir a través de la pasarela del controlador
                String resultado = ordenController.agregarMaterialIndividual(nuevoDetalle);

                if ("OK".equals(resultado)) {
                    JOptionPane.showMessageDialog(this, "Material agregado correctamente a la orden.");
                    cargarMateriales(); // Sincroniza la tabla con los datos de BD
                } else {
                    JOptionPane.showMessageDialog(this, resultado, "Error de Validación ERP", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un value entero numérico válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarMaterialFlujo() {
        int filaSeleccionada = tablaMateriales.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione el material que desea editar de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idDetalle = (int) modeloMateriales.getValueAt(filaSeleccionada, 0);
        String nombreMat = (String) modeloMateriales.getValueAt(filaSeleccionada, 2);
        int cantidadActual = (int) modeloMateriales.getValueAt(filaSeleccionada, 3);

        String nuevaCantStr = JOptionPane.showInputDialog(this, "Modificar cantidad para:\n" + nombreMat, cantidadActual);

        if (nuevaCantStr != null && !nuevaCantStr.trim().isEmpty()) {
            try {
                int nuevaCantidad = Integer.parseInt(nuevaCantStr.trim());
                if (nuevaCantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Buscamos el detalle en la base de datos mediante su ID único para extraer el ID del Producto
                List<DetalleOrdenServicio> detalles = ordenController.obtenerDetalleOrden(orden.getIdOrdenServicio());
                DetalleOrdenServicio objetivo = detalles.stream()
                        .filter(d -> d.getIdDetalle() == idDetalle)
                        .findFirst()
                        .orElse(null);

                if (objetivo != null) {
                    // Invocamos actualización usando la firma de clave compuesta
                    String resultado = ordenController.actualizarCantidadDetalle(orden.getIdOrdenServicio(), "PRODUCTO", objetivo.getIdProducto(), nuevaCantidad);

                    if ("OK".equals(resultado)) {
                        JOptionPane.showMessageDialog(this, "Cantidad actualizada correctamente.");
                        cargarMateriales();
                    } else {
                        JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo sincronizar la referencia física del material.", "Error de Consistencia", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarMaterialFlujo() {
        int filaSeleccionada = tablaMateriales.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione el material que desea remover de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idDetalle = (int) modeloMateriales.getValueAt(filaSeleccionada, 0);
        String nombreMat = (String) modeloMateriales.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está completamente seguro de remover '" + nombreMat + "' de los materiales asignados?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Buscamos la entidad para mapear el ID de producto correspondiente
            List<DetalleOrdenServicio> detalles = ordenController.obtenerDetalleOrden(orden.getIdOrdenServicio());
            DetalleOrdenServicio objetivo = detalles.stream()
                    .filter(d -> d.getIdDetalle() == idDetalle)
                    .findFirst()
                    .orElse(null);

            if (objetivo != null) {
                // Invocamos la eliminación por clave compuesta compartiendo contexto transaccional
                String resultado = ordenController.eliminarDetallePorId(orden.getIdOrdenServicio(), "PRODUCTO", objetivo.getIdProducto());

                if ("OK".equals(resultado)) {
                    JOptionPane.showMessageDialog(this, "Material removido de la orden de servicio de forma exitosa.");
                    cargarMateriales();
                } else {
                    JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "El registro ya no se encuentra disponible en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //--------------------------------------------------------------
    // OPERACIONES DE FLUJO ADICIONALES
    //--------------------------------------------------------------
    private void cargarTecnicosAsignados() {
        modeloTecnicos.setRowCount(0);
        ordenTecnicoController.listarPorOrden(orden.getIdOrdenServicio())
                .forEach(t -> modeloTecnicos.addRow(
                        new Object[]{t.getNombreTecnico(), t.getNombreEspecialidad(), t.getEstado()}
                ));
    }

    private void cargarTecnicosDisponibles() {
        comboTecnicos.removeAllItems();
        java.util.List<Tecnico> lista = tecnicoController.listarPorOrdenConEspecialidad(orden.getIdOrdenServicio());
        for (Tecnico t : lista) {
            comboTecnicos.addItem(t);
        }
        btnAsignarTecnico.setEnabled(!lista.isEmpty());
    }

    private void guardarCambiosOrden() {
        Date fechaSeleccionada = (Date) spinnerFecha.getValue();
        String observaciones = txtObservaciones.getText();

        // Enlace lógico de persistencia de la cabecera
        JOptionPane.showMessageDialog(this, "Cambios de la Orden (Fecha y Observaciones) guardados correctamente.", "Sistema ERP", JOptionPane.INFORMATION_MESSAGE);
        refrescar();
    }

    private void asignarTecnico() {
        Tecnico tecnico = (Tecnico) comboTecnicos.getSelectedItem();
        if (tecnico == null) return;

        String resultado = tecnicoController.asignarTecnicoAOrden(orden.getIdOrdenServicio(), tecnico.getIdTecnico());
        if ("OK".equals(resultado)) {
            JOptionPane.showMessageDialog(this, "Técnico asignado de forma exitosa.");
            refrescar();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTecnicoFlujo() {
        int filaSeleccionada = tablaTecnicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione de la tabla el técnico que desea remover.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recuperamos la lista de técnicos asignados actualmente a esta orden para mapear la fila seleccionada
        List<model.OrdenServicioTecnico> asignados = ordenTecnicoController.listarPorOrden(orden.getIdOrdenServicio());
        if (filaSeleccionada < asignados.size()) {
            model.OrdenServicioTecnico relacion = asignados.get(filaSeleccionada);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de remover al técnico '" + relacion.getNombreTecnico() + "' de esta orden?",
                    "Confirmar Desasignación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Invoca la eliminación en la tabla intermedia mediante los dos identificadores
                String resultado = ordenTecnicoController.eliminarAsignacion(orden.getIdOrdenServicio(), relacion.getIdTecnico());

                if ("OK".equals(resultado) || resultado.toLowerCase().contains("éxito")) {
                    JOptionPane.showMessageDialog(this, "Técnico desasignado correctamente.");
                    refrescar(); // Vuelve a cargar las tablas y combos actualizados
                } else {
                    JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void actualizarEstadoOrden(String nuevoEstado) {
        String resultado = ordenController.actualizarEstado(orden.getIdOrdenServicio(), nuevoEstado);
        JOptionPane.showMessageDialog(this, resultado, "Flujo Operativo", JOptionPane.INFORMATION_MESSAGE);
        refrescar();
    }

    private void refrescar() {
        OrdenServicio actualizada = ordenController.buscarPorId(orden.getIdOrdenServicio());
        if (actualizada != null) {
            txtEstado.setText(actualizada.getEstado());
        }
        cargarTecnicosAsignados();
        cargarTecnicosDisponibles();
        cargarMateriales();
    }
}