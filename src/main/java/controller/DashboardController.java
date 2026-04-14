package controller;

import view.DashboardView;
import view.InventarioView;
import view.ComprasView;

/**
 * Controlador del Dashboard principal.
 *
 * RESPONSABILIDADES:
 * - Manejar navegación entre módulos
 * - Abrir vistas correspondientes
 */
public class DashboardController {

    private DashboardView vista;

    public DashboardController(DashboardView vista) {
        this.vista = vista;
        inicializarEventos();
    }

    private void inicializarEventos() {

        // 🔹 INVENTARIO
        vista.getBtnInventario().addActionListener(e -> abrirInventario());

        // 🔹 COMPRAS
        vista.getBtnCompras().addActionListener(e -> abrirCompras());

        // 🔹 FUTURO
        vista.getBtnVentas().addActionListener(e ->
                mostrarModuloEnConstruccion("Ventas"));

        vista.getBtnReportes().addActionListener(e ->
                mostrarModuloEnConstruccion("Reportes"));
    }

    private void abrirInventario() {

        InventarioView vistaInv = new InventarioView();
        InventarioController controller = new InventarioController(vistaInv);

        vistaInv.setController(controller);
        vistaInv.inicializarDatos();
        vistaInv.setVisible(true);
    }

    private void abrirCompras() {

        ComprasView vistaCompras = new ComprasView();
        ComprasController controller = new ComprasController();

        vistaCompras.setController(controller);
        vistaCompras.inicializarDatos();
        vistaCompras.setVisible(true);
    }

    private void mostrarModuloEnConstruccion(String modulo) {
        javax.swing.JOptionPane.showMessageDialog(
                vista,
                "Módulo " + modulo + " en construcción"
        );
    }
}