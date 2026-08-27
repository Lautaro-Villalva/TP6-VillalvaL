package resol.villalval.controlador;

import resol.villalval.excepcion.ReglaNegocioException;
import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;
import resol.villalval.modelo.Videojuego;
import resol.villalval.modelo.dao.VideojuegoDAO;
import resol.villalval.modelo.dao.VentaDAO;
import resol.villalval.vista.Vista;
import resol.villalval.vista.VentaVista;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Controlador de Venta: coordina la Vista y el Modelo (DAO), calcula
 * a través del modelo el descuento y el stock, y maneja las excepciones
 * de JDBC (SQLException) y de negocio.
 */
public class VentaControlador {

    private final VentaDAO ventaDAO = new VentaDAO();
    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
    private final VentaVista vista = new VentaVista();

    public void ejecutarMenu(Scanner sc) {
        int opcion;
        do {
            opcion = Vista.mostrarMenuVentas(sc);
            try {
                switch (opcion) {
                    case 1 -> vista.mostrarListaVentas(ventaDAO.listarVentasDetalle());
                    case 2 -> {
                        int id = vista.pedirVentaId(sc);
                        vista.mostrarVentaDetalle(ventaDAO.buscarVentaPorId(id));
                    }
                    case 3 -> {
                        int videojuegoId = vista.pedirVideojuegoId(sc);
                        int cantidad = vista.pedirCantidad(sc);
                        LocalDate fecha = vista.pedirFecha(sc);
                        ventaDAO.registrarVenta(fecha, cantidad, videojuegoId);
                        double descuento = VentaDAO.calcularDescuento(cantidad);
                        vista.mostrarMensaje("Venta registrada correctamente (descuento aplicado: "
                                + (int) (descuento * 100) + "%).");
                    }
                    case 4 -> {
                        int videojuegoId = vista.pedirVideojuegoId(sc);
                        Videojuego videojuego = videojuegoDAO.buscarPorId(videojuegoId);
                        vista.mostrarNombreVideojuego(videojuego.getNombre());
                        vista.mostrarListaVentas(ventaDAO.buscarVentasPorVideojuego(videojuegoId));
                    }
                    case 5 -> vista.mostrarListaVentas(ventaDAO.reporteMesActual());
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
