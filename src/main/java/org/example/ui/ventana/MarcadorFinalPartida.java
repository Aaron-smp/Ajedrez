package org.example.ui.ventana;

import javax.swing.*;
import java.awt.*;

public class MarcadorFinalPartida {

    public static int mostrarDialogo(Component component,String ganador) {
        int opcion;
        do {
            String[] opciones = {"Reiniciar", "Cerrar"};

            // Mostrar el JOptionPane con botones personalizados
            opcion =  JOptionPane.showOptionDialog(
                    component,
                    "GANAN " + ganador + "\n¿Qué desea hacer?",
                    "Opciones",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );
        } while (opcion == JOptionPane.CLOSED_OPTION);

        return opcion;
    }
}
