package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;

public class Consumer<I> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class); 
	
	private final BlockingQueue<Message<I>> inputQueue;
	private final BlockingQueue<Message<I>> outputQueue;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<I>> outputQueue) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
	}

	@Override
	public Boolean call() throws Exception {
		
		while(true) {
			
			Message<I> t;
			try {
				t = inputQueue.take();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}
			
			if (t.getBody() == null) {
				System.out.println("poison pill.");
				return true;
			}
			
			if (outputQueue != null) {
				outputQueue.put(t);
			}
		}
	}
}
