package resol.villalval.vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import resol.villalval.modelo.Venta;
import resol.villalval.modelo.VentaDetalle;


public class VentaVista {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public int pedirVideojuegoId(Scanner sc) {
        return leerEntero(sc, "ID del videojuego: ");
    }

    public int pedirVentaId(Scanner sc) {
        return leerEntero(sc, "ID de la venta: ");
    }

    public int pedirCantidad(Scanner sc) {
        return leerEntero(sc, "Cantidad vendida: ");
    }

    public LocalDate pedirFecha(Scanner sc) {
        System.out.print("Fecha de la venta (dd/MM/yyyy, Enter para hoy): ");
        String entrada = sc.nextLine().trim();
        if (entrada.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(entrada, FORMATO_FECHA);
        } catch (Exception e) {
            System.out.println("Fecha inválida, se utilizará la fecha de hoy.");
            return LocalDate.now();
        }
    }

    public void mostrarVenta(Venta v) {
        System.out.println(v);
    }

    public void mostrarVentaDetalle(VentaDetalle v) {
        if (v == null) {
            System.out.println("No existe una venta con el ID indicado.");
        } else {
            System.out.println(v);
        }
    }

    public void mostrarListaVentas(List<VentaDetalle> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay ventas para mostrar.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    public void mostrarNombreVideojuego(String nombre) {
        System.out.println(nombre);
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
}
