package service;

import exception.BusinessException;
import model.Proveedor;
import repository.ProveedorRepository;

/**
 * Servicio encargado de la lógica de negocio de proveedores.
 *
 * RESPONSABILIDADES:
 * ✔ Validar datos
 * ✔ Construir el modelo
 * ✔ Orquestar persistencia
 *
 * ❌ No accede directamente a la UI
 * ❌ No contiene SQL
 *
 * 🔥 Arquitectura:
 * Controller → Service → Repository
 */


public class ProveedorService {



    private ProveedorRepository proveedorRepo;



    /**
     * Constructor
     */
    public ProveedorService() {
        this.proveedorRepo = new ProveedorRepository();
    }



    /**
     * Registra un proveedor en el sistema.
     *
     * @return "OK" si todo es correcto o mensaje de error
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

            // =========================
            // 🔹 1. VALIDACIONES
            // =========================
            validarDatos(nombre, tipoId, numeroId, direccion, ciudad);

            // =========================
            // 🔹 2. CONSTRUIR MODELO
            // =========================

            Proveedor proveedor = construirProveedor(
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


            // =========================
            // 🔹 3. PERSISTENCIA
            // =========================
            boolean guardado = proveedorRepo.guardar(proveedor);

            return guardado ? "OK" : "Error al guardar el proveedor.";

        } catch (BusinessException e) {
            return e.getMessage();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error interno del sistema.";
        }
    }

    // =========================
    // MÉTODOS PRIVADOS
    // =========================

    /**
     * Validaciones de negocio
     */
    private void validarDatos(
            String nombre,
            String tipoId,
            String numeroId,
            String direccion,
            String ciudad
    ) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre o razón social es obligatorio.");
        }

        if (tipoId == null || tipoId.trim().isEmpty()) {
            throw new BusinessException("El tipo de identificación es obligatorio.");
        }

        if (numeroId == null || numeroId.trim().isEmpty()) {
            throw new BusinessException("El número de identificación es obligatorio.");
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BusinessException("La dirección es obligatoria.");
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new BusinessException("La ciudad es obligatoria.");
        }
    }

    /**
     * Construye el objeto Proveedor
     */
    private Proveedor construirProveedor(
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

        Proveedor p = new Proveedor();

        p.setNombreRazonSocial(nombre);
        p.setTipoIdentificacion(tipoId);
        p.setNumeroIdentificacion(numeroId);
        p.setDv(dv == null ? "" : dv.trim());

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