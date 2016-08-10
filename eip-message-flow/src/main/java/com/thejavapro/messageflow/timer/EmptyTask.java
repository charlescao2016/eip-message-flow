package com.thejavapro.messageflow.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

class EmptyTask extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger(EmptyTask.class);
			
	private AtomicInteger count;
	private final Timer timer;
	
	public EmptyTask(int count, Timer timer) {
		this.count = new AtomicInteger(count);
		this.timer = timer;
	}
	
	@Override
	public void run() {
		
		LOGGER.debug("Count: " + count);
		
		int current = count.decrementAndGet();
		if (current < 0) {
			timer.cancel();
		}
	}

	public int getCurrentCount() {
		return count.get();
	}
}
