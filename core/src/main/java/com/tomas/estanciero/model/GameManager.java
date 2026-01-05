package com.tomas.estanciero.model;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Tablero tablero;
    private List<Jugador> jugadores;
    private int jugadorActualIndex;
    private Dado dado;
    private boolean juegoTerminado;
    private Jugador ganador;
    private int dineroObjetivo;

    public GameManager(int numeroJugadores, int dineroInicial, int dineroObjetivo) {
        this.tablero = new Tablero();
        this.jugadores = new ArrayList<>();
        this.dado = new Dado();
        this.jugadorActualIndex = 0;
        this.juegoTerminado = false;
        this.ganador = null;
        this.dineroObjetivo = dineroObjetivo;

        for (int i = 0; i < numeroJugadores; i++) {
            jugadores.add(new Jugador("Jugador " + (i + 1), dineroInicial));
        }
    }

    public Tablero getTablero() {
        return tablero;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(jugadorActualIndex);
    }

    public Dado getDado() {
        return dado;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void ejecutarTurno() {
        if (juegoTerminado) return;

        Jugador jugador = getJugadorActual();
        System.out.println("\n=== Turno de " + jugador.getNombre() + " ===");
        System.out.println("Dinero: $" + jugador.getDinero() + " | Posición: " + jugador.getPosicion());

        if (jugador.isEnCarcel()) {
            manejarTurnoEnCarcel(jugador);
            pasarAlSiguienteJugador();
            return;
        }

        if (jugador.isEnDescanso()) {
            manejarTurnoEnDescanso(jugador);
            pasarAlSiguienteJugador();
            return;
        }

        tirarDados(jugador);

        if (!dado.esDoble()) {
            pasarAlSiguienteJugador();
        } else {
            System.out.println("¡Sacó DOBLE! Puede tirar de nuevo.");
        }
    }

    private void tirarDados(Jugador jugador) {
        int suma = dado.tirar();
        System.out.println(dado);

        if (dado.esDoble()) {
            jugador.incrementarDoblesConsecutivos();

            if (jugador.getDoblesConsecutivos() >= 3) {
                System.out.println("¡Tres dobles seguidos! " + jugador.getNombre() + " va a la cárcel!");
                jugador.setEnCarcel(true);
                jugador.setPosicion(10);
                jugador.resetearDoblesConsecutivos();
                pasarAlSiguienteJugador();
                return;
            }
        } else {
            jugador.resetearDoblesConsecutivos();
        }

        jugador.mover(suma);
        System.out.println(jugador.getNombre() + " se mueve a la posición " + jugador.getPosicion());

        Casilla casilla = tablero.getCasilla(jugador.getPosicion());
        if (casilla != null) {
            System.out.println("Cayó en: " + casilla.getNombre());
            casilla.ejecutarAccion(jugador, tablero);
        }

        verificarFinDeJuego();
    }

    private void manejarTurnoEnCarcel(Jugador jugador) {
        System.out.println(jugador.getNombre() + " está en la cárcel (turno " + (jugador.getTurnosEnCarcel() + 1) + ")");

        int suma = dado.tirar();
        System.out.println(dado);

        if (dado.esDoble()) {
            System.out.println("¡Doble! " + jugador.getNombre() + " sale de la cárcel!");
            jugador.setEnCarcel(false);
            jugador.mover(suma);

            Casilla casilla = tablero.getCasilla(jugador.getPosicion());
            if (casilla != null) {
                casilla.ejecutarAccion(jugador, tablero);
            }
        } else {
            jugador.incrementarTurnosEnCarcel();

            if (jugador.getTurnosEnCarcel() >= 3) {
                System.out.println(jugador.getNombre() + " sale de la cárcel automáticamente");
                jugador.setEnCarcel(false);
            }
        }

        pasarAlSiguienteJugador();
    }

    private void manejarTurnoEnDescanso(Jugador jugador) {
        System.out.println(jugador.getNombre() + " está descansando");

        int suma = dado.tirar();
        System.out.println(dado);

        if (dado.esDoble()) {
            System.out.println("¡Doble! " + jugador.getNombre() + " pierde el descanso");
            jugador.setEnDescanso(false);
        } else {
            jugador.incrementarTurnosDescanso();

            if (jugador.getTurnosDescanso() >= 2) {
                System.out.println(jugador.getNombre() + " termina su descanso");
                jugador.setEnDescanso(false);
            }
        }

        pasarAlSiguienteJugador();
    }

    public boolean comprarPropiedad() {
        Jugador jugador = getJugadorActual();
        Casilla casilla = tablero.getCasilla(jugador.getPosicion());

        if (casilla == null || casilla.getTipo() != Casilla.TipoCasilla.PROPIEDAD) {
            System.out.println("No hay propiedad disponible aquí");
            return false;
        }

        Propiedad propiedad = casilla.getPropiedad();

        if (propiedad.tieneDueno()) {
            System.out.println("Esta propiedad ya tiene dueño");
            return false;
        }

        if (jugador.getDinero() < propiedad.getPrecio()) {
            System.out.println("No tienes suficiente dinero");
            return false;
        }

        jugador.restarDinero(propiedad.getPrecio());
        jugador.agregarPropiedad(propiedad);
        System.out.println(jugador.getNombre() + " compró " + propiedad.getNombre() + " por $" + propiedad.getPrecio());

        return true;
    }

    public boolean venderPropiedad(Propiedad propiedad) {
        Jugador jugador = getJugadorActual();

        if (!jugador.getPropiedades().contains(propiedad)) {
            System.out.println("No eres dueño de esta propiedad");
            return false;
        }

        int precioVenta = propiedad.calcularPrecioVenta();
        jugador.agregarDinero(precioVenta);
        jugador.removerPropiedad(propiedad);
        System.out.println(jugador.getNombre() + " vendió " + propiedad.getNombre() + " por $" + precioVenta);

        return true;
    }

    private void verificarFinDeJuego() {
        for (Jugador jugador : jugadores) {
            if (jugador.getDinero() >= dineroObjetivo) {
                juegoTerminado = true;
                ganador = jugador;
                System.out.println("\n¡¡¡" + jugador.getNombre() + " GANÓ EL JUEGO!!!");
                return;
            }
        }

        List<Jugador> jugadoresActivos = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            if (!jugador.enBancarrota()) {
                jugadoresActivos.add(jugador);
            }
        }

        if (jugadoresActivos.size() == 1) {
            juegoTerminado = true;
            ganador = jugadoresActivos.get(0);
            System.out.println("\n¡¡¡" + ganador.getNombre() + " GANÓ POR ELIMINACIÓN!!!");
        }
    }

    private void pasarAlSiguienteJugador() {
        do {
            jugadorActualIndex = (jugadorActualIndex + 1) % jugadores.size();
        } while (getJugadorActual().enBancarrota());
    }

    public void reiniciarJuego() {
        tablero = new Tablero();
        jugadorActualIndex = 0;
        juegoTerminado = false;
        ganador = null;

        for (Jugador jugador : jugadores) {
            jugador.setDinero(5000);
            jugador.setPosicion(0);
            jugador.getPropiedades().clear();
            jugador.setEnCarcel(false);
            jugador.setEnDescanso(false);
            jugador.resetearDoblesConsecutivos();
        }
    }
}
