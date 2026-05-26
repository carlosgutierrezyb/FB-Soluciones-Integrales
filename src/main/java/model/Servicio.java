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

    private int idServicio;

    private String codigo;

    private String nombre;

    private String descripcion;

    private String categoria;

    private double precioBase;

    private double tiempoEstimadoHoras;

    private boolean estado;

    // =========================
    // CONSTRUCTORES
    // =========================

    public Servicio() {

        this.estado = true;
    }

    public Servicio(
            int idServicio,
            String nombre
    ) {

        setIdServicio(idServicio);

        setNombre(nombre);

        this.estado = true;
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

    public String getCodigo() {

        return codigo;
    }

    public String getNombre() {

        return nombre;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public String getCategoria() {

        return categoria;
    }

    public double getPrecioBase() {

        return precioBase;
    }

    public double getTiempoEstimadoHoras() {

        return tiempoEstimadoHoras;
    }

    public boolean isEstado() {

        return estado;
    }

    // =========================
    // SETTERS
    // =========================

    public void setIdServicio(
            int idServicio
    ) {

        if (idServicio < 0) {

            throw new BusinessException(
                    "ID servicio inválido."
            );
        }

        this.idServicio = idServicio;
    }

    public void setCodigo(
            String codigo
    ) {

        if (
                codigo == null
                        || codigo.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El código del servicio es obligatorio."
            );
        }

        this.codigo = codigo.trim();
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

    public void setCategoria(
            String categoria
    ) {

        this.categoria =
                categoria != null
                        ? categoria.trim()
                        : null;
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