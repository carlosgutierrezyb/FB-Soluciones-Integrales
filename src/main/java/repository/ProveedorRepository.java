package repository;

import model.Proveedor;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository de proveedores.
 */
public class ProveedorRepository {

    /**
     * Lista todos los proveedores.
     */
    public List<Proveedor> listarTodos() {

        List<Proveedor> lista = new ArrayList<>();

        String sql = "SELECT * FROM proveedores ORDER BY nombre_razon_social";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error listando proveedores: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Guarda proveedor.
     */
    public boolean guardar(Proveedor p) {

        String sql = "INSERT INTO proveedores " +
                "(tipo_identificacion, numero_identificacion, dv, nombre_razon_social, direccion, ciudad, telefono, email, contacto_nombre, contacto_celular, contacto_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTipoIdentificacion());
            ps.setString(2, p.getNumeroIdentificacion());
            ps.setString(3, p.getDv());
            ps.setString(4, p.getNombreRazonSocial());
            ps.setString(5, p.getDireccion());
            ps.setString(6, p.getCiudad());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getContactoNombre());
            ps.setString(10, p.getContactoCelular());
            ps.setString(11, p.getContactoEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error guardando proveedor: " + e.getMessage());
            return false;
        }
    }

    private Proveedor mapear(ResultSet rs) throws SQLException {

        Proveedor p = new Proveedor();

        p.setId(rs.getInt("id_proveedor"));
        p.setTipoIdentificacion(rs.getString("tipo_identificacion"));
        p.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        p.setDv(rs.getString("dv"));
        p.setNombreRazonSocial(rs.getString("nombre_razon_social"));
        p.setDireccion(rs.getString("direccion"));
        p.setCiudad(rs.getString("ciudad"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setContactoNombre(rs.getString("contacto_nombre"));
        p.setContactoCelular(rs.getString("contacto_celular"));
        p.setContactoEmail(rs.getString("contacto_email"));

        return p;
    }
}