package model;

import exception.BusinessException;

/**
 * Entidad Cliente.
 *
 * 🔥 ERP F&B:
 * - Clientes del módulo ventas
 * - Base para órdenes de servicio
 * - Base para facturación
 */
public class Cliente {

    private int idCliente;

    private String nombre;

    private String identificacion;

    private String tipoIdentificacion;

    private String telefono;

    private String correo;

    private String direccion;

    private String ciudad;

    // 🔥 NUEVOS CAMPOS CONTACTO
    private String contactoNombre;

    private String contactoTelefono;

    private String contactoEmail;

    private String estado;

    // =========================
    // GETTERS
    // =========================

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
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

    public String getContactoEmail() {
        return contactoEmail;
    }

    public String getEstado() {
        return estado;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdCliente(int idCliente) {

        if (idCliente < 0) {

            throw new BusinessException(
                    "ID cliente inválido."
            );
        }

        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {

        if (nombre == null ||
                nombre.trim().isEmpty()) {

            throw new BusinessException(
                    "Nombre obligatorio."
            );
        }

        this.nombre =
                nombre.trim();
    }

    public void setIdentificacion(
            String identificacion
    ) {

        if (identificacion == null ||
                identificacion.trim().isEmpty()) {

            throw new BusinessException(
                    "Identificación obligatoria."
            );
        }

        this.identificacion =
                identificacion.trim();
    }

    public void setTipoIdentificacion(
            String tipoIdentificacion
    ) {

        this.tipoIdentificacion =
                tipoIdentificacion;
    }

    public void setTelefono(
            String telefono
    ) {

        this.telefono =
                telefono;
    }

    public void setCorreo(
            String correo
    ) {

        this.correo =
                correo;
    }

    public void setDireccion(
            String direccion
    ) {

        this.direccion =
                direccion;
    }

    public void setCiudad(
            String ciudad
    ) {

        this.ciudad =
                ciudad;
    }

    // =========================
    // 🔥 CONTACTO
    // =========================

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

    public void setContactoEmail(
            String contactoEmail
    ) {

        this.contactoEmail =
                contactoEmail;
    }

    public void setEstado(
            String estado
    ) {

        if (estado == null ||
                estado.trim().isEmpty()) {

            estado = "ACTIVO";
        }

        this.estado =
                estado;
    }

    // =========================
    // UI
    // =========================

    @Override
    public String toString() {

        return nombre;
    }
}