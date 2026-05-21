package model;

import exception.BusinessException;

import java.sql.Timestamp;

/**
 * Entidad Entrada de Almacén.
 *
 * Representa el ingreso físico de productos al inventario,
 * asociado a una orden de compra.
 *
 * 🔥 RESPONSABILIDAD:
 * - Registrar lo que realmente llegó
 * - Base para actualización de stock
 * - Trazabilidad logística
 * - Trazabilidad documental
 */
public class EntradaAlmacen {

    private int idEntrada;

    private int idOrden;

    private int idItem;

    private int cantidadRecibida;

    /*
     * 🔥 IMPORTANTE ERP
     *
     * El precio puede ser opcional
     * porque la valorización real
     * puede ocurrir en FacturaCompra.
     */
    private double precioCompraUnitario;

    // 🔥 DOCUMENTOS
    private String numeroFactura;

    private String numeroRemision;

    private Timestamp fechaEntrada;

    // =========================
    // GETTERS
    // =========================

    public int getIdEntrada() {
        return idEntrada;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public int getIdItem() {
        return idItem;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public double getPrecioCompraUnitario() {
        return precioCompraUnitario;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getNumeroRemision() {
        return numeroRemision;
    }

    public Timestamp getFechaEntrada() {
        return fechaEntrada;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public void setIdOrden(int idOrden) {

        if (idOrden <= 0) {

            throw new BusinessException(
                    "La orden es obligatoria."
            );
        }

        this.idOrden = idOrden;
    }

    public void setIdItem(int idItem) {

        if (idItem <= 0) {

            throw new BusinessException(
                    "El producto es obligatorio."
            );
        }

        this.idItem = idItem;
    }

    public void setCantidadRecibida(
            int cantidadRecibida
    ) {

        if (cantidadRecibida <= 0) {

            throw new BusinessException(
                    "La cantidad debe ser mayor a cero."
            );
        }

        this.cantidadRecibida = cantidadRecibida;
    }

    public void setPrecioCompraUnitario(
            double precioCompraUnitario
    ) {

        /*
         * 🔥 ERP REAL
         *
         * Puede venir en 0
         * si todavía no se factura.
         */
        if (precioCompraUnitario < 0) {

            throw new BusinessException(
                    "El precio no puede ser negativo."
            );
        }

        this.precioCompraUnitario =
                precioCompraUnitario;
    }

    public void setNumeroFactura(
            String numeroFactura
    ) {

        /*
         * 🔥 Puede ser opcional
         * durante entrada logística.
         */

        this.numeroFactura =
                numeroFactura;
    }

    public void setNumeroRemision(
            String numeroRemision
    ) {

        this.numeroRemision =
                numeroRemision;
    }

    public void setFechaEntrada(
            Timestamp fechaEntrada
    ) {

        this.fechaEntrada =
                fechaEntrada;
    }

    // =========================
    // MÉTODOS
    // =========================

    public double calcularTotal() {

        return cantidadRecibida
                * precioCompraUnitario;
    }

    @Override
    public String toString() {

        return "Entrada Orden #"
                + idOrden
                + " | Item: "
                + idItem
                + " | Cantidad: "
                + cantidadRecibida;
    }
}