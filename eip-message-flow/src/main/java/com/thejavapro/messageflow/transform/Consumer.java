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
	private IPoisonPillEvent poisonPillEvent;
	
	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<O>> outputQueue, ITranformTaskFactory<I, O> taskFactory, IPoisonPillEvent poisonPillEvent) {
		
		this(inputQueue, outputQueue, taskFactory.create(), poisonPillEvent);
	}

	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<O>> outputQueue, ITransformTask<I, O> task, IPoisonPillEvent poisonPillEvent) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
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
				//Thread.currentThread().interrupt();
				return false;
			}
			
			Message<O> o = task.doTask(t);
			if (outputQueue != null) {
				outputQueue.put(o);
			}
			
			if (t.isPoisonPill()){
				poisonPillEvent.interruptOtherConsumers(Thread.currentThread());
				return true;
			}
		}
	}
}
