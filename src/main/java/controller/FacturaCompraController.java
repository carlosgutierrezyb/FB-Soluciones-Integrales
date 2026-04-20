package controller;

import model.DetalleOrdenCompra;
import model.Producto;
import model.OrdenCompra;
import service.EntradaAlmacenService;
import service.FacturaCompraService;
import repository.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

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
    // 🔹 ORDENES PARA FACTURAR
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {

        // 🔥 Aquí puedes mejorar luego con lógica real
        return entradaService.obtenerOrdenesPendientes();
    }

    // =========================
    // 🔹 RESUMEN ORDEN (PRO)
    // =========================
    public List<Object[]> obtenerResumenOrden(int idOrden) {

        List<Object[]> lista = new ArrayList<>();

        List<DetalleOrdenCompra> detalles = entradaService.obtenerDetalleOrden(idOrden);

        for (DetalleOrdenCompra d : detalles) {

            Producto p = productoRepo.obtenerPorId(d.getIdItem());

            int recibido = entradaService.obtenerCantidadRecibida(
                    d.getIdItem(),
                    idOrden
            );

            lista.add(new Object[]{
                    p != null ? p.getNombre() : "Producto #" + d.getIdItem(),
                    d.getCantidadPedida(),
                    recibido
            });
        }

        return lista;
    }

    // =========================
    // 🔹 REGISTRAR FACTURA
    // =========================
    public String registrarFactura(int idOrden, String numero, String fecha) {

        try {

            System.out.println("🧾 Registrando factura de compra...");

            return facturaService.registrarFactura(
                    idOrden,
                    numero,
                    fecha
            );

        } catch (Exception e) {
            e.printStackTrace();
            return "Error registrando factura.";
        }
    }
}