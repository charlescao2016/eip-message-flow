package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.UnitConnector;

class Consumer<I> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);

	private final BlockingQueue<Message<I>> inputQueue;
	private final UnitConnector<I> connector;
	private long startSequence;
	private Object lock = new Object();
	boolean signalled = false;

	public Consumer(BlockingQueue<Message<I>> inputQueue, UnitConnector<I> connector, long startSequence) {
		this.inputQueue = inputQueue;
		this.connector = connector;
		this.startSequence = startSequence;
		//this.lock = lock;
	}

	public void doNotify() {
		
		synchronized (lock) {
			signalled = true;
			lock.notify();
		}
	}

	private void doWait() throws InterruptedException {

		synchronized (lock) {
			while (!signalled) {
				lock.wait();
			}
			// clear signal and continue running.
			signalled = false;
		}
	}

	@Override
	public Boolean call() throws Exception {

		while (true) {

			Message<I> t;
			try {
				
				while(true) {
					t = inputQueue.peek();
					if ((t == null || (t.getSequenceNumber() != startSequence) && t.getBody() != null)) {
						doWait();	
					} else {
						break;
					}
				}
				
				t = inputQueue.take();
				startSequence++;
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}

			if (t.isPoisonPill()) {
				System.out.println("poison pill.");
				return true;
			}

			connector.put(t);
		}
	}
}
