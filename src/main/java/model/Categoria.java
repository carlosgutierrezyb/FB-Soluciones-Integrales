package model;

import exception.BusinessException;

/**
 * Representa la entidad Categoria del sistema.
 *
 * Modela la tabla 'categorias' en la base de datos
 * y se utiliza para agrupar los productos.
 *
 * Buenas prácticas aplicadas:
 * ✔ Encapsulación
 * ✔ Validaciones de negocio
 * ✔ toString optimizado para UI
 * ✔ equals y hashCode para colecciones
 */
public class Categoria {

    private int id;
    private String nombre;

    public Categoria() {}

    public Categoria(int id, String nombre) {
        setId(id);
        setNombre(nombre);
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
        if (id < 0) {
            throw new BusinessException("ID de categoría inválido.");
        }
        this.id = id;
    }

    /**
     * Establece el nombre de la categoría.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre de la categoría es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    // =========================
    // MÉTODOS
    // =========================

    /**
     * 🔥 Clave para que el JComboBox muestre bien el nombre
     */
    @Override
    public String toString() {
        return nombre != null ? nombre : "Sin nombre";
    }

    /**
     * 🔥 Igualdad por ID (clave primaria)
     */
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