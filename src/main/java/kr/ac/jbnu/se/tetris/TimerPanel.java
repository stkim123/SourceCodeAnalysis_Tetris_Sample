package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerPanel extends JPanel {
    
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private int second = 0, minute = 0;
    private final JLabel timernum;

    public TimerPanel() {
        timernum = new JLabel();
        add(timernum, BorderLayout.CENTER);
    }

    public void startTimer() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                timernum.setText(minute + "m " + second + "s");
                repaint();
                second++;
                if(second >= 60) {
                    second -= 60;
                    minute++;
                }
                if(minute > 10) scheduler.shutdown();
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }
}
