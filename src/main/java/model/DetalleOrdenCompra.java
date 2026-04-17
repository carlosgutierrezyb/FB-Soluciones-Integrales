package model;

import exception.BusinessException;

/**
 * Entidad Detalle de Orden de Compra.
 *
 * Representa los ítems incluidos en una orden de compra.
 */
public class DetalleOrdenCompra {

    private int idDetalle;
    private int idOrden;
    private int idItem;

    private int cantidadPedida;

    // =========================
    // GETTERS
    // =========================

    public int getIdDetalle() {
        return idDetalle;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public int getIdItem() {
        return idItem;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    // =========================
    // SETTERS CON VALIDACIÓN
    // =========================

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public void setIdOrden(int idOrden) {
        if (idOrden <= 0) {
            throw new BusinessException("La orden es obligatoria.");
        }
        this.idOrden = idOrden;
    }

    public void setIdItem(int idItem) {
        if (idItem <= 0) {
            throw new BusinessException("El producto es obligatorio.");
        }
        this.idItem = idItem;
    }

    public void setCantidadPedida(int cantidadPedida) {
        if (cantidadPedida <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero.");
        }
        this.cantidadPedida = cantidadPedida;
    }
}