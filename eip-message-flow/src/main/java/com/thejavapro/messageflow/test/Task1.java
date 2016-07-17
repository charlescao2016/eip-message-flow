package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.ITransformTask;

public class Task1 implements ITransformTask<String, String> {

	public Message<String> doTask(Message<String> t) {
		
		long threadId = Thread.currentThread().getId();
		
		String body = t.getBody();
		System.out.println("Thread # " + threadId + " - Task1 start: " + t.getBody());
		
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		t.setBody(body + "-task1");
		System.out.println("Thread # " + threadId + " - Task1 done: " + t.getBody());
		
		return t;
	}

}
