package model;

import exception.BusinessException;

/**
 * Detalle de Orden de Servicio.
 *
 * 🔥 ERP:
 * Representa los servicios incluidos
 * dentro de una orden de servicio.
 */
public class DetalleOrdenServicio {

    // =========================
    // ATRIBUTOS
    // =========================

    private int idDetalle;

    private int idOrdenServicio;

    private int idServicio;

    // 🔥 NUEVO
    private String nombreServicio;

    private int cantidad;

    private String observacion;

    // 🔥 FUTURO FACTURACIÓN
    private double precioUnitario;

    // =========================
    // GETTERS
    // =========================

    public int getIdDetalle() {
        return idDetalle;
    }

    public int getIdOrdenServicio() {
        return idOrdenServicio;
    }

    public int getIdServicio() {
        return idServicio;
    }

    // 🔥 NUEVO
    public String getNombreServicio() {
        return nombreServicio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdDetalle(int idDetalle) {

        this.idDetalle = idDetalle;
    }

    public void setIdOrdenServicio(
            int idOrdenServicio
    ) {

        if (idOrdenServicio <= 0) {

            throw new BusinessException(
                    "Orden de servicio inválida."
            );
        }

        this.idOrdenServicio =
                idOrdenServicio;
    }

    public void setIdServicio(int idServicio) {

        if (idServicio <= 0) {

            throw new BusinessException(
                    "Servicio inválido."
            );
        }

        this.idServicio = idServicio;
    }

    // 🔥 NUEVO
    public void setNombreServicio(
            String nombreServicio
    ) {

        this.nombreServicio =
                nombreServicio;
    }

    public void setCantidad(int cantidad) {

        if (cantidad <= 0) {

            throw new BusinessException(
                    "Cantidad inválida."
            );
        }

        this.cantidad = cantidad;
    }

    public void setObservacion(
            String observacion
    ) {

        this.observacion =
                observacion;
    }

    public void setPrecioUnitario(
            double precioUnitario
    ) {

        if (precioUnitario < 0) {

            throw new BusinessException(
                    "Precio inválido."
            );
        }

        this.precioUnitario =
                precioUnitario;
    }

    // =========================
    // DEBUG / UI
    // =========================

    @Override
    public String toString() {

        return nombreServicio != null
                ? nombreServicio
                : String.valueOf(idServicio);
    }
}