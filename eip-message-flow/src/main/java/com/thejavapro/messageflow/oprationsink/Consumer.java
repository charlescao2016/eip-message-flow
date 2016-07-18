package com.thejavapro.messageflow.oprationsink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IOperationTask;
import com.thejavapro.messageflow.interfaces.IOperationTaskFactory;
import com.thejavapro.messageflow.interfaces.IPoisonPillEvent;

public class Consumer<I> implements Callable<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);
	
	private final BlockingQueue<Message<I>> inputQueue;
	private final IOperationTask<I> task;
	private IPoisonPillEvent poisonPillEvent;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, IOperationTaskFactory<I> taskFactory, IPoisonPillEvent poisonPillEvent) {
		
		this(inputQueue, taskFactory.create(), poisonPillEvent);
	}
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, IOperationTask<I> task, IPoisonPillEvent poisonPillEvent) {
		
		this.inputQueue = inputQueue;
		this.task = task;
		this.poisonPillEvent = poisonPillEvent;
	}

	public Boolean call() throws Exception {
		
		poisonPillEvent.addConsumerThread(Thread.currentThread());
	
		while(true) {
			
			Message<I> t;
			try {
				t = inputQueue.take();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}
			
			task.doTask(t);
			
			if (t.isPoisonPill()){
				poisonPillEvent.interruptOtherConsumers(Thread.currentThread());
				return true;
			}
		}
		
	}
}
