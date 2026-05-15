package controller;

import view.ComprasMenuView;
import view.DashboardView;
import view.InventarioView;
import view.VentasMenuView;

import javax.swing.*;

/**
 * Controlador principal del ERP.
 *
 * 🔥 RESPONSABILIDADES:
 * - Navegación principal
 * - Apertura de módulos
 * - Base escalable ERP
 */
public class DashboardController {

    private DashboardView vista;

    public DashboardController(DashboardView vista) {

        this.vista = vista;

        inicializarEventos();
    }

    // =========================
    // 🔹 EVENTOS
    // =========================
    private void inicializarEventos() {

        // 🔹 INVENTARIO
        vista.getBtnInventario()
                .addActionListener(e -> abrirInventario());

        // 🔹 COMPRAS
        vista.getBtnCompras()
                .addActionListener(e -> abrirModuloCompras());

        // 🔹 VENTAS
        vista.getBtnVentas()
                .addActionListener(e -> abrirModuloVentas());

        // 🔹 REPORTES
        vista.getBtnReportes()
                .addActionListener(e ->
                        mostrarModuloEnConstruccion("Reportes"));
    }

    // =========================
    // 🔹 INVENTARIO
    // =========================
    private void abrirInventario() {

        InventarioView vistaInv =
                new InventarioView();

        InventarioController controller =
                new InventarioController(vistaInv);

        vistaInv.setController(controller);

        vistaInv.inicializarDatos();

        vistaInv.setVisible(true);
    }

    // =========================
    // 🔹 MÓDULO COMPRAS
    // =========================
    private void abrirModuloCompras() {

        ComprasMenuView view =
                new ComprasMenuView();

        new ComprasMenuController(view);

        view.setVisible(true);
    }

    // =========================
    // 🔹 MÓDULO VENTAS
    // =========================
    private void abrirModuloVentas() {

        VentasMenuView view =
                new VentasMenuView();

        new VentasMenuController(view);

        view.setVisible(true);
    }

    // =========================
    // 🔧 UTIL
    // =========================
    private void mostrarModuloEnConstruccion(
            String modulo
    ) {

        JOptionPane.showMessageDialog(
                vista,
                "Módulo " + modulo + " en construcción"
        );
    }
}