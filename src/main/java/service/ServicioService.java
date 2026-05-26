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
 * - Gestión CRUD
 */
public class ServicioService {

    private final ServicioRepository repository;

    // =========================
    // CONSTRUCTOR
    // =========================

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
    // 🔹 GUARDAR / ACTUALIZAR
    // =========================

    public String guardarServicio(
            Servicio servicio
    ) {

        try {

            validarServicio(servicio);

            boolean guardado;

            // =========================
            // 🔥 NUEVO
            // =========================

            if (servicio.getIdServicio() == 0) {

                // 🔥 VALIDAR CÓDIGO
                if (
                        repository.existeCodigo(
                                servicio.getCodigo()
                        )
                ) {

                    return "Ya existe un servicio con ese código.";
                }

                // 🔥 VALIDAR NOMBRE
                if (
                        repository.existeNombre(
                                servicio.getNombre()
                        )
                ) {

                    return "Ya existe un servicio con ese nombre.";
                }

                guardado =
                        repository.guardar(
                                servicio
                        );

            } else {

                // =========================
                // 🔥 EDITAR
                // =========================

                guardado =
                        repository.actualizar(
                                servicio
                        );
            }

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
    // 🔹 ACTUALIZAR
    // 🔥 Compatibilidad Controller
    // =========================

    public String actualizarServicio(
            Servicio servicio
    ) {

        return guardarServicio(
                servicio
        );
    }

    // =========================
    // 🔹 INACTIVAR
    // =========================

    public boolean inactivarServicio(
            int idServicio
    ) {

        if (idServicio <= 0) {

            throw new BusinessException(
                    "ID servicio inválido."
            );
        }

        return repository.inactivar(
                idServicio
        );
    }

    // =========================
    // 🔥 VALIDACIONES
    // =========================

    private void validarServicio(
            Servicio servicio
    ) {

        // =========================
        // OBJETO
        // =========================

        if (servicio == null) {

            throw new BusinessException(
                    "El servicio es obligatorio."
            );
        }

        // =========================
        // CÓDIGO
        // =========================

        if (
                servicio.getCodigo() == null
                        || servicio.getCodigo()
                        .trim()
                        .isEmpty()
        ) {

            throw new BusinessException(
                    "El código del servicio es obligatorio."
            );
        }

        if (
                servicio.getCodigo()
                        .trim()
                        .length() < 2
        ) {

            throw new BusinessException(
                    "El código del servicio es demasiado corto."
            );
        }

        // =========================
        // NOMBRE
        // =========================

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

        if (
                servicio.getNombre()
                        .trim()
                        .length() < 3
        ) {

            throw new BusinessException(
                    "El nombre del servicio es demasiado corto."
            );
        }

        // =========================
        // DESCRIPCIÓN
        // =========================

        if (
                servicio.getDescripcion() != null
                        && servicio.getDescripcion()
                        .length() > 200
        ) {

            throw new BusinessException(
                    "La descripción es demasiado larga."
            );
        }

        // =========================
        // CATEGORÍA
        // =========================

        if (
                servicio.getCategoria() != null
                        && servicio.getCategoria()
                        .length() > 100
        ) {

            throw new BusinessException(
                    "La categoría es demasiado larga."
            );
        }

        // =========================
        // PRECIO
        // =========================

        if (
                servicio.getPrecioBase() < 0
        ) {

            throw new BusinessException(
                    "El precio base es inválido."
            );
        }

        // =========================
        // TIEMPO ESTIMADO
        // =========================

        if (
                servicio.getTiempoEstimadoHoras() < 0
        ) {

            throw new BusinessException(
                    "El tiempo estimado es inválido."
            );
        }
    }
}