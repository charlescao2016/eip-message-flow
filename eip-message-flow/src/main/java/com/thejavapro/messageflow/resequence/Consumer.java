package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.BoundedPriorityBlockingQueue;
import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.UnitConnector;

class Consumer<I> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);

	private final BoundedPriorityBlockingQueue<Message<I>> inputQueue;
	private final UnitConnector<I> connector;
	private long startSequence;

	public Consumer(BoundedPriorityBlockingQueue<Message<I>> inputQueue, UnitConnector<I> connector, long startSequence) {
		this.inputQueue = inputQueue;
		this.connector = connector;
		this.startSequence = startSequence;
	}

	@Override
	public Boolean call() throws Exception {
		
		while (true) {
			Message<I> t;
			try {
				boolean isTimeout = false;
				while(true) {
					t = inputQueue.peek();
					if ((t == null || (t.getSequenceNumber() > startSequence) && !isTimeout && !t.isPoisonPill())) {
						isTimeout = inputQueue.getMaxBuffeSizerMonitor().doWait(1000 * 5);	
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
			
			LOGGER.debug("Sequence: " + startSequence + " - " + t.getSequenceNumber());
			connector.put(t);
		}
	}
}
