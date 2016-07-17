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

		IProcessingUnit<String, ?> v = new SinkUnit<String>(1, task2Factory, inputQueueSize);
		IProcessingUnit<String, String> u1 = new TransformUnit<String, String>(5, task1Factory, inputQueueSize, v);
		//IProcessingUnit<String, String> u2 = new TransformUnit<String, String>(5, task1Factory, inputQueueSize, v);
		
		for(int i = 0; i < 10; i++) {
			try {
				Message<String> m1 = new Message<String>("" + i, "message_u1-" + i);
				if (i == 9) {
					m1.setPoisonPill(true);
				}
				u1.put(m1);
				
//				Message<String> m2 = new Message<String>("" + i, "message_v1-" + i);
//				if (i == 9) {
//					m2.setPoisonPill(true);
//				}
//				u2.put(m2);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		try {
			u1.awaitPosionPill(true);
			u1.shutdown(true);
			u1.awaitTermination(5, TimeUnit.SECONDS, true);
	
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
