package org.example.ui.juego;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class BotonPieza extends JButton {

    private final String letrasTablero = "ABCDEFGH";
    private int posicionX;
    private int posicionY;

    public BotonPieza(String nombre, int posicionX, int posicionY){
        super(nombre);
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        agregarEventos();
    }

    public void printEstado(){
        System.out.println("Posición: {" + posicionX + ", " + posicionY
                + "}, posicion tablero: {" + letrasTablero.charAt(posicionY) + ", " +
                (8-posicionX) + "}");
    }

    private void agregarEventos(){
        this.addActionListener((e) -> {
            printEstado();
        });
    }
}
