package com.thejavapro.messageflow.resequence;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.transform.TransformUnit;

public class ResequenceUnit<I> implements IProcessingUnit<I, I>{

	private IProcessingUnit<I, I> transformUnit;
	
	public ResequenceUnit(int inputQueueSize, BlockingQueue<Message<I>> outputQueue) {
		
		ITranformTaskFactory<I, I> factory = new ResequenceTaskFactory<I>();
		transformUnit = new TransformUnit<I, I>(1, factory, inputQueueSize, outputQueue);
	}

	public void put(Message<I> message) throws InterruptedException {
		
		transformUnit.put(message);
	}

	public void awaitTermination(long timeout, TimeUnit unit, boolean forAll) throws InterruptedException {
		
		transformUnit.awaitTermination(timeout, unit, forAll);
	}

	public void shutdown(boolean all) {
		
		transformUnit.shutdown(all);
	}

	public BlockingQueue<Message<I>> getInputQueue() {
		
		return transformUnit.getInputQueue();
	}

}
