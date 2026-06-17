package service;

import exception.BusinessException;
import model.DetalleOrdenServicio;
import model.OrdenServicio;
import repository.DetalleOrdenServicioRepository;
import repository.OrdenServicioRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Service de órdenes de servicio.
 *
 * 🔥 ERP PRO:
 * - Manejo transaccional (Cabecera + Detalles)
 * - Control estricto de estados maestros del ERP
 * - Desacoplado de la lógica operativa directa de técnicos
 * - Programación de servicios mixtos (Servicios + Productos)
 */
public class OrdenServicioService {

    private final OrdenServicioRepository ordenRepo;
    private final DetalleOrdenServicioRepository detalleRepo;

    public OrdenServicioService() {
        this.ordenRepo = new OrdenServicioRepository();
        this.detalleRepo = new DetalleOrdenServicioRepository();
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

        OrdenServicio orden = new OrdenServicio();
        orden.setIdCliente(idCliente);
        orden.setFechaProgramada(fechaProgramada);
        orden.setPrioridad(prioridad);
        orden.setDireccionServicio(direccion);
        orden.setContactoNombre(contacto);
        orden.setContactoTelefono(telefono);
        orden.setObservaciones(observaciones);
        orden.setEstado("Pendiente");

        return crearOrden(orden, detalles);
    }

    // =========================
    // 🔹 CREAR ORDEN (TRANSACCIONAL)
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
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // =========================
            // ESTADO INICIAL
            // =========================
            if (orden.getEstado() == null || orden.getEstado().trim().isEmpty()) {
                orden.setEstado("Pendiente");
            }

            // =========================
            // CREAR CABECERA
            // =========================
            int idOrden = ordenRepo.crear(orden, conn);

            if (idOrden <= 0) {
                throw new BusinessException("No se pudo crear la orden de servicio.");
            }

            // =========================
            // ASIGNAR ID ORDEN A LOS DETALLES
            // =========================
            for (DetalleOrdenServicio d : detalles) {
                d.setIdOrdenServicio(idOrden);
            }

            // =========================
            // INSERTAR DETALLES
            // =========================
            boolean guardado = detalleRepo.insertarLista(detalles, conn);

            if (!guardado) {
                throw new BusinessException("Error guardando el detalle de la orden.");
            }

            // =========================
            // COMMIT
            // =========================
            conn.commit();
            System.out.println("✅ Orden de servicio creada correctamente.");
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
    // 🔹 ACTUALIZAR ESTADO DIRECTO
    // =========================
    public String actualizarEstado(int idOrdenServicio, String nuevoEstado) {
        try {
            validarEstado(nuevoEstado);
            boolean exito = ordenRepo.actualizarEstado(idOrdenServicio, nuevoEstado);
            return exito ? "OK" : "No se pudo cambiar el estado de la orden.";
        } catch (BusinessException e) {
            return e.getMessage();
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
    public List<OrdenServicio> listarPorEstado(String estado) {
        try {
            validarEstado(estado);
            return ordenRepo.listarPorEstado(estado);
        } catch (BusinessException e) {
            System.err.println("❌ " + e.getMessage());
            return ordenRepo.listarPorEstado(estado); // fallback por seguridad
        }
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================
    public List<DetalleOrdenServicio> obtenerDetalleOrden(int idOrdenServicio) {
        return detalleRepo.listarPorOrden(idOrdenServicio);
    }

    // =========================
    // 🔹 BUSCAR ORDEN POR ID
    // =========================
    public OrdenServicio buscarPorId(int idOrdenServicio) {
        return ordenRepo.buscarPorId(idOrdenServicio);
    }

    // =========================
    // 🔥 VALIDAR ORDEN
    // =========================
    private void validarOrden(OrdenServicio orden) {
        if (orden == null) {
            throw new BusinessException("La orden es obligatoria.");
        }

        if (orden.getIdCliente() <= 0) {
            throw new BusinessException("Cliente inválido.");
        }

        if (orden.getFechaProgramada() == null) {
            throw new BusinessException("La fecha programada es obligatoria.");
        }

        // Normalización limpia de fechas para permitir el día actual sin desfases
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);
        Date hoySinHora = new Date(calHoy.getTimeInMillis());

        if (orden.getFechaProgramada().before(hoySinHora)) {
            throw new BusinessException("La fecha programada no puede ser anterior a hoy.");
        }

        if (orden.getPrioridad() == null || orden.getPrioridad().trim().isEmpty()) {
            throw new BusinessException("La prioridad es obligatoria.");
        }
    }

    // =========================
    // 🔥 VALIDAR DETALLES
    // =========================
    private void validarDetalles(List<DetalleOrdenServicio> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new BusinessException("Debe agregar al menos un servicio o producto.");
        }

        for (DetalleOrdenServicio d : detalles) {
            if (d.getTipoItem() == null || d.getTipoItem().trim().isEmpty()) {
                throw new BusinessException("Tipo de ítem inválido.");
            }

            if ("SERVICIO".equalsIgnoreCase(d.getTipoItem())) {
                if (d.getIdServicio() == null || d.getIdServicio() <= 0) {
                    throw new BusinessException("Servicio inválido.");
                }
            } else if ("PRODUCTO".equalsIgnoreCase(d.getTipoItem())) {
                if (d.getIdProducto() == null || d.getIdProducto() <= 0) {
                    throw new BusinessException("Producto inválido.");
                }
            } else {
                throw new BusinessException("Tipo de ítem desconocido: " + d.getTipoItem());
            }

            if (d.getCantidad() <= 0) {
                throw new BusinessException("Cantidad inválida.");
            }

            if (d.getPrecioUnitario() < 0) {
                throw new BusinessException("Precio inválido.");
            }
        }
    }

    // =========================
    // 🔥 VALIDAR ESTADO ERP
    // =========================
    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return;
        }

        switch (estado) {
            // 🔥 CORRECCIÓN: Flujo de control maestro unificado (Pendiente, Asignada, En ejecución, Finalizada, Cancelada)
            case "Pendiente":
            case "Asignada":
            case "En ejecución":
            case "Finalizada":
            case "Cancelada":
                break;
            default:
                throw new BusinessException("Estado inválido para el flujo ERP: " + estado);
        }
    }

    // =========================
    // 🔧 ROLLBACK
    // =========================
    private void rollback(Connection conn) {
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