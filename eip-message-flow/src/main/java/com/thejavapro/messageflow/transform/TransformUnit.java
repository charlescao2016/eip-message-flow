package com.thejavapro.messageflow.transform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;

public class TransformUnit<I, O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(TransformUnit.class);

	private final int consumerSize;
	private final BlockingQueue<Message<I>> inputQueue;
	private final BlockingQueue<Message<O>> outputQueue;
	private final ExecutorService consumerPool;
	private ExecutorCompletionService<Boolean> completionService; 
	private IProcessingUnit<O, ?> nextUnit = null;

	public TransformUnit(int consumerSize, ITranformTaskFactory<I, O> taskFactory, int inputQueueSize) {

		this(consumerSize, taskFactory, inputQueueSize, (BlockingQueue<Message<O>>) null);
	}

	public TransformUnit(int consumerSize, ITranformTaskFactory<I, O> taskFactory, int inputQueueSize,
			IProcessingUnit<O, ?> nextUnit) {

		this(consumerSize, taskFactory, inputQueueSize, nextUnit.getInputQueue());
		this.nextUnit = nextUnit;
	}

	public TransformUnit(int consumerSize, ITranformTaskFactory<I, O> taskFactory, int inputQueueSize,
			BlockingQueue<Message<O>> outputQueue) {

		this.consumerSize = consumerSize;
		this.inputQueue = new ArrayBlockingQueue<Message<I>>(inputQueueSize);
		this.outputQueue = outputQueue;

		consumerPool = Executors.newFixedThreadPool(consumerSize);
		completionService = new ExecutorCompletionService<Boolean>(consumerPool);
		for (int i = 0; i < consumerSize; i++) {
			completionService.submit(new Consumer<I, O>(inputQueue, outputQueue, taskFactory));
		}
	}

	public BlockingQueue<Message<I>> getInputQueue() {

		return inputQueue;
	}

	public void put(Message<I> message) throws InterruptedException {

		inputQueue.put(message);
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

	public void gracefullyShutdown(boolean allNext) throws InterruptedException, ExecutionException {

		put(new Message<I>("", null));

		for(int i = 0; i < consumerSize - 1; i++) {
			Future<Boolean> future = completionService.take();
			future.get();
			put(new Message<I>("", null));
		}

		if (allNext && nextUnit != null) {
			nextUnit.shutdown(allNext);
		}
	}

}
