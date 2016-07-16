package com.thejavapro.messageflow.aggregation;

import java.util.concurrent.BlockingQueue;
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

	public void put(Message<I> message) {
		// TODO Auto-generated method stub
		
	}

	public Message<O> take() {
		// TODO Auto-generated method stub
		return null;
	}

	public void awaitPosionPill(boolean forAll) {
		// TODO Auto-generated method stub
		
	}

	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) {
		// TODO Auto-generated method stub
		
	}

	public void shutdown(boolean all) {
		// TODO Auto-generated method stub
		
	}
	
}
