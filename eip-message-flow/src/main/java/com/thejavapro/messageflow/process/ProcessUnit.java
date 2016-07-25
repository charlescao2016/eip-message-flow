package com.thejavapro.messageflow.process;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;


public class ProcessUnit<I, O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(ProcessUnit.class);

	private final IProcessingTaskFactory<I, O> taskFactory;
	private final int consumerSize;
	private final BlockingQueue<Message<I>> inputQueue;
	
	private ExecutorService consumerPool;
	private BlockingQueue<Message<O>> outputQueue = null;
	private IProcessingUnit<O, ?> nextUnit = null;

	private ExecutorCompletionService<Boolean> completionService;
	

	public ProcessUnit(int consumerSize, IProcessingTaskFactory<I, O> taskFactory, int inputQueueSize) {

		this.taskFactory = taskFactory;
		this.consumerSize = consumerSize;
		this.inputQueue = new ArrayBlockingQueue<Message<I>>(inputQueueSize);
	}

	@Override
	public void start() {
		
		consumerPool = Executors.newFixedThreadPool(consumerSize);
		completionService = new ExecutorCompletionService<Boolean>(consumerPool);
		for (int i = 0; i < consumerSize; i++) {
			completionService.submit(new Consumer<I, O>(inputQueue, outputQueue, taskFactory));
		}
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<O>> outputQueue) {
		
		this.outputQueue = outputQueue;
	}

	@Override
	public void addOutputUnit(IProcessingUnit<O, ?> next) {
		
		this.nextUnit = next;
		this.outputQueue = next.getInputQueue();
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
	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException {

		consumerPool.awaitTermination(timeout, unit);

		if (forAll && nextUnit != null) {
			nextUnit.awaitTermination(timeout, unit, forAll);
		}
	}

	@Override
	public void shutdown(boolean allNext) {
		
		consumerPool.shutdown();

		if (allNext && nextUnit != null) {
			nextUnit.shutdown(allNext);
		}
	}

	@Override
	public List<Runnable> shutdownNow() {
		
		return consumerPool.shutdownNow();
	}

	@Override
	public void shutdownTasks(boolean allNext) throws InterruptedException, ExecutionException {

		put(new Message<I>("", null));

		for(int i = 0; i < consumerSize; i++) {
			Future<Boolean> future = completionService.take();
			future.get();
			if (i < consumerSize - 1) {
				put(new Message<I>("", null));
			}
		}
		
		if (allNext && nextUnit != null) {
			nextUnit.shutdownTasks(allNext);
		}	
	}

	@Override
	public boolean shutdownTasks(boolean allNext, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
		   public Object call() throws InterruptedException, ExecutionException {
		      shutdownTasks(allNext);
		      return null;
		   }
		};
		
		Future<Object> future = executor.submit(task);
		try {
		   Object result = future.get(timeout, unit); 
		} catch (TimeoutException ex) {
		   return false;
		} finally {
		   executor.shutdownNow();
		}
		
		return true;
	}
}
