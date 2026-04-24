package service;

import exception.BusinessException;
import model.DetalleOrdenCompra;
import model.EntradaAlmacen;
import model.OrdenCompra;
import repository.DetalleOrdenCompraRepository;
import repository.EntradaAlmacenRepository;
import repository.OrdenCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Service de entradas de almacén.
 *
 * 🔥 ERP PRO:
 * - Permite múltiples entregas por una misma OC
 * - Controla recepción parcial / total
 * - Actualiza inventario
 * - Actualiza estado de la orden
 * - Prepara información para facturación posterior
 */
public class EntradaAlmacenService {

    private OrdenCompraRepository ordenRepo;
    private DetalleOrdenCompraRepository detalleRepo;
    private EntradaAlmacenRepository entradaRepo;

    public EntradaAlmacenService() {
        this.ordenRepo = new OrdenCompraRepository();
        this.detalleRepo = new DetalleOrdenCompraRepository();
        this.entradaRepo = new EntradaAlmacenRepository();
    }

    // =========================
    // 🔹 ÓRDENES CON RECEPCIÓN PENDIENTE
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {
        return ordenRepo.listarPendientes();
    }

    // =========================
    // 🔹 ÓRDENES CON ENTRADAS PENDIENTES DE FACTURA
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientesFacturacion() {
        return ordenRepo.obtenerOrdenesParaFacturaCompra();
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(int idOrden) {
        return detalleRepo.listarPorOrden(idOrden);
    }

    // =========================
    // 🔹 TOTAL RECIBIDO
    // =========================
    public int obtenerCantidadRecibida(int idItem, int idOrden) {
        return entradaRepo.obtenerCantidadRecibida(idItem, idOrden);
    }

    // =========================
    // 🔹 REGISTRAR ENTRADA
    // =========================
    public String registrarEntrada(
            int idOrden,
            int idItem,
            int cantidad,
            double precioCompra,
            String numeroFactura
    ) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 VALIDACIONES
            // =========================
            if (idOrden <= 0) {
                throw new BusinessException("Orden inválida.");
            }

            if (idItem <= 0) {
                throw new BusinessException("Producto inválido.");
            }

            if (cantidad <= 0) {
                throw new BusinessException("Cantidad inválida.");
            }

            if (precioCompra < 0) {
                throw new BusinessException("Precio de compra inválido.");
            }

            int recibido = obtenerCantidadRecibida(idItem, idOrden);

            DetalleOrdenCompra detalle =
                    detalleRepo.obtenerPorOrdenYItem(idOrden, idItem);

            if (detalle == null) {
                throw new BusinessException(
                        "El producto no pertenece a la orden."
                );
            }

            int pendiente =
                    detalle.getCantidadPedida() - recibido;

            if (cantidad > pendiente) {
                throw new BusinessException(
                        "No puede ingresar más de lo pendiente."
                );
            }

            // =========================
            // 🔹 TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("📦 Registrando entrada de almacén...");

            EntradaAlmacen entrada = new EntradaAlmacen();

            entrada.setIdOrden(idOrden);
            entrada.setIdItem(idItem);
            entrada.setCantidadRecibida(cantidad);
            entrada.setPrecioCompraUnitario(precioCompra);
            entrada.setNumeroFactura(numeroFactura);
            entrada.setFechaEntrada(
                    new Timestamp(System.currentTimeMillis())
            );

            boolean guardado =
                    entradaRepo.guardar(conn, entrada);

            if (!guardado) {
                throw new SQLException(
                        "No se pudo registrar la entrada."
                );
            }

            // =========================
            // 🔥 ACTUALIZAR ESTADO OC
            // =========================
            actualizarEstadoOrden(conn, idOrden);

            conn.commit();

            System.out.println(
                    "✅ Entrada registrada correctamente."
            );

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);
            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();
            rollback(conn);
            return "Error registrando entrada.";

        } finally {
            cerrarConexion(conn);
        }
    }

    // =========================
    // 🔥 ACTUALIZAR ESTADO ORDEN
    // =========================
    private void actualizarEstadoOrden(
            Connection conn,
            int idOrden
    ) throws SQLException {

        List<DetalleOrdenCompra> detalles =
                detalleRepo.listarPorOrden(idOrden);

        boolean completa = true;
        boolean tieneRecepcion = false;

        for (DetalleOrdenCompra d : detalles) {

            int recibido =
                    entradaRepo.obtenerCantidadRecibida(
                            d.getIdItem(),
                            idOrden
                    );

            if (recibido > 0) {
                tieneRecepcion = true;
            }

            if (recibido < d.getCantidadPedida()) {
                completa = false;
            }
        }

        if (completa) {
            ordenRepo.actualizarEstado(
                    conn,
                    idOrden,
                    "Recibido"
            );

            System.out.println(
                    "📦 Orden marcada como RECIBIDO"
            );

        } else if (tieneRecepcion) {

            ordenRepo.actualizarEstado(
                    conn,
                    idOrden,
                    "Parcial"
            );

            System.out.println(
                    "📦 Orden marcada como PARCIAL"
            );

        } else {

            ordenRepo.actualizarEstado(
                    conn,
                    idOrden,
                    "Pendiente"
            );
        }
    }

    // =========================
    // 🔧 ROLLBACK
    // =========================
    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                System.out.println(
                        "⚠️ Rollback ejecutado."
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // 🔧 CERRAR CONEXIÓN
    // =========================
    private void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}