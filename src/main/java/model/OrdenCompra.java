package model;

import exception.BusinessException;

import java.sql.Timestamp;

/**
 * Entidad Orden de Compra.
 *
 * 🔥 ERP:
 * Representa una orden de compra
 * realizada a un proveedor.
 */
public class OrdenCompra {

    // =========================
    // ATRIBUTOS
    // =========================

    private int idOrden;

    private int idProveedor;

    // 🔥 NUEVO
    private String nombreProveedor;

    /*
     * Estados ERP:
     * - Pendiente
     * - Parcial
     * - Recibido
     * - Facturada
     * - Cancelado
     */
    private String estado;

    private Timestamp fechaPedido;

    // 🔥 Compatibilidad legacy
    private String numeroFactura;

    // =========================
    // GETTERS
    // =========================

    public int getIdOrden() {
        return idOrden;
    }

    // Compatibilidad UI
    public int getId() {
        return idOrden;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
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
    // SETTERS
    // =========================

    public void setIdOrden(int idOrden) {

        this.idOrden = idOrden;
    }

    public void setIdProveedor(int idProveedor) {

        if (idProveedor <= 0) {

            throw new BusinessException(
                    "El proveedor es obligatorio."
            );
        }

        this.idProveedor = idProveedor;
    }

    public void setNombreProveedor(
            String nombreProveedor
    ) {

        this.nombreProveedor =
                nombreProveedor;
    }

    /**
     * 🔥 IMPORTANTE
     *
     * Se ajustan los estados EXACTAMENTE
     * iguales a la base de datos:
     *
     * ENUM(
     *   'Pendiente',
     *   'Recibido',
     *   'Parcial',
     *   'Cancelado'
     * )
     *
     * Además se soporta "Facturada"
     * por lógica ERP interna.
     */
    public void setEstado(String estado) {

        if (
                estado == null
                        || estado.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El estado es obligatorio."
            );
        }

        estado = estado.trim();

        switch (estado) {

            case "Pendiente":

            case "Parcial":

            case "Recibido":

            case "Facturada":

            case "Cancelado":

                this.estado = estado;
                break;

            default:

                throw new BusinessException(
                        "Estado inválido: "
                                + estado
                );
        }
    }

    public void setFechaPedido(
            Timestamp fechaPedido
    ) {

        this.fechaPedido = fechaPedido;
    }

    public void setNumeroFactura(
            String numeroFactura
    ) {

        this.numeroFactura =
                numeroFactura;
    }

    // =========================
    // UI / DEBUG
    // =========================

    @Override
    public String toString() {

        return "Orden #"
                + idOrden
                + " - "
                + estado;
    }

    // =========================
    // EQUALS / HASHCODE
    // =========================

    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (!(o instanceof OrdenCompra)) {

            return false;
        }

        OrdenCompra that =
                (OrdenCompra) o;

        return idOrden == that.idOrden;
    }

    @Override
    public int hashCode() {

        return Integer.hashCode(idOrden);
    }
}