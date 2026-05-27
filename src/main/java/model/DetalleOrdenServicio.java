package model;

import exception.BusinessException;

/**
 * Detalle de Orden de Servicio.
 *
 * 🔥 ERP F&B
 * Soporta:
 * - Servicios
 * - Productos
 * - Materiales
 * - Facturación futura
 */
public class DetalleOrdenServicio {

    // =========================
    // ATRIBUTOS
    // =========================

    private int idDetalle;

    private int idOrdenServicio;

    // 🔥 NUEVOS ATRIBUTOS INTEGRADOS
    private Integer idServicio;

    private Integer idProducto;

    // 🔥 TIPO ITEM
    // SERVICIO / PRODUCTO
    private String tipoItem;

    // 🔥 REFERENCIA UNIFICADA (Reemplaza a nombreServicio y nombreProducto)
    private String nombreReferencia;

    // =========================
    // GENERAL
    // =========================

    private String codigoReferencia;

    private int cantidad;

    private String observacion;

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

    public Integer getIdServicio() {
        return idServicio;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public String getNombreReferencia() {
        return nombreReferencia;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
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

    public void setIdOrdenServicio(int idOrdenServicio) {

        if (idOrdenServicio <= 0) {

            throw new BusinessException(
                    "Orden inválida."
            );
        }

        this.idOrdenServicio = idOrdenServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public void setTipoItem(String tipoItem) {

        if (
                tipoItem == null
                        || tipoItem.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "Tipo de item obligatorio."
            );
        }

        this.tipoItem = tipoItem;
    }

    public void setNombreReferencia(
            String nombreReferencia
    ) {
        this.nombreReferencia = nombreReferencia;
    }

    public void setCodigoReferencia(
            String codigoReferencia
    ) {

        this.codigoReferencia =
                codigoReferencia;
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

        this.observacion = observacion;
    }

    public void setPrecioUnitario(
            double precioUnitario
    ) {

        if (precioUnitario < 0) {

            throw new BusinessException(
                    "Precio inválido."
            );
        }

        this.precioUnitario = precioUnitario;
    }

    // =========================
    // UI
    // =========================

    @Override
    public String toString() {

        return nombreReferencia != null
                ? nombreReferencia
                : "Detalle";
    }
}