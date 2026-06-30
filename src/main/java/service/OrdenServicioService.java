package service;

import exception.BusinessException;
import model.DetalleOrdenServicio;
import model.OrdenServicio;
import repository.DetalleOrdenServicioRepository;
import repository.OrdenServicioRepository;
import util.DatabaseConnection;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Componente de servicio encargado de orquestar la lógica de negocio, validaciones transaccionales
 * y flujos operacionales del ciclo de vida de las órdenes de servicio.
 */
public class OrdenServicioService {

    private final OrdenServicioRepository ordenRepo;
    private final DetalleOrdenServicioRepository detalleRepo;

    public OrdenServicioService() {
        this.ordenRepo = new OrdenServicioRepository();
        this.detalleRepo = new DetalleOrdenServicioRepository();
    }

    /**
     * Construye y procesa una nueva orden de servicio bajo el estado inicial por defecto.
     */
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

    /**
     * Ejecuta el registro transaccional y atómico de la orden de servicio junto con todos sus detalles vinculados.
     */
    public String crearOrden(OrdenServicio orden, List<DetalleOrdenServicio> detalles) {
        Connection conn = null;
        try {
            validarOrden(orden);
            validarDetalles(detalles);

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (orden.getEstado() == null || orden.getEstado().trim().isEmpty()) {
                orden.setEstado("Pendiente");
            }

            int idOrden = ordenRepo.crear(orden, conn);
            if (idOrden <= 0) {
                throw new BusinessException("No se pudo generar el registro de la cabecera de la orden de servicio.");
            }

            for (DetalleOrdenServicio d : detalles) {
                d.setIdOrdenServicio(idOrden);
            }

            boolean detallesGuardados = detalleRepo.insertarLista(detalles, conn);
            if (!detallesGuardados) {
                throw new BusinessException("No se pudo registrar la lista de ítems detallados de la orden.");
            }

            conn.commit();
            return "OK";

        } catch (BusinessException e) {
            DatabaseUtil.rollbackSilencioso(conn);
            return e.getMessage();
        } catch (Exception e) {
            System.err.println("Error sistémico al procesar la creación transaccional de la orden: " + e.getMessage());
            DatabaseUtil.rollbackSilencioso(conn);
            return "Error interno del sistema al registrar la orden de servicio.";
        } finally {
            DatabaseUtil.cerrarConexionTransaccional(conn);
        }
    }

    /**
     * Cambia de forma directa el estado de una orden de servicio validando previamente las reglas del ERP.
     */
    public String actualizarEstado(int idOrdenServicio, String nuevoEstado) {
        try {
            validarEstado(nuevoEstado);
            boolean exito = ordenRepo.actualizarEstado(idOrdenServicio, nuevoEstado);
            return exito ? "OK" : "No se pudo actualizar el estado de la orden en la base de datos.";
        } catch (BusinessException e) {
            return e.getMessage();
        }
    }

    public List<OrdenServicio> listarTodas() {
        return ordenRepo.listarTodas();
    }

    public List<OrdenServicio> listarPorEstado(String estado) {
        try {
            validarEstado(estado);
            return ordenRepo.listarPorEstado(estado);
        } catch (BusinessException e) {
            System.err.println("Advertencia operativa: " + e.getMessage());
            return ordenRepo.listarPorEstado(estado);
        }
    }

    public List<DetalleOrdenServicio> obtenerDetalleOrden(int idOrdenServicio) {
        return detalleRepo.listarPorOrden(idOrdenServicio);
    }

    public OrdenServicio buscarPorId(int idOrdenServicio) {
        return ordenRepo.buscarPorId(idOrdenServicio);
    }

    // =========================================================================
    // 🔥 MÉTODOS DE INTEGRACIÓN AJUSTADOS AL REPOSITORIO REAL DEL COMPONENTE
    // =========================================================================

    /**
     * Añade un único material al detalle abriendo una conexión transaccional dedicada.
     */
    public String agregarMaterialIndividual(DetalleOrdenServicio detalle) {
        Connection conn = null;
        try {
            List<DetalleOrdenServicio> listaUnitaria = new ArrayList<>();
            listaUnitaria.add(detalle);
            validarDetalles(listaUnitaria);

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Reutiliza el método por lotes envolviendo el objeto individual en la lista
            boolean exito = detalleRepo.insertarLista(listaUnitaria, conn);

            if (exito) {
                conn.commit();
                return "OK";
            } else {
                DatabaseUtil.rollbackSilencioso(conn);
                return "No se pudo insertar el material en el registro seleccionado.";
            }
        } catch (BusinessException e) {
            DatabaseUtil.rollbackSilencioso(conn);
            return e.getMessage();
        } catch (Exception e) {
            System.err.println("Fallo al insertar material individual: " + e.getMessage());
            DatabaseUtil.rollbackSilencioso(conn);
            return "Error interno del sistema al agregar el insumo.";
        } finally {
            DatabaseUtil.cerrarConexionTransaccional(conn);
        }
    }

