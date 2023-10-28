package kr.ac.jbnu.se.tetris;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BlockPreview extends JPanel {

    protected final int PanelWidth = 60, PanelHeight = 80, width = 3, height = 4;

    private Tetrominoes[][] nextPiece;

    private Shape piece;

    BlockPreview(Board board) {
        setPreferredSize(new Dimension(PanelWidth, PanelHeight));
        nextPiece = new Tetrominoes[height][width];
        initNextPiece();
    }
    
    public int panelWidth(){
        return PanelWidth;
    }
    public int panelHeight(){
        return PanelHeight;
    }

    private int squareWidth(){
        return (int) PanelWidth / width;
    }
    private int squareHeight(){
        return (int) PanelHeight / height;
    }

    private void initNextPiece(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                nextPiece[i][j] = Tetrominoes.NoShape;
            }
        }
    }

    public void setNextPiece(Shape piece){
        initNextPiece();
        this.piece = piece;
        for(int i = 0; i < 4; i++){
            int x = 1 + piece.x(i);
            int y = 1 + piece.y(i);
            nextPiece[y][x] = piece.getShape();
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);

        int boardTop = PanelHeight - height * squareHeight();

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(nextPiece[i][j] != Tetrominoes.NoShape){
                    drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), piece.getShape());
                }
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
        BlockImage block = new BlockImage(shape);
        
        int imageSize = squareWidth(); // 이미지 크기를 블록 크기에 맞게 조정합니다.

        if(shape == Tetrominoes.BombBlock) {
            imageSize = 30;
            g.drawImage(block.getImage(), x - 2, y - 6, imageSize, imageSize, null);
        }
        else
            g.drawImage(block.getImage(), x, y, imageSize, imageSize, null);
    }
}
