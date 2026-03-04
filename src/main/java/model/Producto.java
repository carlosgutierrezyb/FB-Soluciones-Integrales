package model;

/**
 * Representa el catálogo maestro de referencias (Tabla 'inventario' en BD).
 * Se eliminó 'precioUnitario' porque el costo se calculará en base a las
 * compras reales (tabla 'entradas_inventario') para tener un costeo real.
 */
public class Producto {
    // Campos originales que mantenemos
    private int id;
    private String nombre;
    private String descripcion; // Puede usarse para guardar la 'unidad' (ej. par, caja, unidad)
    private int stockActual;

    // NUEVOS CAMPOS (Añadidos en la última actualización de BD)
    private int idCategoria;
    private int stockMinimo;
    private String codigoReferencia;

    // 1. Constructor vacío (Obligatorio para frameworks y consultas a BD)
    public Producto() {
    }

    // 2. Constructor completo con los nuevos campos
    public Producto(int id, String nombre, String descripcion, int stockActual, int idCategoria, int stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.idCategoria = idCategoria;
        this.stockMinimo = stockMinimo;
    }

    // --- GETTERS ---

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

    public int getIdCategoria() {
        return idCategoria;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getCodigoReferencia() {
        return codigoReferencia; }

    // --- SETTERS ---

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

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia; }
}