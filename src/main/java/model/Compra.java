package model;

import java.time.LocalDate;

/**
 * Modelo de Compra (versión extendida tipo ERP)
 */
public class Compra {

    private int id;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private String proveedor;
    private LocalDate fecha;
    private double total;

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public int getId() {
        return id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public String getProveedor() {
        return proveedor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularTotal();
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularTotal();
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Calcula automáticamente el total
     */
    private void calcularTotal() {
        this.total = this.cantidad * this.precioUnitario;
    }
}