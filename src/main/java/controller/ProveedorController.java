package controller;

import model.Proveedor;
import service.ProveedorService;

/**
 * Controlador del módulo de proveedores.
 *
 * 🔥 NIVEL PRO:
 * - Construye el objeto Proveedor
 * - Delega al Service
 */
public class ProveedorController {

    private ProveedorService proveedorService;

    public ProveedorController() {
        this.proveedorService = new ProveedorService();
    }

    /**
     * Registra proveedor
     */
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

            System.out.println("🎯 Controller: construyendo proveedor...");

            // =========================
            // 🔹 CONSTRUIR OBJETO
            // =========================
            Proveedor p = new Proveedor();

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

            // =========================
            // 🔹 ENVIAR AL SERVICE
            // =========================
            return proveedorService.guardarProveedor(p);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el controlador.";
        }
    }
}