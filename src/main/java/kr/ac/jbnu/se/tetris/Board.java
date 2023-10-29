package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board extends JPanel {

    protected Tetris parent;

    protected boolean isFallingFinished;
    protected boolean isStarted = false;

    protected final int BoardWidth = 10;
    protected final int BoardHeight = 22;

    protected final int PanelWidth = 220;
    protected final int PanelHeight = 440;

    // 공격 데미지 및 hp 회복량
    protected final int HealthRecover = 9;
    protected final int AttackDamage = 15;

    // 점수
    protected int numLinesRemoved;
    
    // 타이머
    protected Timer timer;

    // 보드판
    protected Tetrominoes[][] board;

    // 현재 떨어지는 도형 / 다음에 떨어질 도형
    protected Shape curPiece, nextPiece;

    // 상대편 보드
    protected Board opponent;

    protected Board(Tetris parent) {
        setPreferredSize(new Dimension(PanelWidth, PanelHeight));

        this.parent = parent;

        board = new Tetrominoes[BoardHeight][BoardWidth];

        curPiece = new Shape(); // 생성자에서 객체 생성 (합성 관계)
        nextPiece = new Shape();

        isFallingFinished = true;
        numLinesRemoved = 0;
        clearBoard();

        nextPiece.setRandomShape();
    }

    public void setOpponent(Board opponent) {
        this.opponent = opponent;
    }

    public void start(){
        isStarted = true;
        timer.start();
    }

    public void gameOver() {
        Board player = this instanceof BoardPlayer ? this : opponent;

        TetrisGameManager manager = player.parent.gameManager();

        if(manager.opponentIsComputer()) { // AI 대전이면서, 플레이어의 점수만 갱신
            MaxScorePanel maxScorePanel = manager.getMaxScorePanel();

            int prevMaxScore = maxScorePanel.getMaxScore();
            
            if(player.numLinesRemoved > prevMaxScore)
                maxScorePanel.FileWriter(player.numLinesRemoved);
        }

        opponent.isStarted = false;
        opponent.timer.stop();

        isStarted = false;
        timer.stop();

        manager.gameOverDialog().setVisible(true);
    }

    private void clearBoard() { // 보드 클리어
        for (int i = 0; i < BoardHeight; ++i) {
            for(int j = 0; j < BoardWidth; ++j) {
                board[i][j] = Tetrominoes.NoShape;
            }
        }
    }

    public boolean newPiece() { // 새로운 떨어지는 블록 생성
        curPiece = nextPiece.copy();

        int initPosX = BoardWidth / 2;
        int initPosY = BoardHeight - 2 + curPiece.minY();

        if (!tryMove(curPiece, initPosX, initPosY))
            return false;

        move(curPiece, initPosX, initPosY);

        nextPiece.setRandomShape();
        parent.getBlockPreview().setNextPiece(nextPiece);

        return true;
    }

    public void bombBlock() { // 폭탄 범위 내 블록 제거
        int x = curPiece.curX();
        int y = curPiece.curY();

        for(int i = y + 1; i >= y - 1; i--){
            for(int j = x - 1; j <= x + 1; j++){
                if(i < 0 || i >= BoardHeight || j < 0 || j >= BoardWidth)
                    continue;
                if(board[i][j] != Tetrominoes.NoShape)
                    board[i][j] = Tetrominoes.NoShape;
            }
        }
    }

    // -------------------------------- get 메소드 --------------------------------

    public int panelWidth(){
        return PanelWidth;
    }
    public int panelHeight(){
        return PanelHeight;
    }

    public int width(){
        return BoardWidth;
    }
    public int height(){
        return BoardHeight;
    }

    public Timer getTimer(){
        return timer;
    }

    public boolean isStarted(){
        return isStarted;
    }

    public Shape getNextPiece() { // 다음에 떨어질 도형
        return nextPiece;
    }
    public Shape getCurPiece() { // 현재 떨어지고 있는 도형
        return curPiece;
    }
    
    private int squareWidth() { // 블록 한칸(1 x 1) 가로 길이
        return getPreferredSize().width / BoardWidth;
    }
    private int squareHeight() { // 블록 한칸(1 x 1) 세로 길이
        return getPreferredSize().height / BoardHeight;
    }
    
    private Tetrominoes shapeAt(int x, int y) { // (x, y)에 있는 블럭의 Tetrominoes 타입
        return board[y][x];
    }

    // -------------------------------- 블록 이동, 점수 획득, 상대방 공격 --------------------------------

    public void move(Shape newPiece, int newX, int newY) { // 실제로 이동
        curPiece = newPiece;
        curPiece.moveTo(newX, newY);
        repaint();
    }
    public boolean tryMove(Shape newPiece, int newX, int newY) { // 이동 가능 여부 체크
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != Tetrominoes.NoShape)
                return false;
        }
        return true;
    }
    public void oneLineDown() { // curPiece 한줄 드롭
        if (!tryMove(curPiece, curPiece.curX(), curPiece.curY() - 1))
            pieceDropped();
        else
            move(curPiece, curPiece.curX(), curPiece.curY() - 1);
    }
    public void dropDown() { // curPiece 한번에 드롭
        int newY = curPiece.curY();
        while (newY > 0) {
            if (!tryMove(curPiece, curPiece.curX(), --newY))
                break;
            move(curPiece, curPiece.curX(), newY);
        }
        pieceDropped();
    }
    private void pieceDropped() { // 블록이 완전히 떨어지면, 해당 블록을 board에 그리는 식
        for (int i = 0; i < 4; ++i) {
            int x = curPiece.curX() + curPiece.x(i);
            int y = curPiece.curY() - curPiece.y(i);
            board[y][x] = curPiece.getShape();
        }

        if(curPiece.getShape() == Tetrominoes.BombBlock) {
            bombBlock();
        }

        removeFullLines();
    }
    private void removeFullLines() { // 한 줄 제거 가능 여부 탐색(점수 획득)
        curPiece.setShape(Tetrominoes.NoShape);

        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;
            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j){
                        board[k][j] = shapeAt(j, k + 1);
                        if(k == BoardHeight - 2 && shapeAt(j, k + 1) != Tetrominoes.NoShape)
                            board[k + 1][j] = Tetrominoes.NoShape;
                    }
                }
            }
        }

        isFallingFinished = true;
        boolean isBlockOvered = false;
        
        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            parent.getStatusBar().setText(String.valueOf(numLinesRemoved)); // 점수 갱신

            isBlockOvered = attackOpponent(numFullLines); // 지운 줄 수 만큼 상대방 공격
        }

        repaint();

        if(isBlockOvered)
            gameOver(); // 블록이 보드 높이를 넘어서면 게임 오버
    }

    private boolean attackOpponent(int count) {
        recoverHp(count); // 지운 줄 수 만큼 자신의 hp 회복
        int attackCount = decreaseOthertHp(count); // 지운 줄 수 만큼 상대 hp 감소

        // 상대 보드에 장애물 블록 생성
        if(attackCount == 0)
            return false;

        boolean isBlockOvered = false;

        Shape opponentPiece = opponent.curPiece;
        for(int i = 0; i < attackCount; i++){
            if(opponent.tryMove(opponentPiece, opponentPiece.curX(), opponentPiece.curY() + 1))
                opponent.move(opponentPiece, opponentPiece.curX(), opponentPiece.curY() + 1);
        }
    
        for (int i = BoardHeight - 1; i >= 0; i--) {
            for (int j = 0; j < BoardWidth; j++) {
                if(opponent.board[i][j] == Tetrominoes.NoShape)
                    continue;

                if (i + attackCount >= BoardHeight) {
                    isBlockOvered = true;
                } else {
                    opponent.board[i + attackCount][j] = opponent.board[i][j];
                }
                opponent.board[i][j] = Tetrominoes.NoShape;
            }
        }

        ThreadLocalRandom r = ThreadLocalRandom.current();
        int x = r.nextInt(BoardWidth);

        for (int i = 0; i < attackCount; i++) {
            for (int j = 0; j < BoardWidth; j++) {
                opponent.board[i][j] = Tetrominoes.LockBlock;
            }
            opponent.board[i][x] = Tetrominoes.NoShape;
        }

        return isBlockOvered;
    }
    private void recoverHp(int count){
        JProgressBar curHp = parent.getHealthBar();
        int increasedHp = curHp.getValue() + count * HealthRecover;
        if(increasedHp > 100)
            increasedHp = 100;
        curHp.setValue(increasedHp);
    }
    private int decreaseOthertHp(int count) {
        int attackCount = 0;

        JProgressBar otherHp = opponent.parent.getHealthBar();
        int decreasedHp = otherHp.getValue() - count * AttackDamage;
        if(decreasedHp < 0){
            attackCount += (-1 * decreasedHp) / AttackDamage;
            decreasedHp = 0;
        }
        otherHp.setValue(decreasedHp);

        return attackCount;
    }

    // -------------------------------- 블록 페인트 --------------------------------

    public void paint(Graphics g) {
        super.paint(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        // 배치된 블록 paint
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
                if(shape != Tetrominoes.NoShape)
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
            }
        }

        // 떨어지는 블록 paint
        if (curPiece.getShape() != Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curPiece.curX() + curPiece.x(i);
                int y = curPiece.curY() - curPiece.y(i);
                drawSquare(g, x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), curPiece.getShape());
            }
        }

        // 블록이 떨어질 위치 표시
        if(curPiece.getShape() == Tetrominoes.NoShape)
            return;

        int nX = curPiece.curX();
        int nY = curPiece.curY();
        while(nY >= 0){
            if(!tryMove(curPiece, nX, nY))
                break;
            nY--;
        }
        nY++;

        if(curPiece.getShape() == Tetrominoes.BombBlock) { // 1. 폭탄이 터지는 범위 표시
            for(int i = nY - 1; i <= nY + 1; i++){
                for(int j = nX - 1; j <= nX + 1; j++){
                    if(i < 0 || i >= BoardHeight || j < 0 || j >= BoardWidth)
                        continue;
                    drawSquare(g, j * squareWidth(), boardTop + (BoardHeight - i - 1) * squareHeight(), Tetrominoes.NoShape);
                }
            }
        } else { // 2. 블록이 떨어질 위치 표시
            for(int i = 0; i < 4; i++){
                int x = nX + curPiece.x(i);
                int y = nY - curPiece.y(i);
                drawSquare(g, x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), Tetrominoes.NoShape);
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
        BlockImage block = new BlockImage(shape);

        int imageSize = squareWidth(); // 이미지 크기를 블록 크기에 맞게 조정합니다.
        
        if(shape == Tetrominoes.BombBlock) {
            imageSize = 30;
            g.drawImage(block.getImage(), x - 2, y - 8, imageSize, imageSize, null);
        }
        else if(shape == Tetrominoes.NoShape){
            Graphics2D g2d = (Graphics2D) g;
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaComposite);
            g2d.drawImage(block.getImage(), x, y - 2, imageSize, imageSize, null);
            g2d.setComposite(AlphaComposite.SrcOver);
        }
        else
            g.drawImage(block.getImage(), x, y - 2, imageSize, imageSize, null);
    }
}
