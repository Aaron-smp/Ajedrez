package org.example.ui.juego;

import org.example.Main;
import org.example.juego.Movimientos;
import org.example.juego.PartidaAjedrez;
import org.example.juego.Tablero;
import org.example.piezas.Pieza;
import org.example.ui.ventana.BotonesControlPartida;
import org.example.ui.ventana.EstadoPartidaPanel;
import org.example.ui.ventana.MarcadorFinalPartida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

import static org.example.Main.ventana;


public class PanelBasePartida extends JPanel {

    private BotonPieza[][] tableroBotones;
    private int clicksTurno;
    private PartidaAjedrez partidaAjedrez;
    private BotonPieza piezaAMover;
    private EstadoPartidaPanel estadoPartidaPanel;
    private final JPanel cuadricula;
    private final BotonesControlPartida botoneraPartida;

    public PanelBasePartida(PartidaAjedrez partidaAjedrez) {
        this.clicksTurno = 2;
        this.partidaAjedrez = partidaAjedrez;
        this.cuadricula = new JPanel();
        this.botoneraPartida = new BotonesControlPartida();
        JPanel coordenadasLetras = new JPanel();
        JPanel coordenadasNums = new JPanel();
        estadoPartidaPanel = new EstadoPartidaPanel(partidaAjedrez);
        setLayout(new BorderLayout());
        coordenadasLetras.setLayout(new GridLayout(1, 9, 20, 10));
        coordenadasLetras.setMinimumSize(new Dimension(50, 200));

        String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letra : letras){
            coordenadasLetras.add(new JLabel(letra));
        }
        coordenadasLetras.setBorder(new EmptyBorder(10, 30, 10, 0));
        coordenadasNums.setLayout(new GridLayout(0, 1, 10, 10));
        String[] numeros = {"8", "7", "6", "5", "4", "3", "2", "1"};

