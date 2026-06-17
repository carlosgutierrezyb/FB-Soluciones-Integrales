package model;

/**
 * Especialidad técnica de un técnico.
 *
 * Ejemplos:
 * - CCTV
 * - Alarmas
 * - Control de Acceso
 * - Redes
 * - Electricidad
 */
public class EspecialidadTecnica {

    private Integer idEspecialidad;

    private String nombre;

    private String descripcion;

    private String estado;

    // =========================
    // CONSTRUCTORES
    // =========================

    public EspecialidadTecnica() {
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(
            Integer idEspecialidad
    ) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre
    ) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion
    ) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(
            String estado
    ) {
        this.estado = estado;
    }

    // =========================
    // AUXILIARES
    // =========================

    @Override
    public String toString() {

        return nombre;
    }
}