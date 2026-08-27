package resol.villalval.modelo;

import java.time.LocalDate;

public class Venta {

    private int id;
    private LocalDate fecha;
    private int cantidad;
    private int videojuegoId;

    public Venta() {
    }

    // Constructor sin id (para registrar una venta nueva)
    public Venta(LocalDate fecha, int cantidad, int videojuegoId) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.videojuegoId = videojuegoId;
    }

    // Constructor completo (para ventas ya existentes en la BD)
    public Venta(int id, LocalDate fecha, int cantidad, int videojuegoId) {
        this.id = id;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.videojuegoId = videojuegoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getVideojuegoId() {
        return videojuegoId;
    }

    public void setVideojuegoId(int videojuegoId) {
        this.videojuegoId = videojuegoId;
    }

    @Override
    public String toString() {
        return "--------------------------------\n" +
                "ID Venta: " + id + "\n" +
                "Fecha: " + fecha + "\n" +
                "Cantidad: " + cantidad + "\n" +
                "Videojuego ID: " + videojuegoId;
    }
}
