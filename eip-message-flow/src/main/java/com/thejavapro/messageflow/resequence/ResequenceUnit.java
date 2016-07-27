package com.thejavapro.messageflow.resequence;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.process.ProcessUnit;

public class ResequenceUnit<I> implements IProcessingUnit<I, I>{

	private IProcessingUnit<I, I> processUnit;
	
	public ResequenceUnit(int inputQueueSize) {
		
		IProcessingTaskFactory<I, I> factory = new ResequenceTaskFactory<I>();
		processUnit = new ProcessUnit<I, I>(1, factory, inputQueueSize);
	}

	@Override
	public void put(Message<I> message) throws InterruptedException {
		
		processUnit.put(message);
	}

	@Override
	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException {
		
		processUnit.awaitTermination(timeout, unit, forAll);
	}

	@Override
	public void shutdown(boolean all) {
		
		processUnit.shutdown(all);
	}

	@Override
	public BlockingQueue<Message<I>> getInputQueue() {
		
		return processUnit.getInputQueue();
	}

	@Override
	public void shutdownTasks(boolean allNext) throws InterruptedException, ExecutionException {
		
		processUnit.shutdownTasks(allNext);
	}

	@Override
	public boolean shutdownTasks(boolean allNext, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
		
		return processUnit.shutdownTasks(allNext, timeout, unit);
	}

	@Override
	public List<Runnable> shutdownNow() {

		return processUnit.shutdownNow();
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<I>> outputQueue) {

		processUnit.addOutputQueue(outputQueue);		
	}

	@Override
	public void addOutputUnit(IProcessingUnit<I, ?> next) {

		processUnit.addOutputUnit(next);
	}

	@Override
	public void start() {

		processUnit.start();
	}

}
