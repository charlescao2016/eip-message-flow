package com.thejavapro.messageflow;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.interfaces.IProcessingUnit;

public class CompletionService<I, O> {

	private static final Logger LOGGER = Logger.getLogger(CompletionService.class);

	private List<IProcessingUnit<I, O>> processUnits; 
	
	public void add(IProcessingUnit<I, O> processUnit) {
		processUnits.add(processUnit);
	}
	
	public void shutDown() throws InterruptedException, ExecutionException {
		
		//processUnits.forEach((processUnit) -> processUnit.gracefullyShutdown(false));
		
		for (IProcessingUnit<I, O> processUnit : processUnits) {
			processUnit.shutdownTasks(false);
		}
	}
}
