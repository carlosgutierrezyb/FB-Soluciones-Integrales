package model;

import java.sql.Timestamp;

/**
 * Representa la asignación de un técnico
 * a una Orden de Servicio.
 *
 * ERP F&B
 */
public class OrdenServicioTecnico {

    private Integer idAsignacion;

    private Integer idOrdenServicio;

    private Integer idTecnico;

    private Integer idEspecialidad;

    private Timestamp fechaAsignacion;

    private String estado;

    private Double horasTrabajadas;

    private String observaciones;

    // =========================
    // CAMPOS AUXILIARES
    // =========================

    private String nombreTecnico;

    private String nombreEspecialidad;

    // =========================
    // CONSTRUCTOR
    // =========================

    public OrdenServicioTecnico() {
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Integer getIdAsignacion() {

        return idAsignacion;
    }

    public void setIdAsignacion(
            Integer idAsignacion
    ) {

        this.idAsignacion = idAsignacion;
    }

    public Integer getIdOrdenServicio() {

        return idOrdenServicio;
    }

    public void setIdOrdenServicio(
            Integer idOrdenServicio
    ) {

        this.idOrdenServicio = idOrdenServicio;
    }

    public Integer getIdTecnico() {

        return idTecnico;
    }

    public void setIdTecnico(
            Integer idTecnico
    ) {

        this.idTecnico = idTecnico;
    }

    public Integer getIdEspecialidad() {

        return idEspecialidad;
    }

    public void setIdEspecialidad(
            Integer idEspecialidad
    ) {

        this.idEspecialidad = idEspecialidad;
    }

    public Timestamp getFechaAsignacion() {

        return fechaAsignacion;
    }

    public void setFechaAsignacion(
            Timestamp fechaAsignacion
    ) {

        this.fechaAsignacion = fechaAsignacion;
    }

    public String getEstado() {

        return estado;
    }

    public void setEstado(
            String estado
    ) {

        this.estado = estado;
    }

    public Double getHorasTrabajadas() {

        return horasTrabajadas;
    }

    public void setHorasTrabajadas(
            Double horasTrabajadas
    ) {

        this.horasTrabajadas = horasTrabajadas;
    }

    public String getObservaciones() {

        return observaciones;
    }

    public void setObservaciones(
            String observaciones
    ) {

        this.observaciones = observaciones;
    }

    public String getNombreTecnico() {

        return nombreTecnico;
    }

    public void setNombreTecnico(
            String nombreTecnico
    ) {

        this.nombreTecnico = nombreTecnico;
    }

    public String getNombreEspecialidad() {

        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(
            String nombreEspecialidad
    ) {

        this.nombreEspecialidad = nombreEspecialidad;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {

        return nombreTecnico;
    }
}