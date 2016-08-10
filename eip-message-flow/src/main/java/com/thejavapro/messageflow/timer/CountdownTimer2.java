package com.thejavapro.messageflow.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CountdownTimer2 {

	private final ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);
	
	public void startCountdown() {		
		
		scheduledPool.scheduleAtFixedRate(new TickerTask(), 0, 1, TimeUnit.SECONDS);
	}
	
	public int getCurrentcount() {
		return task.getCurrentCount();
	}
}
