package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.TaskManager;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITaskManager;

public class ResequenceUnit<I> extends TaskManager<I> implements IProcessingUnit<I, I>{

	private static final Logger LOGGER = Logger.getLogger(ResequenceUnit.class);
			
	private final PriorityBlockingQueue<Message<I>> inputQueue;
	private final long timeout;
	private final TimeUnit unit;
	private final int maxBufferSzie;
	
	private BlockingQueue<Message<I>> outputQueue = null;
	private IProcessingUnit<I, ?> nextUnit = null;

	
	public ResequenceUnit(long timeout, TimeUnit unit, int maxBufferSzie) {
		
		this.timeout = timeout;
		this.unit = unit;
		this.maxBufferSzie = maxBufferSzie;
		
		this.inputQueue = new PriorityBlockingQueue<Message<I>>();
	}

	@Override
	public void put(Message<I> message) throws InterruptedException {

		inputQueue.put(message);
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<I>> outputQueue) {

		this.outputQueue = outputQueue;
	}

	@Override
	public IProcessingUnit<I, ?> addOutputUnit(IProcessingUnit<I, ?> next) {
		this.nextUnit = next;
		this.outputQueue = next.getInputQueue();
		
		return next;
	}

	@Override
	public BlockingQueue<Message<I>> getInputQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITaskManager getITaskManager() {
		return this;
	}

	@Override
	protected IProcessingUnit<I, ?> getNextUnit() {
		return nextUnit;
	}

	@Override
	protected void addPoisonPill() throws InterruptedException {
		put(new Message<I>("", null));
	}

	@Override
	protected int getConsumerSize() {
		return 1;
	}

	@Override
	protected Callable<Boolean> createConsumer() {
		return new Consumer<I>(inputQueue, outputQueue);
	}


}
