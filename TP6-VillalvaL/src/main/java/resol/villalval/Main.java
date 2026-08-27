package resol.villalval;

import java.util.Scanner;

import resol.villalval.conexion.ConexionBD;
import resol.villalval.controlador.VentaControlador;
import resol.villalval.controlador.VideojuegoControlador;
import resol.villalval.modelo.Venta;
import resol.villalval.modelo.Videojuego;
import resol.villalval.vista.Vista;

public class Main {

    public static void main(String[] args) {

        ConexionBD.verificarConexion();
        Videojuego.crearTabla();
        Venta.crearTabla();

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
