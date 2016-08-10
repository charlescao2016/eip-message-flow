package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.EventMonitor;
import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.UnitConnector;

class Consumer<I> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);

	private final BlockingQueue<Message<I>> inputQueue;
	private final UnitConnector<I> connector;
	private long startSequence;
	private EventMonitor inputMonitor;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, UnitConnector<I> connector, long startSequence, EventMonitor inputMonitor) {
		this.inputQueue = inputQueue;
		this.connector = connector;
		this.startSequence = startSequence;
		this.inputMonitor = inputMonitor;
	}

	@Override
	public Boolean call() throws Exception {

		while (true) {
			Message<I> t;
			try {
				while(true) {
					t = inputQueue.peek();
					if ((t == null || (t.getSequenceNumber() > startSequence) && !t.isPoisonPill())) {
						inputMonitor.doWait();	
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
