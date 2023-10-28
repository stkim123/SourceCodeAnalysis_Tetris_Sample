package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TetrisGameManager extends JFrame {
    
    public static int level = 0;

    public static int p2_up = 'w', p2_down = 's', p2_left = 'a', p2_right = 'd',
                        p2_up_upper = 'W', p2_down_upper = 'S', p2_left_upper = 'A', p2_right_upper = 'D',
                        p2_dropDown = KeyEvent.VK_SHIFT;

    private final int Frame_X = 750, Frame_Y = 620;

    private boolean isPaused = false;
    private boolean opponentIsComputer;

    // 최대 점수 표기 (AI 대전)
    private MaxScorePanel maxScorePanel;

    // 타이머
    private TimerPanel timer;

    // 각 보드판 패널
    private Tetris player1Panel;
    private Tetris player2Panel;

    // 일시 정지, 게임 종료 UI
    private JDialog pauseDialog, gameOverDialog;

    public TetrisGameManager(Select select) {
        setFrame();

        setPauseDialog(select); // 일시정지 화면 설정
        setGameOverDialog(select); // 게임종료 화면 설정

        addKeyListener(new PlayerKeyListener());
    }

    // -------------------------------------- get 메소드 --------------------------------------

    public boolean opponentIsComputer(){
        return opponentIsComputer;
    }
    public MaxScorePanel getMaxScorePanel(){
        return maxScorePanel;
    }
    public JDialog gameOverDialog(){
        return gameOverDialog;
    }

    // -------------------------------------- 시작 및 종료 관리 --------------------------------------

    public void start(boolean isComputer) {
        opponentIsComputer = isComputer;
        
        maxScorePanel = new MaxScorePanel(); // 1. 최대 점수 패널     
        timer = new TimerPanel(); // 2. 타이머
        player1Panel = new Tetris(this, false); // 3. 테트리스 패널 1
        player2Panel = new Tetris(this, isComputer); // 4. 테트리스 패널 2

        // 각 테트리스 패널의 대결 상대 설정
        Board p1Board = player1Panel.getBoard();
        Board p2Board = player2Panel.getBoard();
        p1Board.setOpponent(p2Board);
        p2Board.setOpponent(p1Board);

        setLayoutLocation(); // 각 컴포넌트 위치 설정

        addComponent(isComputer); // 각 컴포넌트 배치

        timer.startTimer(); // 타이머 가동
    }

    public void pause() {
        Board p1Board = player1Panel.getBoard();
        Board p2Board = player2Panel.getBoard();

        if (!p1Board.isStarted() || !p2Board.isStarted())
            return;

        isPaused = !isPaused;

        if (isPaused) {
            p1Board.getTimer().stop();
            p2Board.getTimer().stop();
        } else {
            p1Board.start();
            p2Board.start();
        }

        pauseDialog.setVisible(isPaused);
    }

    // -------------------------------------- 컴포넌트 레이아웃 설정 --------------------------------------

    private void setFrame() {
        setTitle("Tetris");
        setSize(Frame_X, Frame_Y);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setFocusable(true);
        getContentPane().setBackground(new Color(220, 220, 220));
    }

    private void setLayoutLocation() {
        timer.setBounds(325, 10, 100, 25);
        maxScorePanel.setBounds(30, 10, 90, 25);
        player1Panel.setBounds(20, 45, player1Panel.frameX(), player1Panel.frameY());
        player2Panel.setBounds(player2Panel.frameX() + 60, 45, player2Panel.frameX(), player2Panel.frameY());
    }

    private void addComponent(boolean isComputer) {
        if(isComputer) add(maxScorePanel);
        add(timer);
        add(player1Panel);
        add(player2Panel);
    }

    // -------------------------------------- 게임 종료 및 일시 정지 UI 설정 --------------------------------------

    private void setGameOverDialog(Select select) {
        TetrisGameManager g = this;

        JLabel gameOverText = new JLabel("게임 종료!");

        JButton retryBtn = new JButton("재시작");
        JButton homeBtn = new JButton("메인화면");

        retryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.dispose();

                gameOverDialog.setVisible(false);

                TetrisGameManager game = new TetrisGameManager(select);
                game.start(opponentIsComputer);
                game.setVisible(true);
            }

        });
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.dispose();

                gameOverDialog.setVisible(false);

                select.setVisible(true);
            }
        });

        gameOverDialog = new JDialog(this, "게임 오버", true);
        gameOverDialog.setUndecorated(true);
        gameOverDialog.setSize(170, 135);
        gameOverDialog.setLocationRelativeTo(null);
        gameOverDialog.setLayout(null);

        gameOverText.setBounds(60, 10, 150, 30);
        retryBtn.setBounds(10, 45, 150, 35);
        homeBtn.setBounds(10, 90, 150, 35);

        gameOverDialog.add(gameOverText);
        gameOverDialog.add(retryBtn);
        gameOverDialog.add(homeBtn);
    }

    private void setPauseDialog(Select select) {
        TetrisGameManager g = this;

        JLabel pauseText = new JLabel("일시 정지");

        JButton resumeBtn = new JButton("계속하기");
        JButton retryBtn = new JButton("재시작");
        JButton homeBtn = new JButton("메인화면");

        resumeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause();
            }

        });
        retryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.dispose();

                pauseDialog.setVisible(false);

                TetrisGameManager game = new TetrisGameManager(select);
                game.start(opponentIsComputer);
                game.setVisible(true);
            }
        });
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.dispose();

                select.setVisible(true);
                pauseDialog.setVisible(false);
            }
        });

        pauseDialog = new JDialog(this, "일시정지", true);
        pauseDialog.setUndecorated(true);
        pauseDialog.setSize(170, 175);
        pauseDialog.setLocationRelativeTo(null); 
        pauseDialog.setLayout(null);

        pauseText.setBounds(60, 5, 150, 30);
        resumeBtn.setBounds(10, 40, 150, 35);
        retryBtn.setBounds(10, 85, 150, 35);
        homeBtn.setBounds(10, 130, 150, 35);

        pauseDialog.add(pauseText);
        pauseDialog.add(resumeBtn);
        pauseDialog.add(retryBtn);
        pauseDialog.add(homeBtn);
    }

    // -------------------------------------- 이미지 --------------------------------------
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        if(!opponentIsComputer)
            return;

        try {
            BufferedImage image = ImageIO.read(new File("image/control.png"));
            g.drawImage(image, 0, Frame_Y - 30, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // -------------------------------------- 키 입력 리스너 --------------------------------------
    
    public class PlayerKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            Board p1Board = player1Panel.getBoard();
            Board p2Board = player2Panel.getBoard();
            
            if (!p1Board.isStarted() || !p2Board.isStarted() || 
                    p1Board.getCurPiece().getShape() == Tetrominoes.NoShape ||
                                p2Board.getCurPiece().getShape() == Tetrominoes.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == KeyEvent.VK_ESCAPE) {
                pause();
                return;
            }
            if (isPaused)
                return;

            // player1 키 입력

            Shape p1CurPiece = p1Board.getCurPiece();

            if (keycode == KeyEvent.VK_LEFT) {
                if (p1Board.tryMove(p1CurPiece, p1CurPiece.curX() - 1, p1CurPiece.curY()))
                    p1Board.move(p1CurPiece, p1CurPiece.curX() - 1, p1CurPiece.curY());

            }
            if (keycode == KeyEvent.VK_RIGHT) {
                if (p1Board.tryMove(p1CurPiece, p1CurPiece.curX() + 1, p1CurPiece.curY()))
                    p1Board.move(p1CurPiece, p1CurPiece.curX() + 1, p1CurPiece.curY());
            }
            if (keycode == KeyEvent.VK_UP) {
                Shape leftRotated = p1CurPiece.rotateLeft();
                if (p1Board.tryMove(leftRotated, p1CurPiece.curX(), p1CurPiece.curY()))
                    p1Board.move(leftRotated, p1CurPiece.curX(), p1CurPiece.curY());
            }
            if (keycode == KeyEvent.VK_DOWN) {
                Shape rightRotated = p1CurPiece.rotateRight();
                if (p1Board.tryMove(rightRotated, p1CurPiece.curX(), p1CurPiece.curY()))
                    p1Board.move(rightRotated, p1CurPiece.curX(), p1CurPiece.curY());
            }
            if (keycode == KeyEvent.VK_SPACE) {
                p1Board.dropDown();
            }
            if (keycode == 'm' || keycode == 'M') {
                p1Board.oneLineDown();
            }

            if(keycode == '/'){
                player1Panel.useBomb();
            }

            // player2 키 입력
            
            if(opponentIsComputer)
                return;

            Shape p2CurPiece = p2Board.getCurPiece();

            if (keycode == p2_left || keycode == p2_left_upper) {
                if (p2Board.tryMove(p2CurPiece, p2CurPiece.curX() - 1, p2CurPiece.curY()))
                    p2Board.move(p2CurPiece, p2CurPiece.curX() - 1, p2CurPiece.curY());
            }
            if (keycode == p2_right || keycode == p2_right_upper) {
                if (p2Board.tryMove(p2CurPiece, p2CurPiece.curX() + 1, p2CurPiece.curY()))
                    p2Board.move(p2CurPiece, p2CurPiece.curX() + 1, p2CurPiece.curY());
            }     
            if (keycode == p2_up || keycode == p2_up_upper) {
                Shape leftRotated = p2CurPiece.rotateLeft();
                if (p2Board.tryMove(leftRotated, p2CurPiece.curX(), p2CurPiece.curY()))
                    p2Board.move(leftRotated, p2CurPiece.curX(), p2CurPiece.curY());
            }
            if (keycode == p2_down || keycode == p2_down_upper) {
                Shape rightRotated = p2CurPiece.rotateRight();
                if (p2Board.tryMove(rightRotated, p2CurPiece.curX(), p2CurPiece.curY()))
                    p2Board.move(rightRotated, p2CurPiece.curX(), p2CurPiece.curY());
            }
            if (keycode == p2_dropDown) {
                p2Board.dropDown();
            }
            if (keycode == KeyEvent.VK_CONTROL) {
                p2Board.oneLineDown();
            }

            if(keycode == 'q' || keycode == 'Q'){
                player2Panel.useBomb();
            }
        }
    }
}
