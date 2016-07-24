package com.thejavapro.messageflow.interfaces;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public interface ITaskManager {
	
	void shutdownTasks(boolean allNext) throws InterruptedException, ExecutionException;
	boolean shutdownTasks(boolean allNext, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException;
	void shutdown(boolean allNext);
	void awaitTermination(long timeout, TimeUnit unit, boolean allNext) throws InterruptedException;
	List<Runnable> shutdownNow();
}
