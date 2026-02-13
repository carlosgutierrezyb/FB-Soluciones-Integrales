package controller;

import model.Producto;
import repository.ProductoRepository;
import java.util.List;

/**
 * Clase mediadora entre la Vista y el Modelo.
 */

public class InventarioController {
    private ProductoRepository repository;

    public InventarioController() {
        this.repository = new ProductoRepository();
    }

    /**
     * Lógica para registrar un producto validando datos básicos.
     */
    public String registrarProducto(int id, String nombre, int stock, double precio) {
        if (nombre.isEmpty() || stock < 0) {
            return "Error: Datos inválidos.";
        }

        Producto p = new Producto(id, nombre, "Sin descripción", stock, precio);
        repository.guardar(p);
        return "Producto registrado con éxito.";
    }

    public String agregarNuevoProducto(String idStr, String nombre, String stockStr, String precioStr) {
        try {
            // Validaciones básicas de ingeniería: no dejar campos vacíos
            if (nombre.trim().isEmpty()) return "El nombre no puede estar vacío.";

            // Conversión de tipos con manejo de errores
            int id = Integer.parseInt(idStr);
            int stock = Integer.parseInt(stockStr);
            double precio = Double.parseDouble(precioStr);

            if (stock < 0) return "El stock no puede ser negativo.";

            // Creamos el objeto y lo mandamos al repositorio
            Producto nuevo = new Producto(id, nombre, "Descripción pendiente", stock, precio);
            repository.guardar(nuevo);

            return "OK"; // Usamos un código simple para indicar éxito
        } catch (NumberFormatException e) {
            return "Error: ID, Stock y Precio deben ser números válidos.";
        }
    }

    public List<Producto> obtenerInventario() {
        return repository.listarTodo();
    }
}
