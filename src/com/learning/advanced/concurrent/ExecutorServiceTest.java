/*
 * 1. newCachedThreadPool()
 * 		�����ͳ��ӣ��Ȳ鿴������û����ǰ�������̣߳�����У���reuse.���û�У��ͽ�һ���µ��̼߳������
 * 		�����ͳ���ͨ������ִ��һЩ�����ں̵ܶ��첽������
 * 		��һЩ�������ӵ�daemon��SERVER���õò���
 * 		��reuse���̣߳�������timeout IDLE�ڵĳ����̣߳�ȱʡtimeout��60s,�������IDLEʱ�����߳�ʵ��������ֹ���Ƴ��ء�
 * 		ע�⣬����CachedThreadPool���̲߳��ص��������������TIMEOUT���������Զ�����ֹ��
 * 2. newFixedThreadPool
 * 		Ҳ����reuse���ã���������ʱ���µ��߳�
 * 		�ص㣺1������ʱ��㣬���ֻ���й̶���Ŀ�Ļ�̴߳��ڣ���ʱ������µ��߳�Ҫ������ֻ�ܷ�������Ķ����еȴ���ֱ����ǰ���߳���ĳ���߳���ֱֹ�ӱ��Ƴ�����
 * 		�������һЩ���ȶ��̶ܹ������沢���̣߳������ڷ�����
 * 3. ScheduledThreadPool
 * 		��������߳̿��԰�schedule����delayִ�У�������ִ��
 * 4. SingleThreadExecutor
 * 		���̣߳�����ʱ�����ֻ����һ���߳�
 */
package com.learning.advanced.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

// ����ExecutorService��submit(Callable)����
public class ExecutorServiceTest {
	public static void main(String[] args) throws Exception {
		//invokeAny1();  // һ����һ�������������(ִ�й�����û�����쳣)���̳߳ػ���ֹ����δ��ɵ�����
		
		invokeAny2(); // ����ύ�������б��У�û��һ��������ɵ�������ô����invokeAny�����쳣�������׵����Ķ���������쳣���޹ؽ�Ҫ��
		
		//invokeAny()��������ύ˳���޹أ�ֻ�Ƿ�����������ִ����ɵ�����
		
		// <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
		// ����ڳ�ʱ֮ǰ�����������Ѿ������쳣��ֹ���Ǿ�û�б�Ҫ�ڵ���ȥ�ˣ�
		// �����ʱ֮����Ȼ���������л�ȴ����е�������ô���׳�TimeoutException��
		
		// invokeAll��һ��������������ȴ������б��е���������ִ����ɡ�
		// ����������������ɣ������쳣��ֹ��Future.isDone()ʼ�շ���true��
		// ͨ��Future.isCanceled()�����ж������Ƿ���ִ�еĹ����б�ȡ����
		// ͨ��Future.get()���Ի�ȡ����ķ��ؽ����������������ִ�����׳����쳣��
		// һ��ExecutorService.invokeAll()�����������쳣���̳߳��л�û����ɵ�����ᱻȡ��ִ�С�
	}
	
	/** 
	* û��1��������ɵ�����,invokeAny()�����׳�ExecutionException,��װ��������Ԫ�ص��쳣 
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
				return "Callableִ�н��";
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
