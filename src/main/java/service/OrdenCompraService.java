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
 * - Manejo transaccional
 * - Control de estados
 * - Flujo logístico
 * - Flujo contable
 */
public class OrdenCompraService {

    private OrdenCompraRepository ordenRepo;

    private DetalleOrdenCompraRepository detalleRepo;

    public OrdenCompraService() {

        this.ordenRepo =
                new OrdenCompraRepository();

        this.detalleRepo =
                new DetalleOrdenCompraRepository();
    }

    // =========================
    // 🔹 CREAR ORDEN
    // =========================
    public String crearOrden(
            int idProveedor,
            List<DetalleOrdenCompra> detalles
    ) {

        Connection conn = null;

        try {

            // =========================
            // VALIDACIONES
            // =========================

            validarProveedor(idProveedor);

            validarDetalles(detalles);

            // =========================
            // TRANSACCIÓN
            // =========================

            conn =
                    DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            // =========================
            // CREAR CABECERA
            // =========================

            OrdenCompra orden =
                    new OrdenCompra();

            orden.setIdProveedor(idProveedor);

            orden.setEstado("Pendiente");

            int idOrden =
                    ordenRepo.crear(orden);

            if (idOrden <= 0) {

                throw new BusinessException(
                        "No se pudo crear la orden."
                );
            }

            // =========================
            // ASIGNAR ORDEN A DETALLES
            // =========================

            for (DetalleOrdenCompra d : detalles) {

                d.setIdOrden(idOrden);
            }

            // =========================
            // INSERTAR DETALLES
            // =========================

            boolean guardado =
                    detalleRepo.insertarLista(
                            detalles,
                            conn
                    );

            if (!guardado) {

                throw new BusinessException(
                        "Error guardando detalle."
                );
            }

            conn.commit();

            System.out.println(
                    "✅ Orden creada correctamente."
            );

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            rollback(conn);

            return "Error creando orden.";

        } finally {

            cerrarConexion(conn);
        }
    }

    // =========================
    // 🔹 LISTAR TODAS
    // =========================
    public List<OrdenCompra> listarTodas() {

        return ordenRepo.listarTodas();
    }

    // =========================
    // 🔹 LISTAR POR ESTADO
    // =========================
    public List<OrdenCompra> listarPorEstado(
            String estado
    ) {

        validarEstado(estado);

        return ordenRepo.listarPorEstado(
                estado
        );
    }

    // =========================
    // 🔹 LISTAR PENDIENTES
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {

        return ordenRepo.listarPendientes();
    }

    // =========================
    // 🔹 FACTURA COMPRA
    // =========================
    public List<OrdenCompra> obtenerOrdenesParaFacturaCompra() {

        return ordenRepo
                .obtenerOrdenesParaFacturaCompra();
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(
            int idOrden
    ) {

        return detalleRepo.listarPorOrden(
                idOrden
        );
    }

    // =========================
    // 🔹 BUSCAR ORDEN
    // =========================
    public OrdenCompra buscarPorId(
            int idOrden
    ) {

        return ordenRepo.buscarPorId(
                idOrden
        );
    }

    // =========================
    // 🔥 VALIDAR PROVEEDOR
    // =========================
    private void validarProveedor(
            int idProveedor
    ) {

        if (idProveedor <= 0) {

            throw new BusinessException(
                    "Proveedor inválido."
            );
        }
    }

    // =========================
    // 🔥 VALIDAR DETALLES
    // =========================
    private void validarDetalles(
            List<DetalleOrdenCompra> detalles
    ) {

        if (
                detalles == null
                        || detalles.isEmpty()
        ) {

            throw new BusinessException(
                    "Debe agregar productos."
            );
        }

        for (DetalleOrdenCompra d : detalles) {

            if (d.getIdItem() <= 0) {

                throw new BusinessException(
                        "Producto inválido."
                );
            }

            if (d.getCantidadPedida() <= 0) {

                throw new BusinessException(
                        "Cantidad inválida."
                );
            }

            /*
             * 🔥 IMPORTANTE
             *
             * Más adelante el precio
             * debería salir de factura compra
             * NO desde orden de compra.
             *
             * Por ahora se deja compatible.
             */
            if (d.getPrecioUnitario() < 0) {

                throw new BusinessException(
                        "Precio inválido."
                );
            }
        }
    }

    // =========================
    // 🔥 VALIDAR ESTADO ERP
    // =========================
    private void validarEstado(
            String estado
    ) {

        if (
                estado == null
                        || estado.trim().isEmpty()
        ) {

            return;
        }

        switch (estado) {

            case "Pendiente":

            case "Parcial":

            case "Recibido":

            case "Facturado":

            case "Cancelado":

                break;

            default:

                throw new BusinessException(
                        "Estado inválido."
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
            }

        } catch (Exception e) {

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

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}