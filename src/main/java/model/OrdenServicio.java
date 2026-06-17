package model;

import exception.BusinessException;

import java.sql.Timestamp;

/**
 * Entidad Orden de Servicio.
 *
 * 🔥 ERP:
 * Representa una solicitud de servicio
 * realizada por un cliente.
 */
public class OrdenServicio {

    // =========================
    // ATRIBUTOS
    // =========================

    private int idOrdenServicio;

    private int idCliente;

    private String nombreCliente;

    /*
     * Estados ERP:
     * - Pendiente
     * - Agendada
     * - En ejecución
     * - Ejecutada
     * - Facturada
     * - Cancelada
     */
    private String estado;

    private Timestamp fechaCreacion;

    private java.sql.Date fechaProgramada;

    private String prioridad;

    private String direccionServicio;

    private String contactoNombre;

    private String contactoTelefono;

    private String observaciones;

    private Integer creadoPor;

    // 🔥 NUEVOS ATRIBUTOS PARA OPERACIONES Y FLUJO OPERATIVO
    private Integer idTecnico;

    private Timestamp fechaInicio;

    private Timestamp fechaFin;

    private String estadoFacturacion;

    // =========================
    // GETTERS
    // =========================

    public int getIdOrdenServicio() {
        return idOrdenServicio;
    }

    public int getId() {
        return idOrdenServicio;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getEstado() {
        return estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public java.sql.Date getFechaProgramada() {
        return fechaProgramada;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getDireccionServicio() {
        return direccionServicio;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public String getContactoTelefono() {
        return contactoTelefono;
    }

    public String getObservaciones() {
        return observaciones;
    }

    // Método de alias por si el repositorio requiere buscarlo en inglés
    public String getObservations() {
        return observaciones;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    // 🔥 GETTERS NUEVOS
    public Integer getIdTecnico() {
        return idTecnico;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public String getEstadoFacturacion() {
        return estadoFacturacion;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdOrdenServicio(int idOrdenServicio) {
        this.idOrdenServicio = idOrdenServicio;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new BusinessException("El cliente es obligatorio.");
        }
        this.idCliente = idCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new BusinessException("El estado es obligatorio.");
        }

        estado = estado.trim();

        switch (estado) {
            case "Pendiente":
            case "Agendada":
            case "En ejecución":
            case "Ejecutada":
            case "Facturada":
            case "Cancelada":
                this.estado = estado;
                break;
            default:
                throw new BusinessException("Estado inválido: " + estado);
        }
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaProgramada(java.sql.Date fechaProgramada) {
        if (fechaProgramada == null) {
            throw new BusinessException("La fecha programada es obligatoria.");
        }
        this.fechaProgramada = fechaProgramada;
    }

    public void setPrioridad(String prioridad) {
        if (prioridad == null || prioridad.trim().isEmpty()) {
            throw new BusinessException("La prioridad es obligatoria.");
        }

        prioridad = prioridad.trim();

        switch (prioridad) {
            case "Alta":
            case "Media":
            case "Baja":
                this.prioridad = prioridad;
                break;
            default:
                throw new BusinessException("Prioridad inválida.");
        }
    }

    public void setDireccionServicio(String direccionServicio) {
        this.direccionServicio = direccionServicio;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    // 🔥 SETTERS NUEVOS
    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setEstadoFacturacion(String estadoFacturacion) {
        this.estadoFacturacion = estadoFacturacion;
    }

    // =========================
    // UI / DEBUG
    // =========================

    @Override
    public String toString() {
        return "OS #" + idOrdenServicio + " - " + estado;
    }

    // =========================
    // EQUALS / HASHCODE
    // =========================

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenServicio)) {
            return false;
        }
        OrdenServicio that = (OrdenServicio) o;
        return idOrdenServicio == that.idOrdenServicio;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idOrdenServicio);
    }
}