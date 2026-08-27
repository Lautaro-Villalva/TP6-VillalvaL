package resol.villalval;

import resol.villalval.conexion.ConexionBD;
import resol.villalval.controlador.VentaControlador;
import resol.villalval.controlador.VideojuegoControlador;
import resol.villalval.modelo.dao.VentaDAO;
import resol.villalval.modelo.dao.VideojuegoDAO;
import resol.villalval.vista.Vista;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. Verificar conexión y crear las tablas (incluye los nuevos campos de stock)
        ConexionBD.verificarConexion();
        VideojuegoDAO.crearTabla();
        VentaDAO.crearTabla();

        Scanner sc = new Scanner(System.in);
        VideojuegoControlador videojuegoControlador = new VideojuegoControlador();
        VentaControlador ventaControlador = new VentaControlador();

        int opcion;
        do {
            opcion = Vista.mostrarMenuPrincipal(sc);
            switch (opcion) {
                case 1 -> videojuegoControlador.ejecutarMenu(sc);
                case 2 -> ventaControlador.ejecutarMenu(sc);
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

        sc.close();
    }
}
