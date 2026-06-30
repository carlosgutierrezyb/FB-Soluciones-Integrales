package model;

import exception.BusinessException;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entidad de dominio que representa una orden de servicio (OS) generada
 * para un cliente, consolidando la información de cabecera del requerimiento.
 */
public class OrdenServicio {

    private int idOrdenServicio;
    private int idCliente;
    private String nombreCliente;
    private String estado;
    private Timestamp fechaCreacion;
    private Date fechaProgramada;
    private String prioridad;
    private String direccionServicio;
    private String contactoNombre;
    private String contactoTelefono;
    private String observaciones;
    private Integer creadoPor;

    private Integer idTecnico;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private String estadoFacturacion;

    public OrdenServicio() {
    }

    public int getIdOrdenServicio() {
        return idOrdenServicio;
    }

    public void setIdOrdenServicio(int idOrdenServicio) {
        this.idOrdenServicio = idOrdenServicio;
    }

    public int getId() {
        return idOrdenServicio;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new BusinessException("El identificador del cliente debe ser un valor positivo válido.");
        }
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Define el estado actual de la orden de servicio validando que pertenezca
     * al ciclo de vida oficial definido en las reglas de negocio del ERP.
     *
     * @param estado Etiqueta del estado (Pendiente, Asignada, En ejecución, Finalizada, Cancelada).
     */
    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new BusinessException("El estado de la orden de servicio no puede estar vacío.");
        }

        String estadoNormalizado = estado.trim();
        switch (estadoNormalizado) {
            case "Pendiente":
            case "Asignada":
            case "En ejecución":
            case "Finalizada":
            case "Cancelada":
                this.estado = estadoNormalizado;
                break;
            default:
                throw new BusinessException("El estado '" + estado + "' no es válido para el flujo de trabajo del ERP.");
        }
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(Date fechaProgramada) {
        if (fechaProgramada == null) {
            throw new BusinessException("La fecha de programación del servicio es obligatoria.");
        }
        this.fechaProgramada = fechaProgramada;
    }

    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Define el nivel de prioridad de atención requerido para el servicio.
     *
     * @param priority Nivel de urgencia (Alta, Media, Baja).
     */
    public void setPrioridad(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            throw new BusinessException("La prioridad de la orden de servicio es obligatoria.");
        }

        String prioridadNormalizada = priority.trim();
        switch (prioridadNormalizada) {
            case "Alta":
            case "Media":
            case "Baja":
                this.prioridad = prioridadNormalizada;
                break;
            default:
                throw new BusinessException("La prioridad especificada es inválida.");
        }
    }

    public String getDireccionServicio() {
        return direccionServicio;
    }

    public void setDireccionServicio(String direccionServicio) {
        this.direccionServicio = direccionServicio;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public String getContactoTelefono() {
        return contactoTelefono;
    }

    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getObservations() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Integer getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstadoFacturacion() {
        return estadoFacturacion;
    }

    public void setEstadoFacturacion(String estadoFacturacion) {
        this.estadoFacturacion = estadoFacturacion;
    }

    @Override
    public String toString() {
        return "OS #" + idOrdenServicio + " [" + estado + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenServicio)) return false;
        OrdenServicio that = (OrdenServicio) o;
        return idOrdenServicio == that.idOrdenServicio;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idOrdenServicio);
    }
}