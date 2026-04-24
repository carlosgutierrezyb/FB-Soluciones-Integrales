package service;

import exception.BusinessException;
import model.DetalleOrdenCompra;
import model.OrdenCompra;
import repository.DetalleOrdenCompraRepository;
import repository.OrdenCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

/**
 * Service de órdenes de compra.
 *
 * 🔥 ERP PRO:
 * - Transacción completa OC + Detalles
 * - Control de rollback
 */
public class OrdenCompraService {

    private OrdenCompraRepository ordenRepo;
    private DetalleOrdenCompraRepository detalleRepo;

    public OrdenCompraService() {
        this.ordenRepo = new OrdenCompraRepository();
        this.detalleRepo = new DetalleOrdenCompraRepository();
    }

    // =========================
    // 🔹 CREAR ORDEN DE COMPRA
    // =========================
    public String crearOrden(int idProveedor, List<DetalleOrdenCompra> detalles) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 VALIDACIONES
            // =========================
            if (idProveedor <= 0) {
                throw new BusinessException("Proveedor inválido.");
            }

            if (detalles == null || detalles.isEmpty()) {
                throw new BusinessException("Debe agregar al menos un producto.");
            }

            for (DetalleOrdenCompra d : detalles) {

                if (d.getIdItem() <= 0) {
                    throw new BusinessException("Producto inválido en el detalle.");
                }

                if (d.getCantidadPedida() <= 0) {
                    throw new BusinessException("Cantidad inválida para el producto ID: " + d.getIdItem());
                }

                if (d.getPrecioUnitario() < 0) {
                    throw new BusinessException("Precio inválido para el producto ID: " + d.getIdItem());
                }
            }

            // =========================
            // 🔹 TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // =========================
            // 🔹 CREAR CABECERA
            // =========================
            OrdenCompra orden = new OrdenCompra();
            orden.setIdProveedor(idProveedor);
            orden.setEstado("Pendiente");

            int idOrden = ordenRepo.crear(orden);

            if (idOrden <= 0) {
                throw new BusinessException("No se pudo crear la orden de compra.");
            }

            // =========================
            // 🔹 ASIGNAR ID ORDEN A DETALLES
            // =========================
            for (DetalleOrdenCompra d : detalles) {
                d.setIdOrden(idOrden);
            }

            // =========================
            // 🔹 INSERTAR DETALLES (BATCH)
            // =========================
            boolean guardado = detalleRepo.insertarLista(detalles, conn);

            if (!guardado) {
                throw new BusinessException("Error guardando detalle de orden.");
            }

            conn.commit();

            System.out.println("✅ Orden de compra creada correctamente.");

            return "OK";

        } catch (BusinessException e) {

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return "Error creando orden de compra.";

        } finally {

            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =========================
    // 🔹 LISTAR ÓRDENES PENDIENTES
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {
        return ordenRepo.listarPendientes();
    }

    // =========================
    // 🔹 LISTAR ÓRDENES PARA FACTURAR
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {
        return ordenRepo.obtenerOrdenesParaFacturaCompra();
    }

    // =========================
    // 🔹 DETALLE DE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(int idOrden) {
        return detalleRepo.listarPorOrden(idOrden);
    }

    // =========================
    // 🔹 BUSCAR ORDEN
    // =========================
    public OrdenCompra buscarPorId(int idOrden) {
        return ordenRepo.buscarPorId(idOrden);
    }
}