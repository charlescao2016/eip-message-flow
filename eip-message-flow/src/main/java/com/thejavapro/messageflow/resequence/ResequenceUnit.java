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
	
	public ResequenceUnit(int inputQueueSize, BlockingQueue<Message<I>> outputQueue) {
		
		IProcessingTaskFactory<I, I> factory = new ResequenceTaskFactory<I>();
		processUnit = new ProcessUnit<I, I>(1, factory, inputQueueSize, outputQueue);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shutdownTasks(boolean allNext, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

}
