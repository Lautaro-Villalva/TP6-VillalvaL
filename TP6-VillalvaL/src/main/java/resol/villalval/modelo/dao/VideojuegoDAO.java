package resol.villalval.modelo.dao;

import resol.villalval.conexion.ConexionBD;
import resol.villalval.excepcion.ReglaNegocioException;
import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;
import resol.villalval.modelo.Videojuego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa Modelo: ejecuta las operaciones contra la base de datos y aplica
 * la lógica de negocio de Videojuego (validaciones, reposición, stock).
 * Las excepciones de negocio y SQLException se propagan para que las
 * maneje el Controlador.
 */
public class VideojuegoDAO {

    // Crea la tabla 'videojuegos' si no existe (incluye los nuevos campos de stock)
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

    // Valida los datos básicos de un videojuego antes de insertarlo o actualizarlo
    private void validarVideojuego(String nombre, String genero, double precio, int unidadesDisponibles)
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

    private Videojuego mapear(ResultSet rs) throws SQLException {
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

    // Crear videojuego (valida precio > 0 y unidadesDisponibles >= 0)
    public void crearVideojuego(Videojuego v) throws VentaInvalidaException, SQLException {
        validarVideojuego(v.getNombre(), v.getGenero(), v.getPrecio(), v.getUnidadesDisponibles());

        String sql = "INSERT INTO videojuegos (nombre, genero, precio, unidades_disponibles, nivel_reposicion, suspendido) " +
                "VALUES (?, ?, ?, ?, ?, 0)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getNombre());
            ps.setString(2, v.getGenero());
            ps.setDouble(3, v.getPrecio());
            ps.setInt(4, v.getUnidadesDisponibles());
            ps.setInt(5, v.getNivelReposicion());
            ps.executeUpdate();
        }
    }

    // Listar todos los videojuegos
    public List<Videojuego> listarVideojuegos() throws SQLException {
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

    // 1. Listar videojuegos disponibles (no suspendidos)
    public List<Videojuego> listarDisponibles() throws SQLException {
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

    // 2. Videojuegos que necesitan reposición (unidadesDisponibles < nivelReposicion)
    public List<Videojuego> listarQueNecesitanReposicion() throws SQLException {
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

    // Buscar videojuego por ID
    public Videojuego buscarPorId(int id) throws VideojuegoNoEncontradoException, SQLException {
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

    // Verifica si existe un videojuego con ese ID
    public boolean existeVideojuego(int id) throws SQLException {
        String sql = "SELECT 1 FROM videojuegos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Actualizar videojuego (incluye los campos de stock y suspendido)
    public void actualizarVideojuego(Videojuego v) throws VideojuegoNoEncontradoException, VentaInvalidaException, SQLException {
        if (!existeVideojuego(v.getId())) {
            throw new VideojuegoNoEncontradoException("No existe un videojuego con el ID indicado.");
        }
        validarVideojuego(v.getNombre(), v.getGenero(), v.getPrecio(), v.getUnidadesDisponibles());

        String sql = "UPDATE videojuegos SET nombre = ?, genero = ?, precio = ?, " +
                "unidades_disponibles = ?, nivel_reposicion = ?, suspendido = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getNombre());
            ps.setString(2, v.getGenero());
            ps.setDouble(3, v.getPrecio());
            ps.setInt(4, v.getUnidadesDisponibles());
            ps.setInt(5, v.getNivelReposicion());
            ps.setInt(6, v.isSuspendido() ? 1 : 0);
            ps.setInt(7, v.getId());
            ps.executeUpdate();
        }
    }

    // Eliminar videojuego
    public void eliminarVideojuego(int id) throws VideojuegoNoEncontradoException, SQLException {
        if (!existeVideojuego(id)) {
            throw new VideojuegoNoEncontradoException("No existe un videojuego con el ID indicado.");
        }

        String sql = "DELETE FROM videojuegos WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Descuenta stock al confirmar una venta. Usado internamente por VentaDAO.
    // Verifica que haya stock suficiente antes de descontar.
    public void descontarStock(int videojuegoId, int cantidad)
            throws VideojuegoNoEncontradoException, ReglaNegocioException, SQLException {

        Videojuego v = buscarPorId(videojuegoId);

        if (v.getUnidadesDisponibles() < cantidad) {
            throw new ReglaNegocioException("Stock insuficiente para realizar la venta.");
        }

        String sql = "UPDATE videojuegos SET unidades_disponibles = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, v.getUnidadesDisponibles() - cantidad);
            ps.setInt(2, videojuegoId);
            ps.executeUpdate();
        }
    }
}
