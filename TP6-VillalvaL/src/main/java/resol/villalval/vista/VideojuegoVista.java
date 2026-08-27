package resol.villalval.vista;

import java.util.List;
import java.util.Scanner;

import resol.villalval.modelo.Videojuego;


public class VideojuegoVista {

    public Videojuego pedirDatosNuevoVideojuego(Scanner sc) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Género: ");
        String genero = sc.nextLine();
        double precio = leerDouble(sc, "Precio: ");
        int unidadesDisponibles = leerEntero(sc, "Unidades disponibles: ");
        int nivelReposicion = leerEntero(sc, "Nivel de reposición: ");

        return new Videojuego(nombre, genero, precio, unidadesDisponibles, nivelReposicion);
    }

    public Videojuego pedirDatosActualizacion(Scanner sc, int id) {
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo género: ");
        String genero = sc.nextLine();
        double precio = leerDouble(sc, "Nuevo precio: ");
        int unidadesDisponibles = leerEntero(sc, "Nuevas unidades disponibles: ");
        int nivelReposicion = leerEntero(sc, "Nuevo nivel de reposición: ");
        System.out.print("¿Suspendido? (s/n): ");
        boolean suspendido = sc.nextLine().trim().equalsIgnoreCase("s");

        return new Videojuego(id, nombre, genero, precio, unidadesDisponibles, nivelReposicion, suspendido);
    }

    public int pedirId(Scanner sc, String mensaje) {
        return leerEntero(sc, mensaje);
    }

    public void mostrarVideojuego(Videojuego v) {
        System.out.println(v);
    }

    public void mostrarListaVideojuegos(List<Videojuego> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay videojuegos para mostrar.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private int leerEntero(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Ingrese un número válido: ");
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }

    private double leerDouble(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print("Ingrese un número válido: ");
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }
}
