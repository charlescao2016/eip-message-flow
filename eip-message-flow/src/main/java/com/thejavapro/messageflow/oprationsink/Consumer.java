package com.thejavapro.messageflow.oprationsink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IOperationTask;
import com.thejavapro.messageflow.interfaces.IOperationTaskFactory;

public class Consumer<I> implements Callable<Message<I>> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);
	
	private final BlockingQueue<Message<I>> inputQueue;
	private final IOperationTask<I> task;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, IOperationTaskFactory<I> taskFactory) {
		
		this.inputQueue = inputQueue;
		this.task = taskFactory.create();
	}
	
	public Message<I> call() throws Exception {
		
		while(true) {
			Message<I> t = inputQueue.take();			
			task.doTask(t);
			
			if (t.isPoisonPill()){
				return t;
			}
		}
	}

}
