package kr.ac.jbnu.se.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BoardAI extends Board implements ActionListener {

    public static int moveDelay = 100;

    private TetrisAI computer;

    private String bestRoute = "";

    private int index = 0;

    public BoardAI(Tetris parent) {
        super(parent);
        computer = new TetrisAI(this);

        bestRoute = computer.findBestRoute();

        timer = new Timer(moveDelay, this);
        
        start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            index = 0;
            if (!newPiece())
                gameOver();
            else 
                bestRoute = computer.findBestRoute();
        } 
        else
            moveToBestRoute(index++);
    }

    private void moveToBestRoute(int i) {
        if(i >= bestRoute.length()){
            dropDown();
            return;
        }

        if(bestRoute.charAt(i) == '0' && tryMove(curPiece, curPiece.curX() - 1, curPiece.curY()))
            move(curPiece, curPiece.curX() - 1, curPiece.curY());
        else if(bestRoute.charAt(i) == '1' && tryMove(curPiece, curPiece.curX() + 1, curPiece.curY()))
            move(curPiece, curPiece.curX() + 1, curPiece.curY());
        else if(bestRoute.charAt(i) == '2' && tryMove(curPiece, curPiece.curX(), curPiece.curY() - 1))
            move(curPiece, curPiece.curX(), curPiece.curY() - 1);
        else if(bestRoute.charAt(i) == '3' && tryMove(curPiece.rotateRight(), curPiece.curX(), curPiece.curY()))
            move(curPiece.rotateRight(), curPiece.curX(), curPiece.curY());    
    }
}
