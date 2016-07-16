package com.thejavapro.messageflow.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IOperationTaskFactory;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.oprationsink.SinkUnit;
import com.thejavapro.messageflow.transform.TransformUnit;

public class Main {

	public static void main(String[] args) {
		
		int inputQueueSize = 100;
		ITranformTaskFactory<String, String> task1Factory = new Task1Factory();
		IOperationTaskFactory<String> task2Factory = new Task2Factory();

		IProcessingUnit<String, ?> u2 = new SinkUnit<String>(1, task2Factory, inputQueueSize);
		IProcessingUnit<String, String> u1 = new TransformUnit<String, String>(5, task1Factory, inputQueueSize, u2); 
		
		for(int i = 0; i < 10; i++) {
			try {
				Message<String> m = new Message<String>("" + i, "message" + i);
				if (i == 9) {
					m.setPoisonPill(true);
				}
				u1.put(m);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		try {
			u2.awaitPosionPill(true);
			u2.awaitTermination(5, TimeUnit.SECONDS, true);
			u2.shutdown(true);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("exit.");
	}

}
