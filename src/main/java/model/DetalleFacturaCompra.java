package model;

import exception.BusinessException;

/**
 * Modelo de detalle de factura de compra.
 *
 * 🔥 ERP PRO:
 * - Representa cada línea facturada
 * - Relaciona factura ↔ entrada ↔ producto
 * - Permite trazabilidad completa
 * - Controla cantidades y valores facturados
 *
 * Tabla:
 * detalle_factura_compra
 */
public class DetalleFacturaCompra {

    private int idDetalleFactura;
    private int idFactura;
    private int idEntrada;
    private int idItem;

    private int cantidadFacturada;
    private double precioUnitarioFactura;

    private String observacion;

    // =========================
    // GETTERS
    // =========================

    public int getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public int getIdItem() {
        return idItem;
    }

    public int getCantidadFacturada() {
        return cantidadFacturada;
    }

    public double getPrecioUnitarioFactura() {
        return precioUnitarioFactura;
    }

    public double getSubtotal() {
        return cantidadFacturada * precioUnitarioFactura;
    }

    public String getObservacion() {
        return observacion;
    }

    // =========================
    // SETTERS (VALIDADOS)
    // =========================

    public void setIdDetalleFactura(int idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public void setIdFactura(int idFactura) {
        if (idFactura <= 0) {
            throw new BusinessException("ID de factura inválido.");
        }
        this.idFactura = idFactura;
    }

    public void setIdEntrada(int idEntrada) {
        if (idEntrada <= 0) {
            throw new BusinessException("ID de entrada inválido.");
        }
        this.idEntrada = idEntrada;
    }

    public void setIdItem(int idItem) {
        if (idItem <= 0) {
            throw new BusinessException("ID de producto inválido.");
        }
        this.idItem = idItem;
    }

    public void setCantidadFacturada(int cantidadFacturada) {
        if (cantidadFacturada <= 0) {
            throw new BusinessException("La cantidad facturada debe ser mayor a cero.");
        }
        this.cantidadFacturada = cantidadFacturada;
    }

    public void setPrecioUnitarioFactura(double precioUnitarioFactura) {
        if (precioUnitarioFactura < 0) {
            throw new BusinessException("El precio unitario no puede ser negativo.");
        }
        this.precioUnitarioFactura = precioUnitarioFactura;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {
        return "DetalleFacturaCompra{" +
                "idDetalleFactura=" + idDetalleFactura +
                ", idFactura=" + idFactura +
                ", idEntrada=" + idEntrada +
                ", idItem=" + idItem +
                ", cantidadFacturada=" + cantidadFacturada +
                ", precioUnitarioFactura=" + precioUnitarioFactura +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}