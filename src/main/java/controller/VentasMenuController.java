package controller;

import view.ClienteListView;
import view.OrdenServicioView;
import view.VentasMenuView;

import javax.swing.*;

/**
 * Controlador del módulo de ventas.
 *
 * 🔥 RESPONSABILIDADES:
 * - Navegación interna del módulo ventas
 * - Apertura de vistas
 * - Base escalable ERP
 */
public class VentasMenuController {

    private VentasMenuView vista;

    public VentasMenuController(
            VentasMenuView vista
    ) {

        this.vista = vista;

        inicializarEventos();
    }

    // =========================
    // 🔹 EVENTOS
    // =========================
    private void inicializarEventos() {

        // =========================
        // 🔹 CLIENTES
        // =========================
        vista.getBtnClientes()
                .addActionListener(
                        e -> abrirClientes()
                );

        // =========================
        // 🔹 ORDEN SERVICIO
        // =========================
        vista.getBtnOrdenServicio()
                .addActionListener(
                        e -> abrirOrdenServicio()
                );

        // =========================
        // 🔹 ÓRDENES SERVICIO
        // =========================
        vista.getBtnOrdenesServicio()
                .addActionListener(
                        e ->
                                mostrarModuloEnConstruccion(
                                        "Órdenes Servicio"
                                )
                );

        // =========================
        // 🔹 SALIDA ALMACÉN
        // =========================
        vista.getBtnSalidaAlmacen()
                .addActionListener(
                        e ->
                                mostrarModuloEnConstruccion(
                                        "Salida Almacén"
                                )
                );

        // =========================
        // 🔹 FACTURA VENTA
        // =========================
        vista.getBtnFacturaVenta()
                .addActionListener(
                        e ->
                                mostrarModuloEnConstruccion(
                                        "Factura Venta"
                                )
                );
    }

    // =========================
    // 🔹 CLIENTES
    // =========================
    private void abrirClientes() {

        ClienteListView view =
                new ClienteListView();

        ClienteController controller =
                new ClienteController();

        view.setController(controller);

        view.cargarClientes();

        view.setVisible(true);
    }

    // =========================
    // 🔹 ORDEN SERVICIO
    // =========================
    // =========================
// 🔹 ORDEN SERVICIO
// =========================
    private void abrirOrdenServicio() {

        OrdenServicioView view =
                new OrdenServicioView();

        OrdenServicioController controller =
                new OrdenServicioController();

        view.setController(controller);

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
                "Módulo "
                        + modulo
                        + " en construcción"
        );
    }
}