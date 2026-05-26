package model;

import exception.BusinessException;

/**
 * Representa la entidad Categoria del sistema.
 *
 * Modela la tabla 'categorias' en la base de datos
 * y se utiliza para agrupar:
 *
 * 🔥 ERP F&B
 * - Productos
 * - Servicios
 * - Clasificación comercial
 * - Generación de códigos ERP
 *
 * Buenas prácticas aplicadas:
 * ✔ Encapsulación
 * ✔ Validaciones de negocio
 * ✔ toString optimizado para UI
 * ✔ equals y hashCode para colecciones
 */
public class Categoria {

    // =========================
    // ATRIBUTOS
    // =========================

    private int id;

    private String nombre;

    /**
     * 🔥 Prefijo ERP:
     * Ej:
     * DVR
     * CIP
     * ALA
     * RED
     */
    private String prefijo;

    // =========================
    // CONSTRUCTORES
    // =========================

    public Categoria() {
    }

    public Categoria(
            int id,
            String nombre
    ) {

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

    public String getPrefijo() {

        return prefijo;
    }

    // =========================
    // SETTERS
    // =========================

    public void setId(
            int id
    ) {

        if (id < 0) {

            throw new BusinessException(
                    "ID de categoría inválido."
            );
        }

        this.id = id;
    }

    /**
     * Establece el nombre de la categoría.
     */
    public void setNombre(
            String nombre
    ) {

        if (
                nombre == null
                        || nombre.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El nombre de la categoría es obligatorio."
            );
        }

        this.nombre = nombre.trim();
    }

    /**
     * 🔥 Prefijo usado para:
     * - SKU productos
     * - Código servicios
     * - Reportes ERP
     */
    public void setPrefijo(
            String prefijo
    ) {

        if (
                prefijo == null
                        || prefijo.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El prefijo de la categoría es obligatorio."
            );
        }

        this.prefijo =
                prefijo
                        .trim()
                        .toUpperCase();
    }

    // =========================
    // MÉTODOS
    // =========================

    /**
     * 🔥 Clave para JComboBox
     */
    @Override
    public String toString() {

        return nombre != null
                ? nombre
                : "Sin nombre";
    }

    /**
     * 🔥 Igualdad por ID
     */
    @Override
    public boolean equals(
            Object o
    ) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Categoria)) {
            return false;
        }

        Categoria categoria =
                (Categoria) o;

        return id == categoria.id;
    }

    @Override
    public int hashCode() {

        return Integer.hashCode(id);
    }
}