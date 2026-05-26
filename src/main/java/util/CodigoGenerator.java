package util;

import exception.BusinessException;

/**
 * 🔥 Generador centralizado de códigos ERP
 *
 * Ejemplos:
 *
 * PROD-DVR-0001
 * PROD-CIP-0001
 * SERV-DVR-0001
 */
public class CodigoGenerator {

    // =========================
    // 🔹 PRODUCTOS
    // =========================
    public static String generarCodigoProducto(
            String prefijoCategoria,
            String ultimoCodigo
    ) {

        return generarCodigo(
                "PROD",
                prefijoCategoria,
                ultimoCodigo
        );
    }

    // =========================
    // 🔹 SERVICIOS
    // =========================
    public static String generarCodigoServicio(
            String prefijoCategoria,
            String ultimoCodigo
    ) {

        return generarCodigo(
                "SERV",
                prefijoCategoria,
                ultimoCodigo
        );
    }

    // =========================
    // 🔥 GENERADOR CENTRAL
    // =========================
    private static String generarCodigo(
            String tipo,
            String prefijoCategoria,
            String ultimoCodigo
    ) {

        int nuevoCorrelativo = 1;

        // =========================
        // EXTRAER ÚLTIMO NÚMERO
        // =========================
        if (
                ultimoCodigo != null
                        && !ultimoCodigo.isEmpty()
        ) {

            try {

                String[] partes =
                        ultimoCodigo.split("-");

                int ultimoNumero =
                        Integer.parseInt(
                                partes[2]
                        );

                nuevoCorrelativo =
                        ultimoNumero + 1;

            } catch (Exception e) {

                throw new BusinessException(
                        "Error generando código."
                );
            }
        }

        // =========================
        // FORMATO
        // =========================
        String correlativo =
                String.format(
                        "%04d",
                        nuevoCorrelativo
                );

        return tipo
                + "-"
                + prefijoCategoria.toUpperCase()
                + "-"
                + correlativo;
    }
}