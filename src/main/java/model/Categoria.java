package model;

/**
 * Representa la tabla 'categorias' de la base de datos.
 * Es fundamental para agrupar los productos (ej. Cámaras, DVRs, Sensores).
 */
public class Categoria {
    private int idCat;
    private String nombre;

    // Constructor vacío
    public Categoria() {}

    // Constructor con parámetros
    public Categoria(int idCat, String nombre) {
        this.idCat = idCat;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdCat() { return idCat; }
    public void setIdCat(int idCat) { this.idCat = idCat; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Este método es CLAVE para la interfaz gráfica.
     * Cuando pongamos este objeto en un JComboBox (lista desplegable),
     * Java usará este método para saber qué texto mostrar en la pantalla.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}