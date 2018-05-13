package com.learning.advanced.concurrent;

import java.util.concurrent.*;

public class CallableTest {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool(); // Executors是一个工厂类，用于创建各种类型ExecutorService线程池
//		1. newCachedThreadPool 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
//		2. newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
//		3. newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
//		4. newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
		Task task = new Task();
		Future<Integer> result = executor.submit(task);
		executor.shutdown();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("主线程在执行任务");

		try {
			System.out.println("task运行结果" + result.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("所有任务执行完毕");
	}

	static class Task implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			System.out.println("子线程在进行计算");
			Thread.sleep(3000);
			int sum = 0;
			for (int i = 0; i < 100; i++)
				sum += i;
			return sum;
		}
	}
}
