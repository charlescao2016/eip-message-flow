package com.thejavapro.messageflow.test;

import com.thejavapro.messageflow.Message;
import com.thejavapro.messageflow.interfaces.IOperationTask;

public class Task2 implements IOperationTask<String> {

	public void doTask(Message<String> t) {
		
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long threadId = Thread.currentThread().getId();
		System.out.println("Thread # " + threadId + " - Task2 done. message: " + t.getBody());
	}

}
