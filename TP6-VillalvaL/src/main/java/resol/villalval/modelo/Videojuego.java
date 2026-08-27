package resol.villalval.modelo;

public class Videojuego {

    private int id;
    private String nombre;
    private String genero;
    private double precio;
    private int unidadesDisponibles;
    private int nivelReposicion;

    // true = suspendido (no disponible para la venta) / false = disponible
    private boolean suspendido;

    public Videojuego() {
    }

    // Constructor sin id (para dar de alta un videojuego nuevo)
    public Videojuego(String nombre, String genero, double precio, int unidadesDisponibles, int nivelReposicion) {
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
        this.unidadesDisponibles = unidadesDisponibles;
        this.nivelReposicion = nivelReposicion;
        this.suspendido = false;
    }

    // Constructor completo (para videojuegos ya existentes en la BD)
    public Videojuego(int id, String nombre, String genero, double precio,
                       int unidadesDisponibles, int nivelReposicion, boolean suspendido) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
        this.unidadesDisponibles = unidadesDisponibles;
        this.nivelReposicion = nivelReposicion;
        this.suspendido = suspendido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getUnidadesDisponibles() {
        return unidadesDisponibles;
    }

    public void setUnidadesDisponibles(int unidadesDisponibles) {
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public int getNivelReposicion() {
        return nivelReposicion;
    }

    public void setNivelReposicion(int nivelReposicion) {
        this.nivelReposicion = nivelReposicion;
    }

    public boolean isSuspendido() {
        return suspendido;
    }

    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }

    // Regla de negocio: necesita reposición si las unidades disponibles
    // son menores que el nivel de reposición configurado.
    public boolean necesitaReposicion() {
        return unidadesDisponibles < nivelReposicion;
    }

    @Override
    public String toString() {
        return "--------------------------------\n" +
                "ID: " + id + "\n" +
                "Nombre: " + nombre + "\n" +
                "Género: " + genero + "\n" +
                "Precio: $" + precio + "\n" +
                "Unidades disponibles: " + unidadesDisponibles + "\n" +
                "Nivel de reposición: " + nivelReposicion + "\n" +
                "Suspendido: " + (suspendido ? "Sí" : "No");
    }
}
