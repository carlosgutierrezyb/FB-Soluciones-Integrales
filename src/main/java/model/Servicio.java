package model;

import exception.BusinessException;

/**
 * Entidad Servicio.
 *
 * 🔥 ERP F&B:
 * Catálogo de servicios ofrecidos.
 */
public class Servicio {

    // =========================
    // ATRIBUTOS
    // =========================

    private int id;

    private String codigoReferencia;

    private String nombre;

    private String descripcion;

    // 🔥 NUEVO
    private int idCategoria;

    private String nombreCategoria;

    private double precioBase;

    private double tiempoEstimadoHoras;

    // 🔥 AHORA:
    // ACTIVO / INACTIVO
    private String estado;

    // =========================
    // CONSTRUCTORES
    // =========================

    public Servicio() {

        this.estado = "ACTIVO";
    }

    public Servicio(
            int id,
            String nombre
    ) {

        setId(id);

        setNombre(nombre);

        this.estado = "ACTIVO";
    }

    // =========================
    // GETTERS
    // =========================

    public int getId() {

        return id;
    }

    // 🔥 Compatibilidad antigua
    public int getIdServicio() {

        return id;
    }

    public String getCodigoReferencia() {

        return codigoReferencia;
    }

    // 🔥 Compatibilidad antigua
    public String getCodigo() {

        return codigoReferencia;
    }

    public String getNombre() {

        return nombre;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public int getIdCategoria() {

        return idCategoria;
    }

    public String getNombreCategoria() {

        return nombreCategoria;
    }

    public double getPrecioBase() {

        return precioBase;
    }

    public double getTiempoEstimadoHoras() {

        return tiempoEstimadoHoras;
    }

    public String getEstado() {

        return estado;
    }

    // =========================
    // SETTERS
    // =========================

    public void setId(
            int id
    ) {

        if (id < 0) {

            throw new BusinessException(
                    "ID servicio inválido."
            );
        }

        this.id = id;
    }

    // 🔥 Compatibilidad antigua
    public void setIdServicio(
            int idServicio
    ) {

        setId(idServicio);
    }

    public void setCodigoReferencia(
            String codigoReferencia
    ) {

        if (
                codigoReferencia == null
                        || codigoReferencia.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El código del servicio es obligatorio."
            );
        }

        this.codigoReferencia =
                codigoReferencia.trim();
    }

    // 🔥 Compatibilidad antigua
    public void setCodigo(
            String codigo
    ) {

        setCodigoReferencia(codigo);
    }

    public void setNombre(
            String nombre
    ) {

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

        this.descripcion =
                descripcion != null
                        ? descripcion.trim()
                        : null;
    }

    public void setIdCategoria(
            int idCategoria
    ) {

        if (idCategoria <= 0) {

            throw new BusinessException(
                    "Categoría inválida."
            );
        }

        this.idCategoria = idCategoria;
    }

    public void setNombreCategoria(
            String nombreCategoria
    ) {

        this.nombreCategoria = nombreCategoria;
    }

    public void setPrecioBase(
            double precioBase
    ) {

        if (precioBase < 0) {

            throw new BusinessException(
                    "El precio base no puede ser negativo."
            );
        }

        this.precioBase = precioBase;
    }

    public void setTiempoEstimadoHoras(
            double tiempoEstimadoHoras
    ) {

        if (tiempoEstimadoHoras < 0) {

            throw new BusinessException(
                    "El tiempo estimado es inválido."
            );
        }

        this.tiempoEstimadoHoras =
                tiempoEstimadoHoras;
    }

    public void setEstado(
            String estado
    ) {

        if (
                estado == null
                        || estado.trim().isEmpty()
        ) {

            estado = "ACTIVO";
        }

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