        for (String numero : numeros){
            coordenadasNums.add(new JLabel(numero));
        }
        coordenadasNums.setMinimumSize(new Dimension(40, 200));
        add(estadoPartidaPanel, BorderLayout.NORTH);
        add(cuadricula, BorderLayout.CENTER);
        add(coordenadasLetras, BorderLayout.SOUTH);
        add(coordenadasNums, BorderLayout.WEST);
        cuadricula.setLayout(new GridLayout(8, 8, 10, 10));
        iniciarBotonera(cuadricula);
        habilitarLado(true);
    }

    public void iniciarPartida(){
        estadoPartidaPanel.iniciarTemporizador();
    }

    public void reiniciarPartida(){
        clicksTurno = 2;
        partidaAjedrez = new PartidaAjedrez();
        estadoPartidaPanel = new EstadoPartidaPanel(partidaAjedrez);
        iniciarPartida();
        cuadricula.removeAll();
        iniciarBotonera(cuadricula);
        addIconos(partidaAjedrez.getTablero().getPiezas());
        habilitarLado(true);
    }

    private void turno(int xDestino, int yDestino) {
        BotonPieza destinoMovimiento = tableroBotones[xDestino][yDestino];

        //Recupero las piezas aliadas
        Pieza[] piezas = partidaAjedrez.isTurnoBlancas() ? partidaAjedrez.getTablero().getBlancas() : partidaAjedrez.getTablero().getNegras();
        boolean movimientoIncorrecto = false;
        //Se recorren las piezas aliadas
        for(Pieza pieza : piezas){
            //Se recogen las coordenadas de la posicion
            int xPieza = pieza.getX();
            int yPieza = pieza.getY();

            //Se compara que la posicion destino y la posicion de la pieza aliada sea la misma en caso verdadero será un movimiento erroneo
            if(xDestino == xPieza && yDestino == yPieza && pieza.getEquipo() != 0){
                movimientoIncorrecto= true;
            }
        }

        //Si clicka en la misma pieza se resetea tanto los colores del movimiento como los clicks del turno o si es un movimiento incorrecto como clickar una pieza aliada
        if(piezaAMover == destinoMovimiento || movimientoIncorrecto){
            resetearColorBotones();
            clicksTurno = 2;
            piezaAMover = null;
            return;
        }

        Pieza piezaObjetivo = partidaAjedrez.getTablero().getPieza(xDestino, yDestino);
        Pieza piezaOrigen = partidaAjedrez.getTablero().getPieza(piezaAMover.getPosicionX(), piezaAMover.getPosicionY());
        if(piezaObjetivo.getEquipo() != piezaOrigen.getEquipo()){
            intercambiarIcono(xDestino, yDestino);
            resetearColorBotones();
            clicksTurno = 2;
            if (partidaAjedrez.getEstadoPartida() == PartidaAjedrez.ESTADO.JAQUEMATE){
                String ganador = !partidaAjedrez.isTurnoBlancas() ? "Blancas" : "Negras";
                int opcion = MarcadorFinalPartida.mostrarDialogo(this, ganador);
                if (opcion == 1) System.exit(0);
                if (opcion == 0) reiniciarPartida();

            }
            System.out.println("Estado partida:" + partidaAjedrez.getEstadoPartida());
        }

    }

    private void iniciarBotonera(JPanel cuadricula){
        tableroBotones = new BotonPieza[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tableroBotones[i][j] = new BotonPieza("", i, j);
                tableroBotones[i][j].setEnabled(false);
                int x = tableroBotones[i][j].getPosicionX();
                int y = tableroBotones[i][j].getPosicionY();
                tableroBotones[i][j].addActionListener((e) -> {
                    if(clicksTurno == 2){
                        activarMovimientosBotones(x, y);
                    }else{
                        turno(x, y);
                    }
                });
                cuadricula.add(tableroBotones[i][j]);
            }
        }
        addIconos(partidaAjedrez.getTablero().getPiezas());
    }

    private void activarMovimientosBotones(int x, int y) {
        piezaAMover = tableroBotones[x][y];
        Pieza pieza = partidaAjedrez.getTablero().getPieza(x, y);
        java.util.List<int[]> botones = Movimientos.getPosiblesMovimientos(partidaAjedrez.getTablero().getPiezas(), pieza);

        for (int[] boton : botones){
            tableroBotones[boton[0]][boton[1]].setBackground(Color.GREEN);
            tableroBotones[boton[0]][boton[1]].setEnabled(true);
        }

        clicksTurno = clicksTurno-1;
    }


    private void intercambiarIcono(int xDestino, int yDestino){
        int xOrigen = piezaAMover.getPosicionX();
        int yOrigen = piezaAMover.getPosicionY();

        tableroBotones[xDestino][yDestino].setIcon(tableroBotones[xOrigen][yOrigen].getIcon());
        tableroBotones[xOrigen][yOrigen].setIcon(null);

        partidaAjedrez.move(xOrigen, yOrigen, xDestino, yDestino);
        habilitarLado(partidaAjedrez.isTurnoBlancas());
    }

    private void habilitarLado(boolean blancas) {
        Tablero tableroPiezas = partidaAjedrez.getTablero();
        resetearColorBotones();
        for (int i = 0; i < tableroPiezas.getPiezas().length; i++) {
            for (int j = 0; j < tableroPiezas.getPiezas().length; j++) {
                int[] posicion = tableroPiezas.getPieza(i, j).getPosicion();
                BotonPieza botonPieza = tableroBotones[posicion[0]][posicion[1]];
                if (tableroPiezas.getPieza(i, j).getEquipo() == 2) {
                    botonPieza.setEnabled(blancas);
                } else if (tableroPiezas.getPieza(i, j).getEquipo() == 1) {
                    botonPieza.setEnabled(!blancas);
                } else {
                    botonPieza.setEnabled(false);
                }
            }
        }
    }

    private void resetearColorBotones(){
        Color colorPredefinido = new Color(238,238,238);
        for (BotonPieza[] lineaBotonPieza : tableroBotones){
            for(BotonPieza botonPieza : lineaBotonPieza){
                botonPieza.setBackground(colorPredefinido);
            }
        }
    }

    public void addIconos(Pieza[][] piezas){
        Icon iconoTorreNegra = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/torreNegra.png")));
        Icon iconoTorreBlanca = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/torreBlanca.png")));
        Icon iconoAlfilBlanca = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/alfilBlanca.png")));
        Icon iconoAlfilNegra = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/alfilNegra.png")));
        Icon iconoCaballoNegro = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/caballoNegro.png")));
        Icon iconoCaballoBlanco = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/caballoBlanco.png")));
        Icon iconoReyNegro = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/reyNegro.png")));
        Icon iconoReyBlanco = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/reyBlanco.png")));
        Icon iconoReinaBlanca = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/reinaBlanca.png")));
        Icon iconoReinaNegra = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/reinaNegra.png")));
        Icon iconoPeonNegro = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/peonNegro.png")));
        Icon iconoPeonBlanco = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/peonBlanco.png")));

        for (Pieza[] lineaPieza : piezas){
            for (Pieza pieza : lineaPieza){
                int[] posicion = pieza.getPosicion();
                if (pieza.getTipoPieza().equals(Pieza.TipoPieza.PEON)){
                   if(pieza.getEquipo() == 1){
                       tableroBotones[posicion[0]][posicion[1]].setIcon(iconoPeonNegro);
                   }else{
                       tableroBotones[posicion[0]][posicion[1]].setIcon(iconoPeonBlanco);
                   }
                }else if(pieza.getTipoPieza().equals(Pieza.TipoPieza.TORRE)) {
                    if(pieza.getEquipo() == 1){
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoTorreNegra);
                    }else{
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoTorreBlanca);
                    }
                }else if(pieza.getTipoPieza().equals(Pieza.TipoPieza.CABALLO)){
                    if(pieza.getEquipo() == 1){
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoCaballoNegro);
                    }else{
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoCaballoBlanco);
                    }
                }else if(pieza.getTipoPieza().equals(Pieza.TipoPieza.ALFIL)){
                    if(pieza.getEquipo() == 1){
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoAlfilNegra);
                    }else{
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoAlfilBlanca);
                    }
                }else if(pieza.getTipoPieza().equals(Pieza.TipoPieza.REINA)){
                    if(pieza.getEquipo() == 1){
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoReinaNegra);
                    }else{
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoReinaBlanca);
                    }
                }else if(pieza.getTipoPieza().equals(Pieza.TipoPieza.REY)){
                    if(pieza.getEquipo() == 1){
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoReyNegro);
                    }else{
                        tableroBotones[posicion[0]][posicion[1]].setIcon(iconoReyBlanco);
                    }
                }
            }
        }
    }
}
