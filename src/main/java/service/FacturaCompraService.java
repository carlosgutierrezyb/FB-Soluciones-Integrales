package service;

import exception.BusinessException;
import model.FacturaCompra;
import repository.DetalleFacturaCompraRepository;
import repository.EntradaAlmacenRepository;
import repository.FacturaCompraRepository;
import repository.OrdenCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class FacturaCompraService {

    private FacturaCompraRepository facturaRepo;
    private EntradaAlmacenRepository entradaRepo;
    private OrdenCompraRepository ordenRepo;
    private DetalleFacturaCompraRepository detalleRepo;

    public FacturaCompraService() {

        this.facturaRepo = new FacturaCompraRepository();
        this.entradaRepo = new EntradaAlmacenRepository();
        this.ordenRepo = new OrdenCompraRepository();
        this.detalleRepo = new DetalleFacturaCompraRepository();
    }

    // =========================================================
    // 🔥 FACTURACIÓN DESDE CONTROLLER
    // =========================================================
    public String registrarFacturaCompleta(
            int idProveedor,
            int idOrden,
            String numeroFactura,
            List<Object[]> itemsFactura
    ) {

        Connection conn = null;

        try {

            if (idProveedor <= 0)
                throw new BusinessException("Proveedor inválido.");

            if (idOrden <= 0)
                throw new BusinessException("Orden inválida.");

            if (numeroFactura == null || numeroFactura.trim().isEmpty())
                throw new BusinessException("Número de factura obligatorio.");

            if (itemsFactura == null || itemsFactura.isEmpty())
                throw new BusinessException("No hay ítems para facturar.");

            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("📄 Registrando factura...");

            FacturaCompra factura = new FacturaCompra();

            factura.setIdProveedor(idProveedor);
            factura.setIdOrden(idOrden);
            factura.setNumeroFactura(numeroFactura);
            factura.setFecha(java.time.LocalDateTime.now());
            factura.setEstado("Registrada");
            factura.setObservacion("Factura desde módulo compras");

            int idFactura = facturaRepo.crear(conn, factura);

            if (idFactura <= 0)
                throw new BusinessException("No se pudo crear la factura.");

            for (Object[] item : itemsFactura) {

                int idItem = (int) item[0];
                int cantidad = (int) item[1];
                double valor = (double) item[2];

                List<Integer> entradas =
                        facturaRepo.obtenerEntradasPendientes(conn, idOrden);

                for (Integer idEntrada : entradas) {

                    int itemEntrada =
                            entradaRepo.obtenerIdItemPorEntrada(idEntrada);

                    if (itemEntrada != idItem)
                        continue;

                    facturaRepo.crearDetalleFactura(
                            conn,
                            idFactura,
                            idEntrada,
                            idItem,
                            cantidad,
                            valor
                    );

                    break;
                }
            }

            ordenRepo.actualizarEstado(conn, idOrden, "Parcial");

            conn.commit();

            return "OK";

        } catch (Exception e) {
            rollback(conn);
            return e.getMessage();
        } finally {
            cerrarConexion(conn);
        }
    }

    // =========================================================
    // 🔥 CANTIDAD FACTURADA (CORREGIDO Y SEGURO)
    // =========================================================
    public int obtenerCantidadFacturada(int idOrden, int idItem) {

        String sql =
                "SELECT COALESCE(SUM(dfc.cantidad_facturada),0) AS total " +
                        "FROM detalle_factura_compra dfc " +
                        "INNER JOIN entradas_almacen ea ON dfc.id_entrada = ea.id_entrada " +
                        "WHERE ea.id_orden = ? AND dfc.id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOrden);
            ps.setInt(2, idItem);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =========================================================
    // UTIL
    // =========================================================
    private void rollback(Connection conn) {
        try {
            if (conn != null) conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarConexion(Connection conn) {
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