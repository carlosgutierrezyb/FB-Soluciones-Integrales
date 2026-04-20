package controller;

import model.DetalleOrdenCompra;
import model.OrdenCompra;
import model.Producto;
import repository.DetalleOrdenCompraRepository;
import repository.EntradaAlmacenRepository;
import repository.OrdenCompraRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DatabaseConnection;

/**
 * Controlador para editar órdenes de compra.
 *
 * 🔥 RESPONSABILIDAD:
 * - Validar reglas de negocio
 * - Construir datos para la vista
 * - Controlar modificaciones seguras
 */
public class EditarOrdenCompraController {

    private DetalleOrdenCompraRepository detalleRepo;
    private EntradaAlmacenRepository entradaRepo;
    private OrdenCompraRepository ordenRepo;

    public EditarOrdenCompraController() {
        this.detalleRepo = new DetalleOrdenCompraRepository();
        this.entradaRepo = new EntradaAlmacenRepository();
        this.ordenRepo = new OrdenCompraRepository();
    }

    // =========================
    // 🔹 OBTENER DATOS PARA TABLA
    // =========================
    public List<Object[]> obtenerDatosOrden(int idOrden) {

        List<Object[]> datos = new ArrayList<>();

        List<DetalleOrdenCompra> detalles = detalleRepo.listarPorOrden(idOrden);

        for (DetalleOrdenCompra d : detalles) {

            int recibido = entradaRepo.obtenerCantidadRecibida(
                    d.getIdItem(),
                    idOrden
            );

            int pendiente = d.getCantidadPedida() - recibido;

            Producto producto = new Producto();
            producto.setId(d.getIdItem());
            producto.setNombre("Producto #" + d.getIdItem()); // 🔥 puedes mejorar esto luego

            datos.add(new Object[]{
                    producto,
                    d.getCantidadPedida(),
                    recibido,
                    pendiente
            });
        }

        return datos;
    }

    // =========================
    // 🔹 ACTUALIZAR CANTIDAD
    // =========================
    public String actualizarCantidad(int idOrden, int idItem, int nuevaCantidad) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 VALIDAR ORDEN
            // =========================
            OrdenCompra orden = obtenerOrden(idOrden);

            if (orden == null) {
                return "Orden no encontrada.";
            }

            if (!orden.getEstado().equals("Pendiente") &&
                    !orden.getEstado().equals("Parcial")) {

                return "No se puede modificar una orden en estado: " + orden.getEstado();
            }

            // =========================
            // 🔹 VALIDAR CONTRA RECIBIDO
            // =========================
            int recibido = entradaRepo.obtenerCantidadRecibida(idItem, idOrden);

            if (nuevaCantidad < recibido) {
                return "No puede ser menor a lo recibido.";
            }

            // =========================
            // 🔹 TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean ok = detalleRepo.actualizarCantidad(
                    conn,
                    idOrden,
                    idItem,
                    nuevaCantidad
            );

            if (!ok) {
                throw new SQLException("No se pudo actualizar.");
            }

            conn.commit();

            return "OK";

        } catch (Exception e) {

            rollback(conn);
            e.printStackTrace();
            return "Error actualizando.";

        } finally {
            cerrar(conn);
        }
    }

    // =========================
    // 🔹 ELIMINAR ITEM
    // =========================
    public String eliminarItem(int idOrden, int idItem) {

        Connection conn = null;

        try {

            OrdenCompra orden = obtenerOrden(idOrden);

            if (orden == null) {
                return "Orden no encontrada.";
            }

            if (!orden.getEstado().equals("Pendiente") &&
                    !orden.getEstado().equals("Parcial")) {

                return "No se puede modificar esta orden.";
            }

            int recibido = entradaRepo.obtenerCantidadRecibida(idItem, idOrden);

            if (recibido > 0) {
                return "No puede eliminar un producto ya recibido.";
            }

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean ok = detalleRepo.eliminarItem(conn, idOrden, idItem);

            if (!ok) {
                throw new SQLException("No se pudo eliminar.");
            }

            conn.commit();

            return "OK";

        } catch (Exception e) {

            rollback(conn);
            return "Error eliminando.";

        } finally {
            cerrar(conn);
        }
    }

    // =========================
    // 🔹 OBTENER ORDEN (SIMPLIFICADO)
    // =========================
    private OrdenCompra obtenerOrden(int idOrden) {

        // 🔥 puedes mejorar esto luego con método real en repository
        List<OrdenCompra> lista = ordenRepo.listarPendientes();

        for (OrdenCompra o : lista) {
            if (o.getIdOrden() == idOrden) {
                return o;
            }
        }

        return null;
    }

    // =========================
    // 🔧 UTIL
    // =========================
    private void rollback(Connection conn) {
        try {
            if (conn != null) conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrar(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}