package model;

import exception.BusinessException;

/**
 * Representa la entidad Producto del sistema.
 *
 * Esta clase modela la tabla 'inventario' en la base de datos.
 * Contiene la información principal de cada referencia del catálogo.
 *
 * Nota:
 * - No incluye precio unitario, ya que el costo se calcula dinámicamente
 *   a partir de las compras (tabla entradas_inventario).
 *
 * Buenas prácticas aplicadas:
 * - Encapsulación
 * - Validaciones básicas en setters
 * - Nombres estándar (compatibles con ORM futuro)
 * - Preparado para lógica de negocio escalable
 */
public class Producto {

    /**
     * Identificador único del producto.
     */
    private int id;

    /**
     * Nombre del producto (ej: Cámara Hikvision 1080p).
     */
    private String nombre;

    /**
     * Descripción o unidad de medida (ej: unidad, caja, par).
     */
    private String descripcion;

    /**
     * Cantidad disponible en inventario.
     */
    private int stockActual;

    /**
     * ID de la categoría a la que pertenece el producto.
     * Relación lógica con la tabla 'categorias'.
     */
    private int idCategoria;

    /**
     * Stock mínimo permitido antes de generar alerta.
     */
    private int stockMinimo;

    /**
     * Código único de referencia generado automáticamente.
     * Formato sugerido: 01-0001
     */
    private String codigoReferencia;

    /**
     * Constructor vacío.
     * Necesario para frameworks y mapeo de datos desde BD.
     */
    public Producto() {}

    /**
     * Constructor completo (sin código de referencia).
     */
    public Producto(int id, String nombre, String descripcion, int stockActual,
                    int idCategoria, int stockMinimo) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.idCategoria = idCategoria;
        this.stockMinimo = stockMinimo;
    }

    // =========================
    // ======= GETTERS =========
    // =========================

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

    /**
     * Retorna el ID de la categoría.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    // =========================
    // ======= SETTERS =========
    // =========================

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre del producto.
     * Validación: no puede ser nulo ni vacío.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre del producto es obligatorio.");
        }
        this.nombre = nombre;
    }

    /**
     * Establece la descripción o unidad.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Establece el stock actual.
     * Validación: no puede ser negativo.
     */
    public void setStockActual(int stockActual) {
        if (stockActual < 0) {
            throw new BusinessException("El stock actual no puede ser negativo.");
        }
        this.stockActual = stockActual;
    }

    /**
     * Establece la categoría del producto.
     */
    public void setIdCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new BusinessException("La categoría es obligatoria.");
        }
        this.idCategoria = idCategoria;
    }

    /**
     * Establece el stock mínimo.
     * Validación: no puede ser negativo.
     */
    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new BusinessException("El stock mínimo no puede ser negativo.");
        }
        this.stockMinimo = stockMinimo;
    }

    /**
     * Establece el código de referencia.
     * Este valor normalmente lo genera el sistema, no el usuario.
     */
    public void setCodigoReferencia(String codigoReferencia) {
        if (codigoReferencia == null || codigoReferencia.trim().isEmpty()) {
            throw new BusinessException("El código de referencia es obligatorio.");
        }
        this.codigoReferencia = codigoReferencia;
    }

    // =========================
    // ======= MÉTODOS =========
    // =========================

    /**
     * Indica si el producto está en estado crítico de stock.
     *
     * @return true si el stock actual es menor o igual al mínimo
     */
    public boolean necesitaReposicion() {
        return stockActual <= stockMinimo;
    }

    /**
     * Representación en texto del producto.
     * Útil para debugging o logs.
     */
    @Override
    public String toString() {
        return codigoReferencia + " - " + nombre;
    }

    /**
     * Dos productos son iguales si tienen el mismo ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;

        Producto producto = (Producto) o;
        return id == producto.id;
    }

    /**
     * Hash basado en el ID.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}