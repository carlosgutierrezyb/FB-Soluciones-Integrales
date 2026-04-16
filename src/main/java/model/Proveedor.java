package model;

import exception.BusinessException;

/**
 * Entidad Proveedor.
 *
 * Representa proveedores del sistema.
 */
public class Proveedor {

    private int id;

    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String dv;

    private String nombreRazonSocial;

    private String direccion;
    private String ciudad;

    private String telefono;
    private String email;

    private String contactoNombre;
    private String contactoCelular;
    private String contactoEmail;

    // =========================
    // GETTERS
    // =========================

    public int getId() {
        return id;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getDv() {
        return dv;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public String getContactoCelular() {
        return contactoCelular;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    // =========================
    // SETTERS CON VALIDACIÓN
    // =========================

    public void setId(int id) {
        this.id = id;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        if (tipoIdentificacion == null || tipoIdentificacion.isEmpty()) {
            throw new BusinessException("Tipo de identificación obligatorio.");
        }
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.isEmpty()) {
            throw new BusinessException("Número de identificación obligatorio.");
        }
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        if (nombreRazonSocial == null || nombreRazonSocial.isEmpty()) {
            throw new BusinessException("Nombre o razón social obligatorio.");
        }
        this.nombreRazonSocial = nombreRazonSocial;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public void setContactoCelular(String contactoCelular) {
        this.contactoCelular = contactoCelular;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    // =========================
    // UI (IMPORTANTE)
    // =========================

    @Override
    public String toString() {
        return nombreRazonSocial;
    }
}