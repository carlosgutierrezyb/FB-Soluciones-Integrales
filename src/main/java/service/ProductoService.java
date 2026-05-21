package service;

import exception.BusinessException;
import model.Categoria;
import model.Producto;
import repository.CategoriaRepository;
import repository.ProductoRepository;
import util.ParseUtil;

import java.util.List;

/**
 * Service de productos.
 *
 * 🔥 RESPONSABILIDAD:
 * - Validaciones
 * - Reglas de negocio
 * - Coordinación entre repositorios
 *
 * ❌ NO SQL
 * ❌ NO Swing
 */
public class ProductoService {

    private ProductoRepository productoRepo;

    private CategoriaRepository categoriaRepo;

    public ProductoService() {

        this.productoRepo =
                new ProductoRepository();

        this.categoriaRepo =
                new CategoriaRepository();
    }

    // =========================
    // 🔹 CREAR PRODUCTO
    // =========================
    public String agregarProducto(
            String nombre,
            String stockStr,
            String idCatStr,
            String stockMinStr
    ) {

        try {

            validarNombre(nombre);

            int stock =
                    ParseUtil.toPositiveInt(
                            stockStr,
                            "Stock"
                    );

            int idCategoria =
                    ParseUtil.toInt(
                            idCatStr,
                            "Categoría"
                    );

            int stockMinimo =
                    ParseUtil.toPositiveInt(
                            stockMinStr,
                            "Stock mínimo"
                    );

            validarCantidades(
                    stock,
                    stockMinimo
            );

            String codigo =
                    generarCodigoProducto(
                            idCategoria
                    );

            Producto producto =
                    construirProducto(
                            nombre,
                            stock,
                            idCategoria,
                            stockMinimo,
                            codigo
                    );

            boolean guardado =
                    productoRepo.guardar(producto);

            if (!guardado) {

                throw new BusinessException(
                        "No se pudo guardar el producto."
                );
            }

            return "OK";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error interno del sistema.";
        }
    }

    // =========================
    // 🔹 EDITAR PRODUCTO
    // =========================
    public String editarProducto(
            int id,
            String nombre,
            String idCatStr,
            String stockMinStr
    ) {

        try {

            validarNombre(nombre);

            if (id <= 0) {

                throw new BusinessException(
                        "ID inválido."
                );
            }

            int idCategoria =
                    ParseUtil.toInt(
                            idCatStr,
                            "Categoría"
                    );

            int stockMinimo =
                    ParseUtil.toPositiveInt(
                            stockMinStr,
                            "Stock mínimo"
                    );

            Producto p =
                    new Producto();

            p.setId(id);

            p.setNombre(nombre);

            p.setIdCategoria(idCategoria);

            p.setStockMinimo(stockMinimo);

            boolean actualizado =
                    productoRepo.actualizar(p);

            return actualizado
                    ? "OK"
                    : "Error al actualizar el producto.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error interno del sistema.";
        }
    }

    // =========================
    // 🔹 INACTIVAR PRODUCTO
    // =========================
    public boolean eliminarProducto(int id) {

        if (id <= 0) {

            throw new BusinessException(
                    "ID inválido."
            );
        }

        return productoRepo.eliminar(id);
    }

    // =========================
    // 🔹 REACTIVAR PRODUCTO
    // =========================
    public boolean reactivarProducto(int id) {

        if (id <= 0) {

            throw new BusinessException(
                    "ID inválido."
            );
        }

        return productoRepo.reactivarProducto(id);
    }

    // =========================
    // 🔹 LISTAR ACTIVOS
    // =========================
    public List<Producto> listarProductos() {

        return productoRepo.listarTodos();
    }

    // =========================
    // 🔹 LISTAR INACTIVOS
    // =========================
    public List<Producto> listarProductosInactivos() {

        return productoRepo.listarInactivos();
    }

    // =========================
    // 🔹 LISTAR CATEGORÍAS
    // =========================
    public List<Categoria> listarCategorias() {

        return categoriaRepo.listarCategorias();
    }

    // =========================
    // 🔥 INVENTARIO
    // =========================
    public void aumentarStock(
            int idProducto,
            int cantidad
    ) {

        if (idProducto <= 0) {

            throw new BusinessException(
                    "Producto inválido."
            );
        }

        if (cantidad <= 0) {

            throw new BusinessException(
                    "Cantidad inválida."
            );
        }

        boolean ok =
                productoRepo.aumentarStock(
                        idProducto,
                        cantidad
                );

        if (!ok) {

            throw new BusinessException(
                    "No se pudo actualizar el stock."
            );
        }
    }

    // =========================
    // 🔹 VALIDAR NOMBRE
    // =========================
    private void validarNombre(String nombre) {

        if (
                nombre == null
                        || nombre.trim().isEmpty()
        ) {

            throw new BusinessException(
                    "El nombre es obligatorio."
            );
        }
    }

    // =========================
    // 🔹 VALIDAR STOCKS
    // =========================
    private void validarCantidades(
            int stock,
            int stockMinimo
    ) {

        if (stock < 0) {

            throw new BusinessException(
                    "El stock no puede ser negativo."
            );
        }

        if (stockMinimo < 0) {

            throw new BusinessException(
                    "El stock mínimo no puede ser negativo."
            );
        }
    }

    // =========================
    // 🔹 GENERAR SKU
    // =========================
    private String generarCodigoProducto(
            int idCategoria
    ) {

        String ultimoCodigo =
                productoRepo.obtenerUltimoCodigoPorCategoria(
                        idCategoria
                );

        int nuevoCorrelativo = 1;

        if (ultimoCodigo != null) {

            try {

                String[] partes =
                        ultimoCodigo.split("-");

                int ultimoNumero =
                        Integer.parseInt(
                                partes[1]
                        );

                nuevoCorrelativo =
                        ultimoNumero + 1;

            } catch (Exception e) {

                throw new BusinessException(
                        "Error generando código."
                );
            }
        }

        String prefijo =
                String.format(
                        "%02d",
                        idCategoria
                );

        String correlativo =
                String.format(
                        "%04d",
                        nuevoCorrelativo
                );

        return prefijo + "-" + correlativo;
    }

    // =========================
    // 🔹 CONSTRUIR PRODUCTO
    // =========================
    private Producto construirProducto(
            String nombre,
            int stock,
            int idCategoria,
            int stockMinimo,
            String codigo
    ) {

        Producto p =
                new Producto();

        p.setCodigoReferencia(codigo);

        p.setNombre(nombre);

        p.setStockActual(stock);

        p.setIdCategoria(idCategoria);

        p.setStockMinimo(stockMinimo);

        p.setEstado("ACTIVO");

        p.setDescripcion(
                "Referencia General"
        );

        return p;
    }
}