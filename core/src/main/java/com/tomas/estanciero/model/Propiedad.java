package com.tomas.estanciero.model;

public class Propiedad {
    public enum TipoPropiedad {
        PROVINCIA,
        FERROCARRIL,
        COMPANIA
    }

    private String nombre;
    private TipoPropiedad tipo;
    private int precio;
    private int alquilerBase;
    private Jugador dueno;
    private int mejoras; // Cantidad de mejoras (campos, chacras, estancias)
    private String zona; // Para agrupar provincias (Norte, Sur, etc.)

    public Propiedad(String nombre, TipoPropiedad tipo, int precio, int alquilerBase, String zona) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.alquilerBase = alquilerBase;
        this.zona = zona;
        this.dueno = null;
        this.mejoras = 0;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public TipoPropiedad getTipo() {
        return tipo;
    }

    public int getPrecio() {
        return precio;
    }

    public int getAlquilerBase() {
        return alquilerBase;
    }

    public Jugador getDueno() {
        return dueno;
    }

    public void setDueno(Jugador dueno) {
        this.dueno = dueno;
    }

    public boolean tieneDueno() {
        return dueno != null;
    }

    public int getMejoras() {
        return mejoras;
    }

    public void agregarMejora() {
        this.mejoras++;
    }

    public void removerMejora() {
        if (mejoras > 0) {
            this.mejoras--;
        }
    }

    public String getZona() {
        return zona;
    }

    /**
     * Calcula el alquiler según las mejoras.
     */
    public int calcularAlquiler() {
        if (tipo == TipoPropiedad.FERROCARRIL || tipo == TipoPropiedad.COMPANIA) {
            // Los ferrocarriles y compañías tienen lógica especial
            return alquilerBase;
        }

        // Para provincias, el alquiler aumenta con mejoras
        return alquilerBase * (1 + mejoras);
    }

    /**
     * Calcula el precio de venta al banco.
     */
    public int calcularPrecioVenta() {
        return precio / 2; // Ejemplo: vende al 50% del precio
    }

    @Override
    public String toString() {
        String info = nombre + " (" + tipo + ")";
        if (dueno != null) {
            info += " - Dueño: " + dueno.getNombre();
        }
        if (mejoras > 0) {
            info += " - Mejoras: " + mejoras;
        }
        return info;
    }
}
