package com.thejavapro.messageflow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.interfaces.ITaskManager;

public class CompletionService {

	private static final Logger LOGGER = Logger.getLogger(CompletionService.class);

	private List<ITaskManager> taskManagers = new ArrayList<ITaskManager>(); 
	
	public void add(ITaskManager taskManager) {
		taskManagers.add(taskManager);
	}
	
	public void shutDown(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
		
		//processUnits.forEach((processUnit) -> processUnit.gracefullyShutdown(false));
		
		for (ITaskManager taskManager : taskManagers) {
			taskManager.shutdownTasks(false, timeout, unit);
		}
		
		for (ITaskManager taskManager : taskManagers) {
			taskManager.shutdown(false);
		}
		
		for (ITaskManager taskManager : taskManagers) {
			taskManager.awaitTermination(timeout / taskManagers.size(), unit, false);
		}
		
		for (ITaskManager taskManager : taskManagers) {
			taskManager.shutdownNow();
		}
	}
}
