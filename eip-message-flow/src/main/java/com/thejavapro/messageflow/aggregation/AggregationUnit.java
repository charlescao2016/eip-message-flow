package com.thejavapro.messageflow.aggregation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITaskManager;

public class AggregationUnit<I, O> implements IProcessingUnit<I, O> {

	private static final Logger LOGGER = Logger.getLogger(AggregationUnit.class);

	@Override
	public void put(Message<I> message) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOutputQueue(BlockingQueue<Message<O>> outputQueue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IProcessingUnit<O, ?> addOutputUnit(IProcessingUnit<O, ?> next) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITaskManager getITaskManager() {
		// TODO Auto-generated method stub
		return null;
	}



}
