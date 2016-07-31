package com.thejavapro.messageflow;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITaskManager;

public abstract class TaskManager<I> implements ITaskManager {

	private ExecutorService consumerPool;
	private ExecutorCompletionService<Boolean> completionService;
	
	protected abstract IProcessingUnit<I, ?> getNextUnit(); 
	protected abstract void addPoisonPill() throws InterruptedException; 
	protected abstract int getConsumerSize();
	protected abstract Callable<Boolean> createConsumer();
	
	@Override
	public void start() {
		
		int consumerSize = getConsumerSize();
		consumerPool = Executors.newFixedThreadPool(consumerSize);
		completionService = new ExecutorCompletionService<Boolean>(consumerPool);
		for (int i = 0; i < consumerSize; i++) {
			completionService.submit(createConsumer());
		}
	}

	@Override
	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException {

		consumerPool.awaitTermination(timeout, unit);

		IProcessingUnit<I, ?> nextUnit = getNextUnit();
		if (forAll && nextUnit != null) {
			nextUnit.getITaskManager().awaitTermination(timeout, unit, forAll);
		}
	}

	@Override
	public void shutdown(boolean allNext) {
		
		consumerPool.shutdown();

		IProcessingUnit<I, ?> nextUnit = getNextUnit();
		if (allNext && nextUnit != null) {
			nextUnit.getITaskManager().shutdown(allNext);
		}
	}

	@Override
	public List<Runnable> shutdownNow() {
		
		return consumerPool.shutdownNow();
	}

	@Override
	public void shutdownTasks(boolean allNext) throws InterruptedException, ExecutionException {

		//put(new Message<I>("", null));
		addPoisonPill();
		
		int consumerSize = getConsumerSize();
		for(int i = 0; i < consumerSize; i++) {
			Future<Boolean> future = completionService.take();
			future.get();
			if (i < consumerSize - 1) {
				//put(new Message<I>("", null));
				addPoisonPill();
			}
		}
		
		IProcessingUnit<I, ?> nextUnit = getNextUnit();
		if (allNext && nextUnit != null) {
			nextUnit.getITaskManager().shutdownTasks(allNext);
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
