package org.example.ui.ventana;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.Getter;
import org.example.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.example.Main.ventana;

@Getter
public class PanelInicio extends JPanel {

    private JButton empezarPartida;
    private boolean modoNoche = true;

    public PanelInicio(){
        empezarPartida = new JButton("Jugar");
        empezarPartida.setFont(new Font("Arial", Font.BOLD, 25));
        JButton modoNoche = new JButton("☾");
        modoNoche.setFont(new Font("Segoe", Font.BOLD, 25));
        modoNoche.addActionListener(eventoModoNoche());

        setLayout(new BorderLayout());

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titulo = new JLabel("AJEDREZ");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        Icon iconoAjedrez = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/icons/piezaIcono.png")));
        titulo.setIcon(iconoAjedrez);
        titulo.setVerticalTextPosition(SwingConstants.TOP);
        titulo.setHorizontalTextPosition(SwingConstants.CENTER);
        titulo.setIconTextGap(30);

        panelContenido.add(titulo, BorderLayout.NORTH);

        JPanel panelMedio = new JPanel(new BorderLayout());
        panelMedio.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.add(empezarPartida, BorderLayout.CENTER);
        panelBotones.add(modoNoche, BorderLayout.EAST);
        panelMedio.add(panelBotones, BorderLayout.SOUTH);
        panelContenido.add(panelMedio, BorderLayout.CENTER);

        JPanel panelIzq = new JPanel();
        panelIzq.setMinimumSize(new Dimension(150, 100));
        panelContenido.add(panelIzq, BorderLayout.WEST);

        JPanel panelDer = new JPanel();
        panelDer.setMinimumSize(new Dimension(150, 100));
        panelContenido.add(panelDer, BorderLayout.EAST);

        add(panelContenido, BorderLayout.CENTER);
        JPanel panelFirma = new JPanel();
        JLabel autor = new JLabel("Aaron Perez");
        URL fuenteURL = getClass().getResource("/fonts/Signlode.ttf");
        Font fuenteAutoria;
        try {
            assert fuenteURL != null;
            fuenteAutoria = Font.createFont(Font.TRUETYPE_FONT, fuenteURL.openStream());
            fuenteAutoria = fuenteAutoria.deriveFont(20F);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        autor.setFont(fuenteAutoria);

        JLabel strAutor = new JLabel("Firmado: ");
        strAutor.setFont(strAutor.getFont().deriveFont(15F));
        panelFirma.add(strAutor);
        panelFirma.add(autor);
        add(panelFirma, BorderLayout.SOUTH);
    }

    private ActionListener eventoModoNoche(){

        return e -> {
            try {
                if (modoNoche) UIManager.setLookAndFeel( new FlatMacLightLaf());
                if (!modoNoche) UIManager.setLookAndFeel( new FlatMacDarkLaf());
                modoNoche = !modoNoche;
            } catch( Exception ex ) {
                System.err.println( "Failed to initialize LaF" );
            }
            SwingUtilities.updateComponentTreeUI(ventana);
        };
    }
}
