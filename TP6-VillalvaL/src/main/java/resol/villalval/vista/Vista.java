package resol.villalval.vista;

import java.util.Scanner;

/**
 * Vista general del sistema. Solo imprime menús y lee la opción elegida;
 * no contiene lógica de negocio.
 */
public class Vista {

    public static int mostrarMenuPrincipal(Scanner sc) {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Gestión de Videojuegos");
        System.out.println("2. Gestión de Ventas");
        System.out.println("0. Salir");
        return leerOpcion(sc);
    }

    public static int mostrarMenuVideojuegos(Scanner sc) {
        System.out.println("\n=== GESTIÓN DE VIDEOJUEGOS ===");
        System.out.println("1. Listar videojuegos");
        System.out.println("2. Buscar videojuego por ID");
        System.out.println("3. Agregar videojuego");
        System.out.println("4. Actualizar videojuego");
        System.out.println("5. Eliminar videojuego");
        System.out.println("6. Videojuegos que necesitan reposición");
        System.out.println("7. Videojuegos disponibles para la venta");
        System.out.println("0. Volver al menú principal");
        return leerOpcion(sc);
    }

    public static int mostrarMenuVentas(Scanner sc) {
        System.out.println("\n=== GESTIÓN DE VENTAS ===");
        System.out.println("1. Listar ventas");
        System.out.println("2. Buscar venta por ID");
        System.out.println("3. Registrar venta");
        System.out.println("4. Buscar ventas de un videojuego");
        System.out.println("5. Reporte de ventas del mes actual");
        System.out.println("0. Volver al menú principal");
        return leerOpcion(sc);
    }

    private static int leerOpcion(Scanner sc) {
        System.out.print("Seleccione una opción: ");
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Ingrese un número válido: ");
        }
        int opcion = sc.nextInt();
        sc.nextLine();
        return opcion;
    }
}
