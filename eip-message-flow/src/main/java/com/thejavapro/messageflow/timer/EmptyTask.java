package com.thejavapro.messageflow.timer;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

class EmptyTask extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger(EmptyTask.class);
			
	private AtomicInteger count;
	
	public EmptyTask(int count) {
		this.count = new AtomicInteger(count);
	}
	
	@Override
	public void run() {
		LOGGER.debug("Count: " + count);
		count.decrementAndGet();
	}

	public int getCurrentCounter() {
		return count.get();
	}

	public void setCounter(int count) {
		this.count.set(count);
	}
}
