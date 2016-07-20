package com.thejavapro.messageflow.transform;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IPoisonPillEvent;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.interfaces.ITransformTask;

class Consumer<I, O> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class); 
	
	private final BlockingQueue<Message<I>> inputQueue;
	private final BlockingQueue<Message<O>> outputQueue;
	private final ITransformTask<I, O> task;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<O>> outputQueue, ITranformTaskFactory<I, O> taskFactory) {
		
		this(inputQueue, outputQueue, taskFactory.create());
	}

	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<O>> outputQueue, ITransformTask<I, O> task) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
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
			
			if (t.getBody() == null) {
				System.out.println("poison pill.");
				return true;
			}
			
			Message<O> o = task.doTask(t);
			if (outputQueue != null) {
				outputQueue.put(o);
			}
		}
	}
}
