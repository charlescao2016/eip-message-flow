package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.EventMonitor;
import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.UnitConnector;
import com.thejavapro.messageflow.timer.CountdownTimer;

class Consumer<I> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);

	private final BlockingQueue<Message<I>> inputQueue;
	private final UnitConnector<I> connector;
	private long startSequence;
	private EventMonitor inputMonitor;
	private CountdownTimer timer;
	
	private int timerCounter = 50;
	private long timerPeriodMilliSec = 100;

	public Consumer(BlockingQueue<Message<I>> inputQueue, UnitConnector<I> connector, long startSequence, EventMonitor inputMonitor) {
		this.inputQueue = inputQueue;
		this.connector = connector;
		this.startSequence = startSequence;
		this.inputMonitor = inputMonitor;
		this.timer = new CountdownTimer(timerCounter, timerPeriodMilliSec);
	}

	@Override
	public Boolean call() throws Exception {

		timer.startCountdown();
		
		while (true) {
			Message<I> t;
			try {
				timer.resetCounter(timerCounter);
				while(true) {
					t = inputQueue.peek();
					int count = timer.getCurrentcount();
					if ((t == null || (t.getSequenceNumber() > startSequence) && count > 0 && !t.isPoisonPill())) {
						inputMonitor.doWait(count * timerPeriodMilliSec);	
					} else {
						break;
					}
				}
				
				t = inputQueue.take();
				if (t.getSequenceNumber() == startSequence) {
					startSequence++;
				} else if (t.getSequenceNumber() > startSequence) {
					startSequence = t.getSequenceNumber() + 1; 
				}
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}

			if (t.isPoisonPill()) {
				LOGGER.debug("poison pill.");
				return true;
			}

			connector.put(t);
		}
	}
}
