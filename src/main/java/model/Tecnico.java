package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Tecnico {

    private Integer idTecnico;

    private String documento;

    private String nombres;

    private String apellidos;

    private String telefono;

    private String email;

    private String direccion;

    private String estado;

    private Date fechaIngreso;

    private String observaciones;

    private Timestamp fechaCreacion;

    // =========================
    // CONSTRUCTORES
    // =========================

    public Tecnico() {
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Integer getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    public String getNombreCompleto() {

        String nom =
                nombres != null
                        ? nombres
                        : "";

        String ape =
                apellidos != null
                        ? apellidos
                        : "";

        return (nom + " " + ape).trim();
    }

    @Override
    public String toString() {

        return getNombreCompleto();
    }
}