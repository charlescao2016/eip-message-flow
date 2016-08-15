package com.thejavapro.messageflow.concurrent;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.timer.CountdownTimer;

public class EventMonitor {

	private static final Logger LOGGER = Logger.getLogger(EventMonitor.class);
	private static Map<String, EventMonitor> EventMonitors = new HashMap<String, EventMonitor>();		
	
	private Object lock = new Object();
	private boolean signalled = false;
	private CountdownTimer timer;
	
	public synchronized static EventMonitor Add(String id, boolean signalled, int count, long periodMilliSec) {
		
		if (EventMonitors.containsKey(id)) {
			throw new RuntimeException("Already has a EventMonitor with ID: " + id);
		}
		
		EventMonitors.put(id, new EventMonitor(signalled, count, periodMilliSec));
				
		return EventMonitors.get(id);
	}

	public synchronized static EventMonitor Get(String id) {
		
		return EventMonitors.get(id);
	}
	
	private EventMonitor(boolean signalled, int count, long periodMilliSec) {
		this.signalled = signalled;
		this.timer = new CountdownTimer(count, periodMilliSec);
		this.timer.start();
	}
	
	public void doNotify() {
		
		synchronized (lock) {
			signalled = true;
			lock.notify();
		}
	}

	public void doWait() throws InterruptedException {

		synchronized (lock) {
			while (!signalled) {
				lock.wait();
			}
			signalled = false;
		}
	}

	public boolean doWait(long timeout) throws InterruptedException {

		synchronized (lock) {
			timer.reset();
			boolean isTimeout = false;
			while (!signalled && !isTimeout) {
				lock.wait(timeout);
				isTimeout = timer.getRemainCount() > 0 ? false : true; 
			}
			signalled = false;
			
			return isTimeout;
		}
	}

	public void doWait(long timeout, int nanos) throws InterruptedException {

		synchronized (lock) {
			while (!signalled) {
				lock.wait(timeout, nanos);
			}
			signalled = false;
		}
	}

}
