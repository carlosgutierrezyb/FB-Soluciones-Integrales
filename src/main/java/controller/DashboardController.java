package controller;

import view.ComprasMenuView;
import view.DashboardView;
import view.InventarioView;
import view.VentasMenuView;
import view.OrdenServicioListView; // Asegúrate de tener este import operativo

import javax.swing.*;

/**
 * Controlador principal del ERP que gestiona los accesos y la navegación de alto nivel.
 */
public class DashboardController {

    private final DashboardView vista;

    public DashboardController(DashboardView vista) {
        this.vista = vista;
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Acceso al Módulo de Inventario
        vista.getBtnInventario().addActionListener(e -> abrirInventario());

        // Acceso al Menú de Operaciones de Compra
        vista.getBtnCompras().addActionListener(e -> abrirModuloCompras());

        // Acceso al Menú de Operaciones de Venta
        vista.getBtnVentas().addActionListener(e -> abrirModuloVentas());

        // Acceso directo desde Dashboard al Monitor de Órdenes de Servicio
        vista.getBtnOrdenesServicio().addActionListener(e -> abrirMonitorOrdenesDirecto());

        // Enlace para asignación técnica y reportes (En construcción)
        vista.getBtnAsignacionTecnica().addActionListener(e -> mostrarModuloEnConstruccion("Asignación Técnica"));
        vista.getBtnReportes().addActionListener(e -> mostrarModuloEnConstruccion("Reportes"));
    }

    private void abrirInventario() {
        InventarioView vistaInv = new InventarioView();
        InventarioController controller = new InventarioController(vistaInv);
        vistaInv.setController(controller);
        vistaInv.inicializarDatos();
        vistaInv.setVisible(true);
    }

    private void abrirModuloCompras() {
        ComprasMenuView view = new ComprasMenuView();
        new ComprasMenuController(view);
        view.setVisible(true);
    }

    private void abrirModuloVentas() {
        VentasMenuView view = new VentasMenuView();
        new VentasMenuController(view);
        view.setVisible(true);
    }

    /**
     * Abre el listado general de órdenes directamente, evitando levantar la vista del menú de ventas.
     */
    private void abrirMonitorOrdenesDirecto() {
        OrdenServicioListView view = new OrdenServicioListView();
        // Si el monitor general requiere controlador, instáncialo aquí de forma limpia
        view.setVisible(true);
    }

    private void mostrarModuloEnConstruccion(String modulo) {
        JOptionPane.showMessageDialog(vista, "Módulo " + modulo + " en construcción", "ERP F&B", JOptionPane.INFORMATION_MESSAGE);
    }
}