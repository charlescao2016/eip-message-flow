package com.thejavapro.messageflow.interfaces;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;

public interface IProcessingUnit<I, O> {
	
	void put(Message<I> message) throws InterruptedException;
	void awaitTermination(long timeout, TimeUnit unit, boolean allNext) throws InterruptedException;
	void shutdown(boolean allNext);
	void gracefullyShutdown(boolean allNext) throws InterruptedException, ExecutionException;
	BlockingQueue<Message<I>> getInputQueue();
}
