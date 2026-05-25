package controller;

import model.DireccionCliente;
import service.DireccionClienteService;

import java.util.List;

/**
 * Controller Direcciones Cliente.
 *
 * 🔥 ERP F&B:
 * - Comunicación View ↔ Service
 * - Gestión sedes clientes
 * - Base órdenes de servicio
 */
public class DireccionClienteController {

    private DireccionClienteService service;

    public DireccionClienteController() {

        this.service =
                new DireccionClienteService();
    }

    // =========================
    // 🔹 LISTAR POR CLIENTE
    // =========================
    public List<DireccionCliente> listarPorCliente(
            int idCliente
    ) {

        return service.listarPorCliente(
                idCliente
        );
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardar(

            int idCliente,

            String nombreSede,

            String direccion,

            String ciudad,

            String contactoNombre,

            String contactoTelefono

    ) {

        try {

            DireccionCliente d =
                    new DireccionCliente();

            d.setIdCliente(
                    idCliente
            );

            d.setNombreSede(
                    nombreSede
            );

            d.setDireccion(
                    direccion
            );

            d.setCiudad(
                    ciudad
            );

            d.setContactoNombre(
                    contactoNombre
            );

            d.setContactoTelefono(
                    contactoTelefono
            );

            d.setEstado(true);

            return service.guardar(d);

        } catch (Exception e) {

            e.printStackTrace();

            return "Error en controlador.";
        }
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public DireccionCliente buscarPorId(
            int idDireccion
    ) {

        return service.buscarPorId(
                idDireccion
        );
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public String actualizar(
            DireccionCliente direccion
    ) {

        try {
            return service.actualizar(
                    direccion
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al actualizar en el controlador.";
        }
    }

    // =========================
    // 🔹 ELIMINAR
    // =========================
    public boolean eliminar(
            int idDireccion
    ) {

        return service.eliminar(
                idDireccion
        );
    }
}