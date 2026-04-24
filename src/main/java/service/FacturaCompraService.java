package service;

import exception.BusinessException;
import model.FacturaCompra;
import repository.EntradaAlmacenRepository;
import repository.FacturaCompraRepository;
import repository.OrdenCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de Factura de Compra
 *
 * 🔥 ERP PRO REAL:
 * - múltiples facturas por una OC
 * - múltiples entradas por factura
 * - usa detalle_factura_compra
 * - NO usa id_factura en entradas_almacen
 */
public class FacturaCompraService {

    private FacturaCompraRepository facturaRepo;
    private EntradaAlmacenRepository entradaRepo;
    private OrdenCompraRepository ordenRepo;

    public FacturaCompraService() {
        this.facturaRepo = new FacturaCompraRepository();
        this.entradaRepo = new EntradaAlmacenRepository();
        this.ordenRepo = new OrdenCompraRepository();
    }

    // =========================
    // 🔥 REGISTRAR FACTURA
    // =========================
    public String registrarFactura(
            int idProveedor,
            int idOrden,
            String numeroFactura,
            String fechaStr
    ) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 VALIDACIONES
            // =========================
            if (idProveedor <= 0) {
                throw new BusinessException("Proveedor inválido.");
            }

            if (idOrden <= 0) {
                throw new BusinessException("Orden inválida.");
            }

            if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
                throw new BusinessException("Número de factura obligatorio.");
            }

            if (fechaStr == null || fechaStr.trim().isEmpty()) {
                throw new BusinessException("Fecha obligatoria.");
            }

            LocalDate fecha;

            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                throw new BusinessException("Formato de fecha inválido (YYYY-MM-DD).");
            }

            // =========================
            // 🔹 TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("📄 Registrando factura de compra...");

            // =========================
            // 🔹 VALIDAR ENTRADAS PENDIENTES
            // =========================
            boolean hayPendientes =
                    facturaRepo.existenEntradasPendientes(conn, idOrden);

            if (!hayPendientes) {
                throw new BusinessException(
                        "No existen entradas pendientes por facturar."
                );
            }

            // =========================
            // 🔹 CREAR FACTURA (ENCABEZADO)
            // =========================
            FacturaCompra factura = new FacturaCompra();

            factura.setIdProveedor(idProveedor);
            factura.setNumeroFactura(numeroFactura);
            factura.setFecha(fecha.atStartOfDay());
            factura.setEstado("Registrada");
            factura.setObservacion("Factura registrada desde módulo compras");

            int idFactura = facturaRepo.crear(conn, factura);

            if (idFactura <= 0) {
                throw new BusinessException(
                        "No se pudo crear la factura."
                );
            }

            // =========================
            // 🔹 OBTENER ENTRADAS PENDIENTES
            // =========================
            List<Integer> entradasPendientes =
                    facturaRepo.obtenerEntradasPendientes(conn, idOrden);

            if (entradasPendientes.isEmpty()) {
                throw new BusinessException(
                        "No hay entradas disponibles para facturar."
                );
            }

            // =========================
            // 🔹 CREAR DETALLE FACTURA
            // =========================
            for (Integer idEntrada : entradasPendientes) {

                int idItem =
                        entradaRepo.obtenerIdItemPorEntrada(idEntrada);

                int cantidad =
                        entradaRepo.obtenerCantidadPorEntrada(idEntrada);

                double precio =
                        entradaRepo.obtenerPrecioCompraPorEntrada(idEntrada);

                boolean ok = facturaRepo.crearDetalleFactura(
                        conn,
                        idFactura,
                        idEntrada,
                        idItem,
                        cantidad,
                        precio
                );

                if (!ok) {
                    throw new BusinessException(
                            "Error creando detalle de factura."
                    );
                }
            }

            // =========================
            // 🔹 ACTUALIZAR ESTADO ORDEN
            // =========================
            ordenRepo.actualizarEstado(
                    conn,
                    idOrden,
                    "Recibido"
            );

            // =========================
            // 🔹 COMMIT
            // =========================
            conn.commit();

            System.out.println("✅ Factura registrada correctamente.");

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);
            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();
            rollback(conn);
            return "Error registrando factura.";

        } finally {
            cerrarConexion(conn);
        }
    }

    // =========================
    // 🔧 UTIL
    // =========================
    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                System.out.println("⚠️ Rollback ejecutado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarConexion(Connection conn) {
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