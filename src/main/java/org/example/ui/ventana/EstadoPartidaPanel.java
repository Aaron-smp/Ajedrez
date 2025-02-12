package org.example.ui.ventana;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.juego.DatosListener;
import org.example.juego.PartidaAjedrez;

import javax.swing.*;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class EstadoPartidaPanel extends JPanel implements DatosListener {

    private JLabel estadoPartida;
    private JLabel turnoEquipo;
    private JLabel tiempoPartida;
    private long segundosPartida;
    private Timer timer;

    public EstadoPartidaPanel(PartidaAjedrez partidaAjedrez){
        super();
        partidaAjedrez.agregarListener(this);
        this.segundosPartida = 0;
        setLayout(new GridLayout(1, 3));
        estadoPartida = new JLabel("Estado: " + partidaAjedrez.getEstadoPartida().getNombre());
        estadoPartida.setHorizontalAlignment(SwingConstants.CENTER);
        turnoEquipo = new JLabel("Turno: " + getTurno(partidaAjedrez));
        turnoEquipo.setHorizontalAlignment(SwingConstants.CENTER);
        tiempoPartida = new JLabel("Tiempo: 00:00");
        tiempoPartida.setHorizontalAlignment(SwingConstants.CENTER);
        add(turnoEquipo);
        add(estadoPartida);
        add(tiempoPartida);
    }

    @Override
    public void onDatosCambiados(PartidaAjedrez partidaAjedrez) {
        estadoPartida.setText("Estado: " + partidaAjedrez.getEstadoPartida().getNombre());
        turnoEquipo.setText("Turno: " + getTurno(partidaAjedrez));
    }

    private String getTurno(PartidaAjedrez partidaAjedrez){
        return partidaAjedrez.isTurnoBlancas() ? "Blancas" : "Negras";
    }

    public void iniciarTemporizador(){
        timer = new Timer(1000, (e) -> {
            segundosPartida = segundosPartida + 1;
            int minutos = (int) segundosPartida /60;
            int segundos = (int) segundosPartida % 60;
            String segundosStr = segundos < 10 ? "0"+segundos : String.valueOf(segundos);
            String minutosStr = minutos < 10 ? "0"+minutos : String.valueOf(minutos);
            SwingUtilities.invokeLater(() -> {
                tiempoPartida.setText("Tiempo: " + minutosStr + ":" + segundosStr);
            });
        });
        timer.start();
    }

    public void pararTemporizador(){
        timer.stop();
    }
}
