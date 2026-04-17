package model;

import exception.BusinessException;

import java.sql.Timestamp;

/**
 * Entidad Orden de Compra.
 *
 * Representa una orden de compra hacia un proveedor.
 */
public class OrdenCompra {

    private int idOrden;
    private int idProveedor;

    private String estado; // Pendiente, Recibido, Parcial, Cancelado
    private Timestamp fechaPedido;

    // =========================
    // GETTERS
    // =========================

    public int getIdOrden() {
        return idOrden;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getEstado() {
        return estado;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    // =========================
    // SETTERS CON VALIDACIÓN
    // =========================

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public void setIdProveedor(int idProveedor) {
        if (idProveedor <= 0) {
            throw new BusinessException("El proveedor es obligatorio.");
        }
        this.idProveedor = idProveedor;
    }

    public void setEstado(String estado) {

        if (estado == null || estado.trim().isEmpty()) {
            throw new BusinessException("El estado es obligatorio.");
        }

        // Validación tipo ENUM lógica
        switch (estado) {
            case "Pendiente":
            case "Recibido":
            case "Parcial":
            case "Cancelado":
                this.estado = estado;
                break;
            default:
                throw new BusinessException("Estado inválido.");
        }
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
}