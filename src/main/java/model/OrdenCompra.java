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

    private String estado; // Pendiente, Parcial, Recibido, Cancelado
    private Timestamp fechaPedido;

    // 🔥 compatibilidad legacy
    private String numeroFactura;

    // =========================
    // GETTERS
    // =========================

    public int getIdOrden() {
        return idOrden;
    }

    // 🔥 compatibilidad con UI
    public int getId() {
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

    public String getNumeroFactura() {
        return numeroFactura;
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

        switch (estado) {
            case "Pendiente":
            case "Parcial":
            case "Recibido":
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

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    // =========================
    // UI / DEBUG
    // =========================

    @Override
    public String toString() {
        return "Orden #" + idOrden + " - " + estado;
    }

    // =========================
    // EQUALS / HASHCODE
    // =========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenCompra)) return false;

        OrdenCompra that = (OrdenCompra) o;
        return idOrden == that.idOrden;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idOrden);
    }
}