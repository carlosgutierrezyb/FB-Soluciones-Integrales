package service;

import exception.BusinessException;
import model.OrdenServicioTecnico;
import repository.OrdenServicioRepository;
import repository.OrdenServicioTecnicoRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Componente de servicios de negocio para la gestión operativa y asignación
 * de técnicos asociados a las órdenes de servicio.
 */
public class OrdenServicioTecnicoService {

    private final OrdenServicioTecnicoRepository asignacionRepo;
    private final OrdenServicioRepository ordenRepo;

    public OrdenServicioTecnicoService() {
        this.asignacionRepo = new OrdenServicioTecnicoRepository();
        this.ordenRepo = new OrdenServicioRepository();
    }

    /**
     * Procesa la asignación de un técnico a una orden de servicio, actualizando el estado de la cabecera.
     */
    public String asignarTecnico(OrdenServicioTecnico asignacion) {
        if (asignacionRepo.existeAsignacion(asignacion.getIdOrdenServicio(), asignacion.getIdTecnico())) {
            return "El técnico ya se encuentra asignado a esta orden de servicio.";
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean asignado = asignacionRepo.asignarTecnico(asignacion);
            if (!asignado) throw new BusinessException("No se pudo registrar la asignación del técnico.");

            boolean ordenActualizada = ordenRepo.actualizarEstadoTransaccional(asignacion.getIdOrdenServicio(), "Asignada", conn);
            if (!ordenActualizada) throw new BusinessException("No se pudo actualizar el estado general de la orden.");

            conn.commit();
            return "OK";
        } catch (Exception e) {
            rollback(conn);
            return "Error al asignar técnico: " + e.getMessage();
        } finally {
            cerrarConexion(conn);
        }
    }

    /**
     * Registra el inicio de actividades de un técnico y cambia el estado macro de la orden a 'En ejecución'.
     */
    public String iniciarServicio(int idAsignacion) {
        OrdenServicioTecnico asignacion = asignacionRepo.buscarPorId(idAsignacion);
        if (asignacion == null) return "La asignación seleccionada no existe.";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean tecnicoOk = asignacionRepo.actualizarEstadoTransaccional(idAsignacion, "En ejecución", conn);
            if (!tecnicoOk) throw new BusinessException("No se pudo actualizar el estado del técnico.");

            boolean ordenOk = ordenRepo.actualizarEstadoTransaccional(asignacion.getIdOrdenServicio(), "En ejecución", conn);
            if (!ordenOk) throw new BusinessException("No se pudo actualizar el estado de la orden maestra.");

            conn.commit();
            return "Labor del técnico iniciada correctamente. Estado de la Orden: En ejecución.";
        } catch (Exception e) {
            rollback(conn);
            return "Error al iniciar el servicio: " + e.getMessage();
        } finally {
            cerrarConexion(conn);
        }
    }

    /**
     * Finaliza la labor de un técnico, registra sus horas y evalúa si corresponde cerrar la orden de forma macro.
     */
    public String finalizarServicio(int idAsignacion, double horas) {
        OrdenServicioTecnico asignacion = asignacionRepo.buscarPorId(idAsignacion);
        if (asignacion == null) return "La asignación seleccionada no existe.";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean horasOk = asignacionRepo.registrarHorasTransaccionales(idAsignacion, horas, conn);
            boolean estadoOk = asignacionRepo.actualizarEstadoTransaccional(idAsignacion, "Finalizado", conn);

            if (!horasOk || !estadoOk) throw new BusinessException("No se pudieron actualizar los datos de la asignación.");

            List<OrdenServicioTecnico> todas = asignacionRepo.listarPorOrden(asignacion.getIdOrdenServicio());
            boolean todasFinalizadas = true;

            for (OrdenServicioTecnico ost : todas) {
                if (ost.getIdAsignacion() == idAsignacion) {
                    continue; // Omitir el estado previo en memoria del registro actual
                }
                if (!"Finalizado".equalsIgnoreCase(ost.getEstado())) {
                    todasFinalizadas = false;
                    break;
                }
            }

            if (todasFinalizadas) {
                boolean ordenOk = ordenRepo.actualizarEstadoTransaccional(asignacion.getIdOrdenServicio(), "Finalizada", conn);
                if (!ordenOk) throw new BusinessException("No se pudo cerrar la orden maestra.");
                conn.commit();
                return "Labor finalizada. ¡Todos los técnicos han terminado! Orden de servicio cerrada como FINALIZADA.";
            }

            conn.commit();
            return "Labor de este técnico finalizada con éxito. La orden general sigue 'En ejecución' esperando a los demás técnicos.";
        } catch (Exception e) {
            rollback(conn);
            return "Error al finalizar el servicio: " + e.getMessage();
        } finally {
            cerrarConexion(conn);
        }
    }

    /**
     * Remueve físicamente de forma transaccional la asignación de un técnico de una orden de servicio.
     *
     * @param idOrdenServicio Identificador de la orden.
     * @param idTecnico       Identificador del técnico.
     * @return "OK" si se eliminó con éxito, o el mensaje de error correspondiente.
     */
    public String eliminarAsignacion(int idOrdenServicio, int idTecnico) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Invocar la eliminación física del registro en la base de datos pasándole la conexión
            boolean eliminado = asignacionRepo.eliminarAsignacionTransaccional(idOrdenServicio, idTecnico, conn);
            if (!eliminado) throw new BusinessException("No se encontró o no se pudo remover el registro de asignación.");

            // 2. Lógica de negocio complementaria: si ya no quedan técnicos asignados, regresar la orden a "Pendiente"
            List<OrdenServicioTecnico> restantes = asignacionRepo.listarPorOrden(idOrdenServicio);
            if (restantes.isEmpty()) {
                ordenRepo.actualizarEstadoTransaccional(idOrdenServicio, "Pendiente", conn);
            }

            conn.commit();
            return "OK";
        } catch (Exception e) {
            rollback(conn);
            return "Error al remover el técnico: " + e.getMessage();
        } finally {
            cerrarConexion(conn);
        }
    }

    public List<OrdenServicioTecnico> listarPorOrden(int idOrdenServicio) {
        return asignacionRepo.listarPorOrden(idOrdenServicio);
    }

    public OrdenServicioTecnico buscarPorId(int idAsignacion) {
        return asignacionRepo.buscarPorId(idAsignacion);
    }

    private void rollback(Connection conn) {
        try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
    }

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