package resol.villalval.controlador;

import java.sql.SQLException;
import java.util.Scanner;

import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;
import resol.villalval.modelo.Videojuego;
import resol.villalval.vista.VideojuegoVista;
import resol.villalval.vista.Vista;


public class VideojuegoControlador {

    private final VideojuegoVista vista = new VideojuegoVista();

    public void ejecutarMenu(Scanner sc) {
        int opcion;
        do {
            opcion = Vista.mostrarMenuVideojuegos(sc);
            try {
                switch (opcion) {
                    case 1 -> vista.mostrarListaVideojuegos(Videojuego.listar());
                    case 2 -> {
                        int id = vista.pedirId(sc, "ID del videojuego: ");
                        vista.mostrarVideojuego(Videojuego.buscarPorId(id));
                    }
                    case 3 -> {
                        Videojuego nuevo = vista.pedirDatosNuevoVideojuego(sc);
                        nuevo.insertar();
                        vista.mostrarMensaje("Videojuego '" + nuevo.getNombre() + "' registrado correctamente.");
                    }
                    case 4 -> {
                        int id = vista.pedirId(sc, "ID del videojuego a actualizar: ");
                        Videojuego actualizado = vista.pedirDatosActualizacion(sc, id);
                        actualizado.actualizar();
                        vista.mostrarMensaje("Videojuego actualizado correctamente.");
                    }
                    case 5 -> {
                        int id = vista.pedirId(sc, "ID del videojuego a eliminar: ");
                        Videojuego.eliminar(id);
                        vista.mostrarMensaje("Videojuego eliminado correctamente.");
                    }
                    case 6 -> vista.mostrarListaVideojuegos(Videojuego.listarQueNecesitanReposicion());
                    case 7 -> vista.mostrarListaVideojuegos(Videojuego.listarDisponibles());
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
