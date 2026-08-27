package resol.villalval.controlador;

import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;
import resol.villalval.modelo.Videojuego;
import resol.villalval.modelo.dao.VideojuegoDAO;
import resol.villalval.vista.Vista;
import resol.villalval.vista.VideojuegoVista;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Controlador de Videojuego: coordina la Vista y el Modelo (DAO),
 * y maneja las excepciones de JDBC (SQLException) y de negocio.
 */
public class VideojuegoControlador {

    private final VideojuegoDAO dao = new VideojuegoDAO();
    private final VideojuegoVista vista = new VideojuegoVista();

    public void ejecutarMenu(Scanner sc) {
        int opcion;
        do {
            opcion = Vista.mostrarMenuVideojuegos(sc);
            try {
                switch (opcion) {
                    case 1 -> vista.mostrarListaVideojuegos(dao.listarVideojuegos());
                    case 2 -> {
                        int id = vista.pedirId(sc, "ID del videojuego: ");
                        vista.mostrarVideojuego(dao.buscarPorId(id));
                    }
                    case 3 -> {
                        Videojuego nuevo = vista.pedirDatosNuevoVideojuego(sc);
                        dao.crearVideojuego(nuevo);
                        vista.mostrarMensaje("Videojuego '" + nuevo.getNombre() + "' registrado correctamente.");
                    }
                    case 4 -> {
                        int id = vista.pedirId(sc, "ID del videojuego a actualizar: ");
                        Videojuego actualizado = vista.pedirDatosActualizacion(sc, id);
                        dao.actualizarVideojuego(actualizado);
                        vista.mostrarMensaje("Videojuego actualizado correctamente.");
                    }
                    case 5 -> {
                        int id = vista.pedirId(sc, "ID del videojuego a eliminar: ");
                        dao.eliminarVideojuego(id);
                        vista.mostrarMensaje("Videojuego eliminado correctamente.");
                    }
                    case 6 -> vista.mostrarListaVideojuegos(dao.listarQueNecesitanReposicion());
                    case 7 -> vista.mostrarListaVideojuegos(dao.listarDisponibles());
                    case 0 -> vista.mostrarMensaje("Volviendo al menú principal...");
                    default -> vista.mostrarMensaje("Opción inválida.");
                }
            } catch (VideojuegoNoEncontradoException | VentaInvalidaException e) {
                vista.mostrarMensaje(e.getMessage());
            } catch (SQLException e) {
                vista.mostrarMensaje("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }
}
