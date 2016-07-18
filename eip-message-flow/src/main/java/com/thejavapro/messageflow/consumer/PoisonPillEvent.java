package com.thejavapro.messageflow.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.interfaces.IPoisonPillEvent;

public class PoisonPillEvent implements IPoisonPillEvent {

	private static final Logger LOGGER = Logger.getLogger(PoisonPillEvent.class);
			
	private List<Thread> consumerThreads = new ArrayList<Thread>();

	public void addConsumerThread(Thread thread) {
		
		consumerThreads.add(thread);
	}

	public void interruptOtherConsumers(Thread callingThread) {
		
		for(Thread t : consumerThreads) {
			if (t != callingThread) {
				t.interrupt();
			}
		}
	} 	
}
