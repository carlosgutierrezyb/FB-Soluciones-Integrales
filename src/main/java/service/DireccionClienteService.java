package service;

import exception.BusinessException;
import model.DireccionCliente;
import repository.DireccionClienteRepository;

import java.util.List;

/**
 * Service Direcciones Cliente.
 *
 * 🔥 ERP F&B:
 * - Validaciones de negocio
 * - Gestión de sedes
 * - Direcciones para órdenes servicio
 */
public class DireccionClienteService {

    private DireccionClienteRepository repository;

    public DireccionClienteService() {

        this.repository =
                new DireccionClienteRepository();
    }

    // =========================
    // 🔹 LISTAR POR CLIENTE
    // =========================
    public List<DireccionCliente> listarPorCliente(
            int idCliente
    ) {

        if (idCliente <= 0) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        return repository.listarPorCliente(
                idCliente
        );
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardar(
            DireccionCliente direccion
    ) {

        try {

            validar(direccion);

            boolean guardado =
                    repository.guardar(
                            direccion
                    );

            if (guardado) {

                return "OK";
            }

            return "No se pudo guardar la dirección.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error guardando dirección.";
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public String actualizar(
            DireccionCliente direccion
    ) {

        try {

            validar(direccion);

            // Validación adicional para actualización segura
            if (direccion.getIdDireccion() <= 0) {
                throw new BusinessException(
                        "ID de dirección inválido para actualizar."
                );
            }

            boolean actualizado =
                    repository.actualizar(
                            direccion
                    );

            if (actualizado) {

                return "OK";
            }

            return "No se pudo actualizar la dirección.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error actualizando dirección.";
        }
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public DireccionCliente buscarPorId(
            int idDireccion
    ) {

        if (idDireccion <= 0) {

            throw new BusinessException(
                    "ID dirección inválido."
            );
        }

        return repository.buscarPorId(
                idDireccion
        );
    }

    // =========================
    // 🔹 ELIMINAR
    // =========================
    public boolean eliminar(
            int idDireccion
    ) {

        if (idDireccion <= 0) {

            throw new BusinessException(
                    "ID dirección inválido para eliminar."
            );
        }

        return repository.eliminar(
                idDireccion
        );
    }

    // =========================
    // 🔥 VALIDACIONES
    // =========================
    private void validar(
            DireccionCliente d
    ) {

        if (d == null) {

            throw new BusinessException(
                    "La dirección es obligatoria."
            );
        }

        if (d.getIdCliente() <= 0) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        if (
                d.getDireccion() == null
                        || d.getDireccion().trim().isEmpty()
        ) {

            throw new BusinessException(
                    "La dirección es obligatoria."
            );
        }

        if (
                d.getDireccion().trim().length() < 5
        ) {

            throw new BusinessException(
                    "La dirección es demasiado corta."
            );
        }
    }
}