package resol.villalval.controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import resol.villalval.excepcion.ReglaNegocioException;
import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;
import resol.villalval.modelo.Venta;
import resol.villalval.modelo.Videojuego;
import resol.villalval.vista.VentaVista;
import resol.villalval.vista.Vista;


public class VentaControlador {

    private final VentaVista vista = new VentaVista();

    public void ejecutarMenu(Scanner sc) {
        int opcion;
        do {
            opcion = Vista.mostrarMenuVentas(sc);
            try {
                switch (opcion) {
                    case 1 -> vista.mostrarListaVentas(Venta.listarDetalle());
                    case 2 -> {
                        int id = vista.pedirVentaId(sc);
                        vista.mostrarVentaDetalle(Venta.buscarPorId(id));
                    }
                    case 3 -> {
                        int videojuegoId = vista.pedirVideojuegoId(sc);
                        int cantidad = vista.pedirCantidad(sc);
                        LocalDate fecha = vista.pedirFecha(sc);
                        Venta.registrarVenta(fecha, cantidad, videojuegoId);
                        double descuento = Venta.calcularDescuento(cantidad);
                        vista.mostrarMensaje("Venta registrada correctamente (descuento aplicado: "
                                + (int) (descuento * 100) + "%).");
                    }
                    case 4 -> {
                        int videojuegoId = vista.pedirVideojuegoId(sc);
                        Videojuego videojuego = Videojuego.buscarPorId(videojuegoId);
                        vista.mostrarNombreVideojuego(videojuego.getNombre());
                        vista.mostrarListaVentas(Venta.buscarPorVideojuego(videojuegoId));
                    }
                    case 5 -> vista.mostrarListaVentas(Venta.reporteMesActual());
                    case 0 -> vista.mostrarMensaje("Volviendo al menú principal...");
                    default -> vista.mostrarMensaje("Opción inválida.");
                }
            } catch (VideojuegoNoEncontradoException | VentaInvalidaException | ReglaNegocioException e) {
                vista.mostrarMensaje(e.getMessage());
            } catch (SQLException e) {
                vista.mostrarMensaje("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }
}
