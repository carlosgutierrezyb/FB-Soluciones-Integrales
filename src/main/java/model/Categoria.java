package model;

/**
 * Representa la entidad Categoria del sistema.
 *
 * Esta clase modela la tabla 'categorias' en la base de datos
 * y se utiliza para agrupar los productos (ej: Cámaras, DVRs, Sensores).
 *
 * Buenas prácticas aplicadas:
 * - Encapsulación (atributos privados)
 * - Validaciones básicas en setters
 * - Sobrescritura de toString() para UI (JComboBox)
 * - Implementación de equals y hashCode para uso en colecciones
 */
public class Categoria {

    /**
     * Identificador único de la categoría.
     * Se recomienda usar nombres estándar como "id" para compatibilidad futura con ORM (JPA/Hibernate).
     */
    private int id;

    /**
     * Nombre de la categoría.
     * Este valor se muestra en la interfaz gráfica.
     */
    private String nombre;

    /**
     * Constructor vacío.
     * Necesario para frameworks, serialización y manejo flexible del objeto.
     */
    public Categoria() {}

    /**
     * Constructor con parámetros.
     * Permite crear una categoría de forma directa.
     */
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID de la categoría.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la categoría.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * Se valida que no sea nulo ni vacío para evitar datos inválidos.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        this.nombre = nombre;
    }

    /**
     * Representación en texto del objeto.
     *
     * CLAVE para la interfaz gráfica (Swing):
     * Cuando este objeto se usa en un JComboBox, este método define
     * el texto que verá el usuario.
     */
    @Override
    public String toString() {
        return this.nombre;
    }

    /**
     * Compara dos objetos Categoria.
     *
     * Se considera que dos categorías son iguales si tienen el mismo ID.
     * Esto es fundamental para evitar duplicados en listas o problemas en búsquedas.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;

        Categoria categoria = (Categoria) o;
        return id == categoria.id;
    }

    /**
     * Genera un código hash basado en el ID.
     *
     * Necesario cuando se usa la clase en estructuras como HashSet, HashMap, etc.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}