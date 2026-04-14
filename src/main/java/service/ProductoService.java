package service;

import exception.BusinessException;
import model.Producto;
import repository.ProductoRepository;
import util.ParseUtil;

/**
 * Servicio encargado de la lógica de negocio relacionada con productos.
 */
public class ProductoService {

    private ProductoRepository productoRepo;

    public ProductoService() {
        this.productoRepo = new ProductoRepository();
    }

    /**
     * Orquesta la creación de un producto.
     */
    public String agregarProducto(String nombre, String stockStr, String idCatStr, String stockMinStr) {

        try {
            // 🔹 1. Validaciones básicas
            validarNombre(nombre);

            // 🔹 2. Parsing (centralizado)
            int stock = ParseUtil.toPositiveInt(stockStr, "Stock");
            int idCategoria = ParseUtil.toInt(idCatStr, "Categoría");
            int stockMinimo = ParseUtil.toPositiveInt(stockMinStr, "Stock mínimo");

            // 🔹 3. Validaciones de negocio
            validarCantidades(stock, stockMinimo);

            // 🔹 4. Generar código
            String codigo = generarCodigoProducto(idCategoria);

            // 🔹 5. Construcción
            Producto producto = construirProducto(nombre, stock, idCategoria, stockMinimo, codigo);

            // 🔹 6. Persistencia
            boolean guardado = productoRepo.guardar(producto);

            return guardado ? "OK" : "Error al guardar en la base de datos.";

        } catch (BusinessException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error interno del sistema.";
        }
    }

    /**
     * Validación del nombre.
     */
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
    }

    /**
     * Validaciones de negocio para cantidades.
     */
    private void validarCantidades(int stock, int stockMinimo) {

        if (stock < 0) {
            throw new BusinessException("El stock no puede ser negativo.");
        }

        if (stockMinimo < 0) {
            throw new BusinessException("El stock mínimo no puede ser negativo.");
        }

        if (stockMinimo > stock) {
            throw new BusinessException("El stock mínimo no puede ser mayor al stock actual.");
        }
    }

    /**
     * Genera código tipo: 01-0001
     */
    private String generarCodigoProducto(int idCategoria) {

        String ultimoCodigo = productoRepo.obtenerUltimoCodigoPorCategoria(idCategoria);

        int nuevoCorrelativo = 1;

        if (ultimoCodigo != null) {
            try {
                String[] partes = ultimoCodigo.split("-");
                int ultimoNumero = Integer.parseInt(partes[1]);
                nuevoCorrelativo = ultimoNumero + 1;
            } catch (Exception e) {
                throw new BusinessException("Error generando el código del producto.");
            }
        }

        String prefijo = String.format("%02d", idCategoria);
        String correlativo = String.format("%04d", nuevoCorrelativo);

        return prefijo + "-" + correlativo;
    }

    /**
     * Construye el objeto Producto.
     */
    private Producto construirProducto(String nombre, int stock, int idCategoria, int stockMinimo, String codigo) {

        Producto p = new Producto();
        p.setCodigoReferencia(codigo);
        p.setNombre(nombre);
        p.setStockActual(stock);
        p.setIdCategoria(idCategoria);
        p.setStockMinimo(stockMinimo);
        p.setDescripcion("Referencia General");

        return p;
    }

    /**
     * Edita un producto existente.
     */
    public String editarProducto(int id, String nombre, String idCatStr, String stockMinStr) {

        try {
            // 🔹 1. Validaciones
            validarNombre(nombre);

            if (id <= 0) {
                throw new BusinessException("ID inválido.");
            }

            // 🔹 2. Parsing
            int idCategoria = ParseUtil.toInt(idCatStr, "Categoría");
            int stockMinimo = ParseUtil.toPositiveInt(stockMinStr, "Stock mínimo");

            // 🔹 3. Validación negocio
            if (stockMinimo < 0) {
                throw new BusinessException("El stock mínimo no puede ser negativo.");
            }

            // 🔹 4. Construcción
            Producto p = new Producto();
            p.setId(id);
            p.setNombre(nombre);
            p.setIdCategoria(idCategoria);
            p.setStockMinimo(stockMinimo);

            // 🔹 5. Persistencia
            boolean actualizado = productoRepo.actualizar(p);

            return actualizado ? "OK" : "Error al actualizar el producto.";

        } catch (BusinessException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error interno del sistema.";
        }
    }
}