package kr.ac.jbnu.se.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BoardPlayer extends Board implements ActionListener {

    public static int moveDelay = 500;

    private final int BombCutlineScore = 10;
    private int cutlineCheck;

    public BoardPlayer(Tetris parent) {
        super(parent);
        //TODO Auto-generated constructor stub 
        cutlineCheck = BombCutlineScore;
        timer = new Timer(moveDelay, this);        
        start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            
            if(numLinesRemoved >= cutlineCheck) { // 점수 기준 넘어가면 폭탄 획득
                parent.acquireBomb();
                cutlineCheck += BombCutlineScore;
            }

            if (!newPiece())
                gameOver();

        } else {
            oneLineDown();
        }
    }
}
