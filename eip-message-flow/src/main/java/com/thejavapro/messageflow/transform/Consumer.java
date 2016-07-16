package com.thejavapro.messageflow.transform;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.interfaces.ITransformTask;

class Consumer<I, O> implements Callable<Message<I>> {

	private static final Logger LOGGER = Logger.getLogger(Consumer.class);
	
	private final BlockingQueue<Message<I>> inputQueue;
	private final BlockingQueue<Message<O>> outputQueue;
	private final ITransformTask<I, O> task;

	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<O>> outputQueue, ITranformTaskFactory<I, O> taskFactory) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
		this.task = taskFactory.create();
	}

	public Consumer(BlockingQueue<Message<I>> inputQueue, BlockingQueue<Message<O>> outputQueue, ITransformTask<I, O> task) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
		this.task = task;
	}
	
	public Message<I> call() throws Exception {
		
		while(true) {
			Message<I> t = inputQueue.take();			
			Message<O> o = task.doTask(t);
			outputQueue.put(o);
			
			if (t.isPoisonPill()){
				return t;
			}
		}
	}

}
