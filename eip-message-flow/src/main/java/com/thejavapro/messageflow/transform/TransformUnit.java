package com.thejavapro.messageflow.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.consumer.PoisonPillEvent;
import com.thejavapro.messageflow.interfaces.IPoisonPillEvent;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;

public class TransformUnit<I, O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(TransformUnit.class);

	private final BlockingQueue<Message<I>> inputQueue;
	private final BlockingQueue<Message<O>> outputQueue;
	private final ExecutorService consumerPool;
	private List<Future<Boolean>> consumerPoolFutures = new ArrayList<Future<Boolean>>();
	private IProcessingUnit<O, ?> nextUnit = null;  

	public TransformUnit(int consumerSize, ITranformTaskFactory<I, O> taskFactory, int inputQueueSize) {
		
		this(consumerSize, taskFactory, inputQueueSize, (BlockingQueue<Message<O>>)null);
	}

	public TransformUnit(int consumerSize, ITranformTaskFactory<I, O> taskFactory, int inputQueueSize, IProcessingUnit<O, ?> nextUnit) {
		
		this(consumerSize, taskFactory, inputQueueSize, nextUnit.getInputQueue());
		this.nextUnit = nextUnit;
	}

	public TransformUnit(int consumerSize, ITranformTaskFactory<I, O> taskFactory, int inputQueueSize, BlockingQueue<Message<O>> outputQueue) {
		
		this.inputQueue = new ArrayBlockingQueue<Message<I>>(inputQueueSize);
		this.outputQueue = outputQueue;
		
		IPoisonPillEvent poisonEvent = new PoisonPillEvent();
		consumerPool = Executors.newFixedThreadPool(consumerSize);
		for(int i = 0; i < consumerSize; i++) {
			Future<Boolean> future = consumerPool.submit(new Consumer<I, O>(inputQueue, outputQueue, taskFactory, poisonEvent));
			consumerPoolFutures.add(future);
		}
	}

	public BlockingQueue<Message<I>> getInputQueue() {
		
		return inputQueue;
	}

	public void put(Message<I> message) throws InterruptedException {
		
		inputQueue.put(message);
	}

	public Message<O> take() throws InterruptedException {
		
		if (outputQueue == null) {
			return null;
		}
		
		return outputQueue.take();
	}

	public void awaitPosionPill(boolean forAll) throws InterruptedException, ExecutionException {
		
		for(Future<Boolean> future : consumerPoolFutures) {
			future.get();
		}
		
		if (forAll && nextUnit != null) {
			nextUnit.awaitPosionPill(forAll);
		}
	}

	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException {
		
		consumerPool.awaitTermination(timeout, unit);
		
		if (forAll && nextUnit != null) {
			nextUnit.awaitTermination(timeout, unit, forAll);
		}
	}

	public void shutdown(boolean all) {
		
		consumerPool.shutdown();
		
		if (all && nextUnit != null) {
			nextUnit.shutdown(all);
		}
	}
	
}
