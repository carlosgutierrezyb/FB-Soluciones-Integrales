package controller;

import view.ComprasMenuView;
import view.ComprasView;
import view.EntradaAlmacenView;
import view.FacturaCompraView;
import view.OrdenCompraListView;
import view.ProveedorListView;

/**
 * Controller del módulo de Compras.
 *
 * 🔥 RESPONSABILIDADES:
 * - Navegación interna del módulo
 * - Apertura de vistas de compras
 * - Gestión proveedores
 * - Mantener desacoplado el Dashboard
 */
public class ComprasMenuController {

    private ComprasMenuView vista;

    public ComprasMenuController(
            ComprasMenuView vista
    ) {

        this.vista = vista;

        inicializarEventos();
    }

    // =========================
    // 🔹 EVENTOS
    // =========================
    private void inicializarEventos() {

        // =========================
        // 🔹 PROVEEDORES
        // =========================
        vista.getBtnProveedores()
                .addActionListener(e -> abrirProveedores());

        // =========================
        // 🔹 CREAR ORDEN
        // =========================
        vista.getBtnCrearOrden()
                .addActionListener(e -> abrirCrearOrden());

        // =========================
        // 🔹 ÓRDENES DE COMPRA
        // =========================
        vista.getBtnOrdenesCompra()
                .addActionListener(e -> abrirOrdenesCompra());

        // =========================
        // 🔹 ENTRADA ALMACÉN
        // =========================
        vista.getBtnEntradaAlmacen()
                .addActionListener(e -> abrirEntradaAlmacen());

        // =========================
        // 🔹 FACTURA COMPRA
        // =========================
        vista.getBtnFacturaCompra()
                .addActionListener(e -> abrirFacturaCompra());
    }

    // =========================
    // 🔹 PROVEEDORES
    // =========================
    private void abrirProveedores() {

        ProveedorListView view =
                new ProveedorListView();

        view.setController(
                new ProveedorController()
        );

        view.cargarProveedores();

        view.setVisible(true);
    }

    // =========================
    // 🔹 CREAR ORDEN
    // =========================
    private void abrirCrearOrden() {

        ComprasView view =
                new ComprasView();

        ComprasController controller =
                new ComprasController();

        view.setController(controller);

        view.inicializarDatos();

        view.setVisible(true);
    }

    // =========================
    // 🔹 ÓRDENES DE COMPRA
    // =========================
    private void abrirOrdenesCompra() {

        OrdenCompraListView view =
                new OrdenCompraListView();

        view.setController(
                new OrdenCompraController()
        );

        view.setVisible(true);
    }

    // =========================
    // 🔹 ENTRADA ALMACÉN
    // =========================
    private void abrirEntradaAlmacen() {

        EntradaAlmacenView view =
                new EntradaAlmacenView();

        view.setController(
                new EntradaAlmacenController()
        );

        view.cargarOrdenes();

        view.setVisible(true);
    }

    // =========================
    // 🔹 FACTURA COMPRA
    // =========================
    private void abrirFacturaCompra() {

        FacturaCompraView view =
                new FacturaCompraView();

        view.setController(
                new FacturaCompraController()
        );

        view.cargarOrdenes();

        view.setVisible(true);
    }
}