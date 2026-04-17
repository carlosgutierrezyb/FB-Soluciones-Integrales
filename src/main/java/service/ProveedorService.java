package service;

import exception.BusinessException;
import model.Proveedor;
import repository.ProveedorRepository;

/**
 * Servicio encargado de la lógica de negocio de proveedores.
 *
 * - Recibe objetos (no 11 parámetros 😅)
 * - Valida antes de persistir
 * - Normaliza datos
 */
public class ProveedorService {

    private ProveedorRepository proveedorRepo;

    public ProveedorService() {
        this.proveedorRepo = new ProveedorRepository();
    }

    /**
     * Guarda proveedor (VERSIÓN PRO)
     */
    public String guardarProveedor(Proveedor proveedor) {

        try {

            System.out.println("📦 Iniciando guardado de proveedor...");

            // =========================
            // 🔹 1. VALIDACIONES
            // =========================
            validarProveedor(proveedor);

            // =========================
            // 🔹 2. NORMALIZACIÓN
            // =========================
            normalizarDatos(proveedor);

            // =========================
            // 🔹 3. PERSISTENCIA
            // =========================
            boolean guardado = proveedorRepo.guardar(proveedor);

            return guardado ? "OK" : "Error al guardar el proveedor.";

        } catch (BusinessException e) {
            return e.getMessage();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error interno del sistema.";
        }
    }

    // =========================
    // VALIDACIONES PRO
    // =========================

    private void validarProveedor(Proveedor p) {

        if (p == null) {
            throw new BusinessException("Proveedor inválido.");
        }

        if (esVacio(p.getNombreRazonSocial())) {
            throw new BusinessException("El nombre o razón social es obligatorio.");
        }

        if (esVacio(p.getTipoIdentificacion())) {
            throw new BusinessException("El tipo de identificación es obligatorio.");
        }

        if (esVacio(p.getNumeroIdentificacion())) {
            throw new BusinessException("El número de identificación es obligatorio.");
        }

        if (esVacio(p.getDireccion())) {
            throw new BusinessException("La dirección es obligatoria.");
        }

        if (esVacio(p.getCiudad())) {
            throw new BusinessException("La ciudad es obligatoria.");
        }
    }

    // =========================
    // NORMALIZACIÓN
    // =========================

    private void normalizarDatos(Proveedor p) {

        // 🔹 DV → solo 1 carácter o null
        if (p.getDv() != null) {
            String dv = p.getDv().trim();

            if (dv.isEmpty()) {
                p.setDv(null);
            } else if (dv.length() > 1) {
                throw new BusinessException("El DV debe ser un solo dígito.");
            } else {
                p.setDv(dv);
            }
        }

        // 🔹 Limpieza general
        p.setNombreRazonSocial(p.getNombreRazonSocial().trim());
        p.setNumeroIdentificacion(p.getNumeroIdentificacion().trim());
    }

    // =========================
    // UTIL
    // =========================

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}