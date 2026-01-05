package com.tomas.estanciero.model;
import java.util.Random;

public class Dado {
    private Random random;
    private int dado1;
    private int dado2;

    public Dado() {
        this.random = new Random();
        this.dado1 = 1;
        this.dado2 = 1;
    }

    public int tirar() {
        dado1 = random.nextInt(6) + 1;
        dado2 = random.nextInt(6) + 1;
        return getSuma();
    }

    public int getDado1() {
        return dado1;
    }

    public int getDado2() {
        return dado2;
    }

    public int getSuma() {
        return dado1 + dado2;
    }

    public boolean esDoble() {
        return dado1 == dado2;
    }

    @Override
    public String toString() {
        return "Dados: [" + dado1 + "] [" + dado2 + "] = " + getSuma() + (esDoble() ? " (Doble!)" : "");
    }
}
