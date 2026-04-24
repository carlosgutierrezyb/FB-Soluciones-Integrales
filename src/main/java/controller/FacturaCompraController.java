package controller;

import model.DetalleOrdenCompra;
import model.OrdenCompra;
import model.Producto;
import repository.ProductoRepository;
import service.EntradaAlmacenService;
import service.FacturaCompraService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Factura de Compra
 *
 * 🔥 ERP PRO REAL:
 * - valida contra entradas recibidas
 * - NO factura de más
 * - soporta múltiples facturas por OC
 * - trabaja con detalle_factura_compra
 */
public class FacturaCompraController {

    private FacturaCompraService facturaService;
    private EntradaAlmacenService entradaService;
    private ProductoRepository productoRepo;

    public FacturaCompraController() {
        this.facturaService = new FacturaCompraService();
        this.entradaService = new EntradaAlmacenService();
        this.productoRepo = new ProductoRepository();
    }

    // =========================
    // 🔹 ÓRDENES PARA FACTURAR
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {

        /*
         * 🔥 Solo órdenes con entradas reales:
         * Pendiente / Parcial / Recibido
         *
         * Luego podemos refinar más con SQL PRO
         */
        return entradaService.obtenerOrdenesPendientes();
    }

    // =========================
    // 🔹 RESUMEN DE ORDEN
    // =========================
    public List<Object[]> obtenerResumenOrden(int idOrden) {

        List<Object[]> lista = new ArrayList<>();

        List<DetalleOrdenCompra> detalles =
                entradaService.obtenerDetalleOrden(idOrden);

        for (DetalleOrdenCompra d : detalles) {

            Producto producto =
                    productoRepo.obtenerPorId(d.getIdItem());

            int recibido =
                    entradaService.obtenerCantidadRecibida(
                            d.getIdItem(),
                            idOrden
                    );

            String nombreProducto =
                    (producto != null)
                            ? producto.getNombre()
                            : "Producto #" + d.getIdItem();

            /*
             * Tabla:
             * Producto | Pedido | Recibido
             */
            lista.add(new Object[]{
                    nombreProducto,
                    d.getCantidadPedida(),
                    recibido
            });
        }

        return lista;
    }

    // =========================
    // 🔹 REGISTRAR FACTURA
    // =========================
    public String registrarFactura(
            int idProveedor,
            int idOrden,
            String numeroFactura,
            String fecha
    ) {

        try {

            System.out.println("🧾 Registrando factura de compra...");

            return facturaService.registrarFactura(
                    idProveedor,
                    idOrden,
                    numeroFactura,
                    fecha
            );

        } catch (Exception e) {

            e.printStackTrace();
            return "Error registrando factura.";
        }
    }
}