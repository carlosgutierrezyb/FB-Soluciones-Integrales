package model;

import exception.BusinessException;

/**
 * Entidad Servicio.
 *
 * 🔥 ERP:
 * Catálogo de servicios ofrecidos.
 */
public class Servicio {

    // =========================
    // ATRIBUTOS
    // =========================

    private int idServicio;

    private String nombre;

    private String descripcion;

    private boolean estado;

    // =========================
    // CONSTRUCTORES
    // =========================

    public Servicio() {
    }

    public Servicio(
            int idServicio,
            String nombre
    ) {

        this.idServicio = idServicio;

        this.nombre = nombre;
    }

    // =========================
    // GETTERS
    // =========================

    public int getIdServicio() {

        return idServicio;
    }

    // 🔥 Compatibilidad UI
    public int getId() {

        return idServicio;
    }

    public String getNombre() {

        return nombre;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public boolean isEstado() {

        return estado;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdServicio(int idServicio) {

        if (idServicio < 0) {

            throw new BusinessException(
                    "ID servicio inválido."
            );
        }

        this.idServicio = idServicio;
    }

    public void setNombre(String nombre) {

        if (
                nombre == null
                        || nombre.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El nombre del servicio es obligatorio."
            );
        }

        this.nombre = nombre.trim();
    }

    public void setDescripcion(
            String descripcion
    ) {

        this.descripcion = descripcion;
    }

    public void setEstado(
            boolean estado
    ) {

        this.estado = estado;
    }

    // =========================
    // UI
    // =========================

    @Override
    public String toString() {

        return nombre;
    }
}