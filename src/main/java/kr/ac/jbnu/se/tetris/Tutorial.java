package kr.ac.jbnu.se.tetris;

import javax.swing.*;

public class Tutorial extends JFrame {
    
    private final int tutorial_Frame_X = 299, tutorial_Frame_Y = 537;

    private Backgrounds background;
    
    private final String tutorialpng = "image/tutorials.png";
    
    Tutorial() {
        setFrame();
    }

    private void setFrame() {
        background = new Backgrounds(tutorialpng, tutorial_Frame_X, tutorial_Frame_Y);
        add(background.getPane());
        setSize(tutorial_Frame_X, tutorial_Frame_Y);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
    }

}