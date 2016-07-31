package com.thejavapro.messageflow.process;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.TaskManager;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITaskManager;


public class ProcessUnit<I, O> extends TaskManager<O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(ProcessUnit.class);

	private final IProcessingTaskFactory<I, O> taskFactory;
	private final int consumerSize;
	private final BlockingQueue<Message<I>> inputQueue;
	
	private BlockingQueue<Message<O>> outputQueue = null;
	private IProcessingUnit<O, ?> nextUnit = null;
	
	public ProcessUnit(int consumerSize, IProcessingTaskFactory<I, O> taskFactory, int inputQueueSize) {

		this.taskFactory = taskFactory;
		this.consumerSize = consumerSize;
		this.inputQueue = new ArrayBlockingQueue<Message<I>>(inputQueueSize);
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<O>> outputQueue) {
		
		this.outputQueue = outputQueue;
	}

	@Override
	public IProcessingUnit<O, ?> addOutputUnit(IProcessingUnit<O, ?> next) {
		
		this.nextUnit = next;
		this.outputQueue = next.getInputQueue();
		
		return next;
	}

	@Override
	public BlockingQueue<Message<I>> getInputQueue() {

		return inputQueue;
	}

	@Override
	public void put(Message<I> message) throws InterruptedException {

		inputQueue.put(message);
	}

	@Override
	public ITaskManager getITaskManager() {		
		return this;
	}

	@Override
	protected IProcessingUnit<O, ?> getNextUnit() {
		return nextUnit;
	}

	@Override
	protected void addPoisonPill() throws InterruptedException {
		put(new Message<I>("", null));
	}

	@Override
	protected int getConsumerSize() {
		return consumerSize;
	}

	@Override
	protected Callable<Boolean> createConsumer() {
		return new Consumer<I, O>(inputQueue, outputQueue, taskFactory);
	}
}
