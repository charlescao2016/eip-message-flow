package com.thejavapro.messageflow;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.thejavapro.messageflow.concurrent.EventMonitor;

public class BoundedPriorityBlockingQueue<E> implements Serializable, Iterable<E>, Collection<E>, BlockingQueue<E>, Queue<E> {

	private static final Logger LOGGER = Logger.getLogger(BoundedPriorityBlockingQueue.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PriorityBlockingQueue<E> queue; 
	private EventMonitor maxBuffeSizerMonitor;
	private final int maxBufferSzie;
	
	public BoundedPriorityBlockingQueue(int maxBufferSzie, Comparator<? super E> comparator) {
		queue = new PriorityBlockingQueue<E>(maxBufferSzie, comparator);
		maxBuffeSizerMonitor = EventMonitor.Add("com.thejavapro.messageflow.BoundedPriorityBlockingQueue", false, 50, 100);
		this.maxBufferSzie = maxBufferSzie;
	}
	
	@Override
	public E element() {
		return queue.element();
	}

	@Override
	public E peek() {
		return queue.peek();
	}

	@Override
	public E poll() {
		E e = queue.poll();
		if (e != null) {
			maxBuffeSizerMonitor.doNotify();
		}
		return e;
	}

	@Override
	public E remove() {
		E e = queue.remove();
		maxBuffeSizerMonitor.doNotify();
		return e;
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		int n = queue.drainTo(c);
		if (n != 0) {
			maxBuffeSizerMonitor.doNotify();
		}
		return n;
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		int n = queue.drainTo(c, maxElements);
		if (n != 0) {
			maxBuffeSizerMonitor.doNotify();
		}
		return n;
	}

	@Override
	public synchronized boolean offer(E e) {
		if (queue.size() >= maxBufferSzie) {
			return false;
		}
		return queue.offer(e);
	}

	@Override
	public synchronized boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		
		throw new RuntimeException();
		
		//maxBuffeSizerMonitor.doWait();	
		//return queue.offer(e, timeout, unit);
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		E e = queue.poll(timeout, unit);
		maxBuffeSizerMonitor.doNotify();
		return e;
	}

	@Override
	public synchronized void put(E e) throws InterruptedException {
		while (queue.size() >= maxBufferSzie) {
			maxBuffeSizerMonitor.doWait();
		}
		queue.put(e);
	}

	@Override
	public int remainingCapacity() {
		return queue.remainingCapacity();
	}

	@Override
	public E take() throws InterruptedException {
		E e = queue.take();
		maxBuffeSizerMonitor.doNotify();
		return e;
	}

	@Override
	public synchronized boolean add(E arg0) {
		if (queue.size() >= maxBufferSzie) {
			throw new IllegalStateException();
		}
		return queue.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		return queue.addAll(arg0);
	}

	@Override
	public void clear() {
		queue.clear();
		maxBuffeSizerMonitor.doNotify();
	}

	@Override
	public boolean contains(Object arg0) {
		return queue.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return queue.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public boolean remove(Object arg0) {
		boolean b = queue.remove(arg0);
		if (b) {
			maxBuffeSizerMonitor.doNotify();
		}
		return b;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean b = queue.removeAll(arg0);
		if (b) {
			maxBuffeSizerMonitor.doNotify();
		}
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		boolean b = queue.retainAll(arg0);
		if (b) {
			maxBuffeSizerMonitor.doNotify();
		}
		return b;
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public Object[] toArray() {
		return queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return queue.toArray(arg0);
	}

	@Override
	public Iterator<E> iterator() {
		return queue.iterator();
	}

	public EventMonitor getMaxBuffeSizerMonitor() {
		return maxBuffeSizerMonitor;
	}
}
