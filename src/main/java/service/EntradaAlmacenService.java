package service;

import exception.BusinessException;
import model.DetalleOrdenCompra;
import model.EntradaAlmacen;
import model.OrdenCompra;
import repository.DetalleOrdenCompraRepository;
import repository.EntradaAlmacenRepository;
import repository.OrdenCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class EntradaAlmacenService {

    private OrdenCompraRepository ordenRepo;
    private DetalleOrdenCompraRepository detalleRepo;
    private EntradaAlmacenRepository entradaRepo;

    public EntradaAlmacenService() {
        this.ordenRepo = new OrdenCompraRepository();
        this.detalleRepo = new DetalleOrdenCompraRepository();
        this.entradaRepo = new EntradaAlmacenRepository();
    }

    // =========================
    // 🔹 ÓRDENES PENDIENTES
    // =========================
    public List<OrdenCompra> obtenerOrdenesPendientes() {
        return ordenRepo.listarPendientes();
    }

    // =========================
    // 🔹 DETALLE ORDEN
    // =========================
    public List<DetalleOrdenCompra> obtenerDetalleOrden(int idOrden) {
        return detalleRepo.listarPorOrden(idOrden);
    }

    // =========================
    // 🔹 CANTIDAD RECIBIDA
    // =========================
    public int obtenerCantidadRecibida(int idItem, int idOrden) {
        return entradaRepo.obtenerCantidadRecibida(idItem, idOrden);
    }

    // =========================
    // 🔹 REGISTRAR ENTRADA
    // =========================
    public String registrarEntrada(int idOrden, int idItem, int cantidad) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 VALIDACIONES
            // =========================
            if (idOrden <= 0) {
                throw new BusinessException("Orden inválida.");
            }

            if (idItem <= 0) {
                throw new BusinessException("Producto inválido.");
            }

            if (cantidad <= 0) {
                throw new BusinessException("Cantidad inválida.");
            }

            int recibido = obtenerCantidadRecibida(idItem, idOrden);

            DetalleOrdenCompra detalle = detalleRepo.obtenerPorOrdenYItem(idOrden, idItem);

            if (detalle == null) {
                throw new BusinessException("El producto no pertenece a la orden.");
            }

            int pendiente = detalle.getCantidadPedida() - recibido;

            if (cantidad > pendiente) {
                throw new BusinessException("No puede ingresar más de lo pendiente.");
            }

            // =========================
            // 🔹 TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("📦 Registrando entrada...");

            EntradaAlmacen entrada = new EntradaAlmacen();
            entrada.setIdOrden(idOrden);
            entrada.setIdItem(idItem);
            entrada.setCantidadRecibida(cantidad);

            // 🔥 CORREGIDO
            entrada.setFechaEntrada(new Timestamp(System.currentTimeMillis()));

            boolean guardado = entradaRepo.guardar(conn, entrada);

            if (!guardado) {
                throw new SQLException("Error guardando entrada.");
            }

            // =========================
            // 🔥 ACTUALIZAR ESTADO
            // =========================
            actualizarEstadoOrden(conn, idOrden);

            conn.commit();

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);
            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();
            rollback(conn);
            return "Error registrando entrada.";

        } finally {
            cerrarConexion(conn);
        }
    }

    // =========================
    // 🔥 ESTADO DE ORDEN
    // =========================
    private void actualizarEstadoOrden(Connection conn, int idOrden) throws SQLException {

        List<DetalleOrdenCompra> detalles = detalleRepo.listarPorOrden(idOrden);

        boolean completa = true;

        for (DetalleOrdenCompra d : detalles) {

            int recibido = entradaRepo.obtenerCantidadRecibida(d.getIdItem(), idOrden);

            if (recibido < d.getCantidadPedida()) {
                completa = false;
                break;
            }
        }

        if (completa) {
            // 🔥 CORREGIDO (tu modelo)
            ordenRepo.actualizarEstado(conn, idOrden, "Recibido");
        } else {
            ordenRepo.actualizarEstado(conn, idOrden, "Parcial");
        }
    }

    // =========================
    // 🔧 UTIL
    // =========================
    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
                System.out.println("⚠️ Rollback ejecutado.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}