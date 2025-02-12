package org.example;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.example.ui.ventana.Ventana;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static Ventana ventana;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatMacDarkLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        System.out.println(UIManager.getFont("Button.font"));
        Font fuenteGlobal = new Font("Arial", Font.BOLD, 15);
        UIManager.put("Label.font", fuenteGlobal);
        UIManager.put("Button.font", fuenteGlobal);
        UIManager.put("TextField.font", fuenteGlobal);
        UIManager.put("TextArea.font", fuenteGlobal);
        UIManager.put("ComboBox.font", fuenteGlobal);

        ventana = new Ventana();
    }
}