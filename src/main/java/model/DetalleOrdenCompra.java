package model;

import exception.BusinessException;

public class DetalleOrdenCompra {

    private int idDetalle;
    private int idOrden;
    private int idItem;

    private int cantidadPedida;
    private double precioUnitario; // 🔥 FALTABA

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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public void setIdOrden(int idOrden) {
        if (idOrden <= 0) {
            throw new BusinessException("Orden inválida.");
        }
        this.idOrden = idOrden;
    }

    public void setIdItem(int idItem) {
        if (idItem <= 0) {
            throw new BusinessException("Producto inválido.");
        }
        this.idItem = idItem;
    }

    public void setCantidadPedida(int cantidadPedida) {
        if (cantidadPedida <= 0) {
            throw new BusinessException("Cantidad inválida.");
        }
        this.cantidadPedida = cantidadPedida;
    }

    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) {
            throw new BusinessException("Precio inválido.");
        }
        this.precioUnitario = precioUnitario;
    }
}