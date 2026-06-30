package controller;

import view.ClienteListView;
import view.OrdenServicioView;
import view.OrdenServicioListView; // Asegúrate de tener este import
import view.ServicioListView;
import view.VentasMenuView;

import javax.swing.*;

/**
 * Controlador encargado de la navegación y flujo operativo del módulo de ventas.
 */
public class VentasMenuController {

    private final VentasMenuView vista;

    public VentasMenuController(VentasMenuView vista) {
        this.vista = vista;
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Gestión de Clientes
        vista.getBtnClientes().addActionListener(e -> abrirClientes());

        // Gestión de Catálogo de Servicios
        vista.getBtnServicios().addActionListener(e -> abrirServicios());

        // Apertura de Formulario para Nueva Orden de Servicio
        vista.getBtnOrdenServicio().addActionListener(e -> abrirOrdenServicio());

        // Apertura del Monitor General de Órdenes de Servicio
        vista.getBtnOrdenesServicio().addActionListener(e -> abrirOrdenesServicio());

        // Módulos Logísticos y Contables pendientes de acople
        vista.getBtnSalidaAlmacen().addActionListener(e -> mostrarModuloEnConstruccion("Salida Almacén"));
        vista.getBtnFacturaVenta().addActionListener(e -> mostrarModuloEnConstruccion("Factura Venta"));
    }

    private void abrirClientes() {
        ClienteListView view = new ClienteListView();
        ClienteController controller = new ClienteController();
        view.setController(controller);
        view.cargarClientes();
        view.setVisible(true);
    }

    private void abrirServicios() {
        ServicioListView view = new ServicioListView();
        ServicioController controller = new ServicioController();
        view.setController(controller);
        view.cargarServicios();
        view.setVisible(true);
    }

    private void abrirOrdenServicio() {
        OrdenServicioView view = new OrdenServicioView();
        OrdenServicioController controller = new OrdenServicioController();
        view.setController(controller);
        view.setVisible(true);
    }

    /**
     * Levanta el monitor general de órdenes de servicio (búsqueda, edición, gestión de técnicos).
     */
    private void abrirOrdenesServicio() {
        OrdenServicioListView view = new OrdenServicioListView();
        // Si tu vista requiere un controlador específico (ej. OrdenServicioListController), lo instancias acá:
        // OrdenServicioListController controller = new OrdenServicioListController();
        // view.setController(controller);

        view.setVisible(true);
    }

    private void mostrarModuloEnConstruccion(String modulo) {
        JOptionPane.showMessageDialog(vista, "Módulo " + modulo + " en construcción", "ERP F&B", JOptionPane.INFORMATION_MESSAGE);
    }
}