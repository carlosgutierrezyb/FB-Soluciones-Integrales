package service;

import exception.BusinessException;
import model.Categoria;
import model.Servicio;
import repository.CategoriaRepository;
import repository.ServicioRepository;
import util.CodigoGenerator;

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

    private final CategoriaRepository categoriaRepo;

    // =========================
    // CONSTRUCTOR
    // =========================

    public ServicioService() {

        this.repository =
                new ServicioRepository();

        this.categoriaRepo =
                new CategoriaRepository();
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
    // 🔹 OBTENER CATEGORÍAS
    // =========================

    public List<Categoria> obtenerCategorias() {

        return categoriaRepo.listarCategorias();
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

            // =========================
            // 🔥 BUSCAR CATEGORÍA
            // =========================

            Categoria categoria =
                    categoriaRepo.buscarPorId(
                            servicio.getIdCategoria()
                    );

            if (categoria == null) {

                return "La categoría no existe.";
            }

            // =========================
            // 🔥 OBTENER ÚLTIMO SKU
            // =========================

            String ultimoCodigo =
                    repository.obtenerUltimoCodigoPorCategoria(
                            servicio.getIdCategoria()
                    );

            // =========================
            // 🔥 GENERAR SKU
            // EJ:
            // SERV-DVR-0001
            // =========================

            String codigoGenerado =
                    CodigoGenerator.generarCodigoServicio(
                            categoria.getPrefijo(),
                            ultimoCodigo
                    );

            servicio.setCodigoReferencia(
                    codigoGenerado
            );

            boolean guardado;

            // =========================
            // 🔥 NUEVO
            // =========================

            if (servicio.getId() == 0) {

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
                        .length() > 255
        ) {

            throw new BusinessException(
                    "La descripción es demasiado larga."
            );
        }

        // =========================
        // CATEGORÍA
        // =========================

        if (
                servicio.getIdCategoria() <= 0
        ) {

            throw new BusinessException(
                    "Debe seleccionar una categoría."
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