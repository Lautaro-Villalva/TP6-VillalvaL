package resol.villalval.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import resol.villalval.conexion.ConexionBD;
import resol.villalval.excepcion.ReglaNegocioException;
import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;


public class Videojuego {

    private int id;
    private String nombre;
    private String genero;
    private double precio;
    private int unidadesDisponibles;
    private int nivelReposicion;

    private boolean suspendido;

    public Videojuego() {
    }

    public Videojuego(String nombre, String genero, double precio, int unidadesDisponibles, int nivelReposicion) {
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
        this.unidadesDisponibles = unidadesDisponibles;
        this.nivelReposicion = nivelReposicion;
        this.suspendido = false;
    }

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


    public static void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS videojuegos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                genero VARCHAR(50) NOT NULL,
                precio DECIMAL(10,2) NOT NULL,
                unidades_disponibles INT NOT NULL,
                nivel_reposicion INT NOT NULL,
                suspendido INT NOT NULL DEFAULT 0
                )
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'videojuegos' creada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla videojuegos: " + e.getMessage());
        }
    }


    private static void validar(String nombre, String genero, double precio, int unidadesDisponibles)
            throws VentaInvalidaException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new VentaInvalidaException("El nombre del videojuego es obligatorio.");
        }
        if (genero == null || genero.trim().isEmpty()) {
            throw new VentaInvalidaException("El género del videojuego es obligatorio.");
        }
        if (precio <= 0) {
            throw new VentaInvalidaException("El precio del videojuego debe ser mayor que cero.");
        }
        if (unidadesDisponibles < 0) {
            throw new VentaInvalidaException("Las unidades disponibles no pueden ser negativas.");
        }
    }

    private static Videojuego mapear(ResultSet rs) throws SQLException {
        return new Videojuego(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("genero"),
                rs.getDouble("precio"),
                rs.getInt("unidades_disponibles"),
                rs.getInt("nivel_reposicion"),
                rs.getInt("suspendido") != 0
        );
    }


    public static List<Videojuego> listar() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos ORDER BY id";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }


    public static List<Videojuego> listarDisponibles() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE suspendido = 0 ORDER BY id";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }


    public static List<Videojuego> listarQueNecesitanReposicion() throws SQLException {
        List<Videojuego> lista = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE unidades_disponibles < nivel_reposicion ORDER BY id";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }


    public static Videojuego buscarPorId(int id) throws VideojuegoNoEncontradoException, SQLException {
        String sql = "SELECT * FROM videojuegos WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                } else {
                    throw new VideojuegoNoEncontradoException("No existe un videojuego con el ID indicado.");
                }
            }
        }
    }

    public static boolean existe(int id) throws SQLException {
        String sql = "SELECT 1 FROM videojuegos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }


    public void insertar() throws VentaInvalidaException, SQLException {
        validar(nombre, genero, precio, unidadesDisponibles);

        String sql = "INSERT INTO videojuegos (nombre, genero, precio, unidades_disponibles, nivel_reposicion, suspendido) " +
                "VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, genero);
            ps.setDouble(3, precio);
            ps.setInt(4, unidadesDisponibles);
            ps.setInt(5, nivelReposicion);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
        this.suspendido = false;
    }

    public void actualizar() throws VideojuegoNoEncontradoException, VentaInvalidaException, SQLException {
        if (!existe(id)) {
            throw new VideojuegoNoEncontradoException("No existe un videojuego con el ID indicado.");
        }
        validar(nombre, genero, precio, unidadesDisponibles);

        String sql = "UPDATE videojuegos SET nombre = ?, genero = ?, precio = ?, " +
                "unidades_disponibles = ?, nivel_reposicion = ?, suspendido = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, genero);
            ps.setDouble(3, precio);
            ps.setInt(4, unidadesDisponibles);
            ps.setInt(5, nivelReposicion);
            ps.setInt(6, suspendido ? 1 : 0);
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public static void eliminar(int id) throws VideojuegoNoEncontradoException, SQLException {
        if (!existe(id)) {
            throw new VideojuegoNoEncontradoException("No existe un videojuego con el ID indicado.");
        }

        String sql = "DELETE FROM videojuegos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }


    public void descontarStock(int cantidad) throws ReglaNegocioException, SQLException {
        if (unidadesDisponibles < cantidad) {
            throw new ReglaNegocioException("Stock insuficiente para realizar la venta.");
        }

        String sql = "UPDATE videojuegos SET unidades_disponibles = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, unidadesDisponibles - cantidad);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
        this.unidadesDisponibles -= cantidad;
    }
}
