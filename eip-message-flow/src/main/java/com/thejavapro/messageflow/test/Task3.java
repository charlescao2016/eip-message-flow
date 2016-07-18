package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.ITransformTask;

public class Task3 implements ITransformTask<String, String> {

		public Message<String> doTask(Message<String> t) {
			
			long threadId = Thread.currentThread().getId();
			
			String body = t.getBody();
			System.out.println("Thread # " + threadId + " - Task3 start: " + t.getBody());
			
			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			t.setBody(body + "-task3");
			System.out.println("Thread # " + threadId + " - Task3 done: " + t.getBody());
			
			return null;
		}
}
