package controller;

import service.ProveedorService;

/**
 * Controlador del módulo de proveedores.
 *
 * RESPONSABILIDADES:
 * ✔ Recibir datos desde la vista
 * ✔ Delegar lógica al Service
 * ✔ No contiene lógica de negocio
 *
 * 🔥 Arquitectura:
 * View → Controller → Service → Repository
 */
public class ProveedorController {

    private ProveedorService proveedorService;

    /**
     * Constructor: inicializa el servicio.
     */
    public ProveedorController() {
        this.proveedorService = new ProveedorService();
    }

    /**
     * Registra un nuevo proveedor en el sistema.
     *
     * @param nombre nombre o razón social
     * @param tipoId tipo de identificación (NIT, CC, etc.)
     * @param numeroId número de identificación
     * @param dv dígito de verificación (opcional)
     * @param direccion dirección
     * @param ciudad ciudad
     * @param telefono teléfono
     * @param email email
     * @param contacto persona de contacto
     * @param celular celular del contacto
     * @param emailContacto email del contacto
     * @return "OK" si todo sale bien o mensaje de error
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

        return proveedorService.guardarProveedor(
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
    }
}