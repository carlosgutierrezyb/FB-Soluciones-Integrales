package model;

import exception.BusinessException;

/**
 * Representa la entidad Categoria del sistema.
 *
 * Esta clase modela la tabla 'categorias' en la base de datos
 * y se utiliza para agrupar los productos (ej: Cámaras, DVRs, Sensores).
 *
 * Buenas prácticas aplicadas:
 * - Encapsulación (atributos privados)
 * - Validaciones alineadas con el dominio (BusinessException)
 * - Sobrescritura de toString() para UI (JComboBox)
 * - Implementación de equals y hashCode para uso en colecciones
 */
public class Categoria {

    private int id;
    private String nombre;

    public Categoria() {}

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // =========================
    // GETTERS
    // =========================

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // =========================
    // SETTERS
    // =========================

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre de la categoría.
     * Validación alineada con reglas de negocio.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre de la categoría es obligatorio.");
        }
        this.nombre = nombre;
    }

    // =========================
    // MÉTODOS
    // =========================

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;

        Categoria categoria = (Categoria) o;
        return id == categoria.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}