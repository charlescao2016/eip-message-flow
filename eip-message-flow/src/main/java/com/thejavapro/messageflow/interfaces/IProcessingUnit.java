package com.thejavapro.messageflow.interfaces;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;

public interface IProcessingUnit<I, O> {
	
	void put(Message<I> message) throws InterruptedException;
	void shutdownTasks(boolean allNext) throws InterruptedException, ExecutionException;
	boolean shutdownTasks(boolean allNext, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException;
	void shutdown(boolean allNext);
	void awaitTermination(long timeout, TimeUnit unit, boolean allNext) throws InterruptedException;
	List<Runnable> shutdownNow();
	BlockingQueue<Message<I>> getInputQueue();
}
