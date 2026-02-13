package model;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private int stockActual;
    private double precioUnitario;

    // Constructor vacío
    public Producto() {
    }

    public Producto(int id, String nombre, String descripcion, int stockActual, double precioUnitario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.precioUnitario = precioUnitario;
    }

    //Getter
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getStockActual() {
        return stockActual;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    //Setter


    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
