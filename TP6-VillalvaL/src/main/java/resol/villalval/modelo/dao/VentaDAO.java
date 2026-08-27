package resol.villalval.modelo.dao;

import resol.villalval.conexion.ConexionBD;
import resol.villalval.excepcion.ReglaNegocioException;
import resol.villalval.excepcion.VentaInvalidaException;
import resol.villalval.excepcion.VideojuegoNoEncontradoException;
import resol.villalval.modelo.Venta;
import resol.villalval.modelo.VentaDetalle;
import resol.villalval.modelo.Videojuego;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Capa Modelo: ejecuta las operaciones contra la base de datos y aplica
 * la lógica de negocio de Venta (descuentos por volumen, verificación y
 * descuento de stock, reportes).
 */
public class VentaDAO {

    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    // Crea la tabla 'ventas' si no existe
    public static void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS ventas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                fecha DATE NOT NULL,
                cantidad INT NOT NULL,
                videojuego_id INT NOT NULL,
                FOREIGN KEY (videojuego_id)
                REFERENCES videojuegos(id)
                )
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'ventas' creada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla ventas: " + e.getMessage());
        }
    }

    // Regla de negocio: descuento por volumen según la cantidad de la operación
    public static double calcularDescuento(int cantidad) {
        if (cantidad >= 10) {
            return 0.15;
        } else if (cantidad >= 5) {
            return 0.10;
        } else if (cantidad >= 2) {
            return 0.05;
        }
        return 0.0;
    }

    // Registrar venta: valida, calcula descuento/total, descuenta stock
    public void registrarVenta(LocalDate fecha, int cantidad, int videojuegoId)
            throws VideojuegoNoEncontradoException, VentaInvalidaException, ReglaNegocioException, SQLException {

        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new VentaInvalidaException("La fecha de venta no puede ser futura.");
        }
        if (cantidad <= 0) {
            throw new VentaInvalidaException("La cantidad vendida debe ser mayor que cero.");
        }

        // Verifica que el videojuego exista y esté disponible
        Videojuego videojuego = videojuegoDAO.buscarPorId(videojuegoId);
        if (videojuego.isSuspendido()) {
            throw new ReglaNegocioException("El videojuego '" + videojuego.getNombre() + "' no está disponible para la venta.");
        }

        // Verifica y descuenta stock (lanza ReglaNegocioException si no alcanza)
        videojuegoDAO.descontarStock(videojuegoId, cantidad);

        String sql = "INSERT INTO ventas (fecha, cantidad, videojuego_id) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));
            ps.setInt(2, cantidad);
            ps.setInt(3, videojuegoId);
            ps.executeUpdate();
        }
    }

    // Listado básico de ventas (sin detalle de videojuego/descuento)
    public List<Venta> listarVentas() throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY id";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Venta(
                        rs.getInt("id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getInt("cantidad"),
                        rs.getInt("videojuego_id")
                ));
            }
        }
        return lista;
    }

    private VentaDetalle mapearDetalle(ResultSet rs) throws SQLException {
        int cantidad = rs.getInt("cantidad");
        double precio = rs.getDouble("precio");
        double descuento = calcularDescuento(cantidad);
        double total = precio * cantidad * (1 - descuento);

        return new VentaDetalle(
                rs.getInt("id"),
                rs.getDate("fecha").toLocalDate(),
                rs.getString("nombre"),
                cantidad,
                descuento,
                total
        );
    }

    // 1. Listar ventas con id, fecha, videojuego, cantidad, descuento % y total
    public List<VentaDetalle> listarVentasDetalle() throws SQLException {
        List<VentaDetalle> lista = new ArrayList<>();
        String sql = "SELECT v.id, v.fecha, v.cantidad, vj.nombre, vj.precio " +
                "FROM ventas v JOIN videojuegos vj ON v.videojuego_id = vj.id " +
                "ORDER BY v.id";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearDetalle(rs));
            }
        }
        return lista;
    }

    // Buscar venta por ID (con detalle). Devuelve null si no existe.
    public VentaDetalle buscarVentaPorId(int id) throws SQLException {
        String sql = "SELECT v.id, v.fecha, v.cantidad, vj.nombre, vj.precio " +
                "FROM ventas v JOIN videojuegos vj ON v.videojuego_id = vj.id " +
                "WHERE v.id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDetalle(rs);
                }
                return null;
            }
        }
    }

    // Buscar las ventas de un videojuego específico
    public List<VentaDetalle> buscarVentasPorVideojuego(int videojuegoId)
            throws VideojuegoNoEncontradoException, SQLException {

        if (!videojuegoDAO.existeVideojuego(videojuegoId)) {
            throw new VideojuegoNoEncontradoException("No existe un videojuego con el ID indicado.");
        }

        List<VentaDetalle> lista = new ArrayList<>();
        String sql = "SELECT v.id, v.fecha, v.cantidad, vj.nombre, vj.precio " +
                "FROM ventas v JOIN videojuegos vj ON v.videojuego_id = vj.id " +
                "WHERE v.videojuego_id = ? ORDER BY v.id";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, videojuegoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDetalle(rs));
                }
            }
        }
        return lista;
    }

    // 3. Reporte de ventas realizadas en el mes actual
    public List<VentaDetalle> reporteMesActual() throws SQLException {
        List<VentaDetalle> lista = new ArrayList<>();
        String sql = "SELECT v.id, v.fecha, v.cantidad, vj.nombre, vj.precio " +
                "FROM ventas v JOIN videojuegos vj ON v.videojuego_id = vj.id " +
                "WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE) AND YEAR(v.fecha) = YEAR(CURRENT_DATE) " +
                "ORDER BY v.fecha";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearDetalle(rs));
            }
        }
        return lista;
    }
}
