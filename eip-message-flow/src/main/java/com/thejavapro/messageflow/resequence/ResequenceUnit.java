package com.thejavapro.messageflow.resequence;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.BoundedPriorityBlockingQueue;
import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.TaskManager;
import com.thejavapro.messageflow.UnitConnector;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITaskManager;

public class ResequenceUnit<I> extends TaskManager<I> implements IProcessingUnit<I, I>{

	private static final Logger LOGGER = Logger.getLogger(ResequenceUnit.class);
			
	private final BoundedPriorityBlockingQueue<Message<I>> inputQueue;
	private final long timeout;
	private final TimeUnit unit;
	private UnitConnector<I> connector = new UnitConnector<I>();
	private long startSequence;
	
	public ResequenceUnit(long timeout, TimeUnit unit, int maxBufferSzie, long startSequence) {
		
		this.timeout = timeout;
		this.unit = unit;
		this.startSequence = startSequence;
		
		Comparator<Message<I>> comparator = new MessageComparator<I>(); 
		this.inputQueue = new BoundedPriorityBlockingQueue<Message<I>>(maxBufferSzie, comparator);
	}

	@Override
	public void put(Message<I> message) throws InterruptedException {
		inputQueue.put(message);
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<I>> outputQueue) {
		connector.setOutputQueue(outputQueue);
	}

	@Override
	public IProcessingUnit<I, ?> addOutputUnit(IProcessingUnit<I, ?> next) {
		connector.setOutputUnit(next);		
		return next;
	}

	@Override
	public ITaskManager getITaskManager() {
		return this;
	}

	@Override
	protected IProcessingUnit<I, ?> getNextUnit() {
		return connector.getOutputUnit();
	}

	@Override
	protected void addPoisonPill() throws InterruptedException {
		put(Message.<I>CreatePoisonPill());
	}

	@Override
	protected int getConsumerSize() {
		return 1;
	}

	@Override
	protected Callable<Boolean> createConsumer() {
		return new Consumer<I>(inputQueue, connector, startSequence);
	}


}
