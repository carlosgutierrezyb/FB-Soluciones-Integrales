package controller;

import model.DetalleOrdenServicio;
import model.OrdenServicio;
import service.OrdenServicioService;

import java.sql.Date;
import java.util.List;

/**
 * Controlador que actúa como intermediario entre la interfaz de usuario
 * y la capa de servicios para la gestión de cabeceras y detalles de órdenes de servicio.
 */
public class OrdenServicioController {

    private final OrdenServicioService service;

    public OrdenServicioController() {
        this.service = new OrdenServicioService();
    }

    /**
     * Registra una nueva orden de servicio parametrizando los valores básicos de su cabecera y detalle.
     *
     * @param idCliente          Identificador único del cliente.
     * @param fechaProgramada    Fecha planificada para la ejecución del servicio.
     * @param prioridad          Nivel de prioridad asignado (Alta, Media, Baja).
     * @param direccionServicio  Dirección física donde se realizará la labor.
     * @param contactoNombre     Nombre de la persona de contacto en el sitio.
     * @param contactoTelefono   Teléfono de contacto.
     * @param observaciones      Notas aclaratorias iniciales.
     * @param detalles           Lista con los ítems o servicios detallados requeridos.
     * @return Cadena que confirma el éxito o el mensaje de error de la operación.
     */
    public String crearOrdenServicio(
            int idCliente,
            Date fechaProgramada,
            String prioridad,
            String direccionServicio,
            String contactoNombre,
            String contactoTelefono,
            String observaciones,
            List<DetalleOrdenServicio> detalles
    ) {
        return service.crearOrdenServicio(
                idCliente,
                fechaProgramada,
                prioridad,
                direccionServicio,
                contactoNombre,
                contactoTelefono,
                observaciones,
                detalles
        );
    }

    /**
     * Registra una orden de servicio procesando directamente la entidad contenedora de datos.
     *
     * @param orden    Objeto de modelo con los datos de cabecera cargados.
     * @param detalles Lista de detalles asociados a la orden.
     * @return Mensaje de confirmación del resultado del servicio de negocio.
     */
    public String crearOrden(OrdenServicio orden, List<DetalleOrdenServicio> detalles) {
        return service.crearOrden(orden, detalles);
    }

    /**
     * Actualiza de forma directa el estado general de una orden de servicio específica.
     *
     * @param idOrdenServicio Identificador de la orden.
     * @param nuevoEstado     Etiqueta del nuevo estado operacional.
     * @return Mensaje indicando el resultado del proceso de actualización.
     */
    public String actualizarEstado(int idOrdenServicio, String nuevoEstado) {
        return service.actualizarEstado(idOrdenServicio, nuevoEstado);
    }

    /**
     * Recupera la totalidad de las órdenes de servicio registradas en el sistema.
     *
     * @return Lista con todas las órdenes de servicio mapeadas.
     */
    public List<OrdenServicio> listarTodas() {
        return service.listarTodas();
    }

    /**
     * Filtra las órdenes de servicio almacenadas en función de su estado actual.
     *
     * @param estado Criterio de estado por el cual filtrar.
     * @return Lista de órdenes de servicio que cumplen con la condición.
     */
    public List<OrdenServicio> listarPorEstado(String estado) {
        return service.listarPorEstado(estado);
    }

    /**
     * Busca una orden de servicio mediante su identificador único.
     *
     * @param idOrdenServicio Identificador de la orden requerida.
     * @return Instancia de la orden de servicio encontrada, o null si no existe.
     */
    public OrdenServicio buscarPorId(int idOrdenServicio) {
        return service.buscarPorId(idOrdenServicio);
    }

    /**
     * Obtiene el listado de componentes detallados o ítems vinculados a una orden.
     *
     * @param idOrdenServicio Identificador de la orden de servicio base.
     * @return Lista conteniendo los detalles de la orden especificada.
     */
    public List<DetalleOrdenServicio> obtenerDetalleOrden(int idOrdenServicio) {
        return service.obtenerDetalleOrden(idOrdenServicio);
    }

    // =========================================================================
    // 🔥 MÉTODOS DE INTEGRACIÓN INDIVIDUAL PARA GESTIÓN DE MATERIALES
    // =========================================================================

    /**
     * Añade un único ítem de material/producto a una orden de servicio existente.
     *
     * @param detalle Instancia del detalle cargado con el id de orden y producto correspondiente.
     * @return "OK" si se insertó con éxito, o el mensaje de error de la lógica de negocio.
     */
    public String agregarMaterialIndividual(DetalleOrdenServicio detalle) {
        if (detalle == null) {
            return "El objeto de detalle no puede ser nulo.";
        }
        return service.agregarMaterialIndividual(detalle);
    }

    /**
     * Modifica de manera aislada la cantidad de un componente asignado en una orden.
     *
     * @param idOrdenServicio Identificador de la orden maestra.
     * @param tipoItem        Tipo de ítem ("PRODUCTO" o "SERVICIO").
     * @param idReferencia    Identificador del producto o servicio (id_producto / id_servicio).
     * @param nuevaCantidad   Nuevo valor entero para la columna cantidad.
     * @return "OK" si el registro se actualizó correctamente, o el error de negocio.
     */
    public String actualizarCantidadDetalle(int idOrdenServicio, String tipoItem, int idReferencia, int nuevaCantidad) {
        if (idOrdenServicio <= 0 || idReferencia <= 0) {
            return "Identificadores de orden o referencia inválidos.";
        }
        if (nuevaCantidad <= 0) {
            return "La cantidad debe ser mayor a cero.";
        }
        return service.actualizarCantidadDetalle(idOrdenServicio, tipoItem, idReferencia, nuevaCantidad);
    }

    /**
     * Elimina físicamente un ítem del detalle de la orden mediante su llave compuesta.
     *
     * @param idOrdenServicio Identificador de la orden maestra.
     * @param tipoItem        Tipo de ítem ("PRODUCTO" o "SERVICIO").
     * @param idReferencia    Identificador del producto o servicio a remover.
     * @return "OK" si se procesó de forma exitosa, o la descripción del fallo.
     */
    public String eliminarDetallePorId(int idOrdenServicio, String tipoItem, int idReferencia) {
        if (idOrdenServicio <= 0 || idReferencia <= 0) {
            return "Identificadores de orden o referencia inválidos para eliminación.";
        }
        return service.eliminarDetallePorId(idOrdenServicio, tipoItem, idReferencia);
    }
}