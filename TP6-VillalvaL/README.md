# TP6-VillalvaL

Trabajo Práctico 6 — MVC aplicado al sistema de Videojuegos (JDBC).

Extiende el TP de JDBC anterior aplicando arquitectura **MVC** y agregando reglas de negocio de
stock (reposición, suspensión) y de ventas (descuento por volumen, control de stock, reportes).

## Estructura (paquete `resol.villalval`)

```
resol.villalval
├── Main.java                    # clase principal (menú general)
├── conexion/ConexionBD.java
├── modelo/
│   ├── Videojuego.java
│   ├── Venta.java
│   ├── VentaDetalle.java        # DTO usado en listados/reportes de ventas
│   └── dao/
│       ├── VideojuegoDAO.java   # acceso a datos + reglas de negocio de Videojuego
│       └── VentaDAO.java        # acceso a datos + reglas de negocio de Venta
├── vista/
│   ├── Vista.java               # mostrarMenuPrincipal / Videojuegos / Ventas
│   ├── VideojuegoVista.java     # solo entrada/salida por consola
│   └── VentaVista.java          # solo entrada/salida por consola
├── controlador/
│   ├── VideojuegoControlador.java
│   └── VentaControlador.java
└── excepcion/
    ├── VideojuegoNoEncontradoException.java
    ├── VentaInvalidaException.java
    └── ReglaNegocioException.java   # nueva: p.ej. stock insuficiente
```

## Cómo correrlo

```bash
mvn compile
mvn exec:java
```

## Nota sobre el campo `suspendido`

El enunciado tiene una ambigüedad: dice que `suspendido` es "1 = disponible, 0 = no disponible",
pero también dice que "los videojuegos suspendidos no se muestran en el listado de disponibles".
Se implementó con el criterio más consistente con el nombre del campo y con esa segunda regla:

- `suspendido = true (1)` → el videojuego **no** está disponible para la venta (no aparece en el
  listado de disponibles).
- `suspendido = false (0)` → el videojuego está disponible.

Si tu cátedra espera el criterio inverso, alcanza con invertir la condición en
`VideojuegoDAO.listarDisponibles()` y en `Videojuego.isSuspendido()`.

## Subir a Git

```bash
git init
git add .
git commit -m "TP6 - MVC videojuegos"
git branch -M main
git remote add origin <URL_DE_TU_REPO_TP6-VillalvaL>
git push -u origin main
```

Recordá que el repositorio debe llamarse igual que el proyecto: `TP6-VillalvaL`.
