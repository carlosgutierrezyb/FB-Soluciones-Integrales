package exception;

/**
 * Excepción personalizada para manejar errores de negocio.
 * Permite centralizar mensajes claros y controlados.
 */
public class BusinessException extends RuntimeException {

    /**
     * Constructor con mensaje personalizado.
     */
    public BusinessException(String message) {
        super(message);
    }
}