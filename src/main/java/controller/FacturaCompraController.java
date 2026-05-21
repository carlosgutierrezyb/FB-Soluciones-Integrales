package controller;

import model.DetalleOrdenCompra;
import model.OrdenCompra;
import model.Producto;
import repository.ProductoRepository;
import service.EntradaAlmacenService;
import service.FacturaCompraService;

import javax.swing.table.DefaultTableModel;
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

    // =========================
    // CONSTRUCTOR
    // =========================
    public FacturaCompraController() {

        this.facturaService =
                new FacturaCompraService();

        this.entradaService =
                new EntradaAlmacenService();

        this.productoRepo =
                new ProductoRepository();
    }

    // =========================
    // 🔹 ÓRDENES PARA FACTURAR
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {

        /*
         * 🔥 SOLO ÓRDENES
         * CON ENTRADAS
         */

        return entradaService.obtenerOrdenesPendientes();
    }

    // =========================
    // 🔹 RESUMEN DE ORDEN
    // =========================
    public List<Object[]> obtenerResumenOrden(
            int idOrden
    ) {

        List<Object[]> lista =
                new ArrayList<>();

        List<DetalleOrdenCompra> detalles =
                entradaService.obtenerDetalleOrden(
                        idOrden
                );

        for (DetalleOrdenCompra d : detalles) {

            Producto producto =
                    productoRepo.obtenerPorId(
                            d.getIdItem()
                    );

            String nombreProducto =
                    (producto != null)
                            ? producto.getNombre()
                            : "Producto #" + d.getIdItem();

            // =========================
            // CANTIDAD RECIBIDA
            // =========================

            int recibido =
                    entradaService.obtenerCantidadRecibida(
                            d.getIdItem(),
                            idOrden
                    );

            // =========================
            // CANTIDAD FACTURADA
            // =========================

            int facturado =
                    facturaService.obtenerCantidadFacturada(
                            idOrden,
                            d.getIdItem()
                    );

            // =========================
            // PENDIENTE FACTURAR
            // =========================

            int pendiente =
                    recibido - facturado;

            if (pendiente < 0) {

                pendiente = 0;
            }

            /*
             * TABLA:
             *
             * 0 -> ID ITEM
             * 1 -> PRODUCTO
             * 2 -> PEDIDA
             * 3 -> RECIBIDA
             * 4 -> FACTURADA
             * 5 -> PENDIENTE
             */

            lista.add(

                    new Object[]{

                            d.getIdItem(),

                            nombreProducto,

                            d.getCantidadPedida(),

                            recibido,

                            facturado,

                            pendiente
                    }
            );
        }

        return lista;
    }

    // =========================
    // 🔹 REGISTRAR FACTURA
    // =========================
    public String registrarFactura(

            OrdenCompra orden,

            String numeroFactura,

            DefaultTableModel modelo
    ) {

        try {

            System.out.println(
                    "🧾 Registrando factura..."
            );

            // =========================
            // VALIDACIONES GENERALES
            // =========================

            if (orden == null) {

                return "Seleccione una orden.";
            }

            if (
                    numeroFactura == null
                            || numeroFactura.trim().isEmpty()
            ) {

                return "Ingrese el número de factura.";
            }

            // =========================
            // LISTA ITEMS FACTURA
            // =========================

            List<Object[]> itemsFactura =
                    new ArrayList<Object[]>();

            // =========================
            // RECORRER TABLA
            // =========================

            for (
                    int i = 0;
                    i < modelo.getRowCount();
                    i++
            ) {

                int idItem =
                        Integer.parseInt(
                                modelo.getValueAt(i, 0)
                                        .toString()
                        );

                int pendiente =
                        Integer.parseInt(
                                modelo.getValueAt(i, 5)
                                        .toString()
                        );

                Object valorCantidad =
                        modelo.getValueAt(i, 6);

                Object valorPrecio =
                        modelo.getValueAt(i, 7);

                // =========================
                // VALIDAR VACÍOS
                // =========================

                if (
                        valorCantidad == null
                                || valorCantidad.toString().trim().isEmpty()
                ) {

                    continue;
                }

                if (
                        valorPrecio == null
                                || valorPrecio.toString().trim().isEmpty()
                ) {

                    continue;
                }

                int cantidadFactura =
                        Integer.parseInt(
                                valorCantidad.toString()
                        );

                String valorStr =
                        modelo.getValueAt(i, 7) == null
                                ? "0"
                                : modelo.getValueAt(i, 7).toString().trim();

// 🔥 soporte coma decimal (muy común en Colombia)
                valorStr = valorStr.replace(",", ".");

                double valorUnitario;

                try {
                    valorUnitario = Double.parseDouble(valorStr);
                } catch (NumberFormatException e) {
                    return "Valor unitario inválido. Use números como 1000 o 1000.50";
                }

                // =========================
                // VALIDACIONES
                // =========================

                if (cantidadFactura < 0) {

                    return "Cantidad inválida.";
                }

                if (cantidadFactura > pendiente) {

                    return "No puede facturar más de lo pendiente.";
                }

                if (cantidadFactura > 0) {

                    if (valorUnitario <= 0) {

                        return "Valor unitario inválido.";
                    }

                    itemsFactura.add(

                            new Object[]{

                                    idItem,

                                    cantidadFactura,

                                    valorUnitario
                            }
                    );
                }
            }

            // =========================
            // VALIDAR ITEMS
            // =========================

            if (itemsFactura.isEmpty()) {

                return "Debe ingresar al menos un ítem.";
            }

            // =========================
            // REGISTRAR FACTURA
            // =========================

            return facturaService.registrarFacturaCompleta(

                    orden.getIdProveedor(),

                    orden.getIdOrden(),

                    numeroFactura,

                    itemsFactura
            );

        } catch (NumberFormatException e) {

            e.printStackTrace();

            return "Valores inválidos en la tabla.";

        } catch (Exception e) {

            e.printStackTrace();

            return "Error registrando factura.";
        }
    }
}