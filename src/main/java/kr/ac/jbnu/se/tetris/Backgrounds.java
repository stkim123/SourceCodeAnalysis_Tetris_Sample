package kr.ac.jbnu.se.tetris;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Backgrounds extends JFrame {

    private BufferedImage img = null;
    
    private BackG background;
    
    private JLayeredPane layeredPane = new JLayeredPane();
    
    Backgrounds(String image, int x, int y) {
        DrawBackGround(image, x, y);
    }

    public JLayeredPane getPane(){
        return layeredPane;
    }

    private void DrawBackGround(String image, int x, int y) { // LayerdPane에 이미지를 덮어씌우는 메소드
        layeredPane.setSize(x, y);
        layeredPane.setLayout(null);

        try {
            img = ImageIO.read(new File(image));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        background = new BackG();
        background.setSize(x, y);
        layeredPane.add(background);
    }

    class BackG extends JPanel { // Panel에 이미지를 나타내기 위해서
        public void paint(Graphics g){
            g.drawImage(img, 0, 0, null);
        }
    }
}
