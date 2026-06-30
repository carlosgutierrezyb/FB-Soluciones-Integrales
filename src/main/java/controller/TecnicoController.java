package controller;

import model.Tecnico;
import repository.TecnicoRepository;

import java.util.List;

/**
 * Controller de Técnicos.
 *
 * ERP F&B:
 * - Consulta de técnicos
 * - Filtro por especialidad
 * - Búsqueda por ID
 * - Soporte para asignación de órdenes de servicio
 */
public class TecnicoController {

    private final TecnicoRepository repository;

    public TecnicoController() {
        this.repository = new TecnicoRepository();
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Tecnico> listarTodos() {
        return repository.listarTodos();
    }

    // =========================
    // LISTAR ACTIVOS
    // =========================
    public List<Tecnico> listarActivos() {
        return repository.listarActivos();
    }

    // =========================
    // LISTAR POR ESPECIALIDAD
    // =========================
    public List<Tecnico> listarPorEspecialidad(int idEspecialidad) {
        return repository.listarPorEspecialidad(idEspecialidad);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public Tecnico buscarPorId(int idTecnico) {
        return repository.buscarPorId(idTecnico);
    }

    // ==========================================
    // 🔥 NUEVO: LISTAR POR ORDEN CON ESPECIALIDAD
    // ==========================================
    public List<Tecnico> listarPorOrdenConEspecialidad(int idOrden) {
        return repository.listarPorOrdenConEspecialidad(idOrden);
    }

    // ==========================================
    // 🔥 NUEVO: ASIGNAR TÉCNICO A ORDEN
    // ==========================================
    public String asignarTecnicoAOrden(int idOrden, int idTecnico) {
        return repository.asignarTecnicoAOrden(idOrden, idTecnico);
    }
}