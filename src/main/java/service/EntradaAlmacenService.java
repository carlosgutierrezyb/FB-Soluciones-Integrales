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
 * - Recepción parcial o total
 * - Control de entregas múltiples
 * - Actualización automática de estados
 * - Trazabilidad logística
 * - Preparación para facturación
 *
 * ⚠ IMPORTANTE:
 * Entrada Almacén = logística
 * Factura Compra = contabilidad
 *
 * El precio NO debería registrarse aquí.
 */
public class EntradaAlmacenService {

    // =========================
    // DEPENDENCIAS
    // =========================
    private OrdenCompraRepository ordenRepo;
    private DetalleOrdenCompraRepository detalleRepo;
    private EntradaAlmacenRepository entradaRepo;

    // =========================
    // CONSTRUCTOR
    // =========================
    public EntradaAlmacenService() {

        this.ordenRepo =
                new OrdenCompraRepository();

        this.detalleRepo =
                new DetalleOrdenCompraRepository();

        this.entradaRepo =
                new EntradaAlmacenRepository();
    }

    // =========================
    // 🔹 ÓRDENES PENDIENTES
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {

        return ordenRepo.listarPendientes();
    }

    // =========================
    // 🔹 ÓRDENES PENDIENTES FACTURA
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientesFacturacion() {

        return ordenRepo.obtenerOrdenesParaFacturaCompra();
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(
            int idOrden
    ) {

        return detalleRepo.listarPorOrden(idOrden);
    }

    // =========================
    // 🔹 TOTAL RECIBIDO
    // =========================
    public int obtenerCantidadRecibida(
            int idItem,
            int idOrden
    ) {

        return entradaRepo.obtenerCantidadRecibida(
                idItem,
                idOrden
        );
    }

    // =========================
    // 🔹 REGISTRAR ENTRADA
    // =========================
    public String registrarEntrada(
            int idOrden,
            int idItem,
            int cantidad,
            String numeroFactura,
            String numeroRemision
    ) {

        Connection conn = null;

        try {

            // =========================
            // VALIDACIONES
            // =========================
            validarDatosEntrada(
                    idOrden,
                    idItem,
                    cantidad,
                    numeroFactura,
                    numeroRemision
            );

            // =========================
            // VALIDAR PENDIENTE
            // =========================
            int recibido =
                    obtenerCantidadRecibida(
                            idItem,
                            idOrden
                    );

            DetalleOrdenCompra detalle =
                    detalleRepo.obtenerPorOrdenYItem(
                            idOrden,
                            idItem
                    );

            if (detalle == null) {

                throw new BusinessException(
                        "El producto no pertenece a la orden."
                );
            }

            int pendiente =
                    detalle.getCantidadPedida()
                            - recibido;

            if (cantidad > pendiente) {

                throw new BusinessException(
                        "No puede ingresar más de lo pendiente."
                );
            }

            // =========================
            // TRANSACCIÓN
            // =========================
            conn =
                    DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            System.out.println(
                    "📦 Registrando entrada..."
            );

            // =========================
            // CREAR ENTRADA
            // =========================
            EntradaAlmacen entrada =
                    new EntradaAlmacen();

            entrada.setIdOrden(idOrden);

            entrada.setIdItem(idItem);

            entrada.setCantidadRecibida(cantidad);

            entrada.setNumeroFactura(
                    numeroFactura
            );

            entrada.setNumeroRemision(
                    numeroRemision
            );

            entrada.setFechaEntrada(
                    new Timestamp(
                            System.currentTimeMillis()
                    )
            );

            // 🔥 COMPATIBILIDAD LEGACY
            entrada.setPrecioCompraUnitario(0);

            boolean guardado =
                    entradaRepo.guardar(
                            conn,
                            entrada
                    );

            if (!guardado) {

                throw new SQLException(
                        "No se pudo registrar la entrada."
                );
            }

            // =========================
            // 🔥 ACTUALIZAR ESTADO OC
            // =========================
            actualizarEstadoOrden(
                    conn,
                    idOrden
            );

            conn.commit();

            System.out.println(
                    "✅ Entrada registrada."
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
    // 🔥 VALIDAR DATOS ENTRADA
    // =========================
    private void validarDatosEntrada(
            int idOrden,
            int idItem,
            int cantidad,
            String numeroFactura,
            String numeroRemision
    ) {

        if (idOrden <= 0) {

            throw new BusinessException(
                    "Orden inválida."
            );
        }

        if (idItem <= 0) {

            throw new BusinessException(
                    "Producto inválido."
            );
        }

        if (cantidad <= 0) {

            throw new BusinessException(
                    "Cantidad inválida."
            );
        }

        boolean facturaVacia =
                numeroFactura == null
                        || numeroFactura.trim().isEmpty();

        boolean remisionVacia =
                numeroRemision == null
                        || numeroRemision.trim().isEmpty();

        if (facturaVacia && remisionVacia) {

            throw new BusinessException(
                    "Debe ingresar número de factura, remisión o ambos."
            );
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

        int totalPedido = 0;

        int totalRecibido = 0;

        for (DetalleOrdenCompra d : detalles) {

            totalPedido +=
                    d.getCantidadPedida();

            // 🔥 IMPORTANTE:
            // usar MISMA conexión
            totalRecibido +=
                    entradaRepo.obtenerCantidadRecibida(
                            conn,
                            d.getIdItem(),
                            idOrden
                    );
        }

        System.out.println(
                "📊 Total pedido: "
                        + totalPedido
        );

        System.out.println(
                "📊 Total recibido: "
                        + totalRecibido
        );

        // =========================
        // ESTADOS ERP
        // =========================

        if (totalRecibido == 0) {

            ordenRepo.actualizarEstado(
                    conn,
                    idOrden,
                    "Pendiente"
            );

            System.out.println(
                    "📦 Orden marcada como PENDIENTE"
            );

        } else if (
                totalRecibido > 0
                        && totalRecibido < totalPedido
        ) {

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
                    "Recibido"
            );

            System.out.println(
                    "📦 Orden marcada como RECIBIDO"
            );
        }
    }

    // =========================
    // 🔧 ROLLBACK
    // =========================
    private void rollback(
            Connection conn
    ) {

        try {

            if (conn != null) {

                conn.rollback();

                System.out.println(
                        "⚠ Rollback ejecutado."
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // =========================
    // 🔧 CERRAR CONEXIÓN
    // =========================
    private void cerrarConexion(
            Connection conn
    ) {

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