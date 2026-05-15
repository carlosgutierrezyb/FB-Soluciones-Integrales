package controller;

import model.Proveedor;
import service.ProveedorService;

import java.util.List;

/**
 * Controlador del módulo de proveedores.
 *
 * 🔥 ERP F&B:
 * - Construye objetos Proveedor
 * - Delega lógica al Service
 * - Intermediario entre UI y negocio
 */
public class ProveedorController {

    private ProveedorService proveedorService;

    public ProveedorController() {

        this.proveedorService =
                new ProveedorService();
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardarProveedor(

            String nombre,
            String tipoId,
            String numeroId,
            String dv,
            String direccion,
            String ciudad,
            String telefono,
            String email,
            String contacto,
            String celular,
            String emailContacto

    ) {

        try {

            Proveedor p =
                    construirProveedor(
                            0,
                            nombre,
                            tipoId,
                            numeroId,
                            dv,
                            direccion,
                            ciudad,
                            telefono,
                            email,
                            contacto,
                            celular,
                            emailContacto
                    );

            return proveedorService
                    .guardarProveedor(p);

        } catch (Exception e) {

            e.printStackTrace();

            return "Error en el controlador.";
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public String actualizarProveedor(

            int idProveedor,
            String nombre,
            String tipoId,
            String numeroId,
            String dv,
            String direccion,
            String ciudad,
            String telefono,
            String email,
            String contacto,
            String celular,
            String emailContacto

    ) {

        try {

            Proveedor p =
                    construirProveedor(
                            idProveedor,
                            nombre,
                            tipoId,
                            numeroId,
                            dv,
                            direccion,
                            ciudad,
                            telefono,
                            email,
                            contacto,
                            celular,
                            emailContacto
                    );

            return proveedorService
                    .actualizarProveedor(p);

        } catch (Exception e) {

            e.printStackTrace();

            return "Error en el controlador.";
        }
    }

    // =========================
    // 🔹 LISTAR
    // =========================
    public List<Proveedor> listarProveedores() {

        return proveedorService
                .listarProveedores();
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Proveedor buscarProveedorPorId(
            int idProveedor
    ) {

        return proveedorService
                .buscarPorId(idProveedor);
    }

    // =========================
    // 🔧 CONSTRUIR OBJETO
    // =========================
    private Proveedor construirProveedor(

            int idProveedor,
            String nombre,
            String tipoId,
            String numeroId,
            String dv,
            String direccion,
            String ciudad,
            String telefono,
            String email,
            String contacto,
            String celular,
            String emailContacto

    ) {

        Proveedor p =
                new Proveedor();

        p.setId(idProveedor);

        p.setNombreRazonSocial(nombre);

        p.setTipoIdentificacion(tipoId);

        p.setNumeroIdentificacion(numeroId);

        p.setDv(dv);

        p.setDireccion(direccion);

        p.setCiudad(ciudad);

        p.setTelefono(telefono);

        p.setEmail(email);

        p.setContactoNombre(contacto);

        p.setContactoCelular(celular);

        p.setContactoEmail(emailContacto);

        return p;
    }
}