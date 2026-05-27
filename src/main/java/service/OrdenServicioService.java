package service;

import exception.BusinessException;
import model.DetalleOrdenServicio;
import model.OrdenServicio;
import repository.DetalleOrdenServicioRepository;
import repository.OrdenServicioRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * Service de órdenes de servicio.
 *
 * 🔥 ERP PRO:
 * - Manejo transaccional
 * - Control de estados
 * - Flujo operativo
 * - Programación de servicios
 * - Servicios + Productos
 */
public class OrdenServicioService {

    private final OrdenServicioRepository ordenRepo;

    private final DetalleOrdenServicioRepository detalleRepo;

    public OrdenServicioService() {

        this.ordenRepo =
                new OrdenServicioRepository();

        this.detalleRepo =
                new DetalleOrdenServicioRepository();
    }

    // =========================
    // 🔹 CREAR ORDEN SERVICIO
    // =========================
    public String crearOrdenServicio(
            int idCliente,
            Date fechaProgramada,
            String prioridad,
            String direccion,
            String contacto,
            String telefono,
            String observaciones,
            List<DetalleOrdenServicio> detalles
    ) {

        OrdenServicio orden =
                new OrdenServicio();

        orden.setIdCliente(idCliente);

        orden.setFechaProgramada(
                fechaProgramada
        );

        orden.setPrioridad(
                prioridad
        );

        orden.setDireccionServicio(
                direccion
        );

        orden.setContactoNombre(
                contacto
        );

        orden.setContactoTelefono(
                telefono
        );

        orden.setObservaciones(
                observaciones
        );

        orden.setEstado(
                "Pendiente"
        );

        return crearOrden(
                orden,
                detalles
        );
    }

    // =========================
    // 🔹 CREAR ORDEN
    // =========================
    public String crearOrden(
            OrdenServicio orden,
            List<DetalleOrdenServicio> detalles
    ) {

        Connection conn = null;

        try {

            // =========================
            // VALIDACIONES
            // =========================

            validarOrden(orden);

            validarDetalles(detalles);

            // =========================
            // TRANSACCIÓN
            // =========================

            conn =
                    DatabaseConnection.getConnection();

            conn.setAutoCommit(false);

            // =========================
            // ESTADO INICIAL
            // =========================

            if (
                    orden.getEstado() == null
                            || orden.getEstado().trim().isEmpty()
            ) {

                orden.setEstado(
                        "Pendiente"
                );
            }

            // =========================
            // CREAR CABECERA
            // =========================

            int idOrden =
                    ordenRepo.crear(
                            orden,
                            conn
                    );

            if (idOrden <= 0) {

                throw new BusinessException(
                        "No se pudo crear la orden de servicio."
                );
            }

            // =========================
            // ASIGNAR ID ORDEN
            // =========================

            for (DetalleOrdenServicio d : detalles) {

                d.setIdOrdenServicio(
                        idOrden
                );
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

            // =========================
            // COMMIT
            // =========================

            conn.commit();

            System.out.println(
                    "✅ Orden de servicio creada correctamente."
            );

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            rollback(conn);

            return "Error creando orden de servicio.";

        } finally {

            cerrarConexion(conn);
        }
    }

    // =========================
    // 🔹 LISTAR TODAS
    // =========================
    public List<OrdenServicio> listarTodas() {

        return ordenRepo.listarTodas();
    }

    // =========================
    // 🔹 LISTAR POR ESTADO
    // =========================
    public List<OrdenServicio> listarPorEstado(
            String estado
    ) {

        validarEstado(estado);

        return ordenRepo.listarPorEstado(
                estado
        );
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================
    public List<DetalleOrdenServicio> obtenerDetalleOrden(
            int idOrdenServicio
    ) {

        return detalleRepo.listarPorOrden(
                idOrdenServicio
        );
    }

    // =========================
    // 🔹 BUSCAR ORDEN
    // =========================
    public OrdenServicio buscarPorId(
            int idOrdenServicio
    ) {

        return ordenRepo.buscarPorId(
                idOrdenServicio
        );
    }

    // =========================
    // 🔥 VALIDAR ORDEN
    // =========================
    private void validarOrden(
            OrdenServicio orden
    ) {

        if (orden == null) {

            throw new BusinessException(
                    "La orden es obligatoria."
            );
        }

        if (orden.getIdCliente() <= 0) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        if (
                orden.getFechaProgramada()
                        == null
        ) {

            throw new BusinessException(
                    "La fecha programada es obligatoria."
            );
        }

        Date hoy =
                new Date(
                        System.currentTimeMillis()
                );

        if (
                orden.getFechaProgramada()
                        .before(hoy)
        ) {

            throw new BusinessException(
                    "La fecha programada no puede ser anterior a hoy."
            );
        }

        if (
                orden.getPrioridad() == null
                        || orden.getPrioridad().trim().isEmpty()
        ) {

            throw new BusinessException(
                    "La prioridad es obligatoria."
            );
        }
    }

    // =========================
// 🔥 VALIDAR DETALLES
// =========================
    private void validarDetalles(
            List<DetalleOrdenServicio> detalles
    ) {

        if (
                detalles == null
                        || detalles.isEmpty()
        ) {

            throw new BusinessException(
                    "Debe agregar servicios o productos."
            );
        }

        for (DetalleOrdenServicio d : detalles) {

            // =========================
            // VALIDAR TIPO
            // =========================

            if (
                    d.getTipoItem() == null
                            || d.getTipoItem().trim().isEmpty()
            ) {

                throw new BusinessException(
                        "Tipo de ítem inválido."
                );
            }

            // =========================
            // VALIDAR SERVICIO
            // =========================

            if (
                    d.getTipoItem().equalsIgnoreCase("SERVICIO")
            ) {

                if (
                        d.getIdServicio() == null
                                || d.getIdServicio() <= 0
                ) {

                    throw new BusinessException(
                            "Servicio inválido."
                    );
                }
            }

            // =========================
            // VALIDAR PRODUCTO
            // =========================

            else if (
                    d.getTipoItem().equalsIgnoreCase("PRODUCTO")
            ) {

                if (
                        d.getIdProducto() == null
                                || d.getIdProducto() <= 0
                ) {

                    throw new BusinessException(
                            "Producto inválido."
                    );
                }
            }

            else {

                throw new BusinessException(
                        "Tipo de ítem desconocido."
                );
            }

            // =========================
            // VALIDAR CANTIDAD
            // =========================

            if (d.getCantidad() <= 0) {

                throw new BusinessException(
                        "Cantidad inválida."
                );
            }

            // =========================
            // VALIDAR PRECIO
            // =========================

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

            case "Agendada":

            case "En ejecución":

            case "Ejecutada":

            case "Facturada":

            case "Cancelada":

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