package util;

import exception.BusinessException;

/**
 * Clase utilitaria para conversión segura de tipos.
 * Evita repetir lógica de parsing en todo el sistema.
 */
public class ParseUtil {

    /**
     * Convierte un String a entero.
     *
     * @param value valor en texto
     * @param fieldName nombre del campo (para mensajes de error claros)
     * @return entero convertido
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static int toInt(String value, String fieldName) {

        try {
            return Integer.parseInt(value);

        } catch (NumberFormatException e) {
            throw new BusinessException(fieldName + " debe ser un número entero válido.");
        }
    }

    /**
     * Convierte un String a double.
     *
     * @param value valor en texto
     * @param fieldName nombre del campo
     * @return número decimal
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static double toDouble(String value, String fieldName) {

        try {
            return Double.parseDouble(value);

        } catch (NumberFormatException e) {
            throw new BusinessException(fieldName + " debe ser un número entero válido.");
        }
    }

    /**
     * Convierte un String a entero positivo (>= 0).
     */
    public static int toPositiveInt(String value, String fieldName) {

        int number = toInt(value, fieldName);

        if (number < 0) {
            throw new BusinessException(fieldName + " no puede ser negativo.");
        }

        return number;
    }

    /**
     * Convierte un String a double positivo (> 0).
     */
    public static double toPositiveDouble(String value, String fieldName) {

        double number = toDouble(value, fieldName);

        if (number <= 0) {
            throw new BusinessException(fieldName + " debe ser mayor a cero.");
        }

        return number;
    }
}