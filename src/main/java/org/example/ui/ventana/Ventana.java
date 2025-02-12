package org.example.ui.ventana;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.example.Main;
import org.example.juego.PartidaAjedrez;
import org.example.ui.juego.PanelBasePartida;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Ventana extends JFrame {

    private JPanel panelContenedor;
    private CardLayout cardLayout;
    private PanelBasePartida panelBasePartida;
    private PanelInicio panelInicio;

    public Ventana(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(500, 500));
        setLocationRelativeTo(null);
        PartidaAjedrez partidaAjedrez = new PartidaAjedrez();

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelBasePartida = new PanelBasePartida(partidaAjedrez);
        panelInicio = new PanelInicio();

        panelContenedor.add(panelInicio, "panelInicio");
        panelContenedor.add(panelBasePartida, "panelPartida");

        panelInicio.getEmpezarPartida().addActionListener(e -> {
            cardLayout.show(panelContenedor, "panelPartida");
            panelBasePartida.iniciarPartida();
        });

        setContentPane(panelContenedor);

        setPreferredSize(new Dimension(600, 450));
        setTitle("AJEDREZ");
        setVisible(true);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/piezaIcono.png"))).getImage());
    }
}
