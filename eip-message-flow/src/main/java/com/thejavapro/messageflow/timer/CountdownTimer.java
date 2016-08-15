package com.thejavapro.messageflow.timer;

import java.util.Timer;

public class CountdownTimer {

	private final Timer timer = new Timer(); 
	private final long periodMilliSec;
	private final EmptyTask task;
	
	private int initCount;
	
	public CountdownTimer(int count, long periodMilliSec) {
		this.initCount = count;
		this.periodMilliSec = periodMilliSec;
		this.task = new EmptyTask(count);
	}
	
	public void start() {		
		timer.scheduleAtFixedRate(task, 0, periodMilliSec);
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public long getRemainTime() {
		return task.getCurrentCounter() * periodMilliSec;
	}

	public int getRemainCount() {
		return task.getCurrentCounter();
	}

	public void reset() {
		task.setCounter(initCount);
	}

	public void set(int count) {
		this.initCount = count;
		task.setCounter(count);
	}
}
