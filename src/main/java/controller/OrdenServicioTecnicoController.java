package controller;

import model.OrdenServicioTecnico;
import repository.OrdenServicioTecnicoRepository;
import repository.OrdenServicioRepository; // 🔄 Necesario para actualizar el estado macro de la orden

import java.util.List;

/**
 * Controller para la gestión de técnicos
 * asignados a órdenes de servicio.
 *
 * ERP F&B
 */
public class OrdenServicioTecnicoController {

    private final OrdenServicioTecnicoRepository repository;
    private final OrdenServicioRepository ordenRepository; // 🛠️ Añadido para controlar el flujo de la Orden Maestra

    public OrdenServicioTecnicoController() {
        this.repository = new OrdenServicioTecnicoRepository();
        this.ordenRepository = new OrdenServicioRepository();
    }

    // =========================
    // 🛠️ SOLUCIÓN AL ROJO 1: ASIGNAR TÉCNICO
    // =========================
    public String asignarTecnico(OrdenServicioTecnico asignacion) {
        if (asignacion == null) return "Datos de asignación inválidos.";

        boolean exito = repository.asignarTecnico(asignacion);
        if (exito) {
            // Si pasamos a "Asignado", actualizamos el estado de la cabecera general a "Asignada"
            ordenRepository.actualizarEstado(asignacion.getIdOrdenServicio(), "Asignada");
            return "OK";
        }
        return "No se pudo registrar la asignación del técnico en la Base de Datos.";
    }

    // =========================
    // 🛠️ SOLUCIÓN AL ROJO 2: INICIAR SERVICIO
    // =========================
    public String iniciarServicio(int idAsignacion) {
        OrdenServicioTecnico asignacion = repository.buscarPorId(idAsignacion);
        if (asignacion == null) return "La asignación seleccionada no existe.";

        // 1. Cambiamos el estado del técnico a "En ejecución"
        boolean exitoTecnico = repository.actualizarEstado(idAsignacion, "En ejecución");

        if (exitoTecnico) {
            // 2. En cascada automática, la orden maestra pasa a "En ejecución"
            ordenRepository.actualizarEstado(asignacion.getIdOrdenServicio(), "En ejecución");
            return "Labor del técnico iniciada correctamente. Estado de la Orden: En ejecución.";
        }
        return "Error al intentar iniciar la labor del técnico.";
    }

    // =========================
    // 🛠️ SOLUCIÓN AL ROJO 3: FINALIZAR SERVICIO
    // =========================
    public String finalizarServicio(int idAsignacion, double horas) {
        OrdenServicioTecnico asignacion = repository.buscarPorId(idAsignacion);
        if (asignacion == null) return "La asignación seleccionada no existe.";

        // 1. Registramos sus horas invertidas
        boolean horasOk = repository.registrarHoras(idAsignacion, horas);
        // 2. Pasamos su estado particular a "Finalizado"
        boolean estadoOk = repository.actualizarEstado(idAsignacion, "Finalizado");

        if (horasOk && estadoOk) {
            // 3. Evaluamos si TODOS los técnicos asignados a esta misma orden ya terminaron
            List<OrdenServicioTecnico> todas = repository.listarPorOrden(asignacion.getIdOrdenServicio());
            boolean todasFinalizadas = true;

            for (OrdenServicioTecnico ost : todas) {
                if (!"Finalizado".equalsIgnoreCase(ost.getEstado())) {
                    todasFinalizadas = false;
                    break;
                }
            }

            // 4. Si no quedan técnicos pendientes de terminar, cerramos la Orden Maestra por completo
            if (todasFinalizadas) {
                ordenRepository.actualizarEstado(asignacion.getIdOrdenServicio(), "Finalizada");
                return "Labor finalizada. ¡Todos los técnicos han terminado! Orden de servicio cerrada como FINALIZADA.";
            }

            return "Labor de este técnico finalizada con éxito. La orden general sigue 'En ejecución' esperando a los demás técnicos.";
        }
        return "Error al registrar el cierre de labor del técnico.";
    }

    // =========================
    // LISTAR POR ORDEN
    // =========================
    public List<OrdenServicioTecnico> listarPorOrden(int idOrdenServicio) {
        return repository.listarPorOrden(idOrdenServicio);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public OrdenServicioTecnico buscarPorId(int idAsignacion) {
        return repository.buscarPorId(idAsignacion);
    }

    // =========================
    // ACTUALIZAR ESTADO (MÉTODO BASE)
    // =========================
    public boolean actualizarEstado(int idAsignacion, String estado) {
        return repository.actualizarEstado(idAsignacion, estado);
    }

    // =========================
    // REGISTRAR HORAS (MÉTODO BASE)
    // =========================
    public boolean registrarHoras(int idAsignacion, double horas) {
        return repository.registrarHoras(idAsignacion, horas);
    }

    // =========================
    // ACTUALIZAR OBSERVACIONES
    // =========================
    public boolean actualizarObservaciones(int idAsignacion, String observaciones) {
        return repository.actualizarObservaciones(idAsignacion, observaciones);
    }

    // =========================
    // ELIMINAR ASIGNACIÓN
    // =========================
    public boolean eliminarAsignacion(int idAsignacion) {
        return repository.eliminarAsignacion(idAsignacion);
    }
}