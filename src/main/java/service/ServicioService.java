package service;

import exception.BusinessException;
import model.Servicio;
import repository.ServicioRepository;

import java.util.List;

/**
 * Service Servicios.
 *
 * 🔥 ERP F&B:
 * - Reglas negocio servicios
 * - Validaciones catálogo
 * - Base órdenes servicio
 */
public class ServicioService {

    private ServicioRepository repository;

    public ServicioService() {

        this.repository =
                new ServicioRepository();
    }

    // =========================
    // 🔹 LISTAR ACTIVOS
    // =========================
    public List<Servicio> listarActivos() {

        return repository.listarActivos();
    }

    // =========================
    // 🔹 LISTAR TODOS
    // =========================
    public List<Servicio> listarTodos() {

        return repository.listarTodos();
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Servicio buscarPorId(
            int idServicio
    ) {

        if (idServicio <= 0) {

            throw new BusinessException(
                    "ID servicio inválido."
            );
        }

        return repository.buscarPorId(
                idServicio
        );
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardarServicio(
            Servicio servicio
    ) {

        try {

            validarServicio(servicio);

            boolean guardado =
                    repository.guardar(
                            servicio
                    );

            if (guardado) {

                return "OK";
            }

            return "No se pudo guardar el servicio.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error guardando servicio.";
        }
    }

    // =========================
    // 🔥 VALIDACIONES
    // =========================
    private void validarServicio(
            Servicio servicio
    ) {

        if (servicio == null) {

            throw new BusinessException(
                    "El servicio es obligatorio."
            );
        }

        if (
                servicio.getNombre() == null
                        || servicio.getNombre()
                        .trim()
                        .isEmpty()
        ) {

            throw new BusinessException(
                    "El nombre del servicio es obligatorio."
            );
        }
    }
}