    /**
     * Actualiza la cantidad asignada a un material usando la firma compuesta del repositorio.
     */
    public String actualizarCantidadDetalle(int idOrdenServicio, String tipoItem, int idReferencia, int nuevaCantidad) {
        Connection conn = null;
        try {
            if (nuevaCantidad <= 0) {
                throw new BusinessException("La cantidad asignada debe ser superior a cero.");
            }

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Se consume la firma: (conn, idOrden, tipoItem, idReferencia, nuevaCantidad)
            boolean exito = detalleRepo.actualizarCantidad(conn, idOrdenServicio, tipoItem, idReferencia, nuevaCantidad);

            if (exito) {
                conn.commit();
                return "OK";
            } else {
                DatabaseUtil.rollbackSilencioso(conn);
                return "No se logró actualizar la cantidad en el almacén de datos.";
            }
        } catch (BusinessException e) {
            DatabaseUtil.rollbackSilencioso(conn);
            return e.getMessage();
        } catch (Exception e) {
            System.err.println("Fallo al modificar cantidad: " + e.getMessage());
            DatabaseUtil.rollbackSilencioso(conn);
            return "Error interno al intentar editar la cantidad del material.";
        } finally {
            DatabaseUtil.cerrarConexionTransaccional(conn);
        }
    }

    /**
     * Elimina una línea de componente mediante el buscador compuesto transaccional.
     */
    public String eliminarDetallePorId(int idOrdenServicio, String tipoItem, int idReferencia) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Se consume la firma: (conn, idOrden, tipoItem, idReferencia)
            boolean exito = detalleRepo.eliminarItem(conn, idOrdenServicio, tipoItem, idReferencia);

            if (exito) {
                conn.commit();
                return "OK";
            } else {
                DatabaseUtil.rollbackSilencioso(conn);
                return "El ítem de detalle especificado no pudo ser borrado.";
            }
        } catch (Exception e) {
            System.err.println("Fallo al eliminar detalle: " + e.getMessage());
            DatabaseUtil.rollbackSilencioso(conn);
            return "Error interno del sistema al intentar remover el material.";
        } finally {
            DatabaseUtil.cerrarConexionTransaccional(conn);
        }
    }

    // =========================================================================
    // VALIDACIONES INTERNAS
    // =========================================================================

    private void validarOrden(OrdenServicio orden) {
        if (orden == null) throw new BusinessException("Los datos de la orden de servicio no pueden ser nulos.");
        if (orden.getIdCliente() <= 0) throw new BusinessException("Debe seleccionar un cliente válido.");
        if (orden.getFechaProgramada() == null) throw new BusinessException("La fecha programada para el servicio es obligatoria.");

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0); calHoy.set(Calendar.MINUTE, 0); calHoy.set(Calendar.SECOND, 0); calHoy.set(Calendar.MILLISECOND, 0);
        Date hoySinHora = new Date(calHoy.getTimeInMillis());

        if (orden.getFechaProgramada().before(hoySinHora)) {
            throw new BusinessException("La fecha programada no puede ser inferior al día actual.");
        }
        if (orden.getPrioridad() == null || orden.getPrioridad().trim().isEmpty()) {
            throw new BusinessException("Debe asignar una prioridad válida a la orden.");
        }
    }

    private void validarDetalles(List<DetalleOrdenServicio> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new BusinessException("La orden debe contener por lo menos un ítem o servicio.");
        }

        for (DetalleOrdenServicio d : detalles) {
            if (d.getTipoItem() == null || d.getTipoItem().trim().isEmpty()) {
                throw new BusinessException("El tipo de ítem del detalle no ha sido especificado.");
            }

            String tipo = d.getTipoItem().toUpperCase();
            if ("SERVICIO".equals(tipo)) {
                if (d.getIdServicio() == null || d.getIdServicio() <= 0) {
                    throw new BusinessException("El detalle contiene una referencia de servicio inválida.");
                }
            } else if ("PRODUCTO".equals(tipo)) {
                if (d.getIdProducto() == null || d.getIdProducto() <= 0) {
                    throw new BusinessException("El detalle contiene una referencia de producto inválida.");
                }
            } else {
                throw new BusinessException("El tipo de ítem '" + d.getTipoItem() + "' no es soportado por el sistema.");
            }

            if (d.getCantidad() <= 0) {
                throw new BusinessException("La cantidad asignada a los ítems debe ser superior a cero.");
            }
            if (d.getPrecioUnitario() < 0) {
                throw new BusinessException("El valor del precio unitario no puede registrarse como negativo.");
            }
        }
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) return;
        String estadoNormalizado = estado.trim();
        switch (estadoNormalizado) {
            case "Pendiente":
            case "Asignada":
            case "En ejecución":
            case "Finalizada":
            case "Cancelada":
                break;
            default:
                throw new BusinessException("El estado '" + estado + "' viola las políticas de transición del flujo de trabajo.");
        }
    }
}