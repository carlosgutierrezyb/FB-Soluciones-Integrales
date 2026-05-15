package service;

import exception.BusinessException;
import model.Cliente;
import repository.ClienteRepository;

import java.util.List;

/**
 * Servicio de clientes.
 *
 * 🔥 ERP F&B:
 * - Lógica de negocio
 * - Validaciones
 * - Normalización
 */
public class ClienteService {

    private ClienteRepository clienteRepo;

    public ClienteService() {

        this.clienteRepo =
                new ClienteRepository();
    }

    // =========================
    // 🔹 GUARDAR
    // =========================
    public String guardarCliente(
            Cliente cliente
    ) {

        try {

            validarCliente(cliente);

            normalizarDatos(cliente);

            boolean guardado =
                    clienteRepo.guardar(cliente);

            return guardado
                    ? "OK"
                    : "Error al guardar cliente.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error interno del sistema.";
        }
    }

    // =========================
    // 🔹 ACTUALIZAR
    // =========================
    public String actualizarCliente(
            Cliente cliente
    ) {

        try {

            if (cliente.getIdCliente() <= 0) {

                throw new BusinessException(
                        "Cliente inválido."
                );
            }

            validarCliente(cliente);

            normalizarDatos(cliente);

            boolean actualizado =
                    clienteRepo.actualizar(cliente);

            return actualizado
                    ? "OK"
                    : "Error al actualizar cliente.";

        } catch (BusinessException e) {

            return e.getMessage();

        } catch (Exception e) {

            e.printStackTrace();

            return "Error interno del sistema.";
        }
    }

    // =========================
    // 🔹 ELIMINAR
    // =========================
    public boolean eliminarCliente(
            int idCliente
    ) {

        if (idCliente <= 0) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        return clienteRepo.eliminar(idCliente);
    }

    // =========================
    // 🔹 LISTAR
    // =========================
    public List<Cliente> listarClientes() {

        return clienteRepo.listarTodos();
    }

    // =========================
    // 🔹 BUSCAR POR ID
    // =========================
    public Cliente buscarClientePorId(
            int idCliente
    ) {

        if (idCliente <= 0) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        return clienteRepo.buscarPorId(idCliente);
    }

    // =========================
    // 🔹 VALIDACIONES
    // =========================
    private void validarCliente(
            Cliente c
    ) {

        if (c == null) {

            throw new BusinessException(
                    "Cliente inválido."
            );
        }

        if (esVacio(
                c.getNombre()
        )) {

            throw new BusinessException(
                    "El nombre es obligatorio."
            );
        }

        if (esVacio(
                c.getIdentificacion()
        )) {

            throw new BusinessException(
                    "La identificación es obligatoria."
            );
        }

        // 🔥 VALIDACIÓN OPCIONAL EMAIL CONTACTO
        if (c.getContactoEmail() != null &&
                !c.getContactoEmail().trim().isEmpty()) {

            if (!c.getContactoEmail().contains("@")) {

                throw new BusinessException(
                        "El email del contacto no es válido."
                );
            }
        }
    }

    // =========================
    // 🔹 NORMALIZACIÓN
    // =========================
    private void normalizarDatos(
            Cliente c
    ) {

        c.setNombre(
                c.getNombre().trim()
        );

        c.setIdentificacion(
                c.getIdentificacion().trim()
        );

        if (c.getCorreo() != null) {

            c.setCorreo(
                    c.getCorreo().trim()
            );
        }

        if (c.getCiudad() != null) {

            c.setCiudad(
                    c.getCiudad().trim()
            );
        }

        // 🔥 NUEVOS CAMPOS
        if (c.getContactoNombre() != null) {

            c.setContactoNombre(
                    c.getContactoNombre().trim()
            );
        }

        if (c.getContactoTelefono() != null) {

            c.setContactoTelefono(
                    c.getContactoTelefono().trim()
            );
        }

        if (c.getContactoEmail() != null) {

            c.setContactoEmail(
                    c.getContactoEmail().trim()
            );
        }

        if (c.getEstado() == null ||
                c.getEstado().trim().isEmpty()) {

            c.setEstado("ACTIVO");
        }
    }

    // =========================
    // 🔹 UTIL
    // =========================
    private boolean esVacio(
            String valor
    ) {

        return valor == null ||
                valor.trim().isEmpty();
    }
}