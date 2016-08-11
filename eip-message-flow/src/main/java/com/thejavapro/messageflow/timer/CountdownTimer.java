package com.thejavapro.messageflow.timer;

import java.util.Timer;

public class CountdownTimer {

	private final Timer timer = new Timer();
	private final long periodMilliSec;
	private final EmptyTask task;
	
	public CountdownTimer(int count, long periodMilliSec) {
		this.periodMilliSec = periodMilliSec;
		this.task = new EmptyTask(count);
	}
	
	public void startCountdown() {		
		timer.scheduleAtFixedRate(task, 0, periodMilliSec);
	}
	
	public int getCurrentcount() {
		return task.getCurrentCounter();
	}
	
	public void resetCounter(int count) {
		task.setCounter(count);
	}
}
