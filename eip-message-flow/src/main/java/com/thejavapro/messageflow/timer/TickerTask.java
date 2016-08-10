package com.thejavapro.messageflow.timer;

import java.util.concurrent.atomic.AtomicInteger;

public class TickerTask<V> implements Runnable {

	private AtomicInteger count;
	
	public TickerTask(int count) {
		this.count = new AtomicInteger(count);
	}
	
	@Override
	public V call() throws Exception {
		int current = count.decrementAndGet();
		return null;
	}

	@Override
	public void run() {
		count.decrementAndGet();
	}

}
