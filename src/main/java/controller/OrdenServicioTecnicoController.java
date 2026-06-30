package controller;

import model.OrdenServicioTecnico;
import service.OrdenServicioTecnicoService;

import java.util.List;

/**
 * Controlador encargado de mediar entre la interfaz de usuario y la capa de servicios
 * para la gestión de asignaciones de técnicos a órdenes de servicio.
 */
public class OrdenServicioTecnicoController {

    private final OrdenServicioTecnicoService service;

    public OrdenServicioTecnicoController() {
        this.service = new OrdenServicioTecnicoService();
    }

    /**
     * Solicita la asignación de un técnico a una orden de servicio.
     *
     * @param asignacion Datos de la asignación.
     * @return Cadena de resultado "OK" o el mensaje de error correspondiente.
     */
    public String asignarTecnico(OrdenServicioTecnico asignacion) {
        if (asignacion == null) return "Datos de asignación inválidos.";
        return service.asignarTecnico(asignacion);
    }

    /**
     * Registra el inicio de las labores asignadas a un técnico.
     *
     * @param idAsignacion Identificador de la asignación.
     * @return Mensaje de respuesta sobre el estado de la operación.
     */
    public String iniciarServicio(int idAsignacion) {
        if (idAsignacion <= 0) return "Identificador de asignación inválido.";
        return service.iniciarServicio(idAsignacion);
    }

    /**
     * Registra la conclusión de las actividades del técnico y sus horas acumuladas.
     *
     * @param idAsignacion    Identificador de la asignación.
     * @param horasTrabajadas Horas invertidas en el servicio.
     * @return Mensaje de respuesta sobre el cierre de la asignación.
     */
    public String finalizarServicio(int idAsignacion, double horasTrabajadas) {
        if (idAsignacion <= 0) return "Identificador de asignación inválido.";
        if (horasTrabajadas < 0) return "La cantidad de horas no puede ser negativa.";
        return service.finalizarServicio(idAsignacion, horasTrabajadas);
    }

    /**
     * Remueve la asignación de un técnico de una orden de servicio específica.
     *
     * @param idOrdenServicio Identificador de la orden maestra.
     * @param idTecnico       Identificador del técnico a remover.
     * @return Cadena de resultado "OK" o el mensaje de error de la capa intermedia.
     */
    public String eliminarAsignacion(int idOrdenServicio, int idTecnico) {
        if (idOrdenServicio <= 0 || idTecnico <= 0) {
            return "Identificadores de orden o técnico inválidos.";
        }
        // Se delega a la capa de servicio (asegúrate de que tu service tenga una firma compatible)
        return service.eliminarAsignacion(idOrdenServicio, idTecnico);
    }

    public List<OrdenServicioTecnico> listarPorOrden(int idOrdenServicio) {
        return service.listarPorOrden(idOrdenServicio);
    }

    public OrdenServicioTecnico buscarPorId(int idAsignacion) {
        return service.buscarPorId(idAsignacion);
    }
}