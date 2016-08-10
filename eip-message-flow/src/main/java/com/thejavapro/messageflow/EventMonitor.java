package com.thejavapro.messageflow;

public class EventMonitor {

	private Object lock = new Object();
	boolean signalled = false;
	
	public EventMonitor() {
	}
	
	public EventMonitor(boolean signalled) {
		this.signalled = signalled;
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

	public void doWait(long timeout) throws InterruptedException {

		synchronized (lock) {
			while (!signalled) {
				lock.wait(timeout);
			}
			signalled = false;
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
