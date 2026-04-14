package service;

import exception.BusinessException;
import model.EntradaAlmacen;
import repository.CompraRepository;
import util.ParseUtil;

/**
 * Servicio encargado de la lógica de negocio relacionada con las compras
 * (entradas de inventario).
 *
 * Responsabilidades:
 * - Validar datos de entrada
 * - Construir el modelo de dominio
 * - Persistir la información
 *
 * 🔥 Base para futuras mejoras:
 * - Actualización automática de stock
 * - Cálculo de costos promedio
 * - Integración con analítica (Power BI)
 */
public class CompraService {

    private CompraRepository compraRepo;

    /**
     * Constructor: inicializa el repositorio de compras.
     */
    public CompraService() {
        this.compraRepo = new CompraRepository();
    }

    /**
     * Registra una nueva entrada de inventario (compra).
     *
     * Flujo:
     * 1. Conversión de datos (ParseUtil)
     * 2. Validaciones de negocio
     * 3. Construcción del objeto EntradaAlmacen
     * 4. Persistencia en base de datos
     *
     * @param idProducto ID del producto
     * @param cantStr cantidad en texto
     * @param precioStr precio en texto
     * @param factura número de factura
     * @return "OK" si es exitoso, mensaje de error si falla
     */
    public String registrarCompra(int idProducto, String cantStr, String precioStr, String factura) {

        try {
            // 🔹 1. Conversión de datos
            int cantidad = ParseUtil.toPositiveInt(cantStr, "Cantidad");
            double precio = ParseUtil.toPositiveDouble(precioStr, "Precio");

            // 🔹 2. Validaciones de negocio
            validarDatos(idProducto, factura);

            // 🔹 3. Construcción del objeto
            EntradaAlmacen entrada = construirEntrada(idProducto, cantidad, precio, factura);

            // 🔹 4. Persistencia
            boolean exito = compraRepo.registrarEntrada(entrada);

            return exito ? "OK" : "Error al registrar la compra en la base de datos.";

        } catch (BusinessException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 luego lo reemplazamos por logger
            return "Error interno del sistema.";
        }
    }

    /**
     * Valida reglas de negocio generales.
     */
    private void validarDatos(int idProducto, String factura) {

        if (idProducto <= 0) {
            throw new BusinessException("El producto es obligatorio.");
        }

        if (factura == null || factura.trim().isEmpty()) {
            throw new BusinessException("El número de factura es obligatorio.");
        }
    }

    /**
     * Construye el objeto EntradaAlmacen con los datos recibidos.
     */
    private EntradaAlmacen construirEntrada(int idProducto, int cantidad, double precio, String factura) {

        EntradaAlmacen entrada = new EntradaAlmacen();
        entrada.setIdProducto(idProducto);
        entrada.setCantidad(cantidad);
        entrada.setPrecioCompra(precio);
        entrada.setNumeroFactura(factura);

        // 🔥 Opcional (si luego agregas proveedor en UI)
        // entrada.setProveedor("Proveedor General");

        return entrada;
    }

}