package service;

import exception.BusinessException;
import model.FacturaCompra;
import repository.FacturaCompraRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FacturaCompraService {

    private FacturaCompraRepository facturaRepo;

    public FacturaCompraService() {
        this.facturaRepo = new FacturaCompraRepository();
    }

    // =========================
    // 🔥 REGISTRAR FACTURA
    // =========================
    public String registrarFactura(int idOrden, String numeroFactura, String fechaStr) {

        Connection conn = null;

        try {

            // =========================
            // 🔹 VALIDACIONES
            // =========================
            if (idOrden <= 0) {
                throw new BusinessException("Orden inválida.");
            }

            if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
                throw new BusinessException("Número de factura obligatorio.");
            }

            if (fechaStr == null || fechaStr.trim().isEmpty()) {
                throw new BusinessException("Fecha obligatoria.");
            }

            LocalDate fecha;
            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                throw new BusinessException("Formato de fecha inválido (YYYY-MM-DD).");
            }

            // =========================
            // 🔹 TRANSACCIÓN
            // =========================
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("📄 Registrando factura de compra...");

            // =========================
            // 🔹 VALIDAR ENTRADAS DISPONIBLES
            // =========================
            boolean hayEntradas = facturaRepo.existenEntradasSinFactura(conn, idOrden);

            if (!hayEntradas) {
                throw new BusinessException("No hay entradas pendientes por facturar.");
            }

            // =========================
            // 🔹 CREAR FACTURA
            // =========================
            FacturaCompra factura = new FacturaCompra();
            factura.setIdOrden(idOrden);
            factura.setNumeroFactura(numeroFactura);
            factura.setFecha(LocalDateTime.now()); // o usar fecha convertida
            factura.setEstado("Registrada");

            int idFactura = facturaRepo.crear(conn, factura);

            if (idFactura <= 0) {
                throw new BusinessException("No se pudo crear la factura.");
            }

            // =========================
            // 🔹 ASOCIAR ENTRADAS
            // =========================
            boolean asociado = facturaRepo.asociarEntradas(conn, idFactura, idOrden);

            if (!asociado) {
                throw new BusinessException("No se pudieron asociar las entradas.");
            }

            // =========================
            // 🔹 COMMIT
            // =========================
            conn.commit();

            return "OK";

        } catch (BusinessException e) {

            rollback(conn);
            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();
            rollback(conn);
            return "Error registrando factura.";

        } finally {
            cerrarConexion(conn);
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