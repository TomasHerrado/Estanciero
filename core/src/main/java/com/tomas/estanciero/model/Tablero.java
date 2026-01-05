package com.tomas.estanciero.model;
import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private List<Casilla> casillas;
    private static final int TOTAL_CASILLAS = 40;

    public Tablero() {
        this.casillas = new ArrayList<>();
        inicializarTablero();
    }

    private void inicializarTablero() {
        casillas.add(new Casilla(0, "SALIDA", Casilla.TipoCasilla.SALIDA));

        casillas.add(crearCasillaPropiedad(1, "FORMOSA", Propiedad.TipoPropiedad.PROVINCIA, 100, 10, "Norte"));
        casillas.add(new Casilla(2, "SUERTE", Casilla.TipoCasilla.SUERTE));
        casillas.add(crearCasillaPropiedad(3, "CÓRDOBA", Propiedad.TipoPropiedad.PROVINCIA, 120, 12, "Centro"));
        casillas.add(crearCasillaPropiedad(4, "FERROCARRIL N.", Propiedad.TipoPropiedad.FERROCARRIL, 200, 25, "Ferrocarril"));
        casillas.add(crearCasillaPropiedad(5, "RÍO NEGRO", Propiedad.TipoPropiedad.PROVINCIA, 140, 14, "Sur"));
        casillas.add(new Casilla(6, "IMPUESTO", Casilla.TipoCasilla.IMPUESTO, 100));
        casillas.add(crearCasillaPropiedad(7, "MISIONES", Propiedad.TipoPropiedad.PROVINCIA, 160, 16, "Norte"));
        casillas.add(new Casilla(8, "DESTINO", Casilla.TipoCasilla.DESTINO));
        casillas.add(crearCasillaPropiedad(9, "CHACO", Propiedad.TipoPropiedad.PROVINCIA, 180, 18, "Norte"));

        casillas.add(new Casilla(10, "CÁRCEL", Casilla.TipoCasilla.CARCEL));

        casillas.add(crearCasillaPropiedad(11, "SAN LUIS", Propiedad.TipoPropiedad.PROVINCIA, 200, 20, "Centro"));
        casillas.add(crearCasillaPropiedad(12, "COMPAÑÍA", Propiedad.TipoPropiedad.COMPANIA, 150, 20, "Compañía"));
        casillas.add(crearCasillaPropiedad(13, "CORRIENTES", Propiedad.TipoPropiedad.PROVINCIA, 220, 22, "Norte"));
        casillas.add(crearCasillaPropiedad(14, "FERROCARRIL S.", Propiedad.TipoPropiedad.FERROCARRIL, 200, 25, "Ferrocarril"));
        casillas.add(crearCasillaPropiedad(15, "CHUBUT", Propiedad.TipoPropiedad.PROVINCIA, 240, 24, "Sur"));
        casillas.add(new Casilla(16, "SUERTE", Casilla.TipoCasilla.SUERTE));
        casillas.add(crearCasillaPropiedad(17, "SALTA", Propiedad.TipoPropiedad.PROVINCIA, 260, 26, "Norte"));
        casillas.add(crearCasillaPropiedad(18, "NEUQUÉN", Propiedad.TipoPropiedad.PROVINCIA, 280, 28, "Sur"));
        casillas.add(new Casilla(19, "DESTINO", Casilla.TipoCasilla.DESTINO));

        casillas.add(new Casilla(20, "DESCANSO", Casilla.TipoCasilla.DESCANSO));

        casillas.add(crearCasillaPropiedad(21, "MENDOZA", Propiedad.TipoPropiedad.PROVINCIA, 300, 30, "Centro"));
        casillas.add(new Casilla(22, "SUERTE", Casilla.TipoCasilla.SUERTE));
        casillas.add(crearCasillaPropiedad(23, "LA RIOJA", Propiedad.TipoPropiedad.PROVINCIA, 320, 32, "Centro"));
        casillas.add(crearCasillaPropiedad(24, "FERROCARRIL O.", Propiedad.TipoPropiedad.FERROCARRIL, 200, 25, "Ferrocarril"));
        casillas.add(crearCasillaPropiedad(25, "TUCUMÁN", Propiedad.TipoPropiedad.PROVINCIA, 340, 34, "Norte"));
        casillas.add(new Casilla(26, "IMPUESTO", Casilla.TipoCasilla.IMPUESTO, 150));
        casillas.add(crearCasillaPropiedad(27, "JUJUY", Propiedad.TipoPropiedad.PROVINCIA, 360, 36, "Norte"));
        casillas.add(new Casilla(28, "DESTINO", Casilla.TipoCasilla.DESTINO));
        casillas.add(crearCasillaPropiedad(29, "SANTA FE", Propiedad.TipoPropiedad.PROVINCIA, 380, 38, "Centro"));

        casillas.add(new Casilla(30, "IR A LA CÁRCEL", Casilla.TipoCasilla.IR_A_CARCEL));

        casillas.add(crearCasillaPropiedad(31, "CATAMARCA", Propiedad.TipoPropiedad.PROVINCIA, 400, 40, "Norte"));
        casillas.add(crearCasillaPropiedad(32, "COMPAÑÍA", Propiedad.TipoPropiedad.COMPANIA, 150, 20, "Compañía"));
        casillas.add(crearCasillaPropiedad(33, "ENTRE RÍOS", Propiedad.TipoPropiedad.PROVINCIA, 420, 42, "Centro"));
        casillas.add(crearCasillaPropiedad(34, "FERROCARRIL E.", Propiedad.TipoPropiedad.FERROCARRIL, 200, 25, "Ferrocarril"));
        casillas.add(new Casilla(35, "SUERTE", Casilla.TipoCasilla.SUERTE));
        casillas.add(crearCasillaPropiedad(36, "SANTA CRUZ", Propiedad.TipoPropiedad.PROVINCIA, 440, 44, "Sur"));
        casillas.add(new Casilla(37, "PREMIO", Casilla.TipoCasilla.PREMIO, 200));
        casillas.add(crearCasillaPropiedad(38, "BUENOS AIRES", Propiedad.TipoPropiedad.PROVINCIA, 500, 50, "Centro"));
        casillas.add(crearCasillaPropiedad(39, "TIERRA DEL FUEGO", Propiedad.TipoPropiedad.PROVINCIA, 600, 60, "Sur"));
    }

    private Casilla crearCasillaPropiedad(int posicion, String nombre, Propiedad.TipoPropiedad tipo, int precio, int alquiler, String zona) {
        Propiedad propiedad = new Propiedad(nombre, tipo, precio, alquiler, zona);
        return new Casilla(posicion, propiedad);
    }

    public Casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < TOTAL_CASILLAS) {
            return casillas.get(posicion);
        }
        return null;
    }

    public List<Casilla> getCasillas() {
        return casillas;
    }

    public int getTotalCasillas() {
        return TOTAL_CASILLAS;
    }

    public List<Propiedad> getPropiedades() {
        List<Propiedad> propiedades = new ArrayList<>();
        for (Casilla casilla : casillas) {
            if (casilla.getTipo() == Casilla.TipoCasilla.PROPIEDAD && casilla.getPropiedad() != null) {
                propiedades.add(casilla.getPropiedad());
            }
        }
        return propiedades;
    }
    public List<Propiedad> getPropiedadesDisponibles() {
        List<Propiedad> disponibles = new ArrayList<>();
        for (Propiedad propiedad : getPropiedades()) {
            if (!propiedad.tieneDueno()) {
                disponibles.add(propiedad);
            }
        }
        return disponibles;
    }
}
