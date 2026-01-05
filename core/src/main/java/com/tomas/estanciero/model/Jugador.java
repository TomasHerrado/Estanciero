package com.tomas.estanciero.model;
import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private int dinero;
    private int posicion;
    private List<Propiedad> propiedades;
    private boolean enCarcel;
    private int turnosEnCarcel;
    private boolean enDescanso;
    private int turnosDescanso;
    private int doblesConsecutivos;

    public Jugador(String nombre, int dineroInicial) {
        this.nombre = nombre;
        this.dinero = dineroInicial;
        this.posicion = 0;
        this.propiedades = new ArrayList<>();
        this.enCarcel = false;
        this.turnosEnCarcel = 0;
        this.enDescanso = false;
        this.turnosDescanso = 0;
        this.doblesConsecutivos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public void agregarDinero(int cantidad) {
        this.dinero += cantidad;
    }

    public boolean restarDinero(int cantidad) {
        if (dinero >= cantidad) {
            dinero -= cantidad;
            return true;
        }
        return false;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion % 40;
    }

    public void mover(int casillas) {
        int posicionAnterior = this.posicion;
        this.posicion = (this.posicion + casillas) % 40;

        if (posicionAnterior > this.posicion) {
            agregarDinero(200);
        }
    }

    public List<Propiedad> getPropiedades() {
        return propiedades;
    }

    public void agregarPropiedad(Propiedad propiedad) {
        propiedades.add(propiedad);
        propiedad.setDueno(this);
    }

    public void removerPropiedad(Propiedad propiedad) {
        propiedades.remove(propiedad);
        propiedad.setDueno(null);
    }

    public boolean enBancarrota() {
        return dinero < 0;
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
        if (enCarcel) {
            this.turnosEnCarcel = 0;
        }
    }

    public int getTurnosEnCarcel() {
        return turnosEnCarcel;
    }

    public void incrementarTurnosEnCarcel() {
        this.turnosEnCarcel++;
    }

    public boolean isEnDescanso() {
        return enDescanso;
    }

    public void setEnDescanso(boolean enDescanso) {
        this.enDescanso = enDescanso;
        if (enDescanso) {
            this.turnosDescanso = 0;
        }
    }

    public int getTurnosDescanso() {
        return turnosDescanso;
    }

    public void incrementarTurnosDescanso() {
        this.turnosDescanso++;
    }

    public int getDoblesConsecutivos() {
        return doblesConsecutivos;
    }

    public void incrementarDoblesConsecutivos() {
        this.doblesConsecutivos++;
    }

    public void resetearDoblesConsecutivos() {
        this.doblesConsecutivos = 0;
    }

    @Override
    public String toString() {
        return nombre + " - $" + dinero + " - Posición: " + posicion + " - Propiedades: " + propiedades.size();
    }
}
