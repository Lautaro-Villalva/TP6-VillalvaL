package resol.villalval.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa una venta "enriquecida" con datos que no viven en la tabla
 * ventas pero que se calculan a partir de la regla de negocio de descuentos:
 * el nombre del videojuego, el % de descuento aplicado y el importe total.
 * Se usa para listar ventas y para el reporte del mes actual.
 */
public class VentaDetalle {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int id;
    private LocalDate fecha;
    private String nombreVideojuego;
    private int cantidad;
    private double descuentoPorcentaje; // ej: 0.10 = 10%
    private double total;

    public VentaDetalle(int id, LocalDate fecha, String nombreVideojuego, int cantidad,
                         double descuentoPorcentaje, double total) {
        this.id = id;
        this.fecha = fecha;
        this.nombreVideojuego = nombreVideojuego;
        this.cantidad = cantidad;
        this.descuentoPorcentaje = descuentoPorcentaje;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getNombreVideojuego() {
        return nombreVideojuego;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "--------------------------------\n" +
                "ID Venta: " + id + "\n" +
                "Fecha: " + fecha.format(FORMATO_FECHA) + "\n" +
                "Videojuego: " + nombreVideojuego + "\n" +
                "Cantidad: " + cantidad + "\n" +
                "Descuento: " + (int) (descuentoPorcentaje * 100) + "%\n" +
                "Total: $" + String.format("%.2f", total);
    }
}
