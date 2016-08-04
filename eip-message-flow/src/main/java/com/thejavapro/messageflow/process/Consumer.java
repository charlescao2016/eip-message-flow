package com.thejavapro.messageflow.process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.UnitConnector;
import com.thejavapro.messageflow.interfaces.IProcessingTask;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;

class Consumer<I, O> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class); 
	
	private final BlockingQueue<Message<I>> inputQueue;
	private final UnitConnector<O> connector;
	
	private final IProcessingTask<I, O> task;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, UnitConnector<O> connector, IProcessingTaskFactory<I, O> taskFactory) {
		this(inputQueue, connector, taskFactory.create());
	}

	public Consumer(BlockingQueue<Message<I>> inputQueue, UnitConnector<O> connector, IProcessingTask<I, O> task) {
		this.inputQueue = inputQueue;
		this.connector = connector;
		this.task = task;
	}
	
	public Boolean call() throws Exception {
		
		//poisonPillEvent.addConsumerThread(Thread.currentThread());
		
		while(true) {
			
			Message<I> t;
			try {
				t = inputQueue.take();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}
			
			if (t.isPoisonPill()) {
				System.out.println("poison pill.");
				return true;
			}
			
			Message<O> o = task.doTask(t);
			connector.put(o);
		}
	}
}
