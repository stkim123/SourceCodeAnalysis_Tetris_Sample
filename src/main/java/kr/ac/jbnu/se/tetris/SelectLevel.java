package kr.ac.jbnu.se.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class SelectLevel extends JFrame {

    private Select home;

    private final int LevelNumber = 5;

    private final int Bt_W = 120, Bt_H = 35;

    private JButton[] level;

    private JButton backselect;

    private Backgrounds backgrounds;

    public SelectLevel(Select home){
        this.home = home;

        level = new JButton[LevelNumber];
        for(int i = 1; i <= LevelNumber; i++){
            level[i - 1] = new JButton(new ImageIcon("image/buttons/level" + i + ".png")); // 레벨 이미지 지정 필요
        }

        backselect = new JButton(new ImageIcon("image/buttons/back.png"));

        backgrounds = new Backgrounds("image/Background.jpg", Select.Frame_X, Select.Frame_Y);

        setFrame();
    }

    private void setFrame() {
        setSize(Select.Frame_X, Select.Frame_Y);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setButton();

        for(int i = 0; i < LevelNumber; i++){
            add(level[i]);
        }

        add(backselect);
        add(backgrounds.getPane());
    }

    private void setButton() {
        for(int i = 0; i < LevelNumber; i++){
            level[i].setBounds(10 + (30 * (i + 1)) + i * Bt_W, Select.Frame_Y - 150, Bt_W, Bt_H);
            setButtonBorder(level[i]);
        }
        backselect.setBounds(20, 20, 70, 58);
        setButtonBorder(backselect);

        addButtonAction();
    }

    private void setButtonBorder(JButton jb){
        jb.setBorderPainted(false);
        jb.setContentAreaFilled(false);
        jb.setFocusPainted(false);
        jb.setOpaque(false);
    }

    private void addButtonAction() {
        for(int i = 0; i < LevelNumber; i++) {
            int index = i;
            level[index].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    
                    TetrisGameManager.level = index + 1; // level 설정

                    BoardAI.moveDelay = 160 - TetrisGameManager.level * 20;  // level에 따른 ai 속도 조정
                    
                    TetrisGameManager game = new TetrisGameManager(home);
                    game.setVisible(true);
                    game.start(true); 
                }
            });
        }
        backselect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                home.setVisible(true);
            }
        });
    }   
}
