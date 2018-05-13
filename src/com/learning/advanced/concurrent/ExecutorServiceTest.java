/*
 * 1. newCachedThreadPool()
 * 		缓存型池子，先查看池中有没有以前建立的线程，如果有，就reuse.如果没有，就建一个新的线程加入池中
 * 		缓存型池子通常用于执行一些生存期很短的异步型任务
 * 		在一些面向连接的daemon型SERVER中用得不多
 * 		能reuse的线程，必须是timeout IDLE内的池中线程，缺省timeout是60s,超过这个IDLE时长，线程实例将被终止及移出池。
 * 		注意，放入CachedThreadPool的线程不必担心其结束，超过TIMEOUT不活动，其会自动被终止。
 * 2. newFixedThreadPool
 * 		也是能reuse就用，但不能随时建新的线程
 * 		特点：1）任意时间点，最多只能有固定数目的活动线程存在，此时如果有新的线程要建立，只能放在另外的队列中等待，直到当前的线程中某个线程终止直接被移出池子
 * 		多数针对一些很稳定很固定的正规并发线程，多用于服务器
 * 3. ScheduledThreadPool
 * 		池子里的线程可以按schedule依次delay执行，或周期执行
 * 4. SingleThreadExecutor
 * 		单线程，任意时间池中只能有一个线程
 */
package com.learning.advanced.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

// 测试ExecutorService的submit(Callable)方法
public class ExecutorServiceTest {
	public static void main(String[] args) throws Exception {
		//invokeAny1();  // 一旦有一个任务正常完成(执行过程中没有抛异常)，线程池会终止其他未完成的任务。
		
		invokeAny2(); // 如果提交的任务列表中，没有一个正常完成的任务，那么调用invokeAny会抛异常，究竟抛的是哪儿个任务的异常，无关紧要。
		
		//invokeAny()和任务的提交顺序无关，只是返回最早正常执行完成的任务。
		
		// <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
		// 如果在超时之前，所有任务已经都是异常终止，那就没有必要在等下去了；
		// 如果超时之后，仍然有正在运行或等待运行的任务，那么会抛出TimeoutException。
		
		// invokeAll是一个阻塞方法，会等待任务列表中的所有任务都执行完成。
		// 不管任务是正常完成，还是异常终止，Future.isDone()始终返回true。
		// 通过Future.isCanceled()可以判断任务是否在执行的过程中被取消。
		// 通过Future.get()可以获取任务的返回结果，或者是任务在执行中抛出的异常。
		// 一旦ExecutorService.invokeAll()方法产生了异常，线程池中还没有完成的任务会被取消执行。
	}
	
	/** 
	* 没有1个正常完成的任务,invokeAny()方法抛出ExecutionException,封装了任务中元素的异常 
	*  
	*/  
	public static void invokeAny2() throws Exception  
	{  
	    ExecutorService executorService = Executors.newFixedThreadPool(3); 
	  
	    List<Callable<String>> tasks = new ArrayList<Callable<String>>();  
	  
	    tasks.add(new ExceptionCallable());  
	    tasks.add(new ExceptionCallable());  
	    tasks.add(new ExceptionCallable());  
	  
	    String result = executorService.invokeAny(tasks);  
	  
	    System.out.println("result=" + result);  
	  
	    executorService.shutdown();  
	}
	
	static void Run1() throws InterruptedException, ExecutionException{
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		System.out.println("------------------submit(Callable)------------");
		Future<String> future = executorService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "Callable执行结果";
			}
		});

		System.out.println(future.get());
		executorService.shutdown();
		System.out.println("------------------end------------");

		System.out.println("------------------invokeAny(Callable)------------");
		executorService = Executors.newSingleThreadExecutor();

		Set<Callable<String>> callables = new HashSet<Callable<String>>();

		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 1";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 2";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 3";
			}
		});
		for (int i = 0; i < 50; i++) {
			String result = executorService.invokeAny(callables);
			System.out.println("result = " + result);
		}

		executorService.shutdown();
		System.out.println("------------------end------------");
	}

	public static void invokeAny1() throws Exception  
	{  
	    ExecutorService executorService = Executors.newFixedThreadPool(3);  
	  
	    List<Callable<String>> tasks = new ArrayList<Callable<String>>();  
	  
	    tasks.add(new SleepSecondsCallable("t1", 2));  
	    tasks.add(new SleepSecondsCallable("t2", 1));  
	  
	    String result = executorService.invokeAny(tasks);  
	  
	    System.out.println("result=" + result);  
	  
	    executorService.shutdown();  
	}
	
	static class SleepSecondsCallable implements Callable<String> {
		private String name;

		private int seconds;

		public SleepSecondsCallable(String name, int seconds) {
			this.name = name;
			this.seconds = seconds;
		}

		public String call() throws Exception {
			System.out.println(name + ",begin to execute");

			try {
				TimeUnit.SECONDS.sleep(seconds);
			} catch (InterruptedException e) {
				System.out.println(name + " was disturbed during sleeping.");
				e.printStackTrace();
				return name + "_SleepSecondsCallable_failed";
			}

			System.out.println(name + ",success to execute");

			return name + "_SleepSecondsCallable_succes";
		}

	}

	static class ExceptionCallable implements Callable<String> {

		private String name = null;

		public ExceptionCallable() {

		}

		public ExceptionCallable(String name) {
			this.name = name;
		}

		@Override
		public String call() throws Exception {
			System.out.println("begin to ExceptionCallable.");

			System.out.println(name.length());

			System.out.println("end to ExceptionCallable.");

			return name;
		}
	}

	static class RandomTenCharsTask implements Callable<String> {

		@Override
		public String call() throws Exception {
			System.out.println("RandomTenCharsTask begin to execute...");

			StringBuffer content = new StringBuffer();

			String base = "abcdefghijklmnopqrstuvwxyz0123456789";
			Random random = new Random();
			for (int i = 0; i < 10; i++) {
				int number = random.nextInt(base.length());
				content.append(base.charAt(number));
			}

			System.out.println("RandomTenCharsTask complete.result=" + content);
			return content.toString();
		}
	}
}
