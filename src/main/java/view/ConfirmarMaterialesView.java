package view; //  CORREGIDO: Mismo paquete que la vista de gestión

import controller.InventarioController;
import model.DetalleOrdenServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Diálogo modal para la verificación final de insumos antes del cierre de órdenes.
 */
public class ConfirmarMaterialesView extends JDialog {

    private final List<DetalleOrdenServicio> materiales;
    private final InventarioController inventarioController;

    private JTable tablaConfirmacion;
    private DefaultTableModel modeloTabla;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private boolean operacionConfirmada = false;

    public ConfirmarMaterialesView(Frame padre, List<DetalleOrdenServicio> materiales, InventarioController inventarioController) {
        super(padre, "Confirmación de Insumos y Descuento de Inventario", true);
        this.materiales = materiales;
        this.inventarioController = inventarioController;

        inicializarComponentes();
        cargarMaterialesATabla();

        setSize(650, 400);
        setLocationRelativeTo(padre);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Encabezado informativo
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelNorte.setBackground(new Color(230, 242, 255));
        JLabel lblInfo = new JLabel("<html><b>Verificación de Materiales:</b> Indique la cantidad real consumida en el servicio.<br>Al confirmar, se descontarán las unidades de forma definitiva del inventario.</html>");
        lblInfo.setFont(lblInfo.getFont().deriveFont(Font.PLAIN));
        panelNorte.add(lblInfo);
        add(panelNorte, BorderLayout.NORTH);

        // Tabla de edición masiva
        modeloTabla = new DefaultTableModel(new Object[]{"ID Producto", "Código", "Descripción Material", "Cant. Planificada", "Cant. Real Usada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo la columna Cant. Real Usada se puede modificar
            }
        };

        tablaConfirmacion = new JTable(modeloTabla);
        tablaConfirmacion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaConfirmacion), BorderLayout.CENTER);

        // Barra inferior de acciones
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnAceptar = new JButton("Confirmar y Descontar Stock ✔️");
        btnAceptar.setFont(btnAceptar.getFont().deriveFont(Font.BOLD));
        btnCancelar = new JButton("Cancelar");

        panelSur.add(btnCancelar);
        panelSur.add(btnAceptar);
        add(panelSur, BorderLayout.SOUTH);

        // Eventos
        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> procesarDescuentoInventario());
    }

    private void cargarMaterialesATabla() {
        modeloTabla.setRowCount(0);
        for (DetalleOrdenServicio m : materiales) {
            modeloTabla.addRow(new Object[]{
                    m.getIdProducto(),
                    m.getCodigoReferencia(),
                    m.getNombreReferencia(),
                    m.getCantidad(),
                    m.getCantidad() // Se pre-carga por defecto el mismo valor planificado
            });
        }
    }

    private void procesarDescuentoInventario() {
        // 🔥 CORREGIDO: Asegurar que se detenga la edición de la celda activa para no perder el dato actual
        if (tablaConfirmacion.isEditing()) {
            tablaConfirmacion.getCellEditor().stopCellEditing();
        }

        try {
            int filas = modeloTabla.getRowCount();

            // 1. Fase de Validación preventoria en caliente
            for (int i = 0; i < filas; i++) {
                Object valorCelda = modeloTabla.getValueAt(i, 4);
                int cantidadReal;

                if (valorCelda instanceof Integer) {
                    cantidadReal = (Integer) valorCelda;
                } else {
                    cantidadReal = Integer.parseInt(valorCelda.toString().trim());
                }

                if (cantidadReal < 0) {
                    JOptionPane.showMessageDialog(this,
                            "La cantidad real usada no puede ser negativa en la fila " + (i + 1),
                            "Error de Datos", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 2. Fase de ejecución transaccional distributiva
            for (int i = 0; i < filas; i++) {
                int idProducto = (int) modeloTabla.getValueAt(i, 0);
                Object valorCelda = modeloTabla.getValueAt(i, 4);
                int cantidadReal = (valorCelda instanceof Integer) ? (Integer) valorCelda : Integer.parseInt(valorCelda.toString().trim());

                if (cantidadReal > 0) {
                    // 🔥 CORREGIDO: Se usa descontarStock y se evalúa el String "OK"
                    String resultadoDescuento = inventarioController.descontarStock(idProducto, cantidadReal);

                    if (!"OK".equals(resultadoDescuento)) {
                        JOptionPane.showMessageDialog(this,
                                "Error al descontar stock para el producto ID " + idProducto + ":\n" + resultadoDescuento +
                                        "\nEl proceso de cierre ha sido pausado de forma segura.",
                                "Error en Base de Datos", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // 3. Éxito de la operación
            this.operacionConfirmada = true;
            JOptionPane.showMessageDialog(this, "Inventario descontado y sincronizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Asegúrese de ingresar solo números enteros válidos en la cantidad usada.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método de lectura pública para la vista principal
    public boolean isOperacionConfirmada() {
        return operacionConfirmada;
    }
}