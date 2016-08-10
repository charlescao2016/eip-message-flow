package com.thejavapro.messageflow.test;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

	public static void main(String[] args) {
        
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 10; //Integer.parseInt(args[0]);
            public void run() {
                System.out.println(i--);
                if (i< 0)
                    timer.cancel();
            }
        }, 0, 1000);
    }
}
