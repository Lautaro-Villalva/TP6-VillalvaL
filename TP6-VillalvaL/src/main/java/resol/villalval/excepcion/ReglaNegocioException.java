package resol.villalval.excepcion;

/**
 * Excepción de negocio genérica para reglas del TP6, por ejemplo:
 * - Stock insuficiente al registrar una venta.
 * - Venta sobre un videojuego suspendido.
 */
public class ReglaNegocioException extends Exception {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
