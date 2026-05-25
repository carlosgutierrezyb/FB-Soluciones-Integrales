package controller;

import model.Cliente;
import service.ClienteService;

import java.util.List;

/**
 * Controlador del módulo clientes.
 *
 * 🔥 ERP F&B:
 * - Construye objetos Cliente
 * - Delega lógica al Service
 * - Intermediario entre UI y negocio
 */
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController() {

        this.clienteService =
                new ClienteService();
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardarCliente(

            String nombre,
            String tipoIdentificacion,
            String identificacion,
            String direccion,
            String ciudad,
            String telefono,
            String correo,

            // 🔥 NUEVOS CAMPOS
            String contactoNombre,
            String contactoTelefono,
            String contactoEmail

    ) {

        try {

            Cliente c =
                    construirCliente(

                            0,

                            nombre,

                            tipoIdentificacion,

                            identificacion,

                            direccion,

                            ciudad,

                            telefono,

                            correo,

                            contactoNombre,

                            contactoTelefono,

                            contactoEmail,

                            "ACTIVO"
                    );

            return clienteService
                    .guardarCliente(c);

        } catch (Exception e) {

            e.printStackTrace();

            return "Error en controlador.";
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public String actualizarCliente(

            int idCliente,

            String nombre,

            String tipoIdentificacion,

            String identificacion,

            String direccion,

            String ciudad,

            String telefono,

            String correo,

            String contactoNombre,

            String contactoTelefono,

            String contactoEmail,

            String estado
    ) {

        try {

            Cliente c =
                    construirCliente(

                            idCliente,

                            nombre,

                            tipoIdentificacion,

                            identificacion,

                            direccion,

                            ciudad,

                            telefono,

                            correo,

                            contactoNombre,

                            contactoTelefono,

                            contactoEmail,

                            estado
                    );

            return clienteService
                    .actualizarCliente(c);

        } catch (Exception e) {

            e.printStackTrace();

            return "Error en controlador.";
        }
    }

    // =========================
    // 🔹 ELIMINAR
    // =========================
    public boolean eliminarCliente(
            int idCliente
    ) {

        return clienteService
                .eliminarCliente(idCliente);
    }

    // =========================
    // 🔹 LISTAR
    // =========================
    public List<Cliente> listarClientes() {

        return clienteService
                .listarClientes();
    }

    // =========================
    // 🔥 ALIAS ERP/UI
    // =========================
    public List<Cliente> listarTodos() {

        return listarClientes();
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Cliente buscarClientePorId(
            int idCliente
    ) {

        return clienteService
                .buscarClientePorId(idCliente);
    }

    // =========================
    // 🔧 CONSTRUIR CLIENTE
    // =========================
    private Cliente construirCliente(

            int idCliente,

            String nombre,

            String tipoIdentificacion,

            String identificacion,

            String direccion,

            String ciudad,

            String telefono,

            String correo,

            // 🔥 NUEVOS CAMPOS
            String contactoNombre,

            String contactoTelefono,

            String contactoEmail,

            String estado

    ) {

        Cliente c =
                new Cliente();

        c.setIdCliente(
                idCliente
        );

        c.setNombre(
                nombre
        );

        c.setTipoIdentificacion(
                tipoIdentificacion
        );

        c.setIdentificacion(
                identificacion
        );

        c.setDireccion(
                direccion
        );

        c.setCiudad(
                ciudad
        );

        c.setTelefono(
                telefono
        );

        c.setCorreo(
                correo
        );

        // 🔥 NUEVOS CAMPOS
        c.setContactoNombre(
                contactoNombre
        );

        c.setContactoTelefono(
                contactoTelefono
        );

        c.setContactoEmail(
                contactoEmail
        );

        c.setEstado(
                estado
        );

        return c;
    }
}