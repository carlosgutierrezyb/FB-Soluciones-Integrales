package model;

import java.time.LocalDateTime;
import exception.BusinessException;

/**
 * Representa una entrada de inventario (compra o ingreso de productos).
 *
 * Esta clase modela la tabla 'entradas_inventario' en la base de datos.
 * Permite llevar trazabilidad de:
 * - Cantidades ingresadas
 * - Costos reales de compra
 * - Facturación
 * - Proveedores
 *
 * 🔥 IMPORTANTE:
 * Esta entidad es la base para:
 * - Cálculo de costos promedio
 * - Históricos de compras
 * - Analítica (Power BI, BI, etc.)
 *
 * Buenas prácticas aplicadas:
 * - Validaciones en setters
 * - Uso de BusinessException
 * - Manejo de fecha y hora
 * - Preparado para escalabilidad
 */
public class EntradaAlmacen {

    /**
     * ID del producto al que pertenece la entrada.
     */
    private int idProducto;

    /**
     * Cantidad de unidades ingresadas al inventario.
     */
    private int cantidad;

    /**
     * Precio de compra por unidad.
     */
    private double precioCompra;

    /**
     * Número de factura asociado a la compra.
     */
    private String numeroFactura;

    /**
     * Nombre del proveedor.
     */
    private String proveedor;

    /**
     * Fecha y hora de la entrada.
     * Se asigna automáticamente al momento de crear el objeto.
     */
    private LocalDateTime fechaEntrada;

    /**
     * Constructor vacío.
     */
    public EntradaAlmacen() {
        this.fechaEntrada = LocalDateTime.now(); // 🔥 se asigna automáticamente
    }

    // =========================
    // ======= GETTERS =========
    // =========================

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getProveedor() {
        return proveedor;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    // =========================
    // ======= SETTERS =========
    // =========================

    /**
     * Establece el producto asociado.
     */
    public void setIdProducto(int idProducto) {
        if (idProducto <= 0) {
            throw new BusinessException("El producto es obligatorio.");
        }
        this.idProducto = idProducto;
    }

    /**
     * Establece la cantidad ingresada.
     */
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero.");
        }
        this.cantidad = cantidad;
    }

    /**
     * Establece el precio de compra.
     */
    public void setPrecioCompra(double precioCompra) {
        if (precioCompra <= 0) {
            throw new BusinessException("El precio debe ser mayor a cero.");
        }
        this.precioCompra = precioCompra;
    }

    /**
     * Establece el número de factura.
     */
    public void setNumeroFactura(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new BusinessException("El número de factura es obligatorio.");
        }
        this.numeroFactura = numeroFactura;
    }

    /**
     * Establece el proveedor.
     */
    public void setProveedor(String proveedor) {
        if (proveedor == null || proveedor.trim().isEmpty()) {
            throw new BusinessException("El proveedor es obligatorio.");
        }
        this.proveedor = proveedor;
    }

    /**
     * Permite establecer manualmente la fecha (opcional).
     */
    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    // =========================
    // ======= MÉTODOS =========
    // =========================

    /**
     * Calcula el costo total de la entrada.
     *
     * @return cantidad * precioCompra
     */
    public double calcularTotalCompra() {
        return cantidad * precioCompra;
    }

    /**
     * Representación en texto del movimiento.
     */
    @Override
    public String toString() {
        return "Entrada #" + numeroFactura +
                " | Producto ID: " + idProducto +
                " | Cantidad: " + cantidad;
    }
}