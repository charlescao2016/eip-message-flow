package com.thejavapro.messageflow.oprationsink;

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
import com.thejavapro.messageflow.interfaces.IOperationTaskFactory;
import com.thejavapro.messageflow.interfaces.IPoisonPillEvent;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class SinkUnit<I> implements IProcessingUnit<I, String> {

	private static final Logger LOGGER = Logger.getLogger(SinkUnit.class);

	private final BlockingQueue<Message<I>> inputQueue;
	private final ExecutorService consumerPool;
	private List<Future<Boolean>> consumerPoolFutures = new ArrayList<Future<Boolean>>();
	
	public SinkUnit(int consumerSize, IOperationTaskFactory<I> taskFactory, int inputQueueSize) {
		
		this.inputQueue = new ArrayBlockingQueue<Message<I>>(inputQueueSize);
				
		IPoisonPillEvent poisonEvent = new PoisonPillEvent();
		consumerPool = Executors.newFixedThreadPool(consumerSize);
		for(int i = 0; i < consumerSize; i++) {
			Future<Boolean> future = consumerPool.submit(new Consumer<I>(inputQueue, taskFactory, poisonEvent));
			consumerPoolFutures.add(future);
		}
	}

	public BlockingQueue<Message<I>> getInputQueue() {
		
		return inputQueue;
	}

	public void put(Message<I> message) throws InterruptedException {
		
		inputQueue.put(message);
	}

	public Message<String> take() {
		
		return null;
	}

	public void awaitPosionPill(boolean forAll) throws InterruptedException, ExecutionException {
		
		for(Future<Boolean> future : consumerPoolFutures) {
			future.get();
		}
	}

	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException {
		
		consumerPool.awaitTermination(timeout, unit);
	}
	
	public void shutdown(boolean all) {
		
		consumerPool.shutdown();		
	}

}
