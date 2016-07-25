package com.thejavapro.messageflow.aggregation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class AggregationUnit<I, O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(AggregationUnit.class);

	public BlockingQueue<Message<I>> getInputQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(Message<I> message) throws InterruptedException {
		// TODO Auto-generated method stub
		
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
	public void shutdown(boolean allNext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void awaitTermination(long timeout, TimeUnit unit, boolean allNext) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<O>> outputQueue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOutputUnit(IProcessingUnit<O, ?> next) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}


}
