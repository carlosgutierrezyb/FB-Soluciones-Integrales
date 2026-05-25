package model;

import exception.BusinessException;

/**
 * Entidad Dirección Cliente.
 *
 * 🔥 ERP F&B:
 * - Manejo de múltiples sedes
 * - Direcciones para órdenes de servicio
 * - Contactos por sede
 */
public class DireccionCliente {

    // =========================
    // ATRIBUTOS
    // =========================

    private int idDireccion;

    private int idCliente;

    private String nombreSede;

    private String direccion;

    private String ciudad;

    private String contactoNombre;

    private String contactoTelefono;

    /*
     * 1 = Activa
     * 0 = Inactiva
     */
    private boolean estado;

    // =========================
    // GETTERS
    // =========================

    public int getIdDireccion() {
        return idDireccion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public String getContactoTelefono() {
        return contactoTelefono;
    }

    public boolean isEstado() {
        return estado;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdDireccion(int idDireccion) {

        if (idDireccion < 0) {

            throw new BusinessException(
                    "ID dirección inválido."
            );
        }

        this.idDireccion =
                idDireccion;
    }

    public void setIdCliente(int idCliente) {

        if (idCliente <= 0) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        this.idCliente =
                idCliente;
    }

    public void setNombreSede(
            String nombreSede
    ) {

        this.nombreSede =
                nombreSede;
    }

    public void setDireccion(
            String direccion
    ) {

        if (
                direccion == null
                        || direccion.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "La dirección es obligatoria."
            );
        }

        this.direccion =
                direccion.trim();
    }

    public void setCiudad(
            String ciudad
    ) {

        this.ciudad =
                ciudad;
    }

    public void setContactoNombre(
            String contactoNombre
    ) {

        this.contactoNombre =
                contactoNombre;
    }

    public void setContactoTelefono(
            String contactoTelefono
    ) {

        this.contactoTelefono =
                contactoTelefono;
    }

    public void setEstado(
            boolean estado
    ) {

        this.estado =
                estado;
    }

    // =========================
    // UI
    // =========================

    @Override
    public String toString() {

        if (
                nombreSede != null
                        && !nombreSede.trim().isEmpty()
        ) {

            return nombreSede
                    + " - "
                    + direccion;
        }

        return direccion;
    }
}