package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Select extends JFrame {

    public static final int Frame_X = 800, Frame_Y = 453;
    private final int Bt_W = 172, Bt_H = 50, Bt_Y = Frame_Y - 150;

    private Backgrounds background;
    private final Music music;

    private final SelectLevel selectLevel;

    private final Setting setting;

    private final Tutorial tutorial;

    private JButton ai, versus, settingBtn, tutorialBtn;

    Select() {
        setFrame();

        selectLevel = new SelectLevel(this);
        setting = new Setting(this);
        tutorial = new Tutorial();
        
        music = new Music();
        music.startMusic();
    }

    public Music getMusic(){
        return music;
    }

    public void setFrame() {
        setSize(Frame_X, Frame_Y);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        background = new Backgrounds("image/Background.jpg", Frame_X, Frame_Y);
        setButton();

        add(ai); add(versus); add(settingBtn); add(tutorialBtn);
        add(background.getPane());
    }

    public void setButton() {
        ai = new JButton(new ImageIcon("image/buttons/AImodes.png"));
        ai.setBounds(71, Bt_Y, Bt_W, Bt_H);
        setButtonBorder(ai);

        versus = new JButton(new ImageIcon("image/buttons/2P_modes.png"));
        versus.setBounds(142 + Bt_W, Bt_Y, Bt_W, Bt_H);
        setButtonBorder(versus);

        settingBtn = new JButton(new ImageIcon("image/buttons/settings.png"));
        settingBtn.setBounds(213 + Bt_W * 2, Bt_Y, Bt_W, Bt_H);
        setButtonBorder(settingBtn);
        
        tutorialBtn = new JButton(new ImageIcon("image/buttons/tutorial.png"));
        tutorialBtn.setBounds(Frame_X - 150, 30, 120, 35);
        setButtonBorder(tutorialBtn);

        buttonAction();
    }

    private void setButtonBorder(JButton jb){ // 버튼의 이미지를 나타내기 위해 테두리 등을 제거 (부드럽게 나타내기 .. )
        jb.setBorderPainted(false);
        jb.setContentAreaFilled(false);
        jb.setFocusPainted(false);
        jb.setOpaque(false);
    }

    public void buttonAction() {
        Select select = this;
        ai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                selectLevel.setVisible(true);
            }
        });

        versus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);

                TetrisGameManager.level = 0; // 플레이어 대전인 경우 0 레벨 설정
                
                TetrisGameManager game = new TetrisGameManager(select);
                game.start(false);
                game.setVisible(true);
            }
        });

        settingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                setting.setVisible(true);
            }
        });

        tutorialBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tutorial.setVisible(true);
            }
            
        });
    }
}