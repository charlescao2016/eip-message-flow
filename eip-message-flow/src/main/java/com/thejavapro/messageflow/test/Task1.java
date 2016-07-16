package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.ITransformTask;

public class Task1 implements ITransformTask<String, String> {

	public Message<String> doTask(Message<String> t) {
		
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String body = t.getBody();
		t.setBody(body + "-task1");
		
		long threadId = Thread.currentThread().getId();
		System.out.println("Thread # " + threadId + " - Task1 done. message: " + t.getBody());
		
		return t;
	}

}
