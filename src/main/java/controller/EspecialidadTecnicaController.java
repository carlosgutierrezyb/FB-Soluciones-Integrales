package controller;

import model.EspecialidadTecnica;
import repository.EspecialidadTecnicaRepository;

import java.util.List;

/**
 * Controller de Especialidades Técnicas.
 *
 * ERP F&B:
 * - Consulta de especialidades
 * - Soporte para asignación de técnicos
 * - Filtro de técnicos por especialidad
 */
public class EspecialidadTecnicaController {

    private final EspecialidadTecnicaRepository repository;

    public EspecialidadTecnicaController() {

        this.repository =
                new EspecialidadTecnicaRepository();
    }

    // =========================
    // LISTAR TODAS
    // =========================

    public List<EspecialidadTecnica> listarTodas() {

        return repository.listarTodas();
    }

    // =========================
    // LISTAR ACTIVAS
    // =========================

    public List<EspecialidadTecnica> listarActivas() {

        return repository.listarActivas();
    }

    // =========================
    // BUSCAR POR ID
    // =========================

    public EspecialidadTecnica buscarPorId(
            int idEspecialidad
    ) {

        return repository.buscarPorId(
                idEspecialidad
        );
    }
}