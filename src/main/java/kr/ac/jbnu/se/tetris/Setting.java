package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Setting extends JFrame {

    private final Select home;

    private Backgrounds backgrounds;
    private JButton musicButton, changekey, backselect;
    private boolean isMusicStarted = true;

    private final KeyChange keyChange;

    Setting(Select home) {
        this.home = home;
        keyChange = new KeyChange();
        setFrame();
    }

    private void setFrame() {
        setSize(Select.Frame_X, Select.Frame_Y);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        backgrounds = new Backgrounds("image/Background.jpg", Select.Frame_X, Select.Frame_Y);
        setButton();
        add(musicButton); add(changekey); add(backselect); add(backgrounds.getPane());
    }

    private void addButton() {
        musicButton = new JButton(new ImageIcon("image/buttons/musicOn.png"));
        changekey = new JButton(new ImageIcon("image/buttons/keychange.png"));
        backselect = new JButton(new ImageIcon("image/buttons/back.png"));
    }

    private void setButton() {
        addButton();
        
        musicButton.setBounds(350, 40, 100, 100);
        setButtonBorder(musicButton);

        changekey.setBounds(250, 250, 300, 150);
        setButtonBorder(changekey);

        backselect.setBounds(20, 20, 70, 58);
        setButtonBorder(backselect);

        buttonAction();
    }

    private void pauseMusic(){
        isMusicStarted = !isMusicStarted;
        if(isMusicStarted){
            musicButton.setIcon(new ImageIcon("image/buttons/musicOn.png"));
            home.getMusic().startMusic();
        }
        else{
            musicButton.setIcon(new ImageIcon("image/buttons/musicOff.png"));
            home.getMusic().stopMusic();
        }
    }

    private void setButtonBorder(JButton jb){ // 버튼의 이미지를 나타내기 위해 테두리 등을 제거 (부드럽게 나타내기 .. )
        jb.setBorderPainted(false);
        jb.setContentAreaFilled(false);
        jb.setFocusPainted(false);
        jb.setOpaque(false);
    }

    private void buttonAction() {
        musicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseMusic();
            }
        });
        changekey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyChange.setVisible(true);
            }
        });
        backselect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {           
                setVisible(false);
                home.setVisible(true);
            }
        });
    }
}
