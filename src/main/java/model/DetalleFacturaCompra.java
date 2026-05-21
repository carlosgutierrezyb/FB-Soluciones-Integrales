package model;

import exception.BusinessException;

/**
 * Modelo Detalle Factura Compra
 *
 * 🔥 ERP PRO:
 * - Línea individual de factura
 * - Relación:
 *      Factura ↔ Entrada ↔ Producto
 * - Soporta múltiples facturas por OC
 * - Soporta facturación parcial
 * - Maneja trazabilidad completa
 */
public class DetalleFacturaCompra {

    // =========================
    // ATRIBUTOS
    // =========================

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

        return cantidadFacturada
                * precioUnitarioFactura;
    }

    public String getObservacion() {
        return observacion;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdDetalleFactura(
            int idDetalleFactura
    ) {

        this.idDetalleFactura =
                idDetalleFactura;
    }

    public void setIdFactura(
            int idFactura
    ) {

        if (idFactura <= 0) {

            throw new BusinessException(
                    "ID factura inválido."
            );
        }

        this.idFactura =
                idFactura;
    }

    public void setIdEntrada(
            int idEntrada
    ) {

        if (idEntrada <= 0) {

            throw new BusinessException(
                    "ID entrada inválido."
            );
        }

        this.idEntrada =
                idEntrada;
    }

    public void setIdItem(
            int idItem
    ) {

        if (idItem <= 0) {

            throw new BusinessException(
                    "ID producto inválido."
            );
        }

        this.idItem =
                idItem;
    }

    public void setCantidadFacturada(
            int cantidadFacturada
    ) {

        if (cantidadFacturada <= 0) {

            throw new BusinessException(
                    "Cantidad facturada inválida."
            );
        }

        this.cantidadFacturada =
                cantidadFacturada;
    }

    public void setPrecioUnitarioFactura(
            double precioUnitarioFactura
    ) {

        if (precioUnitarioFactura <= 0) {

            throw new BusinessException(
                    "Precio unitario inválido."
            );
        }

        this.precioUnitarioFactura =
                precioUnitarioFactura;
    }

    public void setObservacion(
            String observacion
    ) {

        this.observacion =
                observacion;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {

        return

                "DetalleFacturaCompra{" +

                        "idDetalleFactura=" +
                        idDetalleFactura +

                        ", idFactura=" +
                        idFactura +

                        ", idEntrada=" +
                        idEntrada +

                        ", idItem=" +
                        idItem +

                        ", cantidadFacturada=" +
                        cantidadFacturada +

                        ", precioUnitarioFactura=" +
                        precioUnitarioFactura +

                        ", subtotal=" +
                        getSubtotal() +

                        '}';
    }
}