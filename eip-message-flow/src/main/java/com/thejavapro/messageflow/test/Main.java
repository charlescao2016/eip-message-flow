package com.thejavapro.messageflow.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.thejavapro.messageflow.CompletionService;
import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IProcessingTaskFactory;
import com.thejavapro.messageflow.interfaces.IProcessingUnit;
import com.thejavapro.messageflow.process.ProcessUnit;
import com.thejavapro.messageflow.resequence.ResequenceUnit;
import com.thejavapro.messageflow.timer.CountdownTimer;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);
			
	public static void main(String[] args) {
		
//		CountdownTimer t = new CountdownTimer(8, 1000);
//		t.start();
//		
//		while(true) {
//			System.out.println("count: " + t.getRemainCount() + " time: " + t.getRemainTime());
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		DOMConfigurator.configure("src/main/resources/log4j.xml");
		LOGGER.info("start ...");
		
		IProcessingTaskFactory<String, String> task1Factory = new Task1Factory();
		IProcessingTaskFactory<String, String> task2Factory = new Task2Factory();
		IProcessingTaskFactory<String, String> task3Factory = new Task3Factory();

		IProcessingUnit<String, String> v = new ProcessUnit<String, String>(1, task3Factory, 1);
		IProcessingUnit<String, String> u1 = new ProcessUnit<String, String>(50, task1Factory, 100);
		//IProcessingUnit<String, String> u2 = new TransformUnit<String, String>(5, task2Factory, 100, v);
		
		IProcessingUnit<String, String> seq = new ResequenceUnit<String>(2, TimeUnit.SECONDS, 3, 0);
		
		//u1.addOutputUnit(v);
		u1.addOutputUnit(seq);
		seq.addOutputUnit(v);
		
		CompletionService cs = new CompletionService();
		cs.add(u1.getITaskManager()).add(seq.getITaskManager()).add(v.getITaskManager());
		//cs.add(u1.getITaskManager()).add(v.getITaskManager());
		cs.start();
		
		for(int i = 0; i < 10; i++) {
			try {
				Message<String> m1 = new Message<String>("" + i, "message_u-" + i, i);
				u1.put(m1);
				
				//Message<String> m2 = new Message<String>("" + i, "message_v-" + i);
				//u2.put(m2);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
//		System.out.println("shutdownTasks.");
//		try {
//			//u1.shutdownTasks(true);
//			u1.shutdownTasks(true, 100, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println("shutdown.");
//		u1.shutdown(true);
//
//		
//		System.out.println("awaitTermination.");
//		try {
//			u1.awaitTermination(1, TimeUnit.SECONDS, true);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("shutdown now.");
//		List<Runnable> remain = u1.shutdownNow();
//		System.out.println("u1: " + remain.size());
//		
//		System.out.println("shutdown now.");
//		List<Runnable> remain_v = v.shutdownNow();
//		System.out.println("v: " + remain_v.size());

		try {
			cs.shutDown(100, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("exit.");
	}

}
