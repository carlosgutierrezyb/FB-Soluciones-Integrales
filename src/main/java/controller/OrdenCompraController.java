package controller;

import model.OrdenCompra;
import service.OrdenCompraService;

import java.util.List;

public class OrdenCompraController {

    private OrdenCompraService service;

    public OrdenCompraController() {
        this.service = new OrdenCompraService();
    }

    public List<OrdenCompra> listarTodas() {
        return service.obtenerOrdenesPendientes(); // o crear listarTodas()
    }

    public OrdenCompra buscarPorId(int id) {
        return service.buscarPorId(id);
    }
}