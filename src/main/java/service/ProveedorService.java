package service;

import exception.BusinessException;
import model.Proveedor;
import repository.ProveedorRepository;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio de proveedores.
 *
 * 🔥 ERP F&B:
 * - Valida proveedores
 * - Normaliza datos
 * - Maneja persistencia
 */
public class ProveedorService {

    private ProveedorRepository proveedorRepo;

    public ProveedorService() {

        this.proveedorRepo =
                new ProveedorRepository();
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardarProveedor(
            Proveedor proveedor
    ) {

        try {

            validarProveedor(proveedor);

            normalizarDatos(proveedor);

            boolean guardado =
                    proveedorRepo.guardar(proveedor);

            return guardado
                    ? "OK"
                    : "Error al guardar el proveedor.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error interno del sistema.";
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public String actualizarProveedor(
            Proveedor proveedor
    ) {

        try {

            if (proveedor.getId() <= 0) {

                throw new BusinessException(
                        "Proveedor inválido."
                );
            }

            validarProveedor(proveedor);

            normalizarDatos(proveedor);

            boolean actualizado =
                    proveedorRepo.actualizar(proveedor);

            return actualizado
                    ? "OK"
                    : "No se pudo actualizar el proveedor.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error interno del sistema.";
        }
    }

    // =========================
    // 🔹 LISTAR
    // =========================
    public List<Proveedor> listarProveedores() {

        return proveedorRepo
                .listarTodos();
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Proveedor buscarPorId(
            int idProveedor
    ) {

        return proveedorRepo
                .buscarPorId(idProveedor);
    }

    // =========================
    // 🔹 VALIDACIONES
    // =========================
    private void validarProveedor(
            Proveedor p
    ) {

        if (p == null) {

            throw new BusinessException(
                    "Proveedor inválido."
            );
        }

        if (esVacio(
                p.getNombreRazonSocial()
        )) {

            throw new BusinessException(
                    "El nombre o razón social es obligatorio."
            );
        }

        if (esVacio(
                p.getTipoIdentificacion()
        )) {

            throw new BusinessException(
                    "El tipo de identificación es obligatorio."
            );
        }

        if (esVacio(
                p.getNumeroIdentificacion()
        )) {

            throw new BusinessException(
                    "El número de identificación es obligatorio."
            );
        }

        if (esVacio(
                p.getDireccion()
        )) {

            throw new BusinessException(
                    "La dirección es obligatoria."
            );
        }

        if (esVacio(
                p.getCiudad()
        )) {

            throw new BusinessException(
                    "La ciudad es obligatoria."
            );
        }
    }

    // =========================
    // 🔹 NORMALIZACIÓN
    // =========================
    private void normalizarDatos(
            Proveedor p
    ) {

        // DV
        if (p.getDv() != null) {

            String dv =
                    p.getDv().trim();

            if (dv.isEmpty()) {

                p.setDv(null);

            } else if (dv.length() > 1) {

                throw new BusinessException(
                        "El DV debe ser un solo dígito."
                );

            } else {

                p.setDv(dv);
            }
        }

        // Limpieza
        p.setNombreRazonSocial(
                p.getNombreRazonSocial().trim()
        );

        p.setNumeroIdentificacion(
                p.getNumeroIdentificacion().trim()
        );
    }

    // =========================
    // 🔧 UTIL
    // =========================
    private boolean esVacio(
            String valor
    ) {

        return valor == null
                || valor.trim().isEmpty();
    }
}