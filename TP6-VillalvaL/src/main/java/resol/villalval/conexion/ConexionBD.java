package resol.villalval.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // La base de datos se guarda como archivo local "videojuegos.mv.db"
    private static final String URL = "jdbc:h2:./videojuegos";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static void verificarConexion() {
        try (Connection conn = obtenerConexion()) {
            System.out.println("Conexión exitosa a la base de datos 'videojuegos'.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
