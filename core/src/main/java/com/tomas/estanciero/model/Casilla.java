package com.tomas.estanciero.model;

public class Casilla {public enum TipoCasilla {
    SALIDA,
    PROPIEDAD,
    SUERTE,
    DESTINO,
    IMPUESTO,
    PREMIO,
    CARCEL,
    DESCANSO,
    IR_A_CARCEL
}

    private int posicion; // 0-39
    private String nombre;
    private TipoCasilla tipo;
    private Propiedad propiedad; // null si no es una casilla de propiedad
    private int valor; // Para impuestos o premios

    public Casilla(int posicion, String nombre, TipoCasilla tipo) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.propiedad = null;
        this.valor = 0;
    }

    // Constructor para casillas con propiedad
    public Casilla(int posicion, Propiedad propiedad) {
        this.posicion = posicion;
        this.nombre = propiedad.getNombre();
        this.tipo = TipoCasilla.PROPIEDAD;
        this.propiedad = propiedad;
        this.valor = 0;
    }

    // Constructor para impuestos/premios
    public Casilla(int posicion, String nombre, TipoCasilla tipo, int valor) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.propiedad = null;
        this.valor = valor;
    }

    // Getters
    public int getPosicion() {
        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCasilla getTipo() {
        return tipo;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public int getValor() {
        return valor;
    }

    /**
     * Ejecuta la acción de la casilla cuando un jugador cae en ella.
     */
    public void ejecutarAccion(Jugador jugador, Tablero tablero) {
        switch (tipo) {
            case SALIDA:
                // Solo se cobra al pasar, no al caer
                break;

            case PROPIEDAD:
                if (propiedad != null) {
                    if (!propiedad.tieneDueno()) {
                        // La propiedad está disponible para comprar
                        System.out.println(jugador.getNombre() + " puede comprar " + nombre);
                    } else if (propiedad.getDueno() != jugador) {
                        // Debe pagar alquiler
                        int alquiler = propiedad.calcularAlquiler();
                        if (jugador.restarDinero(alquiler)) {
                            propiedad.getDueno().agregarDinero(alquiler);
                            System.out.println(jugador.getNombre() + " paga $" + alquiler + " a " + propiedad.getDueno().getNombre());
                        } else {
                            System.out.println(jugador.getNombre() + " no tiene dinero para pagar!");
                        }
                    }
                }
                break;

            case IMPUESTO:
                jugador.restarDinero(valor);
                System.out.println(jugador.getNombre() + " paga impuesto de $" + valor);
                break;

            case PREMIO:
                jugador.agregarDinero(valor);
                System.out.println(jugador.getNombre() + " recibe premio de $" + valor);
                break;

            case IR_A_CARCEL:
                jugador.setEnCarcel(true);
                jugador.setPosicion(10); // Posición de la cárcel
                System.out.println(jugador.getNombre() + " va a la cárcel!");
                break;

            case CARCEL:
                // Si solo está de visita, no pasa nada
                if (!jugador.isEnCarcel()) {
                    System.out.println(jugador.getNombre() + " está de visita en la cárcel");
                }
                break;

            case DESCANSO:
                System.out.println(jugador.getNombre() + " puede descansar");
                break;

            case SUERTE:
            case DESTINO:
                // Se implementará con tarjetas
                System.out.println(jugador.getNombre() + " saca una carta de " + tipo);
                break;
        }
    }

    @Override
    public String toString() {
        return posicion + ": " + nombre + " (" + tipo + ")";
    }
}
