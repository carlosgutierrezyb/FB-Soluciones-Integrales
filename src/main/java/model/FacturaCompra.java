package model;

import exception.BusinessException;

import java.time.LocalDateTime;

/**
 * Factura de compra (contable).
 *
 * 🔥 ERP REAL:
 * - Representa el documento del proveedor
 * - Valida lo recibido en entradas
 * - NO maneja stock directamente
 * - Puede soportar múltiples facturas por OC
 */
public class FacturaCompra {

    private int idFactura;
    private int idProveedor;
    private int idOrden;
    private String numeroFactura;
    private LocalDateTime fecha;
    private String estado; // Registrada, Validada

    // 🔥 NUEVO CAMPO
    private String observacion;

    // =========================
    // GETTERS
    // =========================

    public int getIdFactura() {
        return idFactura;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservacion() {
        return observacion;
    }

    // =========================
    // SETTERS (VALIDADOS)
    // =========================

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public void setIdProveedor(int idProveedor) {
        if (idProveedor <= 0) {
            throw new BusinessException("Proveedor inválido.");
        }
        this.idProveedor = idProveedor;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public void setNumeroFactura(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new BusinessException("Número de factura obligatorio.");
        }
        this.numeroFactura = numeroFactura;
    }

    public void setFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new BusinessException("Fecha obligatoria.");
        }
        this.fecha = fecha;
    }

    public void setEstado(String estado) {

        if (estado == null || estado.trim().isEmpty()) {
            throw new BusinessException("Estado obligatorio.");
        }

        switch (estado) {
            case "Registrada":
            case "Validada":
            case "Anulada":
                this.estado = estado;
                break;

            default:
                throw new BusinessException("Estado inválido.");
        }
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {
        return numeroFactura != null
                ? numeroFactura
                : "Factura #" + idFactura;
    }
}