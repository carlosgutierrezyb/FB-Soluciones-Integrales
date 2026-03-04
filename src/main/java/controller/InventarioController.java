package controller;

import model.Categoria;
import model.Producto;
import repository.CategoriaRepository;
import repository.ProductoRepository;
import java.util.List;

public class InventarioController {

    private ProductoRepository productoRepo;
    private CategoriaRepository categoriaRepo;

    public InventarioController() {
        this.productoRepo = new ProductoRepository();
        this.categoriaRepo = new CategoriaRepository();
    }

    /**
     * Recibe los datos de la Vista, los valida y los envía al Repositorio.
     */
    public String agregarNuevoProducto(String nombre, String stockStr, String idCatStr, String stockMinStr) {
        try {
            // 1. Validaciones básicas
            if (nombre.trim().isEmpty()) return "El nombre de la referencia es obligatorio.";

            // 2. Conversión de datos (Parsing)
            int stock = Integer.parseInt(stockStr);
            int idCategoria = Integer.parseInt(idCatStr);
            int stockMinimo = Integer.parseInt(stockMinStr);

            if (stock < 0 || stockMinimo < 0) return "Las cantidades no pueden ser negativas.";

            // 3. GENERAR CÓDIGO SECUENCIAL POR CATEGORÍA
            String ultimoCodigo = productoRepo.obtenerUltimoCodigoPorCategoria(idCategoria);

            int nuevoCorrelativo = 1;

            if (ultimoCodigo != null) {
                // Ejemplo formato: 01-0005
                String[] partes = ultimoCodigo.split("-");
                int ultimoNumero = Integer.parseInt(partes[1]);
                nuevoCorrelativo = ultimoNumero + 1;
            }

            String prefijo = String.format("%02d", idCategoria);
            String correlativoFormateado = String.format("%04d", nuevoCorrelativo);
            String nuevoCodigo = prefijo + "-" + correlativoFormateado;

            // 4. Crear el objeto Producto
            Producto p = new Producto();
            p.setCodigoReferencia(nuevoCodigo);
            p.setNombre(nombre);
            p.setStockActual(stock);
            p.setIdCategoria(idCategoria);
            p.setStockMinimo(stockMinimo);
            p.setDescripcion("Referencia General");

            // 5. Guardar en base de datos
            boolean exito = productoRepo.guardar(p);

            return exito ? "OK" : "Error al guardar en la base de datos MySQL.";

        } catch (NumberFormatException e) {
            return "Error: Stock y Categoría deben ser números válidos.";
        } catch (Exception e) {
            return "Error en los datos.";
        }
    }

    /**
     * Elimina un producto por ID.
     */
    public boolean eliminarProducto(int id) {
        return productoRepo.eliminar(id);
    }

    /**
     * Edita los datos básicos de un producto existente.
     */
    public String editarProducto(int id, String nombre, String idCatStr, String stockMinStr) {
        try {
            Producto p = new Producto();
            p.setId(id);
            p.setNombre(nombre);
            p.setIdCategoria(Integer.parseInt(idCatStr));
            p.setStockMinimo(Integer.parseInt(stockMinStr));

            return productoRepo.actualizar(p) ? "OK" : "Error al actualizar";

        } catch (Exception e) {
            return "Datos inválidos";
        }
    }

    public List<Producto> obtenerInventario() {
        return productoRepo.listarTodo();
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepo.listarCategorias();
    }
}