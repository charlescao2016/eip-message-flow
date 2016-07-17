package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IOperationTask;

public class Task2 implements IOperationTask<String> {

	public void doTask(Message<String> t) {
		
		long threadId = Thread.currentThread().getId();
		String body = t.getBody();
		System.out.println("Thread # " + threadId + " - Task2 start: " + t.getBody());
		
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t.setBody(body + "-task2");
		System.out.println("Thread # " + threadId + " - Task2 done: " + t.getBody());
	}

}
