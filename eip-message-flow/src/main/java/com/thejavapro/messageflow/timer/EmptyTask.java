package com.thejavapro.messageflow.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

class EmptyTask extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger(EmptyTask.class);
			
	private AtomicInteger count;
	private final Timer timer;
	
	public EmptyTask(int count) {
		this(count, null); 
	}

	public EmptyTask(int count, Timer timer) {
		this.count = new AtomicInteger(count);
		this.timer = timer;
	}
	
	@Override
	public void run() {
		
		LOGGER.debug("Count: " + count);
		
		int current = count.decrementAndGet();
		if (current < 0 && timer != null) {
			timer.cancel();
		}
	}

	public int getCurrentCounter() {
		return count.get();
	}

	public void setCounter(int count) {
		this.count.set(count);
	}
}
