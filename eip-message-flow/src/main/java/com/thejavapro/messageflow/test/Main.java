package com.thejavapro.messageflow.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.transform.TransformUnit;

public class Main {

	public static void main(String[] args) {
		
		ITranformTaskFactory<String, String> task1Factory = new Task1Factory();
		ITranformTaskFactory<String, String> task3Factory = new Task3Factory();

		//IProcessingUnit<String, ?> v = new SinkUnit<String>(1, task2Factory, inputQueueSize);
		IProcessingUnit<String, ?> v = new TransformUnit<String, String>(1, task3Factory, 100);
		IProcessingUnit<String, String> u1 = new TransformUnit<String, String>(5, task1Factory, 100, v);
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
			System.out.println("awaitPosionPill");
			u1.awaitPosionPill(true);
			System.out.println("shutdown.");
			u1.shutdown(true);
			System.out.println("awaitTermination.");
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
