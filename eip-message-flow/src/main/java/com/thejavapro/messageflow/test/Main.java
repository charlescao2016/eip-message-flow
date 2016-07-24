package com.thejavapro.messageflow.test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.interfaces.ITranformTaskFactory;
import com.thejavapro.messageflow.process.ProcessUnit;

public class Main {

	public static void main(String[] args) {
		
		ITranformTaskFactory<String, String> task1Factory = new Task1Factory();
		ITranformTaskFactory<String, String> task2Factory = new Task2Factory();
		ITranformTaskFactory<String, String> task3Factory = new Task3Factory();

		IProcessingUnit<String, ?> v = new ProcessUnit<String, String>(1, task3Factory, 1);
		IProcessingUnit<String, String> u1 = new ProcessUnit<String, String>(5, task1Factory, 100, v);
		//IProcessingUnit<String, String> u2 = new TransformUnit<String, String>(5, task2Factory, 100, v);
		
		for(int i = 0; i < 10; i++) {
			try {
				Message<String> m1 = new Message<String>("" + i, "message_u-" + i);
				u1.put(m1);
				
				//Message<String> m2 = new Message<String>("" + i, "message_v-" + i);
				//u2.put(m2);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
		System.out.println("shutdownTasks.");
		try {
			//u1.shutdownTasks(true);
			u1.shutdownTasks(true, 100, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("shutdown.");
		u1.shutdown(true);

		
		System.out.println("awaitTermination.");
		try {
			u1.awaitTermination(1, TimeUnit.SECONDS, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("shutdown now.");
		List<Runnable> remain = u1.shutdownNow();
		System.out.println("u1: " + remain.size());
		
		System.out.println("shutdown now.");
		List<Runnable> remain_v = v.shutdownNow();
		System.out.println("v: " + remain_v.size());

		System.out.println("exit.");
	}

}
