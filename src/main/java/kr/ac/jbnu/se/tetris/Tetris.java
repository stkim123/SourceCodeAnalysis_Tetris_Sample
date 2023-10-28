package kr.ac.jbnu.se.tetris;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Tetris extends JPanel {

    private TetrisGameManager parent;

    final int Frame_X = 335, Frame_Y = 500, Status_X = 60, Status_Y = 30;

    private boolean isComputer;
    
    // 체력바
    private JProgressBar healthBar;

    // 보드
    private Board board;

    // Next 블록 프리뷰
    private BlockPreview blockPreview;

    // 점수 패널
    private JPanel statusPanel;
    private JLabel statusbar;
    
    // 폭탄
    private JLabel bombLabel;
    private int bombCount;

    public Tetris(TetrisGameManager parent, boolean isComputer) {
        this.parent = parent;    
        this.isComputer = isComputer; 
        setTetrisLayout(isComputer);
    }

    // -------------------------------------- 아이템 --------------------------------------

    public void acquireBomb() { // 폭탄 획득
        String bomb = Integer.toString(++bombCount);
        bombLabel.setText("X " + bomb);
    }
    public void useBomb() { // 폭탄 사용
        if(bombCount > 0) {
            String bomb = Integer.toString(--bombCount);
            bombLabel.setText("X " + bomb);
            board.getNextPiece().setShape(Tetrominoes.BombBlock);
            blockPreview.setNextPiece(board.nextPiece);
        }
    }

    // -------------------------------------- get 메소드 --------------------------------------

    public TetrisGameManager gameManager() {
        return parent;
    }
    public JLabel getStatusBar() {
        return statusbar;
    }
    public Board getBoard() {
        return board;
    }
    public JProgressBar getHealthBar() {
        return healthBar;
    }
    public BlockPreview getBlockPreview(){
        return blockPreview;
    }
    public int frameX(){
        return Frame_X;
    }
    public int frameY(){
        return Frame_Y;
    }

    // -------------------------------------- 컴포넌트 레이아웃 설정 --------------------------------------

    private void setTetrisLayout(boolean isComputer) {
        setPreferredSize(new Dimension(Frame_X, Frame_Y));
        setBackground(new Color(210, 210, 210));
        setLayout(null);

        board = isComputer ? new BoardAI(this) : new BoardPlayer(this); // 1. 보드판
        blockPreview =  new BlockPreview(board); // 2. Next 블록 프리뷰
        setHealthBar(); // 3. 체력바
        setScorePanel(); // 4. 스코어 패널
        setBombLabel(); // 5. 폭탄 개수

        setLayoutLocation(); // 각 컴포넌트 위치 조정

        addComponent(isComputer); // 각 컴포넌트 배치
    }

    private void setHealthBar() {
        healthBar = new JProgressBar(0, 100); // 최소 값과 최대 값을 설정
        healthBar.setValue(100); // 초기 체력 설정
        healthBar.setStringPainted(true); // 백분율 표시 활성화
        healthBar.setForeground(new Color(220, 120, 120)); // 체력 바의 색상 변경
        healthBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionForeground() {
                return Color.WHITE; // 글자 색 변경
            }
        });
    }
    
    private void setScorePanel(){
        statusbar = new JLabel("0");
        statusPanel = new JPanel();
        statusPanel.add(statusbar, BorderLayout.CENTER);
    }

    private void setBombLabel() { // 폭탄 개수 텍스트
        bombLabel = new JLabel();
        bombLabel.setFont(bombLabel.getFont().deriveFont(17.0f));
        bombLabel.setText("X 1");
        bombCount = 1;
    }
    
    private void setLayoutLocation() {
        healthBar.setBounds(20, 15, board.panelWidth(), 15);
        board.setBounds(20, 40, board.panelWidth(), board.panelHeight());
        blockPreview.setBounds(35 + board.panelWidth(), 40, blockPreview.panelWidth(), blockPreview.panelHeight());
        statusPanel.setBounds(35 + board.panelWidth(), 40 + board.panelHeight() - Status_Y, Status_X, Status_Y);
        bombLabel.setBounds(64 + board.panelWidth(), 40 + board.panelHeight() - Status_Y - 49, 30, 30);
    }

    private void addComponent(boolean isComputer){
        add(board);
        add(blockPreview);
        add(healthBar);
        add(statusPanel);
        if(!isComputer) add(bombLabel); // 플레이어 패널만 폭탄 카운트 생성
    }
    
    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);
        if(!isComputer) { // 플레이어 패널만 폭탄 아이콘 생성
            BlockImage image = new BlockImage(Tetrominoes.BombBlock);
            g.drawImage(image.getImage(), 32 + board.panelWidth(), 40 + board.panelHeight() - Status_Y - 52, 
                        30, 30, null);
        }
    }
}

